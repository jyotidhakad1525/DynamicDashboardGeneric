package com.automate.df.model.oh;

import java.util.List;

import com.automate.df.entity.oh.LocationNodeData;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmployeeRootV2 {

	
	 public int empId;
	    public String designation;
	    public String branchId;
	    public String orgId;
	    public String orgName;
	    public String mappedLevel;
	    public String designationName;
	    public String cognitoName;
	    public String reportingTo;
	    public String empName;
	    
	    public String hrmsEmpCode;
	    public String hrmsEmpId;
	    public String hrmsRoleId;
	    public String hrmsRole;
	    

	    List<LocationNodeData> nodes; 
}
