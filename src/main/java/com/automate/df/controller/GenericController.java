package com.automate.df.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.automate.df.entity.LeadStageRefEntity;
import com.automate.df.exception.DynamicFormsServiceException;
import com.automate.df.model.DMSResponse;
import com.automate.df.model.LeadBulkUploadRes;
import com.automate.df.model.LeadCustomerReferenceDto;
import com.automate.df.model.LeadMapReq;
import com.automate.df.service.GenericService;
import com.automate.df.service.impl.AutoLeadAllocationRes;
import java.util.Optional;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Api(value = "/dynamic-formapi", tags = "Common", description = "Common")
@RequestMapping(value="/common")
public class GenericController {
	
	@Autowired
	GenericService genericService;
	
	@Autowired
	Environment env;
	
	@CrossOrigin
	@PostMapping(value="/uploadAttachment")
	public ResponseEntity<String> upload(MultipartFile file) throws DynamicFormsServiceException{
		String response = null;
		log.debug("uploadAttachment() ");
		if(Optional.of(file).isPresent()) {
			response = genericService.uploadAttachment(file);
		}
		return new ResponseEntity<String>(response,HttpStatus.CREATED);
		
	}
	

	@CrossOrigin
	@PostMapping(value="/lead-mgmt-bulkupload")
	public ResponseEntity<LeadBulkUploadRes> processLeadMgmtBulkUpload(MultipartFile file) throws DynamicFormsServiceException{
		LeadBulkUploadRes response = null;
		if(Optional.of(file).isPresent()) {
			response = genericService.processLeadMgmtBulkUpload(file);
		}
		return new ResponseEntity<LeadBulkUploadRes>(response,HttpStatus.CREATED);
		
	}

	
	
	@CrossOrigin
	@PostMapping(value="/emp_lead_mapping")
	public ResponseEntity<?> mapEmpLeads(@RequestBody LeadMapReq req)
			throws DynamicFormsServiceException {
		List<AutoLeadAllocationRes> response = null;
		if (Optional.of(req).isPresent()) {
			response = genericService.mapEmpLeads(req);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@CrossOrigin
	@PostMapping(value="/auto_emp_lead_mapping")
	public ResponseEntity<?> autoAllocationOfLeads(@RequestBody LeadMapReq req)
			throws DynamicFormsServiceException {
		List<AutoLeadAllocationRes> response = null;
		if (Optional.of(req).isPresent()) {
			response = genericService.autoAllocationOfLeads(req);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	   @PostMapping(value="/lead-customer-reference")
	    public DMSResponse leadRef(@RequestBody LeadCustomerReferenceDto customerRefDto) throws DynamicFormsServiceException {
	        return genericService.saveCustomerRef(customerRefDto);
	    }

	   
	   @GetMapping(value="/get_lead_stagesby_id/{leadid}")
	   List<LeadStageRefEntity> getLeadStagesById(@PathVariable(name="leadid") String id) throws DynamicFormsServiceException{
		return genericService.getLeadStagesById(id);
		   
	   }
	   
	   

}