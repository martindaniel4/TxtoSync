package com.example.txtosync;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.example.txtosync.Login.MyAsyncTask;
import com.example.txtosync.data.SMSData;
import com.google.gson.Gson;
 
public class Sync extends Activity implements OnClickListener {
 
	RelativeLayout layout = null;
	TextView smsDesc = null;
	TextView smsMsg = null;
	String sampleText,token,smsDataJson,smsData2Json;
	int numberText;
		
  @Override
  public void onCreate(Bundle savedInstanceState) {
    
	  // Créer le contenu
	  
	  super.onCreate(savedInstanceState);
	  
	  layout = (RelativeLayout) View.inflate(this, R.layout.sync, null);
	  setContentView(layout);
	  
	  // Déclarer des variables
	  
	  Button btnSync = (Button) findViewById(R.id.buttonSync);
	  btnSync.setOnClickListener(this);
	  
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
					
					sms.setDate(c.getString(c.getColumnIndexOrThrow("date")).toString());
					sms.setNumber(c.getString(c.getColumnIndexOrThrow("address")).toString());
					sms.setBody(c.getString(c.getColumnIndexOrThrow("body")).toString());
					sms.setType(c.getString(c.getColumnIndexOrThrow("type")).toString());
					
					smsList.add(sms);
					
					c.moveToNext();
				} 
			}
			c.close();		
	}
	
	String tab[][] = new String[smsList.size()][4];
	
	     for (int i=0; i<smsList.size(); i++) {
	         tab[i][0]= smsList.get(i).getDate();
	         tab[i][1]= smsList.get(i).getNumber();
	         tab[i][2]= smsList.get(i).getBody();
	         tab[i][3]= smsList.get(i).getType();
	     }
	
	// Convertir le fichier sms en JSON 
	
	smsDataJson = new Gson().toJson(smsList);
	
	smsData2Json = new Gson().toJson(tab);
	
	Log.i("martin",smsData2Json);
	
	
	
	// Afficher le nombre de sms à synchroniser
		
	smsDesc = (TextView) layout.findViewById(R.id.smsDesc);
	smsMsg = (TextView) layout.findViewById(R.id.smsMsg);
	
	numberText = smsList.size();
	
	smsDesc.setText(numberText + " sms to sync");
	
	
	
	// Chopper le token client de l'activité précédente 
	
	Intent i = getIntent();	
	
	token = i.getStringExtra("token");
	
  }
	
	// Post to Txto platform
	

	public class postPlatform extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			Log.i("martin", "init Post to Platform");
			postSmsData(token, smsData2Json);
			
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... progress){
			//pb.setProgress(progress[0]);
		}
		
		@Override
		protected void onPostExecute(String result){
						
			Log.i("martin", "post sms Data executed ! Youhou !");
			
			smsMsg.setText("sms synced ! ");
			
		}
		

		public String postSmsData(String token, String smsDataJson) {

			// Create a new HttpClient and Post Header

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://10.0.2.2:3000/api/v1/sync");
			
			
			try {
				// Ajouter email et password au sein de la requête post

				ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
				
				parameters.add(new BasicNameValuePair("token", token));
				parameters.add(new BasicNameValuePair("data", smsDataJson));
				
				httppost.setEntity(new UrlEncodedFormEntity(parameters));;

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				
				HttpEntity entity = response.getEntity();
				
				String retourResponse = EntityUtils.toString(entity);
				
				Log.i("martin",retourResponse);
				
				String[] essai = retourResponse.split(":");
				
				String taskId = essai[1].replace("}", "");

				return taskId;

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				
				Log.i("martin", e.toString());
				Log.i("martin", "merde 1");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
				Log.i("martin", e.toString());
				Log.i("martin", "merde 2");
			}

			Log.i("martin", "en bas de l'Async");
			
			return null;
		}

	}
         	
  

@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	
	Log.i("martin", "clic envoyé");
	
	new postPlatform().execute();
	
}
 
}