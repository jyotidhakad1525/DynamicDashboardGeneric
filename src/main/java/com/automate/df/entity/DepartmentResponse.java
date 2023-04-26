package com.automate.df.entity;


import com.automate.df.entity.oh.DmsBranch;
import com.automate.df.entity.oh.DmsDesignation;
import com.automate.df.entity.sales.DmsOrganization;
import com.automate.df.entity.sales.employee.DMSEmployee;
import com.automate.df.entity.sales.employee.DmsDepartment;
import com.automate.df.entity.sales.employee.DmsEmployeeRoleMapping;
import com.automate.df.entity.sales.employee.DmsRole;
import com.automate.df.entity.sales.workflow.DMSTaskCategory;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
public class DepartmentResponse {
    List<DepartmentData> departmentData;

}
