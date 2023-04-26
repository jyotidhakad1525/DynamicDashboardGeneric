package com.automate.df.model;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 
 * @author srujan
 *
 */



@Data
@NoArgsConstructor
public class OrgVerticalLocationPageGroupResponse {

	private int id;
	int group;
	String name;
	int minItems;
	int maxItems;
	String iconClass;
	int noOfCols;
	Role role;
	private List<OrgVerticalLocationPageGroupFieldResponse> fieldList;
}
