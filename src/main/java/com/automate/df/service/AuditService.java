package com.automate.df.service;

import java.util.List;
import java.util.Map;

import com.automate.df.entity.AuditEmpMapping;
import com.automate.df.exception.DynamicFormsServiceException;
import com.automate.df.model.AuditHistoryRes;
import com.automate.df.model.AuditMasterRes;
import com.automate.df.model.AuditMasterSaveReq;
import com.automate.df.model.AuditSearchReq;
import com.automate.df.model.AuditTrailReq;

public interface AuditService {

	List<AuditMasterRes> getAuditMasterDataWithMapping(String orgId) throws DynamicFormsServiceException;

	List<AuditEmpMapping> mapEmployes(List<AuditMasterSaveReq> req) throws DynamicFormsServiceException;

	//Object saveAuditData(AuditTrailReq req) throws DynamicFormsServiceException;

	Object saveAuditData(AuditTrailReq req) throws DynamicFormsServiceException;

	List<AuditHistoryRes> getAuditDataById(AuditSearchReq req) throws DynamicFormsServiceException;

	Object getAuditDataByTaskStage(String stageName, String task) throws DynamicFormsServiceException;

	Object getAuditDataBasedOnTime(String leadId, String universalId)  throws DynamicFormsServiceException;

}
