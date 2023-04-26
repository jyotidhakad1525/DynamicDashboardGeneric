package com.automate.df.model.oh;


import java.util.List;

import com.automate.df.entity.oh.LocationNodeData;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LocationDefNodeRes {

	private int id;
	String locationNodeDefName;
	String locationNodeDefType;
	int parentId;
	int orgId;
	String displayName;
	List<LocationNodeData> nodes;
	boolean leafLevel;
}

