package com.automate.df.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.automate.df.exception.DynamicFormsServiceException;
import com.automate.df.model.AuditMasterSaveReq;
import com.automate.df.model.AuditSearchReq;
import com.automate.df.model.AuditTrailReq;
import com.automate.df.service.AuditService;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Api(value = "/audit", tags = "audit", description = "audit")
@RequestMapping(value="/audit")
public class AuditController {

	@Autowired
	Environment env;
	
	
	@Autowired
	AuditService auditService;
	
	

	@CrossOrigin
	@GetMapping(value = "wf_list")
	public ResponseEntity<?> getAuditMasterDataWithMapping(@RequestParam(required = false,name = "orgId") String orgId)
			throws DynamicFormsServiceException {
			return new ResponseEntity<>(auditService.getAuditMasterDataWithMapping(orgId), HttpStatus.OK);
	}
	
	@CrossOrigin
	@PostMapping(value = "map_employees")
	public ResponseEntity<?> mapEmployes(@RequestBody List<AuditMasterSaveReq> req)
			throws DynamicFormsServiceException {
			return new ResponseEntity<>(auditService.mapEmployes(req), HttpStatus.OK);
	}
	
	@CrossOrigin
	@PostMapping(value = "traildata")
	public ResponseEntity<?> saveAuditData(@RequestBody AuditTrailReq req) throws DynamicFormsServiceException {
			return new ResponseEntity<>(auditService.saveAuditData(req), HttpStatus.OK);
	}

	@CrossOrigin
	@PostMapping(value = "audit_search")
	public ResponseEntity<?> perfomAuditSearch(@RequestBody AuditSearchReq req)
			throws DynamicFormsServiceException {
			return new ResponseEntity<>(auditService.getAuditDataById(req), HttpStatus.OK);
	}
	
	@CrossOrigin
	@GetMapping(value = "auditDataByTaskStage")
	public ResponseEntity<?> getAuditDataByTaskStage(@RequestParam(required = false,name="stage") String stageName,
			@RequestParam(required = false,name="task") String task 
		)
			throws DynamicFormsServiceException {
			return new ResponseEntity<>(auditService.getAuditDataByTaskStage(stageName,task), HttpStatus.OK);
	}
	
	
}
