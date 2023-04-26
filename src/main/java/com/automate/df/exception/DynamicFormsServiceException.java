package com.automate.df.exception;

import org.springframework.http.HttpStatus;

public class DynamicFormsServiceException extends Exception{
	
	/**
	 * 
	 */
	
	
	
	
	private static final long serialVersionUID = 1L;
	private HttpStatus statusCode;

	public DynamicFormsServiceException(String msg) {
		super(msg);
	}
	
	public DynamicFormsServiceException(String msg,HttpStatus statusCode) {
		super(msg);
		this.statusCode=statusCode;
	}
	
	public DynamicFormsServiceException(String msg,Throwable t) {
		super(msg,t);
	}
	
	public HttpStatus getStatusCode() {
		return statusCode;
	}

}
