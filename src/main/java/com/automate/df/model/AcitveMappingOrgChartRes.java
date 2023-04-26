package com.automate.df.model;

import java.util.List;
import java.util.Map;

import com.automate.df.entity.oh.LocationNodeData;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AcitveMappingOrgChartRes {

	Map<String,List<LocationNodeData>> map;
	
}
