package com.automate.df.model;



import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MasterData {
	
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
	FieldGroup fieldGroup;
}
