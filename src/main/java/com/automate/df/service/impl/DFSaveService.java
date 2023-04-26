package com.automate.df.service.impl;

import java.util.Map;

import com.automate.df.entity.BulkUploadTemplate;
import com.automate.df.exception.DynamicFormsServiceException;
import com.automate.df.model.DFFieldRes;
import com.automate.df.model.DFSave;
import com.automate.df.model.DFUpdate;


public interface DFSaveService {

	//DFFieldRes saveDFFormDataWthReflection(DFSave dfSave) throws DynamicFormsServiceException;
	
	DFFieldRes saveDFForm(DFSave dfSave) throws DynamicFormsServiceException;

	String getAllDfFormData(String id) throws DynamicFormsServiceException;

	String getDfFormData(String recordId,String pageId,String UUID) throws DynamicFormsServiceException;

	Map<String, String> updateDFFormData(DFUpdate dfUpdate) throws DynamicFormsServiceException;

	// Mutli Entity Methods - Starts Here
	
	String saveOrganization(String str) throws DynamicFormsServiceException;

	String getOrganization(Integer recordId) throws DynamicFormsServiceException;

	String getAllOrganization(int pageNo,int size) throws DynamicFormsServiceException;

	String updateOrganization(String str) throws DynamicFormsServiceException;

	String deleteOrganization(int recordId) throws DynamicFormsServiceException;
	
	// Mutli Entity Methods - Ends Here


	String deleteDfFormData(String recordId,String pageId) throws DynamicFormsServiceException;

	String softDeleteDfFormData(String recordId, String pageId, String orgId) throws DynamicFormsServiceException;

	public BulkUploadTemplate getBulkUpoladTemplet(int pageId,int orgId) throws DynamicFormsServiceException;
	
	String getAllDfFormDataByOrg(String id, int orgId,String status,String UUID) throws DynamicFormsServiceException;

}
