package com.automate.df.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.automate.df.entity.LostReasons;
import com.automate.df.entity.OtherMaker;
import com.automate.df.entity.OtherModel;
import com.automate.df.entity.Source;
import com.automate.df.exception.DynamicFormsServiceException;
import com.automate.df.model.DFFieldRes;
import com.automate.df.model.DFSave;
import com.automate.df.model.DFUpdate;
import com.automate.df.model.DropDownRequest;
import com.automate.df.model.ListOFCheckListTypeWithItems;
import com.automate.df.service.CheckListItemsService;
import com.automate.df.service.EvaluationParametersService;
import com.automate.df.service.LostSubLostServices;
import com.automate.df.service.OtherMakerModelService;
import com.automate.df.service.SourceSubSourceService;
import com.automate.df.service.impl.DFSaveService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping
@CrossOrigin
@Api(value = "Dynamic Forms Save Module", tags = "Dynamic Forms Save Module", description = "Dynamic Forms Save Module")
public class DFSaveController {

	@Autowired
	Environment env;
	
	@Autowired
	DFSaveService dfSaveService;
	@Autowired
	OtherMakerModelService othermakermodelservice;
	@Autowired
	SourceSubSourceService sourcesubsourceservice;
	@Autowired
	LostSubLostServices lostsublostreasons;
	@Autowired
	EvaluationParametersService evaluationParametersService;
	@Autowired
	CheckListItemsService checkListItemsService;

	@CrossOrigin
	@PostMapping(value = "/df-save")
	public ResponseEntity<?> saveDFFormData(@RequestBody DFSave dfSave)
			throws DynamicFormsServiceException {
		DFFieldRes response = null;
		if (Optional.of(dfSave).isPresent()) {
			response = dfSaveService.saveDFForm(dfSave);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	
	@CrossOrigin
	@PostMapping(value = "/df-update")
	public ResponseEntity<?> updateDFFormData(@RequestBody DFUpdate dfUpdate)
			throws DynamicFormsServiceException {
		Map<String,String> response = null;
		if (Optional.of(dfUpdate).isPresent()) {
			response = dfSaveService.updateDFFormData(dfUpdate);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	
	@CrossOrigin
	@GetMapping(value = "/df-get-all/{page_id}")
	public ResponseEntity<?> getAllDfFormData(@PathVariable(name="page_id") String id)
			throws DynamicFormsServiceException {
		String response = null;
		if (Optional.of(id).isPresent()) {
			response = dfSaveService.getAllDfFormData(id);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	
	
	@CrossOrigin
	@GetMapping(value = "/df-get")
	public ResponseEntity<?> getDfFormData(@RequestParam(name="recordId") String recordId,@RequestParam(name="pageId") String pageId,@RequestParam(name="UUID") String UUID)
			throws DynamicFormsServiceException {
		String response = null;
		if (Optional.of(recordId).isPresent() && Optional.of(pageId).isPresent()&& Optional.of(UUID).isPresent()) {
			response = dfSaveService.getDfFormData(recordId,pageId,UUID);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	
	
	@CrossOrigin
	@DeleteMapping(value = "/df-delete")
	public ResponseEntity<String> deleteDfFormData(@RequestParam(name="recordId") String recordId,@RequestParam(name="pageId") String pageId)
			throws DynamicFormsServiceException {
		String response = null;
		if (Optional.of(recordId).isPresent()) {
			response = dfSaveService.deleteDfFormData(recordId,pageId);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	
	@CrossOrigin
	@DeleteMapping(value = "/df-softdelete")
	public ResponseEntity<String> softDeleteDfFormData(@RequestParam(name="recordId") String recordId,@RequestParam(name="pageId") String pageId,@RequestParam(name="orgId") String orgId)
			throws DynamicFormsServiceException {
		String response = null;
		if (Optional.of(recordId).isPresent()) {
			response = dfSaveService.softDeleteDfFormData(recordId,pageId,orgId);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	
	// Mutli Entity Methods - Starts Here
	@CrossOrigin
	@PostMapping(value = "/organization")
	public ResponseEntity<?> saveMultiDFFormData(@RequestBody String str)
			throws DynamicFormsServiceException {
		String response = null;
		if (Optional.of(str).isPresent()) {
			response = dfSaveService.saveOrganization(str);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	@CrossOrigin
	@GetMapping(value = "/organization")
	public ResponseEntity<?> getOrganization(@RequestParam(name="recordId") Integer recordId)
			throws DynamicFormsServiceException {
		String response = null;
		if (Optional.of(recordId).isPresent()) {
			response = dfSaveService.getOrganization(recordId);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	
	@CrossOrigin
	@GetMapping(value = "/organization-all")
	public ResponseEntity<?> getAllOrganization(@RequestParam(defaultValue = "0") int pageNo,
			@RequestParam(defaultValue = "10") int size) throws DynamicFormsServiceException {
			return new ResponseEntity<>(dfSaveService.getAllOrganization(pageNo, size), HttpStatus.OK);
	}
	
	@CrossOrigin
	@PutMapping(value = "/organization")
	public ResponseEntity<?> updateOrganization(@RequestBody String str)
			throws DynamicFormsServiceException {
		String response = null;
		if (Optional.of(str).isPresent()) {
			response = dfSaveService.updateOrganization(str);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	
	@CrossOrigin
	@DeleteMapping(value = "/organization/{recordId}")
	public ResponseEntity<?> deleteOrganization(@PathVariable(name="recordId") int recordId )
			throws DynamicFormsServiceException {
		String response = null;
		if (Optional.of(recordId).isPresent()) {
			response = dfSaveService.deleteOrganization(recordId);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	
	@CrossOrigin
	@GetMapping(value = "/bulkUploadTemplate")
	public ResponseEntity<?> getBulkUpload(@RequestParam(name = "pageId") int pageId,
			@RequestParam(name = "orgId") int orgId) throws DynamicFormsServiceException {
		
		return new ResponseEntity<>(dfSaveService.getBulkUpoladTemplet(pageId, orgId), HttpStatus.OK);
	}
	// Get All by PageId&OrgId
	@CrossOrigin
	@GetMapping(value = "/df-get-all/{org_id}/{Status}/{UUID}/{page_id}")
	public ResponseEntity<?> getAllDfFormDataByOrg(@PathVariable(name="org_id") int orgid,@PathVariable(name="Status") String status,@PathVariable(name="page_id") String id,@PathVariable(name="UUID") String UUID)
			throws DynamicFormsServiceException {
		String response = null;
		if (Optional.of(id).isPresent()) {
			response = dfSaveService.getAllDfFormDataByOrg(id, orgid,status,UUID);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	
	//Multi Entity Methods - Ends Here
	
	 //24/11
	//Get otherModelsByotherMaker
    private Map<String, Object> toModelByMakerMap(final OtherModel othermaker){
    	Map<String, Object> otherMakerModel = new HashMap<>();
    	
    	otherMakerModel.put("id",othermaker.getOtherMaker());
    	otherMakerModel.put("key", othermaker.getOtherModel());
    	otherMakerModel.put("value",othermaker.getOtherModel());
    	return otherMakerModel;
    }
   
  // @Operation(summary = "Get maker models details", description = "Get maker models details")
    @CrossOrigin
    @PostMapping(value = "/other_maker_models", produces = "application/json")
    public ResponseEntity<List<Map<String, Object>>> getModelByMake(@RequestBody DropDownRequest dropdownReq) {
    	

    	ResponseEntity<List<Map<String, Object>>> response = null;
     	 List<OtherModel> othermake = othermakermodelservice.getModelByMaker((dropdownReq.getBu()),(dropdownReq.getParentId()));
     	 
     	if(null != othermake) {
     		final List<Map<String, Object>> othermodel = othermake.stream()
   				.map(this::toModelByMakerMap)
   				.collect(Collectors.toList());
   		response = ResponseEntity.ok(othermodel);
     		 
     	}
     	else {
     		response = ResponseEntity.noContent().build();
     	}
    	return response;
    }
    //25/11
    @CrossOrigin
    @GetMapping(value = "/Other_Maker_AllDetails",produces = "application/json")
    public ResponseEntity<List<OtherMaker>> getAllOtherModels(@RequestParam("organizationId") String organizationId) {
             List<OtherMaker> othermaker = othermakermodelservice.getAllOtherModels(organizationId);
        return ResponseEntity.ok(othermaker);
    }
    //03/02/22
    @CrossOrigin
    @GetMapping(value = "/Source_SubSource_AllDetails",produces = "application/json")
    public ResponseEntity<List<Source>> getAllSourceSubSource(@RequestParam("organizationId") String organizationId) {
             List<Source> source = sourcesubsourceservice.getAllSubsourcedetails(organizationId);
        return ResponseEntity.ok(source);
    }
    
    //11/02/22
    @CrossOrigin
    @GetMapping(value = "/Lost_SubLost_AllDetails",produces = "application/json")
    public ResponseEntity<List<LostReasons>> getAllLostSubLost(@RequestParam("organizationId") String organizationId,@RequestParam("stageName") String stageName) {
             List<LostReasons> lostreason = lostsublostreasons.getAllSubLostAllDetails(organizationId,stageName);
        return ResponseEntity.ok(lostreason);
    }
    
    
    @CrossOrigin
    @GetMapping(value = "/get_All_evaluation",produces = "application/json")
    public ResponseEntity<Map> getAllEvaluationParameters(@RequestParam("orgId") String orgId) {
        return ResponseEntity.ok( evaluationParametersService.getAllParameters(orgId));
    }
    
    @CrossOrigin
    @GetMapping(value = "/Check_List_AllDetails",produces = "application/json")
    public ResponseEntity<List<ListOFCheckListTypeWithItems>> getAllCheckList(@RequestParam("orgId") int orgId) {
             List<ListOFCheckListTypeWithItems> checkListType = checkListItemsService.getAllCheckListTypesDetails(orgId);
        return ResponseEntity.ok(checkListType);
    }
    
}
