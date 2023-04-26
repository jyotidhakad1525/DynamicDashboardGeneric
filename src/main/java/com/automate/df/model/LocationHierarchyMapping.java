package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LocationHierarchyMapping{
	private String sno;
	String createdBy;
	String createdOn;
	String modifiedBy;
	String modifiedOn;
	String version;
	String cananicalName;
	String code;
	String name;
	String parentId;
	String tenantId;
	String type;
	String refParentId;
	String locationNodeDefId;
	String active;
	String leafNode;
	private String orgId;
	private String bulkUploadId;
}
