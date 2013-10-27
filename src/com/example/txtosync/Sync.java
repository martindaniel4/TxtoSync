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
import com.example.txtosync.data.SMSData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Sync extends Activity implements OnClickListener {

	RelativeLayout layout = null;
	TextView smsDesc = null;
	TextView smsMsg = null;
	String sampleText, token, smsDataJson, smsData2Json;
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
		
		Log.i("martin",smsDataJson.toString());

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
			
			smsMsg.setText(" Initialization. ");
		}
		
		
		@Override
		protected String doInBackground(String... params) {

			Log.i("martin", "init Post to Platform");
			
			taskId = postSmsData(token, smsDataJson);

			String progress = null;
			Boolean done = false;
			
			while (done == false) {
				
				String retourGet = getSyncProgress(taskId);
				
				String progressValue = retourGet.split(":")[2].replace("}", "").replaceAll("^\"|\"$", "");
				
				Log.i("martin",progressValue);
				
				if (progressValue.equals("null") ) {
					
				progress = "Please wait";
				
				try {
					
					Thread.sleep(1000L);
					
				} catch (InterruptedException e) {

					e.printStackTrace();
				} 
				
				} else if (Integer.parseInt(progressValue) < 100) {
					
				progress = progressValue + " %";
				
				} else if (Integer.parseInt(progressValue) >=100 ){
					
				progress = progressValue + " %";
				
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
			
			smsMsg.setText(progress[0]);

		}

		@Override
		protected void onPostExecute(String result) {

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

				httppost.setEntity(new UrlEncodedFormEntity(parameters));
				;

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				HttpEntity entity = response.getEntity();

				String retourResponse = EntityUtils.toString(entity);

				String taskId = retourResponse.split(":")[1].replace("}", "").replaceAll("^\"|\"$", "");

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

		public String getSyncProgress(String taskId) {

			// Create a new HttpClient and Post Header
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(
					"http://10.0.2.2:3000/api/v1/progress/"+taskId);
			

			try {
				
				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httpget);

				HttpEntity entity = response.getEntity();

				String retourResponse = EntityUtils.toString(entity);

				return retourResponse;

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