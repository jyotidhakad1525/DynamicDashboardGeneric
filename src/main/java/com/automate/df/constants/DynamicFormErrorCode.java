package com.automate.df.constants;

public enum DynamicFormErrorCode {

	EXCEPTION_CLASS("Exception"),
	EXCEPTION_MESSAGE(""),
	INVALID_DYNMIC_FORM_EXCELL_DATA("Invalid Form Fields Data");

	
	private String value;

	private DynamicFormErrorCode(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
