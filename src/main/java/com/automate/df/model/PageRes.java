package com.automate.df.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PageRes {
	
	private int id;
	String UUID;
	int pageId;
	String name;
	int noOfClns;
	String endPoint;
	private List<GroupRes> groupList;

}
