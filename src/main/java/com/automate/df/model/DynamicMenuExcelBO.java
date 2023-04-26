package com.automate.df.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DynamicMenuExcelBO {

	private String sno;
	private String applicationType;
	private String applicationModule;
	private String businessSegment;
	private String businessType;
	private String businessLocation;
	private String menu;
	private String menuRedirectPath;
	private String menuPosition;
	private String subMenu;
	private String subMenuRedirectPath;
	private String subMenuPosition;
	private String stage;
	private String role;

	@Override
	public String toString() {

		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);

	}

}
