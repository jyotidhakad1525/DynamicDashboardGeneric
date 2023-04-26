package com.automate.df.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.automate.df.exception.DynamicFormsServiceException;
import com.automate.df.model.df.OrgDropDownReq;
import com.automate.df.model.salesgap.OrgDropDown;

import com.automate.df.service.OrgHierarchyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Api(value = "/org-hierarchy", tags = "Org Hierarchy", description = "Org Hierarchy")
@RequestMapping(value="/sales-gap")
public class OrgHierarchyController {

	@Autowired
	OrgHierarchyService orgHierarchyService;
	
	@Autowired
	Environment env;
	
	
	@CrossOrigin
	@PostMapping(value = "/org_level_data")
	@ApiOperation(value = "Org Hierarchy dropdown", tags = "Org Hierarchy")
	public ResponseEntity<?> getOrgLevelDropdownData(@RequestBody OrgDropDownReq request)
			throws DynamicFormsServiceException {
		List<OrgDropDown> response = null;
		if (Optional.of(request).isPresent()) {
			response = orgHierarchyService.getOrgLevelDropdownData(request);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
}
