package com.automate.df.controller;

import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.automate.df.entity.OrgVerticalLocationRoleMenu;
import com.automate.df.exception.DynamicFormsServiceException;
import com.automate.df.exception.DynmFormExcelException;
import com.automate.df.model.BulkUploadReq;
import com.automate.df.model.ButtonControl;
import com.automate.df.model.ButtonReq;
import com.automate.df.model.DFFieldRes;
import com.automate.df.model.DropDownRes;
import com.automate.df.model.DropdownReq;
import com.automate.df.model.DynmFormExcelResponseDO;
import com.automate.df.model.DynmMenuExcelResponseDO;
import com.automate.df.model.ErrorDetails;
import com.automate.df.model.Mappings;
import com.automate.df.model.MenuMappings;
import com.automate.df.model.PageRes;
import com.automate.df.service.impl.DynamicFormService;
import com.automate.df.util.DynamicFormExcelUtils;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(value = "/dynamic-forms")
@CrossOrigin
@Api(value = "Dynamic Forms", tags = "Dynamic Forms", description = "Dynamic Forms")
public class DynamicFormController {
	
	@Autowired
	DynamicFormService dynamicFormService;
	
	@Autowired
	Environment env;
	
	@Autowired
	DynamicFormExcelUtils dynmicUtils;
	
	@CrossOrigin
	@PostMapping(value = "/page-fields")
	public ResponseEntity<?> getBusinessUnits(@RequestBody Mappings mappings)
			throws DynamicFormsServiceException {
		PageRes response = null;
		if (Optional.of(mappings).isPresent()) {
			response = dynamicFormService.getBusinessFormKeyMappings(mappings);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	
	
	
	@CrossOrigin
	@GetMapping(value = "/get-pojoName/{id}")
	public ResponseEntity<?> getPojoName(@PathVariable(name="id") int id)
			throws DynamicFormsServiceException {
		String response = null;
		if (Optional.of(id).isPresent()) {
			response = dynamicFormService.getPojoName(id);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@CrossOrigin
	@GetMapping(value = "/get-pojoNameV2/{id}/{UUID}")
	public ResponseEntity<?> getPojoName(@PathVariable(name="id") int id,@PathVariable(name="UUID") String UUID)
			throws DynamicFormsServiceException {
		String response = null;
		if (Optional.of(id).isPresent()) {
			response = dynamicFormService.getPojoNameV2(id,UUID);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@CrossOrigin
	@GetMapping(value = "/get/master-data/{formkeymapid}")
	public ResponseEntity<?> getMasterDataBasedOnFormKeyMapId(@PathVariable(name="formkeymapid") String id)
			throws DynamicFormsServiceException {
		DFFieldRes response = null;
		if (Optional.of(id).isPresent()) {
			response = dynamicFormService.getMasterDataBasedOnFormKeyMapId(id);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	
	
	@CrossOrigin
	@PostMapping(value = "/pageUpload")
	public DynmFormExcelResponseDO createDynFrmFldUpload(@RequestParam("file") MultipartFile dynamicFormExcel) throws DynmFormExcelException, DynamicFormsServiceException {
			return dynamicFormService.createDynamicFormFieldsExcel(DynamicFormExcelUtils.processDynamicFormExcelCSV(dynamicFormExcel));
	
	}
	
	@CrossOrigin
	@PostMapping(value = "/rolePageUpload")
	public DynmFormExcelResponseDO createDynFrmRoleUpload(@RequestParam("file") MultipartFile dynamicFormExcel) throws DynmFormExcelException, DynamicFormsServiceException {
			return dynamicFormService.createDynamicRoleFormsExcel(DynamicFormExcelUtils.processDynamicRoleFormExcelCSV(dynamicFormExcel));
	
	}
	

	@CrossOrigin
	@PostMapping(value = "/menuList")
	public ResponseEntity<?> getMenuList(@RequestBody MenuMappings mappings)
			throws DynamicFormsServiceException {
		List<OrgVerticalLocationRoleMenu> response = null;
		if (Optional.of(mappings).isPresent()) {
			response = dynamicFormService.getMenuList(mappings);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	
	@CrossOrigin
	@PostMapping(value = "/uploadAutomateMenu")
	public DynmMenuExcelResponseDO createAutomateMenuUpload(@RequestParam("file") MultipartFile dynamicMenuExcel) throws DynmFormExcelException, DynamicFormsServiceException {
		
		 
		return dynamicFormService.createAutoModuleMenuExcel(DynamicFormExcelUtils.processAutomateMenuExcelCSV(dynamicMenuExcel));
	
	}
	

	
	
	@CrossOrigin
	@PostMapping(value = "/button-controls")
	public ResponseEntity<?> getButtonControls(@RequestBody ButtonReq buttonReq)
			throws DynamicFormsServiceException {
		List<ButtonControl> response = null;
		if (Optional.of(buttonReq).isPresent()) {
			response = dynamicFormService.getButtonControls(buttonReq);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	
	
	@CrossOrigin
	@PostMapping(value = "/dropdown")
	public ResponseEntity<?> getDropDownData(@RequestBody DropdownReq dropdownReq)
			throws DynamicFormsServiceException {
		List<DropDownRes> response = null;
		if (Optional.of(dropdownReq).isPresent()) {
			response = dynamicFormService.getDropDownData(dropdownReq);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	
	@CrossOrigin
	@PostMapping(value = "/uploadBulkUpload/{name}/{flush}/{bulkUploadId}/{empId}")
	public ResponseEntity<?> createAutBulkUpload(@PathVariable(name="name") String name,@PathVariable(name="flush") boolean flush,@PathVariable(name="bulkUploadId") String bulkUploadId,@PathVariable(name="empId") Integer empId,@RequestParam("file") MultipartFile bulkExcel) throws DynmFormExcelException, DynamicFormsServiceException {
		
		BulkUploadReq bulkUploadReq = new BulkUploadReq();
		if(null != empId) {
		bulkUploadReq.setEmpId(empId.toString());
		}
		bulkUploadReq.setPageIdentifier(name);
		bulkUploadReq.setFlushAndFill(flush);
		bulkUploadReq.setBulkUploadIdentifier(bulkUploadId);

		ErrorDetails error = dynmicUtils.processBulkExcel(bulkExcel,bulkUploadReq);

//			

//		    HttpHeaders headers = new HttpHeaders();
//		    headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
//		    headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"myexcelfile.xls\"");
//		    headers.setContentLength(documentContent.length);
//		    response = new ResponseEntity<byte[]>(documentContent, headers, HttpStatus.OK);
//		    HttpServletResponse response1;
//		    byte[] excel = Files.toByteArray((File) file);
//		    String fileName = "anyFileName.xlsx";
//		    response1.setContentType("application/vnd.ms-excel");
//		    response1.setHeader("Content-Disposition", "attachment;filename=" + fileName);
//		    response1.getWriter().write(excel);;
		return new ResponseEntity<>(error, HttpStatus.OK);
		

//		return new ResponseEntity<>("failed", HttpStatus.EXPECTATION_FAILED);
//

//		return new ResponseEntity<>("success", HttpStatus.OK);
//

			
//	    return ResponseEntity.ok()
//	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name+ ".xlsx")
//	        .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))

//		}else {
//		String response = "Success";
//		 //return new ResponseEntity<>(response, HttpStatus.OK);
//		}
//		return null;
	
	}
	
	@CrossOrigin
	@PostMapping(value = "/uploadBulkVehicle/{name}/{flush}/{bulkUploadId}")
	public ResponseEntity<?> createVehBulkUpload(@PathVariable(name="name") String name,@PathVariable(name="flush") boolean flush,@PathVariable(name="bulkUploadId") String bulkUploadId,@RequestParam("file") MultipartFile bulkExcel) throws DynmFormExcelException, DynamicFormsServiceException {
	
		BulkUploadReq bulkUploadReq = new BulkUploadReq();
		bulkUploadReq.setPageIdentifier(name);
		bulkUploadReq.setFlushAndFill(flush);
		bulkUploadReq.setBulkUploadIdentifier(bulkUploadId);

			boolean response = dynmicUtils.processBulkExcelVehicles(bulkExcel,bulkUploadReq);
		

	if(response) {
		return new ResponseEntity<>("failed", HttpStatus.EXPECTATION_FAILED);

	}else {
		return new ResponseEntity<>("success", HttpStatus.OK);

	}
			
//	    return ResponseEntity.ok()
//	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name+ ".xlsx")
//	        .contentType(MediaType.parseMediaType("application/vnd.ms-excel;charset=UTF-8"))

//		}else {
//		String response = "Success";
//		 //return new ResponseEntity<>(response, HttpStatus.OK);
//		}
//		return null;
	
	}
	
}
