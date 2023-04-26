package com.automate.df.model;

import java.util.List;

import com.automate.df.error.DynamicUploadExcelError;


public class DynmMenuExcelResponseDO {

	private String message;

	private String status;
	
	private List<DynamicUploadExcelError> dynamicFormFieldExcelError;
	
	public DynmMenuExcelResponseDO(String message, String status,List<DynamicUploadExcelError> dynamicFormFieldExcelErrors) {
		super();
		this.message = message;
		this.status = status;
		this.dynamicFormFieldExcelError=dynamicFormFieldExcelErrors;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<DynamicUploadExcelError> getDynamicFormFieldExcelError() {
		return dynamicFormFieldExcelError;
	}

	public void setDynamicFormFieldExcelError(List<DynamicUploadExcelError> dynamicFormFieldExcelError) {
		this.dynamicFormFieldExcelError = dynamicFormFieldExcelError;
	}



}
