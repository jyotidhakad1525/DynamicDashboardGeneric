package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BulkUploadReq {

	
	String fileuploadName;
	String pageIdentifier;
	String bussinessUnitIdentifier;
	String empId;
	String bulkUploadIdentifier;
	boolean flushAndFill;
	boolean append;
}
