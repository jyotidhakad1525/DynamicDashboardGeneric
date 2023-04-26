package com.automate.df.service;

import java.util.List;
import java.util.Map;

import com.automate.df.entity.AutoSaveEntity;
import com.automate.df.exception.DynamicFormsServiceException;
import com.automate.df.model.AutoSave;
import com.automate.df.model.ETVRequest;
import com.automate.df.model.QueryRequestV2;

public interface DFReportService {

	public String generateDynamicQueryV2(QueryRequestV2 request) throws DynamicFormsServiceException;

	public AutoSaveEntity saveAutoSave(AutoSave req);

	public AutoSaveEntity updateAutoSsave(AutoSaveEntity req);

	public List<AutoSaveEntity> getAllAutoSave(String type,int pageNo,int sizes);

	public String deleteAutoSave(int id);

	String generateDropdownQueryV2(QueryRequestV2 request) throws DynamicFormsServiceException;

	public String getAutoSaveByUid(String uid) throws DynamicFormsServiceException;

	public Map<String,String>  generateETVBRLReport(ETVRequest request) throws DynamicFormsServiceException;

}
