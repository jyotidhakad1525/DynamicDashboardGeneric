package com.automate.df.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupRes {

	private int id;
	int group;
	String name;
	int minItems;
	int maxItems;
	String iconClass;
	int noOfCols;
	
	private List<FieldRes> fieldList;
}
