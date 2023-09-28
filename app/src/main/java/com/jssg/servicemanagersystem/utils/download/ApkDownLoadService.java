package com.jssg.servicemanagersystem.utils.download;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import com.jssg.servicemanagersystem.BuildConfig;
import com.jssg.servicemanagersystem.R;
import com.jssg.servicemanagersystem.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * NewRcApp
 * author:wangqiong on 2018/5/28.
 * mail:wqandroid@gmail.com
 */
public class ApkDownLoadService extends Service {

    public static final String DO_WHAT = "do_what";

    public static final String ACTION_DOWNLOAD = "DOWNLOAD";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private UpdateEntity taskInfo;
    private NotificationManager notificationManager;
    private Notification notification;
    private NotificationCompat.Builder builder;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onHandleIntent(intent);
        return super.onStartCommand(intent, flags, startId);

    }

    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent == null || !intent.hasExtra(DO_WHAT)) return;

        String action = intent.getStringExtra(DO_WHAT);

        if (action.equals(ACTION_DOWNLOAD)) {
            //执行下载任务
            if (taskInfo == null || downloadId == 0) {
                try {
                    if (intent.hasExtra("taskInfo")) {
                        taskInfo = intent.getParcelableExtra("taskInfo");
                        downloadApk(taskInfo);
                        initNotification();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //多次启动到下载service
                if (notification == null || notificationManager == null) {
                    initNotification();
                }

                if (downloadId != 0) {
                    DownloadApkProgressEvent event = getAndPostEvent(downloadId);
                    //执行重新下载的逻辑
                    if (event.getStatus() == DownloadManager.STATUS_PENDING) {
                        downloadApk(taskInfo);
                        initNotification();
                    }
                }
            }
        } else {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }


    }

    private long downloadId = 0;
    private DownloadManager downloadManager;
    private Disposable disposable;


    public void downloadApk(UpdateEntity taskInfo) {
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadId != 0) {
            downloadManager.remove(downloadId);
        }
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(taskInfo.getDownloadUrl()));
        request.setTitle("准备下载" + getString(R.string.app_name));
//        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setAllowedOverRoaming(false);
        request.allowScanningByMediaScanner();
        request.setMimeType("application/vnd.android.package-archive");
        //下载完成之后显示系统的点击安装
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
        //创建目录
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir();
        File file = new File(taskInfo.getApkLocalPath());
        if (file.exists()) {
            file.delete();
        }
        request.setDestinationUri(Uri.fromFile(file));
        downloadId = downloadManager.enqueue(request);
        sendApkUpdate();
    }


    /**
     * 每个人一秒钟去查询下载状态
     */
    public void sendApkUpdate() {
        Observable.interval(300, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .map(a -> getAndPostEvent(downloadId))
                .observeOn(AndroidSchedulers.mainThread())
                .takeUntil(event -> event.getStatus() == DownloadManager.STATUS_SUCCESSFUL || event.getStatus() == DownloadManager.STATUS_FAILED)
                .subscribe(new Observer<DownloadApkProgressEvent>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(DownloadApkProgressEvent ints) {
                        updateNowStatus(ints);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }


    private void updateNowStatus(DownloadApkProgressEvent event) {

        if (event.getCurrent() == 0) return;
        LogUtil.e("download", "下载状态:" + event);

        int status = event.getStatus();


        if (status == DownloadManager.STATUS_SUCCESSFUL) {
            //点击通知安装
//            String filePath=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+File.separator+taskInfo.getApkFileName();
            String filePath = getExternalFilesDir("apk").getAbsolutePath() + "/customer_manager_system.apk";
            //直接安装更新
            try {
                install(this, filePath);
                //正常安装清空通知栏
                notificationManager.cancelAll();
            } catch (Exception e) {
                e.printStackTrace();
                //安装权限被禁止 则通知栏提示点击安装
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri apkUri = FileProvider.getUriForFile(getBaseContext(), BuildConfig.APPLICATION_ID + ".fileProvider", new File(filePath));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
                notification = builder.setContentIntent(pi)
                        .setContentText("下载成功")
                        .setProgress(0, 0, false)
                        .build();
                notificationManager.notify(1, notification);
            }
            endDownloadTask();
        } else if (status == DownloadManager.STATUS_FAILED) {
            //下载失败
            Intent intent = webLauncher("https://fir.xcxwo.com/cqr9e8");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent pi = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            notification = builder.setContentIntent(pi)
                    .setContentText("下载失败(点击从浏览器下载)")
                    .build();
            notificationManager.notify(1, notification);

            endDownloadTask();
        } else if (status == DownloadManager.STATUS_RUNNING) {
            //下载中
            int progress = event.getProgress();
            builder.setProgress(100, progress, false);
            builder.setContentTitle(getString(R.string.app_name));
            builder.setContentText("下载中 " + progress + "%");
            notification = builder.build();
            notificationManager.notify(1, notification);
        }
    }

    public static void install(Context context, String filePath) {
        LogUtil.e("install", filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = new File(filePath);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }


    public void endDownloadTask() {
        downloadId = 0;
        //停止查询下载状态
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        //并关闭自己
        stopSelf();
    }


    public DownloadApkProgressEvent getAndPostEvent(long downloadId) {

        DownloadApkProgressEvent event = new DownloadApkProgressEvent(-1, -1, DownloadManager.STATUS_PENDING);
        if (downloadId == 0 || downloadManager == null) return event;
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = null;
        try {
            c = downloadManager.query(query);
            if (c != null && c.moveToFirst()) {
                event = new DownloadApkProgressEvent(c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)) / 1000,
                        c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)) / 1000,
                        c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                );
                EventBus.getDefault().post(event);
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return event;
    }


    //初始化通知
    private void initNotification() {
        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//适配8.0,自行查看8.0的通知，主要是NotificationChannel

            NotificationChannel chan1 = new NotificationChannel("channel_1",
                    "Primary Channel", NotificationManager.IMPORTANCE_DEFAULT);
//            chan1.setSound(null,null);
            chan1.setLightColor(Color.GREEN);
            chan1.enableLights(true);
            chan1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            notificationManager.createNotificationChannel(chan1);
            builder = new NotificationCompat.Builder(this, "channel_1").setOnlyAlertOnce(true);
        } else {
            builder = new NotificationCompat.Builder(this).setOnlyAlertOnce(true);
        }

        builder.setContentTitle("准备下载" + getString(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)) //设置通知的大图标
                .setDefaults(Notification.DEFAULT_LIGHTS) //设置通知的提醒方式： 呼吸灯
                .setPriority(NotificationCompat.PRIORITY_MAX) //设置通知的优先级：最大
                .setAutoCancel(false)//设置通知被点击一次是否自动取消
                .setContentText("下载中 1%")
                .setProgress(100, 0, false);

        notification = builder.build();//构建通知对象
    }


    private static Intent webLauncher(String downloadUrl) {
        Uri download = Uri.parse(downloadUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, download);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }


}
