package com.example.txtosync;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

// Créer ma classe et l'étendre à l'activité Android

public class Login extends Activity implements OnClickListener {

	// Initializing variables
	EditText inputEmail;
	EditText inputPassword;
	TextView loginError;

	// Créer ma vue

	@Override
	public void onCreate(Bundle savedInstanceState) {

		// Créer le contenu 

		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		// Déclarer les variables

		inputEmail = (EditText) findViewById(R.id.email);
		inputPassword = (EditText) findViewById(R.id.password);
		loginError = (TextView) findViewById(R.id.error);
		Button btnNextScreen = (Button) findViewById(R.id.login);
		btnNextScreen.setOnClickListener(this);

	}
	


	// Construire ma requête au web-service
	/*
public String postData(String email, String password) {

    // Create a new HttpClient and Post Header

    HttpClient httpclient = new DefaultHttpClient();
    HttpPost httppost = new HttpPost("http://localhost:3000/api/v1/login");

    try {
      // Ajouter email et password au sein de la requête post

    	ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();

      parameters.add(new BasicNameValuePair("email", email));
      parameters.add(new BasicNameValuePair("password", password));

      httppost.setEntity(new UrlEncodedFormEntity(parameters));

      // Execute HTTP Post Request
      HttpResponse response = httpclient.execute(httppost);

      return response.toString();

    } catch (ClientProtocolException e) {
      // TODO Auto-generated catch block
    } catch (IOException e) {
      // TODO Auto-generated catch block
    }
	return null;

  }

	 */
	private class MyAsyncTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			Log.i("martin", "trying to connect");
			postData(params[0], params[1]);
			return null;
		}

		protected void onProgressUpdate(Void... progress){
			//pb.setProgress(progress[0]);
		}
		
		protected void onPostExecute(String result){
			//pb.setVisibility(View.GONE);
			//Toast.makeText(getApplicationContext(), "command sent", Toast.LENGTH_LONG).show();
			Log.i("martin", "post executed");
			//Log.i("martin", result);
		}
		

		public String postData(String email, String password) {

			// Create a new HttpClient and Post Header

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://localhost:3000/api/v1/login");

			try {
				// Ajouter email et password au sein de la requête post

				ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();

				parameters.add(new BasicNameValuePair("email", email));
				parameters.add(new BasicNameValuePair("password", password));

				httppost.setEntity(new UrlEncodedFormEntity(parameters));
				
				Log.i("martin", parameters.toString());

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);

				Log.i("martin", "Ok tout est bon");
				
				return response.toString();

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				
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
	public void onClick(View arg0) {
	    // TODO Auto-generated method stub
		
		Log.i("martin", "clic envoyé");
		
		String[] params =  new String[2];
		
		params[0] = inputEmail.getText().toString();
		params[1] = inputPassword.getText().toString();
		
	    new MyAsyncTask().execute(params);    
	    
	}
}



