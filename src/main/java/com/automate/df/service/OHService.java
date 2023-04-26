package com.automate.df.service;

import java.util.List;
import java.util.Map;

import com.automate.df.entity.oh.LocationNodeData;
import com.automate.df.entity.oh.LocationNodeDef;
import com.automate.df.exception.DynamicFormsServiceException;
import com.automate.df.model.oh.EmployeeRootV2;
import com.automate.df.model.oh.LevelDataReq;
import com.automate.df.model.oh.LocationDefNodeRes;
import com.automate.df.model.oh.OHEmpLevelMapping;
import com.automate.df.model.oh.OHEmpLevelMappingV2;
import com.automate.df.model.oh.OHEmpLevelUpdateMapReq;
import com.automate.df.model.oh.OHLeveDeleteReq;
import com.automate.df.model.oh.OHLeveUpdateReq;
import com.automate.df.model.oh.OHLevelReq;
import com.automate.df.model.oh.OHLevelUpdateReq;
import com.automate.df.model.oh.OHNodeUpdateReq;
import com.automate.df.model.oh.OHRes;

public interface OHService {

	List<OHRes> getOHDropdown(Integer orgId, Integer empId, Integer id) throws DynamicFormsServiceException;

	List<String> getLevelData(Integer orgId, Integer empId) throws DynamicFormsServiceException;

	List<OHRes> getEmpParentDropdown(Integer orgId, Integer empId) throws DynamicFormsServiceException;

	String addOHMapping(LevelDataReq req) throws DynamicFormsServiceException;

	List<OHRes> getEmpBranches(Integer orgId, Integer empId) throws DynamicFormsServiceException;

	List<LocationNodeData> createLevels(OHLevelReq req) throws DynamicFormsServiceException;

	List<?> removeDataLevels(OHLeveDeleteReq req) throws DynamicFormsServiceException;

	List<?> setEmpLevelMapping(OHEmpLevelMapping req,String active) throws DynamicFormsServiceException;

	List<LocationNodeDef> getOrgLevels(Integer orgId) throws DynamicFormsServiceException;

	List<LocationNodeData> getLevelDataNodes(Integer orgId,String levelCode) throws DynamicFormsServiceException;

	List<?> updateOrgLevels(OHLeveUpdateReq req) throws DynamicFormsServiceException;

	String updateEmpLevelMapping(OHEmpLevelUpdateMapReq req) throws DynamicFormsServiceException;

	List<LocationNodeData> getActiveEmpMappings(Integer orgId, Integer empId) throws DynamicFormsServiceException;

	Map<String, Object> getEmployeesListWithMapping(Integer pageNo, Integer size, Integer orgId) throws DynamicFormsServiceException;

	List<LocationDefNodeRes> getActiveEmpMappingsAll(Integer orgId) throws DynamicFormsServiceException;

	List<EmployeeRootV2> getMappedEmployees(String type, String orgId) throws DynamicFormsServiceException;

	String updateOrgLevels(OHLevelUpdateReq req) throws DynamicFormsServiceException;

	String updateNodes(OHNodeUpdateReq req) throws DynamicFormsServiceException;

	Map<String, Object> getMappingByEmpId(Integer empId) throws DynamicFormsServiceException;

	Map<String, Object> getActiveDropdownsV2(List<Integer> levelList,Integer orgId,Integer empId) throws DynamicFormsServiceException;
	
	Map<String, Object> getActiveDropdowns(Integer orgId,Integer empId) throws DynamicFormsServiceException;

	Object getActiveLevels(Integer orgId, Integer empId) throws DynamicFormsServiceException;

	List<?> setEmpLevelMappingMultiple(OHEmpLevelMappingV2 req, String string) throws DynamicFormsServiceException;

	Object getActiveBranches(Integer orgId, Integer empId) throws DynamicFormsServiceException;



}
