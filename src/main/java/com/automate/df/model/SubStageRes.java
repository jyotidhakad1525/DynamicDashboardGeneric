package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubStageRes {

	int subStageId;
	String subStageName;
	String positionLevel;
	String subStageLabel;
	int columns;
	int minItems;
	int maxItems;
	
}
