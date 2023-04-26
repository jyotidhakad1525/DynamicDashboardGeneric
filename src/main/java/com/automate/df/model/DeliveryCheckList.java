package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeliveryCheckList {

		private String itemName;
		private int checkList;
		private int leadId;
		String orgId;
		private String bulkUploadId;
		private String status;
		private String createdAt;
		String updatedAt;
		String createdBy;
		String updatedBy;
		
}
