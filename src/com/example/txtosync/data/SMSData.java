package com.example.txtosync.data;

import java.util.Date;

/**
 * This class represents SMS.
 *
 */
public class SMSData {

	// Number from witch the sms was send
	private String number;
	// SMS text body
	private String body;
	// Date received
	private String date;
	
	public String getNumber() {
		return number;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public String getDate() {
		return date.toString();
	}
	
	public void setDate(Date body) {
		this.date = date.toString();
	}
	
}
