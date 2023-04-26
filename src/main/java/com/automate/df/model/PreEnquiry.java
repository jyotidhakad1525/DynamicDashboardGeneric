package com.automate.df.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PreEnquiry {

	private String sno;
	private String firstName;
	private String lastName;
	private String address;
	private String type;
	private String colour;
	private String model;
	private int bulkUploadId;
	private String parentIdentifier;

	@Override
	public String toString() {

		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);

	}

}
