package com.automate.df.service;

import java.util.List;
import java.util.Map;

import com.automate.df.entity.salesgap.TSAdminUpdateReq;
import com.automate.df.entity.salesgap.TargetEntity;
import com.automate.df.entity.salesgap.TargetRoleReq;
import com.automate.df.exception.DynamicFormsServiceException;
import com.automate.df.model.salesgap.TargetDropDown;
import com.automate.df.model.salesgap.TargetMappingAddReq;
import com.automate.df.model.salesgap.TargetMappingReq;
import com.automate.df.model.salesgap.TargetRoleRes;
import com.automate.df.model.salesgap.TargetSearch;
import com.automate.df.model.salesgap.TargetSettingReq;
import com.automate.df.model.salesgap.TargetSettingRes;

public interface SalesGapService {

	List<TargetSettingRes> getTargetSettingData(int pageNo,int size);

	TargetSettingRes saveTargetSettingData(TargetSettingReq request) throws DynamicFormsServiceException;

	//String saveTargetMappingData(TargetMappingReq request);

	TargetMappingReq getTargetMappingData(Integer id);

	String updateTargetSettingData(TargetSettingRes request);

	List<TargetSettingRes> searchTargetMappingData(TargetSearch request);

	List<TargetDropDown> getTargetDropdown(String type);

	Map<String, Object> getTargetDataWithRole(TargetRoleReq req) throws DynamicFormsServiceException;

	TargetSettingRes updateTargetDataWithRole(TargetSettingRes req);

	List<TargetDropDown> getTargetDropdownV2(String orgId,String branchId,String parent, String child, String parentId);

	TargetSettingRes addTargetDataWithRole(TargetMappingAddReq req) throws DynamicFormsServiceException;

	String deleteTSData(String recordId, String empId);

	TargetSettingRes editTargetDataWithRoleV2(TargetMappingAddReq req) throws DynamicFormsServiceException;
	
	/**
	 * @param empId
	 * @return
	 * @throws DynamicFormsServiceException 
	 */
	public TargetRoleRes getEmpRoleData(int empId) throws DynamicFormsServiceException;
	
	/**
	 * @param empId
	 * @return
	 */
	public List<TargetRoleRes> getEmpRoles(final int empId);

	Map<String, String> getEmployeeRole(Integer empId) throws DynamicFormsServiceException;
	
	String getEmployeeRoleV2(Integer empId);

	String deleteAdminTargetMapping(Integer recordId);

	TargetSettingRes updateTargetSettingDataV2(TSAdminUpdateReq request) throws DynamicFormsServiceException;

	TSAdminUpdateReq getTargetSettingAdminById(int id)  throws DynamicFormsServiceException;;

	

}
