package com.automate.df.model.oh;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OHEmpLevelUpdateMapReq {

	List<Integer> empId;
	Integer orgId;
	boolean removeCurrentActiveLevel;
	List<Integer> removeActiveNodeIds;
	String newLevelAdded;
	List<Integer> newlyAddedNodeIds;
	List<Integer> updateNodesInCurrentLevel;

}
