package com.jssg.servicemanagersystem.ui.main.update

import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.jssg.servicemanagersystem.core.AppApplication.Companion.get
import com.jssg.servicemanagersystem.databinding.UpdataDialogBinding
import com.jssg.servicemanagersystem.utils.download.ApkDownLoadService
import com.jssg.servicemanagersystem.utils.download.ApkDownLoadService.install
import com.jssg.servicemanagersystem.utils.download.UpdateEntity
import com.jssg.servicemanagersystem.utils.download.DownloadApkProgressEvent
import com.tencent.mmkv.MMKV
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File

/**
 * @Author wangqiong
 * @create 2019-05-31 11:07
 */
class UpdateDialogFragment : DialogFragment() {
    private var updateEntity: UpdateEntity? = null
    private lateinit var binding: UpdataDialogBinding
    override fun onAttach(context: Context) {
        super.onAttach(context)
        updateEntity = arguments?.getParcelable("mUpdateEntity")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.setBackgroundDrawable(BitmapDrawable())
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = UpdataDialogBinding.inflate(inflater, container, false)
        EventBus.getDefault().register(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (updateEntity == null) dismiss()
        status = MMKV.defaultMMKV()
            .decodeInt("app_download_" + updateEntity!!.version, DownloadManager.STATUS_PENDING)
        binding.tvVersion.text = "Version ${updateEntity!!.version}"

        binding.tvUpdateContent.text = updateEntity!!.updateInfo
        binding.ivCancel.setOnClickListener { v: View? -> dismiss() }

        //强制升级
        if (updateEntity!!.force) {
            binding.ivCancel.visibility = View.GONE
            isCancelable = false
        } else {
            binding.ivCancel.visibility = View.VISIBLE
            isCancelable = true
        }

        binding.dialogBtnSure.setOnClickListener {
            when (status) {
                DownloadManager.STATUS_FAILED, DownloadManager.STATUS_PENDING -> {
                    startUpdate(false)
                }
                DownloadManager.STATUS_SUCCESSFUL -> {
                    //下载成功
                    startUpdate(true)
                }
                DownloadManager.STATUS_RUNNING -> {
                    //下载中 也可能是下载中进程被杀死了
                }
            }
        }

        //下载成功之后这个状态就一直显示
        if (isDownLoadSuccess) {
            binding.dialogBtnSure.text = "下载完成点击安装"
        }
        //将当前显示的时间更新写入
        MMKV.defaultMMKV().encode(CANCEL_TIME_KEY, System.currentTimeMillis())
        val oriText = binding.tvUpdateErrorTips.text.toString()
        // 去浏览器更新提示
        binding.tvUpdateErrorTips.setOnClickListener { v: View? ->
            //调用内置动作
            val intent = Intent(Intent.ACTION_VIEW)
            //将url解析为Uri对象，再传递出去
            intent.data =
                Uri.parse("https://fir.xcxwo.com/cqr9e8")
            //启动
            startActivity(intent)
        }
    }

    //下载状态
    var status = DownloadManager.STATUS_PENDING

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updateProgressChange(event: DownloadApkProgressEvent) {
        //下载状态
        status = event.status
        val progress = event.getProgress()
        when (status) {
            DownloadManager.STATUS_RUNNING -> {
                binding.dialogBtnSure.text = "下载中 $progress%"
            }
            DownloadManager.STATUS_FAILED -> {
                binding.dialogBtnSure.text = "下载失败点击重试"
            }
            DownloadManager.STATUS_SUCCESSFUL -> {
                binding.dialogBtnSure.text = "下载完成点击安装"
            }
        }
        MMKV.defaultMMKV().encode("app_download_" + updateEntity!!.version, status)
    }

    private val isDownLoadSuccess: Boolean
        private get() {
            val file = File((get().getExternalFilesDir("apk")!!.absolutePath) + "/customer_manager_system.apk")

            if (checkDownLoadAPKIsSuccess(updateEntity!!.version, file)) {
                status = DownloadManager.STATUS_SUCCESSFUL
            }
            return status == DownloadManager.STATUS_SUCCESSFUL && file.exists() && file.length() > 90000000
        }

    //versionId":"3.0.2"
    private fun checkDownLoadAPKIsSuccess(versionCode: String, file: File): Boolean {
        try {
            val lastVerCode = versionCode.replace(".", "").toInt()
            if (file != null && file.exists() && file.isFile) {
                val pm = requireContext().packageManager
                pm.getPackageArchiveInfo(
                    file.path,
                    PackageManager.GET_ACTIVITIES
                )?.also {
//                    val appInfo: ApplicationInfo = it.applicationInfo
                    if (it.versionName.replace(".", "").toInt() >= lastVerCode) {
                        return true
                    }
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return false
    }


    private fun startUpdate(isInstall: Boolean) {
        val file = File(get().getExternalFilesDir("apk")!!.absolutePath + "/customer_manager_system.apk")
        updateEntity?.apkLocalPath = file.absolutePath
        if (isInstall) {
            install(context, file.absolutePath)
        } else {
            val intent = Intent(activity, ApkDownLoadService::class.java)
            intent.putExtra(ApkDownLoadService.DO_WHAT, ApkDownLoadService.ACTION_DOWNLOAD)
            intent.putExtra("taskInfo", updateEntity)
            requireActivity().startService(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    companion object {
        const val CANCEL_TIME_KEY = "update_cancel_time"

        //点击取消更新下次的提示时间间隔
        private const val SHOW_DURATION = (1 * 24 * 60 * 60 * 1000).toLong()

        @JvmStatic
        fun newInstance(mUpdateEntity: UpdateEntity?): UpdateDialogFragment {
            val args = Bundle()
            args.putParcelable("mUpdateEntity", mUpdateEntity)
            val fragment = UpdateDialogFragment()
            fragment.arguments = args
            return fragment
        }

        /**
         * 是否需要提示版本更新弹窗
         * 1.强制升级 必须弹窗
         * 2.普通升级 第一次或者24小时以后
         *
         * @param mUpdateEntity
         * @return
         */
        fun isCanShowUpdateDialog(mUpdateEntity: UpdateEntity?): Boolean {
            if (mUpdateEntity == null) return false
            //发现强制升级版本 直接提示升级
            if (mUpdateEntity.hasUpdate() && mUpdateEntity.force) {
                return true
            }

            //当前是最新版本则重置普通升级标记
            if (!mUpdateEntity.hasUpdate()) {
                return false
            }

            //有普通升级未升级  每隔1天提醒一次
            val lastShowTime =
                MMKV.defaultMMKV().decodeLong(CANCEL_TIME_KEY, 0)
            return mUpdateEntity.hasUpdate() && System.currentTimeMillis() - lastShowTime > SHOW_DURATION
        }
    }
}