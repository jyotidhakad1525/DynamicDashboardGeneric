package com.automate.df.model.oh;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmployeeRoot {
	
	 public int empId;
	    public String designation;
	    public String branchId;
	    public String orgId;
	    public String orgName;
	    public String mappedLevel;
	    public String levelDisplayName;
	    public List<String> mappings;
	    public String designationName;
	    public String cognitoName;
	    public String reportingTo;
	    public String empName;
	    
	    public String hrmsEmpCode;
	    public String hrmsEmpId;
	    public String hrmsRoleId;
	    public String hrmsRole;
	    

}
