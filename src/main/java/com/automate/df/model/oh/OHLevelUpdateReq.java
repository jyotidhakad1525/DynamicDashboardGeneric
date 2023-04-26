package com.automate.df.model.oh;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OHLevelUpdateReq {
	
	Integer empId;
	Integer orgId;
	String levelCode;
	String updateDisplayName;
	List<Integer> removeDataNodes;
	
}
