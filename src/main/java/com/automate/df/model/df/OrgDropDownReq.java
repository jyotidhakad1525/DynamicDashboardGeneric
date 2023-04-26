package com.automate.df.model.df;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrgDropDownReq {

	String orgId;
	Integer levelId;
	Integer parentLevelId;
}
