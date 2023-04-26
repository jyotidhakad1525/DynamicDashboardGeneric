package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OtherMaker  {
	private String sno;	
	String otherMaker;
	String vehicleSegment;
	String status;
	String orgId;
    String bulkUploadId;
	String createdBy;
	String updatedBy;
	String createdAt;
	String updatedAt;
	String isBulkUpload;


}