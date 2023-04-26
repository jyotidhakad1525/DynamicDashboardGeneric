package com.automate.df.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DFFieldRes {

	int busSegId;
	String busSegName;
	int busLocId;
	String busLocName;
	
	int busTypeId;
	String busTypeName;
	
	int appModuleId;
	String appModuleName;
	int formKeyMappingId;
	
	String stageName;
	int stageId;
	SubStageRes subStages;
	
	
	FieldGroup fieldGroup;
	boolean isSubStage;
	
	private int stageCols;
	private String stageOrientation;
	
	
}
