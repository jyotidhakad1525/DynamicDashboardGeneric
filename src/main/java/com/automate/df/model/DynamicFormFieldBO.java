package com.automate.df.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DynamicFormFieldBO {

	private String sno;
	private String uuid;
	private String pageId;
	private String pageName;
	private String pageNumOfColumns;
	private String pagePosition;
	private String pagePojo;
	private String pageJson;
	private String pageEndPoint;
	private String pageGroup;
	private String pageGroupName;
	private String pageGroupPosition;
	private String pageGroupMinItems;
	private String pageGroupMaxItems;
	private String pageGroupIconCls;
	private String pageGroupNumOfColumns;
	private String fieldId;
	private String fieldName;
	private String label;
	private String css;
	private String toolTip;
	private String defaultText;
	private String identifier;
	private String dependsOn;
	private String domType;
	private String domInputType;
	private String dtStatic;
	private String dtChoices;
	private String dtUrl;
	private String dtAttributes;
	private String fieldDefAttributes;
	private String required;
	private String requiredValidationMsg;
	private String lengthValidationMsg;
	private String regExpression;
	private String validJsFunc;
	private String validJsFuncOn;
	private String validJsFuncMsg;
	private String validJsFuncMsgKey;
	private String minLength;
	private String maxLength;
	private String uiIdentifier;
	
	



	@Override
	public String toString() {
		
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
		
	}
	
	
	

}
