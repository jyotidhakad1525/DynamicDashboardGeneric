package com.automate.df.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DynamicMenuFormBO {

	private String name;
	private String orgIdentifier;
	private int pageIdentifier;
	private int roleIdentifier;
	private String permissionType;
	private String mode;

	@Override
	public String toString() {

		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);

	}

}
