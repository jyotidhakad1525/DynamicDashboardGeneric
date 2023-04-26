package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewModel {
	private String sno;

	String orgId;

	String model;

	String imageUrl;

	String createdBy;

	String modifiedBy;

	String createdDate;

	String modifiedDate;

	String status;

	String booking_amount;

	String waiting_period;

	String description;

	String priceRange;

	String maker;

	String makerId;

	String bulkUploadId;

	String isBulkUpload;

	String type;

	String typeCategory;

}
