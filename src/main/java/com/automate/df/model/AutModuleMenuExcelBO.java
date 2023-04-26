package com.automate.df.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AutModuleMenuExcelBO {

	private String sno;
	private String uuid;
	private String roleIdentifier;
	private String meniId;
	private String name;
	private String pageIdentifier;
	private String parentIdentifier;
	private String mode;
	private String redirectUrl;

	@Override
	public String toString() {

		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);

	}

}
