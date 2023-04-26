package com.automate.df.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.automate.df.entity.LeadStageRefEntity;
import com.automate.df.exception.DynamicFormsServiceException;
import com.automate.df.model.DMSResponse;
import com.automate.df.model.LeadBulkUploadRes;
import com.automate.df.model.LeadCustomerReferenceDto;
import com.automate.df.model.LeadMapReq;
import com.automate.df.service.impl.AutoLeadAllocationRes;


public interface GenericService {
	
	public String uploadAttachment(MultipartFile file) throws DynamicFormsServiceException;

	public LeadBulkUploadRes processLeadMgmtBulkUpload(MultipartFile file) throws DynamicFormsServiceException;

	public List<AutoLeadAllocationRes> mapEmpLeads(LeadMapReq req) throws DynamicFormsServiceException;

	public List<AutoLeadAllocationRes> autoAllocationOfLeads(LeadMapReq req) throws DynamicFormsServiceException;

	public DMSResponse saveCustomerRef(LeadCustomerReferenceDto customerRefDto) throws DynamicFormsServiceException;

	public List<LeadStageRefEntity> getLeadStagesById(String id) throws DynamicFormsServiceException;

}
