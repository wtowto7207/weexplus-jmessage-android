package com.weexplus.jim.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;

import com.weexplus.jim.R;
import com.weexplus.jim.framework.base.BaseActivity;

import cn.jpush.im.android.api.content.VideoContent;

/**
 * Created by wapchief on 2017/7/20.
 */

public class MsgVideoActivity extends BaseActivity {

    public static final String VIDEO_PATH = "VIDEO_PATH";

    private VideoView mVideoView;

    private int mSavedCurrentPosition;
    @Override
    protected int setContentView() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        String videoPath = getIntent().getStringExtra(VIDEO_PATH);

        mVideoView = (VideoView) findViewById(R.id.videoview_video);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(mVideoView);

        mVideoView.setMediaController(mediaController);
        mVideoView.setVideoPath(videoPath);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoView.requestLayout();
                if (mSavedCurrentPosition != 0) {
                    mVideoView.seekTo(mSavedCurrentPosition);
                    mSavedCurrentPosition = 0;
                } else {
                    play();
                }
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mVideoView.setKeepScreenOn(false);
            }
        });
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
    @Override
    protected void onResume() {
        super.onResume();
        mVideoView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSavedCurrentPosition = mVideoView.getCurrentPosition();
        mVideoView.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        pause();
    }

    private void play() {
        mVideoView.start();
        mVideoView.setKeepScreenOn(true);
    }

    private void pause() {
        mVideoView.pause();
        mVideoView.setKeepScreenOn(false);
    }
}
