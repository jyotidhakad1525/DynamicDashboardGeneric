package com.automate.df.model;

import java.util.Date;


public class ErrorDetails {
	
	
		private Date timeStamp;
		private String message;
		private String details;
		private String code;
		public ErrorDetails(Date timeStamp, String message, String details, String code) {
			super();
			this.timeStamp = timeStamp;
			this.message = message;
			this.details = details;
			this.code = code;
		}
		public Date getTimeStamp() {
			return timeStamp;
		}
		public void setTimeStamp(Date timeStamp) {
			this.timeStamp = timeStamp;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public String getDetails() {
			return details;
		}
		public void setDetails(String details) {
			this.details = details;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		@Override
		public String toString() {
			return "ErrorDetails [timeStamp=" + timeStamp + ", message=" + message + ", details=" + details + ", code="
					+ code + "]";
		}
		
		

}
