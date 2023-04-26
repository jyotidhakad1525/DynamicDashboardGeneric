package com.automate.df.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TemplateOptions {

	String label;
	String toolTip;
	String placeHolder;
	String type;
	boolean required;
	boolean disabled;
	boolean validation;
	String message;
	int minLength;
	int maxLength;
	List<DataOptions> options;
	
	
}
