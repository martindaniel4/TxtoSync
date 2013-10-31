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



import com.example.txtosync.data.LoginObject;
import com.google.gson.Gson;

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

public class SignUp extends Activity implements OnClickListener {

	// Initializing variables
	EditText inputFirstname;
	EditText inputLastname;
	EditText inputEmail;
	EditText inputPassword;
	TextView loginScreen;
	TextView loginError;
	
	// Créer ma vue

	@Override
	public void onCreate(Bundle savedInstanceState) {

		// Créer le contenu 

		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);

		// Déclarer les variables
		
		inputFirstname = (EditText) findViewById(R.id.firstname);
		inputLastname= (EditText) findViewById(R.id.lastname);
		inputEmail = (EditText) findViewById(R.id.email);
		inputPassword = (EditText) findViewById(R.id.password);
		loginError = (TextView) findViewById(R.id.error);
		loginScreen = (TextView) findViewById(R.id.loginPage);
		Button btnNextScreen = (Button) findViewById(R.id.signup);
		btnNextScreen.setOnClickListener(this);
		loginScreen.setOnClickListener(loginPage);
		
	}
	
	public class MyAsyncTask extends AsyncTask<String, Void, String>{

		LoginObject signUp;
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			Log.i("martin", "trying to connect");
			signUp = postData(params[0], params[1], params[2], params[3]);
			
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... progress){
			//pb.setProgress(progress[0]);
		}
		
		@Override
		protected void onPostExecute(String result){
			
			Intent sync = new Intent(SignUp.this, Sync.class);
			
			if (signUp.getError() != null && !"".equals(signUp.getError())) {
												
				loginError.setText(signUp.getError());
				
			} else  {
				
				loginError.setText("Success !");
				
				sync.putExtra("token", signUp.getToken());
				
				SignUp.this.startActivity(sync);
				
				Log.i("martin", "post executed");
				
			}
			
			
		
		}
		

		public LoginObject postData(String firstname, String lastname, String email, String password) {

			// Create a new HttpClient and Post Header

			HttpClient httpclient = new DefaultHttpClient();
			//HttpPost httppost = new HttpPost("http://10.0.2.2:3000/api/v1/register");
			HttpPost httppost = new HttpPost("https://www.txto.io/api/v1/register");
			
			try {
				// Ajouter email et password au sein de la requête post

				ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
				
				parameters.add(new BasicNameValuePair("firstname", firstname));
				parameters.add(new BasicNameValuePair("lastname", lastname));
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
		
		Log.i("martin", "clic envoyé");
		
		String[] params =  new String[4];
		
		params[0] = inputFirstname.getText().toString();
		params[1] = inputLastname.getText().toString();
		params[2] = inputEmail.getText().toString();
		params[3] = inputPassword.getText().toString();
		
	    new MyAsyncTask().execute(params);
		
	}
	
	
	private OnClickListener loginPage = new OnClickListener()
	{
	    public void onClick(View view)
	    {                 
			Intent login = new Intent(SignUp.this, Login.class);
			
			SignUp.this.startActivity(login);
	    }
	};

}
