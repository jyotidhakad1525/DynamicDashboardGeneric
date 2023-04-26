package com.automate.df.model.df.dashboard;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LostRes {

	Integer modelId;
	String modelName;
	Integer lostCount;
	Long lostAmount;
	String lostPercentage;
	
	
}
