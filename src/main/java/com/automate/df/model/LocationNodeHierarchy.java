package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LocationNodeHierarchy {
	private String sno;
	String createdBy;
	String createdOn;
	String modifiedBy;
	String modifiedOn;
	String version;
	String locationNodeDefName;
	String parentId;
	String tenentId;
	String locationNodeDefType;
	String displayName;
	String active;
	private String orgId;
	private String bulkUploadId;
}
