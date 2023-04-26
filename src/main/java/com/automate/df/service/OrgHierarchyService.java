package com.automate.df.service;

import java.util.List;

import com.automate.df.model.df.OrgDropDownReq;
import com.automate.df.model.salesgap.OrgDropDown;

public interface OrgHierarchyService {

	List<OrgDropDown> getOrgLevelDropdownData(OrgDropDownReq request);
}
