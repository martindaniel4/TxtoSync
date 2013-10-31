package com.example.txtosync;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.Type;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.example.txtosync.Login.MyAsyncTask;
import com.example.txtosync.data.LoginObject;
import com.example.txtosync.data.SMSData;
import com.example.txtosync.data.MeInfo;
import com.example.txtosync.data.SyncObject;
import com.example.txtosync.data.TaskIdObject;
import com.example.txtosync.data.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Sync extends Activity implements OnClickListener {

	RelativeLayout layout = null;
	TextView sms_synced = null;
	TextView sms_to_sync = null;
	TextView alert = null;
	TextView title = null;
	String sampleText, token, smsDataJson, smsData2Json;
	int number_sms_to_sync;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		// Créer le contenu

		super.onCreate(savedInstanceState);

		layout = (RelativeLayout) View.inflate(this, R.layout.sync, null);
		setContentView(layout);

		// Modifier les infos user 
		
		new editUserInfo().execute();
		
		// Chopper le token client de l'activité précédente

		Intent previous_screen = getIntent();

		token = previous_screen.getStringExtra("token");
		
		// Déclarer des variables

		Button btnSync = (Button) findViewById(R.id.buttonSync);
		btnSync.setOnClickListener(this);

		ArrayList smsList = new ArrayList();

		Uri uri = Uri.parse("content://sms/inbox");

		Cursor c = getContentResolver().query(uri, null, null, null, null);
		startManagingCursor(c);

		// Read the sms data and store it in the list

		if (c != null) {
			if (c.moveToFirst()) {
				for (int i = 0; i < c.getCount(); i++) {

					ArrayList sms = new ArrayList();

					sms.add(Float.parseFloat(c.getString(c
							.getColumnIndexOrThrow("date"))));
					sms.add(c.getString(c.getColumnIndexOrThrow("address"))
							.toString());
					sms.add(c.getString(c.getColumnIndexOrThrow("body"))
							.toString());
					sms.add(c.getString(c.getColumnIndexOrThrow("type"))
							.toString());

					smsList.add(sms);

					c.moveToNext();
				}
			}
			c.close();
		}

		// Convertir le fichier sms en JSON

		smsDataJson = new Gson().toJson(smsList);
		

		// Afficher le nombre de sms à synchroniser
		
		sms_synced = (TextView) layout.findViewById(R.id.sms_synced_value);
		sms_to_sync = (TextView) layout.findViewById(R.id.sms_to_sync_value);
		alert = (TextView) layout.findViewById(R.id.alert);
		title = (TextView) layout.findViewById(R.id.title_sync);
		
		number_sms_to_sync = smsList.size();

		sms_to_sync.setText(String.valueOf(number_sms_to_sync));
		
	}

	// Post to Txto platform

	public class postPlatform extends AsyncTask<String, String, String> {

		String taskId = null;

		/*
		 * @Override protected Void doInBackground(Void... arg0) { // TODO
		 * Auto-generated method stub
		 * 
		 * int progress; for (progress=0;progress<=100;progress++) {
		 * publishProgress(progress); progress++; }
		 * 
		 * 
		 * }
		 */

		@Override
		protected void onPreExecute() {
			
			alert.setText(" Initialization. ");
			
		}
		
		
		@Override
		protected String doInBackground(String... params) {

			Log.i("martin", "init Post to Platform");
			
			taskId = postSmsData(token, smsDataJson);

			String progress = null;
			Boolean done = false;
			
			while (done == false) {
				
				TaskIdObject retourGet = getSyncProgress(taskId);
				
				if (retourGet.getPercent() == null ) {
					
			    Log.i("martin","dans le null");
					
				progress = "Please wait";
				
				try {
					
					Thread.sleep(1000L);
					
				} catch (InterruptedException e) {

					e.printStackTrace();
				} 
				
				} else if (Integer.parseInt(retourGet.getPercent()) < 100) {
					
				progress = retourGet.getPercent() + " %";
				
				} else if (Integer.parseInt(retourGet.getPercent()) >=100 ){
					
				progress = retourGet.getPercent() + " %";
				
				done = true;
					
				}
				
				Log.i("martin","progress  "+progress);
								
				publishProgress(progress);		
				
			}

			return null;
		}

		protected void onProgressUpdate(String... progress) {
			// pb.setProgress(progress[0]);

			Log.i("martin", progress[0]);
			
			alert.setText(progress[0]);

		}

		@Override
		protected void onPostExecute(String result) {

			Log.i("martin", "post sms Data executed ! Youhou !");

			alert.setText("sms synced ! ");

		}

		public String postSmsData(String token, String smsDataJson) {
			
			// Create a new HttpClient and Post Header

			HttpClient httpclient = new DefaultHttpClient();
			//HttpPost httppost = new HttpPost("http://10.0.2.2:3000/api/v1/sync");
			HttpPost httppost = new HttpPost("https://www.txto.io/api/v1/sync");

			try {
				// Ajouter email et password au sein de la requête post

				ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();

				parameters.add(new BasicNameValuePair("token", token));
				parameters.add(new BasicNameValuePair("data", smsDataJson));

				httppost.setEntity(new UrlEncodedFormEntity(parameters));
				;

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				HttpEntity entity = response.getEntity();

				String retourResponse = EntityUtils.toString(entity);
				
				Gson gson = new Gson();
				
				SyncObject syncResponse = gson.fromJson(retourResponse, SyncObject.class);
				
				String taskId = syncResponse.getTaskId();
			
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

		public TaskIdObject getSyncProgress(String taskId) {

			// Create a new HttpClient and Post Header
			
			HttpClient httpclient = new DefaultHttpClient();
			//HttpGet httpget = new HttpGet("http://10.0.2.2:3000/api/v1/progress/"+taskId);
			HttpGet httpget = new HttpGet("https://www.txto.io/api/v1/progress/"+taskId);

			try {
				
				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httpget);

				HttpEntity entity = response.getEntity();

				String retourResponse = EntityUtils.toString(entity);
				
				Gson gson = new Gson();
				
				TaskIdObject retourTaskId = gson.fromJson(retourResponse, TaskIdObject.class);
			
				return retourTaskId;

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
	
	
	public class editUserInfo extends AsyncTask<String, String, String> {

		String firstname = null;
		Integer messages_count = 0;
		
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
			MeInfo me = postMeInfo(token);
			
			User user = me.getUser();
			
			firstname = user.getFirstname();
			messages_count = me.getMessages();
			
			runOnUiThread(new Runnable() {
			     public void run() {

			//updates ui
			
			 sms_synced.setText(messages_count.toString());
			 title.setText(firstname+"'s Sync");
						
			    	 
			    	 
			    }
			});
			
			return null;
		}
		
		
		public MeInfo postMeInfo(String token) {

			// Create a new HttpClient and Post Header

			HttpClient httpclient = new DefaultHttpClient();
			//HttpPost httppost = new HttpPost("http://10.0.2.2:3000/api/v1/me");
			HttpPost httppost = new HttpPost("https://www.txto.io/api/v1/me");
			
			
			try {
				// Ajouter email et password au sein de la requête post

				ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();

				parameters.add(new BasicNameValuePair("token", token));

				httppost.setEntity(new UrlEncodedFormEntity(parameters));

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				HttpEntity entity = response.getEntity();

				String retourResponse = EntityUtils.toString(entity);
				
				Gson gson = new Gson();
				
				MeInfo meinfo = gson.fromJson(retourResponse, MeInfo.class);
			
				return meinfo;

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