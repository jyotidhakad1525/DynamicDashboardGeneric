package com.automate.df.model.oh;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OHEmpLevelMapping {
	
	List<Integer> empId;
	Integer orgId;
	String levelCode;
	List<Integer> nodesIds;
	

}
