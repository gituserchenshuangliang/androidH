package com.h.demo;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
/**
 * 读取手机短信
 * @author Cherry
 * @date  2019年6月14日
 */
public class MessageList extends BaseActivity {
	private ArrayList<String> list = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.sms_layout);
		getSmsFromPhone();
		ListView view = (ListView) findViewById(R.id.msg);
		ArrayAdapter<String> ad = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 , list);
		view.setAdapter(ad);
	}
	
	 public void getSmsFromPhone() {  
	        ContentResolver cr = getContentResolver();  
	        /*
	         * 短信表字段："_id", "address", "person",, "date", "type",....
	         */
	        String[] projection = new String[] { "body","address" }; 
	        Cursor cur = cr.query(Uri.parse("content://sms/inbox"), projection, null, null, "date desc");  
	        if (null == cur)  
	            return;  
	        if (cur.moveToFirst()) {  
	            String number = cur.getString(cur.getColumnIndex("address"));
	            String body = cur.getString(cur.getColumnIndex("body"));  
	            list.add(number+"\n"+body);
	        }  
	    }
}
