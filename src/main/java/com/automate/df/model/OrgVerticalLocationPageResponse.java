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
public class OrgVerticalLocationPageResponse {

	private int id;
	String UUID;
	int pageId;
	String name;
	int noOfClns;
	String endPoint;
	Role role;
	private List<OrgVerticalLocationPageGroupResponse> groupList;
}
