package com.automate.df.error;

import com.automate.df.constants.DynamicFormErrorCode;

/**
 * @author sgogusetty
 *
 */
public class Error {

	private DynamicFormErrorCode errorCode;
	private String errorMessage;

	public Error(){ }
	
	public Error(DynamicFormErrorCode errorCode, String errorMessage){
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public DynamicFormErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(DynamicFormErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}
