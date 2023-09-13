package com.jssg.servicemanagersystem.helper;

import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.jssg.servicemanagersystem.R;
import com.jssg.servicemanagersystem.ui.workorder.adapter.AudioRecordAdapter;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 播放音频附件，并通过更新界面实现动画效果
 * Created by wudeng on 2017/11/2.
 */

public class AudioPlayHandler {

    //语音动画控制器
    private Timer mTimer = null;
    //语音动画控制任务
    private TimerTask mTimerTask = null;
    //记录语音动画图片索引
    private int index = 0;
    //根据 index 更换动画
    private AudioAnimHandler mAudioAnimHandler = null;

    /**
     * 播放音频动画
     */
    public void startAudioAnim(AudioRecordAdapter adapter, int pos, boolean isLeft){

        stopAnimTimer();

        // 语音播放动画效果
        mAudioAnimHandler = new AudioAnimHandler(adapter,pos, isLeft);

        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mAudioAnimHandler.obtainMessage(index).sendToTarget();
                index = (index+1)%3;
            }
        };
        mTimer = new Timer();
        // 每半秒更新一次界面
        mTimer.schedule(mTimerTask,0,500);
    }

    /**
     * 停止语音播放动画
     */
    public  void stopAnimTimer(){

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }

        // 将上一个语音消息界面复位
        if (mAudioAnimHandler != null){
            mAudioAnimHandler.obtainMessage(3).sendToTarget();
        }
    }

    private static class AudioAnimHandler extends Handler {

        private final AudioRecordAdapter adapter;
        private final int pos;
        private final boolean isLeft;

        private int[] mLeftIndex = {
                R.mipmap.sound_left_1,R.mipmap.sound_left_2,R.mipmap.sound_left_3};

        private int[] mRightIndex = {
                R.mipmap.sound_right_1, R.mipmap.sound_right_2,R.mipmap.sound_right_3};

        AudioAnimHandler(AudioRecordAdapter adapter, int pos, boolean isLeft){
            this.adapter = adapter;
            this.pos = pos;
            this.isLeft = isLeft;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    adapter.setVoiceImageRes(mLeftIndex[0], pos);
//                    mIvAudio.setImageResource(isLeft? mLeftIndex[0]:mRightIndex[0]);
                    break;
                case 1:
                    adapter.setVoiceImageRes(mLeftIndex[1], pos);
//                    mIvAudio.setImageResource(isLeft? mLeftIndex[1]:mRightIndex[1]);
                    break;
                case 2:
                    adapter.setVoiceImageRes(mLeftIndex[2], pos);
//                    mIvAudio.setImageResource(isLeft? mLeftIndex[2]:mRightIndex[2]);
                    break;
                default:
                    adapter.setVoiceImageRes(R.mipmap.sound_left_0, pos);
//                    mIvAudio.setImageResource(isLeft? R.mipmap.sound_left_0:R.mipmap.sound_right_0);
                    removeCallbacks(null);
                    break;
            }
        }
    }

}
