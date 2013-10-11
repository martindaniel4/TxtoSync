package com.example.txtosync;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.txtosync.data.SMSData;
 
public class Sync extends Activity {
 
	RelativeLayout layout = null;
	TextView smsDesc = null;
	TextView text = null;
	String sampleText,token;
	int numberText;
	
  @Override
  public void onCreate(Bundle savedInstanceState) {
    
	  super.onCreate(savedInstanceState);
    
   List<SMSData> smsList = new ArrayList<SMSData>();
    
    Uri uri = Uri.parse("content://sms/inbox");
	Cursor c= getContentResolver().
			query(uri, null, null ,null,null);
	startManagingCursor(c);
    
	// Read the sms data and store it in the list
			
	if(c!=null) {
			if(c.moveToFirst()) {
				for(int i=0; i < c.getCount(); i++) {
					SMSData sms = new SMSData();
					sms.setBody(c.getString(c.getColumnIndexOrThrow("body")).toString());
					sms.setNumber(c.getString(c.getColumnIndexOrThrow("address")).toString());
					smsList.add(sms);
					
					c.moveToNext();
				} 
			}
			c.close();
				}
	layout = (RelativeLayout) View.inflate(this, R.layout.sync, null);
	
	smsDesc = (TextView) layout.findViewById(R.id.smsDesc);
	text = (TextView) layout.findViewById(R.id.sms1);
	
	numberText = smsList.size();
	
	sampleText = smsList.get(0).getBody();
	
	// get client token from previous activity
	
	Intent i = getIntent();	
	
	String token = i.getStringExtra("token");
	
	smsDesc.setText(numberText + " sms to sync");
	text.setText(token);
         
    setContentView(layout);
	
  }
 
}