package com.h.demo;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author Cherry
 * @date  2019年6月6日
 */
public class BaseActivity extends Activity implements OnClickListener{
	protected static final String TAG = "BaseActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		 * 输出当前活动名称
		 */
		Log.d(TAG,getClass().getSimpleName());
		/*
		 * 活动的添加到管理器
		 */
		ActivityManager.addActivity(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		/*
		 * 从管理器移除活动
		 */
		ActivityManager.removeActivity(this);
	}
	
	/*
	 * 创建监听返回按钮对象
	 */
	public  Button returnCreateListenButton(int r){
		Button btn =  (Button) findViewById(r);
		btn.setOnClickListener(this);
		return btn;
	}
	
	/*
	 * 创建监听按钮对象
	 */
	public  void createListenButton(int r){
		Button btn =  (Button) findViewById(r);
		btn.setOnClickListener(this);
	}
	
	/*
	 * 直接点击按钮进入活动
	 */
	@SuppressWarnings("rawtypes")
	public void startButtonsActivity(int r,final Context context,final Class activityClass){
		Button btn =  (Button) findViewById(r);
		btn.setOnClickListener(this);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent ints = new Intent(context, activityClass);
				startActivity(ints);
			}
		});
	}
	
	/*
	 * 根据ID输入框文本
	 */
	public String getEditTextString(int id){
		EditText et = (EditText) findViewById(id);
		String msg = null;
		if(null != et){
			msg = et.getText().toString();
		}
		return msg;
	}
	
	/*
	 * 根据ID输入框对象
	 */
	public EditText getEditTextObject(int id){
		EditText et = (EditText) findViewById(id);
		if(null == et){
			throw new NullPointerException("EditText对象为空！");
		}
		return et;
	}
	@Override
	public void onClick(View v) {
	}
	
	/**
	 * 活动管理器
	 * 统一管理活动
	 * @author Cherry
	 * @date  2019年6月5日
	 */
	static class ActivityManager {
		private static List<Activity> activities = new ArrayList<Activity>();
		
		/*
		 * 添加活动到管理器
		 */
		public static void addActivity(Activity act){
			activities.add(act);
		}
		
		/*
		 * 从管理器移除活动
		 */
		public static void removeActivity(Activity act){
			activities.remove(act);
		}
		
		/*
		 * 退出所有活动
		 */
		public static void exitAllActivity(){
			for (Activity activity : activities) {
				if(null != activity){
					activity.finish();
				}
			}
		}
	}
}
