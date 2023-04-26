package com.automate.df.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DynamicRoleFormBO {

	private String sno;
	private String uuid;
	private String pageId;
	private String pageName;
	private String pageGroupId;
	private String pageGroupName;
	private String fieldId;
	private String fieldName;
	private String roleIdentifier;
	private String permissionType;
	private String mode;

	@Override
	public String toString() {

		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);

	}

}
