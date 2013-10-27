package com.example.txtosync.data;

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
	private Float date;
	// Type (a.k.a flags)
	private String type;
	
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
	
	public Float getDate() {
		return date;
	}
	
	public void setDate(Float date) {
		this.date = date;
	}
	
	public String getType() {
		return type.toString();
	}
	
	public void setType(String type) {
		this.type = type.toString();
	}
	
}
