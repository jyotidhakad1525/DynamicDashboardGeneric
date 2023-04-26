package com.automate.df.model.df.dashboard;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DropRes {

	Integer modelId;
	String modelName;
	Integer dropCount;
	Long dropAmount;
	String dropPercentage;
}
