package com.automate.df.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QueryRequest {

	String tableName;
	List<WhereRequest> where;
	List<String> columns;
	List<String> groupBy;
	String orderBy;
	String fromDate;
	String toDate;
	
}
