package com.example.txtosync;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.os.AsyncTask;

public class LoginRequest extends AsyncTask <ArrayList<NameValuePair>, Void, String> {

	private Exception exception;
	private ArrayList<NameValuePair> parameters;
	
	@Override
	protected String doInBackground(ArrayList<NameValuePair>... String) {
		
		 // Construire la requête
        
        String response = null;
        try {
            response = CustomHttpClient.executeHttpPost("http://localhost:3000/api/v1/login", parameters);
            String res=response.toString();
            res= res.replaceAll("\\s+","");
            
        } catch (Exception e) {
        	this.exception = e;
        }
		
		return response;
	}

}