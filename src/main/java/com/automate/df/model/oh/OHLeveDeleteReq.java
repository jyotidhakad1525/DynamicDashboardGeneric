package com.automate.df.model.oh;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OHLeveDeleteReq {

	Integer orgId;
	String levelCodeToRemove;
/*	String removedLevelName;
	List<Integer> removedNodeIds;
	String newlyaddedLevelName;
	List<Integer> newlyAddedNodeIds;*/
}
