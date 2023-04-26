package com.automate.df.model.oh;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OHLevelReq {
	
	Integer orgId;
	Integer empId;
	String empName;
	List<LevelReq> levelList; 

}
