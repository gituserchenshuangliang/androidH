package com.h.demo;

import java.io.File;
import java.io.IOException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 通知消息
 * 
 * @author Cherry
 * @date 2019年6月14日
 */
public class MainActivity extends BaseActivity {
	private EditText to;
	private EditText msgInput;
	private IntentFilter inits;
	private BroadCastA bca;
	private MediaPlayer mp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.createListenButton(R.id.A);
		this.startButtonsActivity(R.id.B, this, MessageList.class);
		this.startButtonsActivity(R.id.video, this, VideoActivity.class);
		this.createListenButton(R.id.send);
		this.createListenButton(R.id.play);
		this.createListenButton(R.id.pause);
		this.createListenButton(R.id.stop);
		to = this.getEditTextObject(R.id.to);
		msgInput = this.getEditTextObject(R.id.msg_input);
		inits = new IntentFilter();
		inits.addAction("SENT_SMS_ACTION");
		bca = new BroadCastA();
		registerReceiver(bca, inits);
		
		/*
		 * 载入音频
		 */
		String song = "樹妮妮Serrini-放棄治療.flac";
		inputAudio(song);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.A:
			sendNotify();
			break;
		case R.id.send:
			sendMsn(to.getText().toString(), msgInput.getText().toString());
			break;
		case R.id.play:
			if(!mp.isPlaying()){
				mp.setLooping(true);
				mp.start();
				Toast.makeText(this, mp.getDuration()+"",Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.pause:
			if(mp.isPlaying()){
				mp.pause();
			}
			break;
		case R.id.stop:
			if(mp.isPlaying()){
				mp.reset();
				String song = "樹妮妮Serrini-其实我…[Demo].flac";
				inputAudio(song);
			}
			break;
		default:
			break;
		}
	}
	
	/*
	 * 初始化音频
	 */
	private void inputAudio(String song){
		mp = new MediaPlayer();
		File file = new File(Environment.getExternalStorageDirectory()+"/song/",song);
		String path = file.getPath();
		try {
			mp.setDataSource(path);
			mp.prepare();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*
	 * 发送通知
	 */
	@SuppressWarnings("deprecation")
	private void sendNotify() {
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		Notification nf = new Notification(R.drawable.arrow_right, "Demo", System.currentTimeMillis());
		/*
		 * 点击通知进入一个活动
		 */
		Intent intent = new Intent(MainActivity.this, NotifyActivity.class);

		PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		
		nf.setLatestEventInfo(this, "测试", "一些测试内容！", pi);
		/*
		 * 通知声音
		 */
//		Uri soundUri = Uri.parse("android.resource://com.h.demo/" + R.raw.freedom);
//		nf.sound = soundUri;
		/*
		 * 震动
		 */
//		long[] vibrates = { 0, 1000, 1000, 1000 };
//		nf.vibrate = vibrates;
		/*
		 * LED灯
		 */
//		nf.ledARGB = Color.GREEN;
//		nf.ledOnMS = 1000;
//		nf.ledOffMS = 1000;
//		nf.flags = Notification.FLAG_SHOW_LIGHTS;
		/*
		 * 所有默认设置
		 */
//		nf.defaults = Notification.DEFAULT_ALL;
		
		/*
		 * 发送通知，1为通知ID
		 */
		nm.notify(1, nf);
	}

	/*
	 * 发送短信
	 */
	public void sendMsn(String phoneNumber, String message) {
		if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
			/*
			 * 系统发短信
			 */
//			Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
//			intent.putExtra("sms_body", message);
//			startActivity(intent);
			/*
			 * 直接发短信
			 */
			SmsManager sms = SmsManager.getDefault();
			Intent in = new Intent("SENT_SMS_ACTION");
			PendingIntent pi = PendingIntent.getBroadcast(this, 0, in, 0);
			sms.sendTextMessage(phoneNumber, null, message, pi, null);
		}else{
			Toast.makeText(this, "the phone number is bad!",Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(bca);
		if(mp != null){
			mp.stop();
			mp.release();
		}
	}
	
	/*
	 * 发送成功给出提示
	 */
	class BroadCastA extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			if(getResultCode() == RESULT_OK){
				Toast.makeText(context, "Send succeeded",Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(context, "Send failed",Toast.LENGTH_LONG).show();
			}
		}
	}
}
