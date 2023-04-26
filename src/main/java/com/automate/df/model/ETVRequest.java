package com.automate.df.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ETVRequest {

	String orgId;
	String parentBranchId;
	List<String> branchIdList;
	String fromDate;
	String toDate;
}
