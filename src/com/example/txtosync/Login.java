package com.example.txtosync;

import java.io.IOException;
import java.util.ArrayList;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.txtosync.data.LoginObject;
import com.google.gson.Gson;

// Cr�er ma classe et l'�tendre � l'activit� Android

public class Login extends Activity implements OnClickListener {
	
	// Initializing variables
	EditText inputEmail;
	EditText inputPassword;
	TextView loginError;

	// Cr�er ma vue

	@Override
	public void onCreate(Bundle savedInstanceState) {

		// Cr�er le contenu 

		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		// D�clarer les variables

		inputEmail = (EditText) findViewById(R.id.email);
		inputPassword = (EditText) findViewById(R.id.password);
		loginError = (TextView) findViewById(R.id.error);
		Button btnNextScreen = (Button) findViewById(R.id.login);
		btnNextScreen.setOnClickListener(this);

	}
	
	public class MyAsyncTask extends AsyncTask<String, Void, String>{

		LoginObject login;
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			Log.i("martin", "trying to connect");
			login = postData(params[0], params[1]);
			
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... progress){
			//pb.setProgress(progress[0]);
		}
		
		@Override
		protected void onPostExecute(String result){
			
			Intent sync = new Intent(Login.this, Sync.class);
			
			if (login.getError() != null && !"".equals(login.getError())) {
												
				loginError.setText(login.getError());
				
			} else  {
				
				loginError.setText("Success !");
				
				sync.putExtra("token", login.getToken());
				
				Login.this.startActivity(sync);
				
				Log.i("martin", "post executed");
				
			}
			
			
		
		}
		

		public LoginObject postData(String email, String password) {

			// Create a new HttpClient and Post Header

			HttpClient httpclient = new DefaultHttpClient();
			//HttpPost httppost = new HttpPost("http://10.0.2.2:3000/api/v1/login");
			HttpPost httppost = new HttpPost("https://www.txto.io/api/v1/login");
			
			
			try {
				// Ajouter email et password au sein de la requ�te post

				ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();

				parameters.add(new BasicNameValuePair("email", email));
				parameters.add(new BasicNameValuePair("password", password));
				
				httppost.setEntity(new UrlEncodedFormEntity(parameters));;

				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				
				HttpEntity entity = response.getEntity();
				
				String retourResponse = EntityUtils.toString(entity);
				
				Gson gson = new Gson();
				
				LoginObject login = gson.fromJson(retourResponse, LoginObject.class);
				
				return login;

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
	public void onClick(View arg0) {
	    // TODO Auto-generated method stub
		
		Log.i("martin", "clic envoy�");
		
		String[] params =  new String[2];
		
		params[0] = inputEmail.getText().toString();
		params[1] = inputPassword.getText().toString();
		
	    new MyAsyncTask().execute(params);
	    
	}
	
	public void signUpPage(View arg0) {
	
		Intent signUp = new Intent(Login.this, SignUp.class);
		
		Login.this.startActivity(signUp);
		
	}
}



