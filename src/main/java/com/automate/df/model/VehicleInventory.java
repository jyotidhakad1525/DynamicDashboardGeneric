package com.automate.df.model;

import java.sql.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VehicleInventory {

	private String sno;
	String model;
	String variant;
	String colour;
	String fuel;
	String orgId;
	String bulkUploadId;
	String transmission;
	String vinNumber;
	String purchaseDate;
	String chassis_no;
	String engineno;
	String make;
	String stage;
	String ageing;
	String status;
	String createdBy;
	String updatedBy;
	String createAt;
	String isBulkUpload;
	String updatedAt;

}
