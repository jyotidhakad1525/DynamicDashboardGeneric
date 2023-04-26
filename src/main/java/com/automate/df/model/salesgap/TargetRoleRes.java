package com.automate.df.model.salesgap;



import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TargetRoleRes implements Comparable<TargetRoleRes> {

	String empId;
	String orgId;
	String orgName;
	String branchId;
	String branchName;
	String locationId;
	String locationName;
	String roleName;
	String roleId;
	String deptName;
	String deptId;
	String designationName;
	String designationId;
	String salary;
	String experience;
	int precedence;
	String hrmsRole;
	Integer level;
	List<String> orgMapBranches;
	
	@Override
	public int compareTo(TargetRoleRes o) {
		return precedence - o.precedence;
	}
	
	

}
