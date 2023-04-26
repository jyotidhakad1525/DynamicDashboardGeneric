package com.automate.df.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 
 * @author srujan
 *
 */

@Table(name="organisation_vertical_location_page_group_field")
@Entity
@Data
@NoArgsConstructor
public class OrgVerticalLocationPageGroupField {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="field_id")
	int fieldId;
	
	@Column(name="org_acc_loc_page_group_id")
	int pageGroupId;
	
	@Column(name="identifier")
	String identifier;
	
	@Column(name="depends_on")
	String dependsOn;

	@Column(name="dom_type")
	String domType;
	
	@Column(name="dom_input_type")
	String domInputType;
	
	@Column(name="dt_static")
	String dtStatic;
	
	
	@Column(name="dt_choices")
	String dtChoices;
	
	@Column(name="dt_url")
	String dtUrl;

	@Column(name="dt_attributes")
	String dtAttributes;
	
	@Column(name="name")
	String name;
	
	@Column(name="label")
	String label;
	
	@Column(name="css")
	String css;
	
	@Column(name="tool_tip")
	String toolTip;

	@Column(name="default_text")
	String defaultText;
	
	@Column(name="field_def_attributes")
	String fieldDefAttributes;
	
	@Column(name="required")
	boolean required;
	
	@Column(name="required_validation_msg")
	String requiredValidationMsg;
	
	@Column(name="min_length")
	int minLength;

	@Column(name="max_length")
	int maxLength;
	
	@Column(name="length_validation_msg")
	String lengthValidationMsg;
	
	@Column(name="reg_expression")
	String regExpression;
	
	@Column(name="valid_js_func")
	String validJsFunc;
	
	@Column(name="valid_js_func_msg_key")
	String validJsFuncMsgKey;
	
	@Column(name="valid_js_func_msg")
	String validJsFuncMsg;
	
	@Column(name="valid_func_on")
	String validFuncOn;
	
	@Column(name="created_at")
	Timestamp createdAt;
	
	@Column(name="updated_at")
	Timestamp updated_at;
	
	@Column(name="ui_identifier")
	String uiIdentifier;
	
	
}
