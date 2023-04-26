package com.automate.df.model.df.dashboard;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TargetAchivement {
	
	String paramName;
	String paramShortName;
	String target;
	String achievment;
	String achivementPerc;
	String shortfall;
	String shortFallPerc;
	List<Object> data;

}
