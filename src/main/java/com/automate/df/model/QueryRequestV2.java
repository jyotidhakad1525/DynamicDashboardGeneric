package com.automate.df.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class QueryRequestV2 {
	
	String reportIdentifier;
	String roleIdentifier;
	Integer empId;
	List<WhereRequest> where;
	List<String> groupBy;
	List<String> orderBy;
	String orderByType;
	String fromDate;
	String toDate;
	boolean isPaginationRequired;
	int pageNo;
	int size;
	
}
