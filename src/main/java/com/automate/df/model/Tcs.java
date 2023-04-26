package com.automate.df.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Tcs {
	private String sno;
	String model;
	String variant;
	String price;
	String enterTCS;
	String status;
	String orgId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	String startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	String endDate;
    String bulkUploadId;  
	String createdBy;
	String updatedBy;
	String isBulkUpload;
}