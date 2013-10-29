package com.example.txtosync.data;

public class User {
	
	// Number from witch the sms was send
		private String created_at;
		// SMS text body
		private Float credit_kkbb;
		// Date received
		private String email;
		// Type (a.k.a flags)
		
		private Integer id; 
		
		private String firstname;
		
		private String lastname;
		
		private String paymill_client_id;
		
		private String updated_at;
		
		private Integer uploaded;
	
		public String getCreatedAt() {
			return created_at;
		}
		
		public void setNumber(String number) {
			this.created_at = number;
		}
		
		public Float getCredit() {
			return credit_kkbb;
		}
		
		public void setBody(Float credit) {
			this.credit_kkbb = credit;
		}
		
		public String getEmail() {
			return email;
		}
		
		public void setEmail(String email) {
			this.email = email;
		}
		
		public String getFirstname() {
			return firstname;
		}
		
		public void setFirstname(String firstname) {
			this.firstname = firstname;
		}
		public String getLastname() {
			return lastname;
		}
		
		public Integer getId() {
			return id;
		}
		
		public void setId(Integer id) {
			this.id = id;
		}
		
		public void setLastname(String lastname) {
			this.lastname = lastname;
		}
		public String getPaymill() {
			return paymill_client_id;
		}
		
		public void setPaymill(String paymill_client_id) {
			this.paymill_client_id = paymill_client_id;
		}
		public String getUpdated() {
			return updated_at;
		}
		
		public void setUpdated(String updated_at) {
			this.updated_at = updated_at;
		}

		public Integer getUploaded() {
			return uploaded;
		}
		
		public void setUploaded(Integer uploaded) {
			this.uploaded = uploaded;
		}

}
