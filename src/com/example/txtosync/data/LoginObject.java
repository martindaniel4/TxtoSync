package com.example.txtosync.data;

public class LoginObject {

	private String authentication_token;
	private String firstname;
	private String error;
	
	public String getToken() {
		return authentication_token;
	}
	
	public void setToken(String authentification_token) {
		this.authentication_token = authentification_token;
	}
	
	public String getFirstname() {
		return firstname;
	}
	
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public String getError() {
		return error;
	}
	
	public void setError(String error) {
		this.error = error;
	}
		
}
