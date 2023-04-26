package com.automate.df.error;

import java.util.List;

import com.automate.df.constants.DynamicFormErrorCode;


public class DynamicFormFieldExcelError {

	private DynamicFormErrorCode errorCode;
	private String errorMessage;
	private String recordLineNumber;
	private List<String> errorFields;

	public DynamicFormFieldExcelError(){ }
	
	public DynamicFormFieldExcelError(DynamicFormErrorCode errorCode, String errorMessage,List<String> errorFields,String recordLineNumber){
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.errorFields=errorFields;
		this.recordLineNumber=recordLineNumber;
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

	public String getRecordLineNumber() {
		return recordLineNumber;
	}

	public void setRecordLineNumber(String recordLineNumber) {
		this.recordLineNumber = recordLineNumber;
	}

	public List<String> getErrorFields() {
		return errorFields;
	}

	public void setErrorFields(List<String> errorFields) {
		this.errorFields = errorFields;
	}

	
	
}
