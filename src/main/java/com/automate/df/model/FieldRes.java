package com.automate.df.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FieldRes {

	private int id;
	int fieldId;
	String identifier;
	boolean editable;
	String dependsOn;
	String domType;
	String domInputType;
	String dtStatic;
	String dtChoices;
	String dtUrl;
	String dtAttributes;
	String name;
	String label;
	String css;
	String toolTip;
	String defaultText;
	String fieldDefAttributes;
	boolean required;
	String requiredValidationMsg;
	int minLength;
	int maxLength;
	String lengthValidationMsg;
	String regExpression;
	String validJsFunc;
	String validJsFuncMsgKey;
	String validJsFuncMsg;
	String validFuncOn;
	String uiIdentifier;
}
