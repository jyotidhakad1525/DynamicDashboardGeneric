package com.automate.df.model.oh;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OHNodeUpdateReq {

	Integer orgId;
	Integer dataNodeId;
	String updateNodeNameTo;
}
