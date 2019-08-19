package com.h.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 通知，拍照
 * 
 * @author Cherry
 * @date 2019年6月14日
 */
public class NotifyActivity extends BaseActivity {
	public static final int TAKE_PHOTO = 1;
	public static final int CROP_PHOTO = 2;
	private ImageView picture;
	private Uri imageUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notify_layout);
		this.createListenButton(R.id.take_photo);
		this.createListenButton(R.id.view_photo);
		picture = (ImageView) findViewById(R.id.picture);
		TextView tv = (TextView) findViewById(R.id.B);
		tv.setText("床前明月光，疑是地上霜。举头望明月，低头思故乡。");
		/*
		 * 取消通知栏状态
		 */
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nm.cancel(1);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.take_photo:
			takePhoto();
			break;
		case R.id.view_photo:
			viewPhoto();
			break;
		default:
			break;
		}
	}

	private void viewPhoto() {
		/*
		 * 创建File 对象，用于存储选择的照片
		 */
		File outputImage = new File(Environment.getExternalStorageDirectory(), "output_image.jpg");
		try {
			if (outputImage.exists()) {
				outputImage.delete();
			}
			outputImage.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		imageUri = Uri.fromFile(outputImage);
		Intent intent = new Intent("android.intent.action.GET_CONTENT");
		intent.setType("image/*");
		intent.putExtra("crop", true);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent, CROP_PHOTO);
	}

	/*
	 * 拍照
	 */
	private void takePhoto() {
		// 创建File对象，用于存储拍照后的图片
		File outputImage = new File(Environment.getExternalStorageDirectory(), "tempImage.jpg");
		try {
			if (outputImage.exists()) {
				outputImage.delete();
			}
			outputImage.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		imageUri = Uri.fromFile(outputImage);
		/*
		 * 隐式启动活动
		 */
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		/*
		 * 设置图片存储位置
		 */
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		/*
		 * 启动相机程序
		 */
		startActivityForResult(intent, TAKE_PHOTO); 
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PHOTO:
			if (resultCode == RESULT_OK) {
				/*
				 * 图片处理活动
				 */
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(imageUri, "image/*");
				intent.putExtra("scale", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CROP_PHOTO);
			}
			break;
		case CROP_PHOTO:
			if (resultCode == RESULT_OK) {
				if(null != data){
					imageUri = data.getData();
				}
				try {
					Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
					picture.setImageBitmap(bitmap);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			break;
		default:
			break;
		}
	}
}
