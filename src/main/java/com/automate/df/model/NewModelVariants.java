package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewModelVariants {

	private String sno;

	String name;

	String vehicleId;

	String mileage;

	String enginecc;

	String bhp;

	String orgId;

	String bulkUploadId;

	String isBulkUpload;

	String fuelType;

	String transmissionType;

	String status;

}
