package com.h.demo;

import java.io.File;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.VideoView;

/**
 * 播放视频
 * @author Cherry
 * @date  2019年6月16日
 */
public class VideoActivity extends BaseActivity {
	private VideoView vp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.videos_layout);
		this.createListenButton(R.id.vplay);
		this.createListenButton(R.id.vpause);
		this.createListenButton(R.id.replay);
		vp = (VideoView) findViewById(R.id.video_view);
		String video = "视频.mp4";
		inputVideo(video);
	}

	private void inputVideo(String video) {
		File file = new File(Environment.getExternalStorageDirectory(),video);
		vp.setVideoPath(file.getPath());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.vplay:
			if(!vp.isPlaying()){
				vp.start();
			}
			break;
		case R.id.vpause:
			if(vp.isPlaying()){
				vp.pause();
			}
			break;
		case R.id.replay:
			if(vp.isPlaying()){
				vp.resume();
			}
			break;
		default:
			break;
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(vp != null){
			vp.suspend();
		}
	}
}
