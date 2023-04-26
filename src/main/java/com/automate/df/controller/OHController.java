package com.automate.df.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.automate.df.entity.oh.LocationNodeData;
import com.automate.df.entity.oh.LocationNodeDef;
import com.automate.df.exception.DynamicFormsServiceException;
import com.automate.df.model.AcitveMappingOrgChartRes;
import com.automate.df.model.DropDownRes;
import com.automate.df.model.oh.EmployeeRoot;
import com.automate.df.model.oh.LocationDefNodeRes;
import com.automate.df.model.oh.OHEmpLevelMapping;
import com.automate.df.model.oh.OHEmpLevelMappingV2;
import com.automate.df.model.oh.OHEmpLevelUpdateMapReq;
import com.automate.df.model.oh.OHLeveDeleteReq;
import com.automate.df.model.oh.OHLevelReq;
import com.automate.df.model.oh.OHLevelUpdateReq;
import com.automate.df.model.oh.OHNodeUpdateReq;
import com.automate.df.model.oh.OHRes;
import com.automate.df.model.salesgap.TargetDropDownV2;
import com.automate.df.service.OHService;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Api(value = "/Org Hierarchy", tags = "Org Hierarchy", description = "Org Hierarchy")
@RequestMapping(value="/oh")
public class OHController {

	@Autowired
	Environment env;
	
	@Autowired
	OHService ohService;
	
	
	@CrossOrigin
	@GetMapping(value="/child-dropdown-data")
	public ResponseEntity<List<OHRes>> getOHDropdown(
			@RequestParam(name="orgId",required = true) Integer orgId,
			@RequestParam(name="empId",required = true) Integer empId,
			@RequestParam(name="parent_key_id",required = true) Integer id
			)
			throws DynamicFormsServiceException {
		List<OHRes> response = null;
		if (Optional.of(orgId).isPresent() && Optional.of(empId).isPresent()) {
			response = ohService.getOHDropdown(orgId,empId,id);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	@CrossOrigin
	@GetMapping(value="/emp-level-data")
	public ResponseEntity<List<String>> getLevelData(
			@RequestParam(name="orgId",required = true) Integer orgId,
			@RequestParam(name="empId",required = true) Integer empId
			)
			throws DynamicFormsServiceException {
		List<String> response = null;
		if (Optional.of(orgId).isPresent() && Optional.of(empId).isPresent()) {
			response = ohService.getLevelData(orgId,empId);
		} else {
			throw new DynamicFormsServiceException("Bad Request", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	
	@CrossOrigin
	@GetMapping(value="/emp-parent-dropdown")
	public ResponseEntity<List<OHRes>> getEmpParentDropdown(
			@RequestParam(name="orgId",required = true) Integer orgId,
			@RequestParam(name="empId",required = true) Integer empId
			)
			throws DynamicFormsServiceException {
		List<OHRes> response = null;
		if (Optional.of(orgId).isPresent() && Optional.of(empId).isPresent()) {
			response = ohService.getEmpParentDropdown(orgId,empId);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@CrossOrigin
	@GetMapping(value="/emp-branches")
	public ResponseEntity<List<OHRes>> getEmpBranches(
			@RequestParam(name="orgId",required = true) Integer orgId,
			@RequestParam(name="empId",required = true) Integer empId
			)
			throws DynamicFormsServiceException {
		List<OHRes> response = null;
		if (Optional.of(orgId).isPresent() && Optional.of(empId).isPresent()) {
			response = ohService.getEmpBranches(orgId,empId);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
/*	
	@CrossOrigin
	@PostMapping(value="/emp-level-data-mapping")
	public ResponseEntity<?> addOHMapping(@RequestBody LevelDataReq req)
			throws DynamicFormsServiceException {
		String response = null;
		if (Optional.of(req).isPresent()) {
			response = ohService.addOHMapping(req);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}*/

	@CrossOrigin
	@GetMapping(value="/org-levels")
	public ResponseEntity<List<LocationNodeDef>> getOrgLevels(@RequestParam(name="orgId",required = true) Integer orgId)
			throws DynamicFormsServiceException {
		List<LocationNodeDef> response = null;
		if (Optional.of(orgId).isPresent()) {
			response = ohService.getOrgLevels(orgId);
		} else {
			throw new DynamicFormsServiceException("Bad Request", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@CrossOrigin
	@GetMapping(value="/data-nodes")
	public ResponseEntity<List<LocationNodeData>> getLevelDataNodes(
			@RequestParam(name="orgId",required = true) Integer orgId,
			@RequestParam(name="levelCode",required = true) String levelCode
			)
			throws DynamicFormsServiceException {
		List<LocationNodeData> response = null;
		if (Optional.of(orgId).isPresent() &&Optional.of(levelCode).isPresent() ) {
			response = ohService.getLevelDataNodes(orgId,levelCode);
		} else {
			throw new DynamicFormsServiceException("Bad Request", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@CrossOrigin
	@PostMapping(value="/level-data-creation")
	public ResponseEntity<?> createDataLevels(@RequestBody OHLevelReq req)
			throws DynamicFormsServiceException {
		List<?> response = null;
		if (Optional.of(req).isPresent()) {
			response = ohService.createLevels(req);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@CrossOrigin
	@PostMapping(value="/org-level-removal")
	public ResponseEntity<?> removeDataLevels(@RequestBody OHLeveDeleteReq req)
			throws DynamicFormsServiceException {
		List<?> response = null;
		if (Optional.of(req).isPresent()) {
			response = ohService.removeDataLevels(req);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/*
	@CrossOrigin
	@PostMapping(value="/org-level-update")
	public ResponseEntity<?> updateOrgLevels(@RequestBody OHLeveUpdateReq req)
			throws DynamicFormsServiceException {
		List<?> response = null;
		if (Optional.of(req).isPresent()) {
			response = ohService.updateOrgLevels(req);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	*/
	@CrossOrigin
	@PostMapping(value="/emp-level-data-mapping")
	public ResponseEntity<?> setEmpLevelMapping(@RequestBody OHEmpLevelMapping req)
			throws DynamicFormsServiceException {
		List<?> response = null;
		if (Optional.of(req).isPresent()) {
			response = ohService.setEmpLevelMapping(req,"Y");
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@CrossOrigin
	@PostMapping(value="/emp-multilevel-data-mapping")
	public ResponseEntity<?> setEmpLevelMappingMultiple(@RequestBody OHEmpLevelMappingV2 req)
			throws DynamicFormsServiceException {
		List<?> response = null;
		if (Optional.of(req).isPresent()) {
			response = ohService.setEmpLevelMappingMultiple(req,"Y");
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@CrossOrigin
	@PutMapping(value="/update-emp-level-mapping")
	public ResponseEntity<?> updateEmpLevelMapping(@RequestBody OHEmpLevelUpdateMapReq req)
			throws DynamicFormsServiceException {
		String response = null;
		if (Optional.of(req).isPresent()) {
			response = ohService.updateEmpLevelMapping(req);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@CrossOrigin
	@PostMapping(value="/active-emp-mappings")
	public ResponseEntity<?> getActiveEmpMappings(@RequestParam(name="orgId") Integer orgId,
			@RequestParam(name="empId") Integer empId)
			throws DynamicFormsServiceException {
		List<LocationNodeData> response = null;
		if (Optional.of(empId).isPresent() && Optional.of(orgId).isPresent()) {
			response = ohService.getActiveEmpMappings(orgId,empId);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@CrossOrigin
	@PostMapping(value="/active-org-mappings-all")
	public ResponseEntity<?> getActiveEmpMappingsAll(@RequestParam(name="orgId") Integer orgId
			)
			throws DynamicFormsServiceException {
		List<LocationDefNodeRes> response = null;
		if (Optional.of(orgId).isPresent()) {
			
			response = ohService.getActiveEmpMappingsAll(orgId);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@CrossOrigin
	@GetMapping(value="/employees/{orgId}")
	public ResponseEntity<?> getEmployeesListWithMapping(@RequestParam(name="pageNo") Integer pageNo,
			@RequestParam(name="size") Integer size ,@PathVariable(name="orgId") Integer orgId)
			throws DynamicFormsServiceException {
		Map<String, Object> response = null;
		if (Optional.of(pageNo).isPresent() && Optional.of(size).isPresent()) {
			response = ohService.getEmployeesListWithMapping(pageNo,size,orgId);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@CrossOrigin
	@GetMapping(value="/employees/{type}/{orgId}")
	public ResponseEntity<?> getMappedEmployees(@PathVariable(name="type") String type,
			@PathVariable(name="orgId") String orgId)
			throws DynamicFormsServiceException {
			return new ResponseEntity<>(ohService.getMappedEmployees(type,orgId), HttpStatus.OK);
	}
	
	@CrossOrigin
	@GetMapping(value="/active-mappings/{empId}")
	public ResponseEntity<?> getMappingByEmpId(@PathVariable(name="empId") Integer empId)
			throws DynamicFormsServiceException {
			return new ResponseEntity<>(ohService.getMappingByEmpId(empId), HttpStatus.OK);
	}
	
	@CrossOrigin
	@PutMapping(value="/org-level-update")
	public ResponseEntity<?> updateOrgLevels(@RequestBody OHLevelUpdateReq req)
			throws DynamicFormsServiceException {
		String response = null;
		if (Optional.of(req).isPresent()) {
			response = ohService.updateOrgLevels(req);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@CrossOrigin
	@PutMapping(value="/update-node-displayname")
	public ResponseEntity<?> updateNodes(@RequestBody OHNodeUpdateReq req)
			throws DynamicFormsServiceException {
		String response = null;
		if (Optional.of(req).isPresent()) {
			response = ohService.updateNodes(req);
		} else {
			throw new DynamicFormsServiceException(env.getProperty("BAD_REQUEST"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/*
	@CrossOrigin
	@GetMapping(value="/active-dropdowns/{orgId}/{empId}")
	public ResponseEntity<?> getActiveDropdowns(@PathVariable(name="empId") Integer empId,
			@PathVariable(name="orgId") Integer orgId)
			throws DynamicFormsServiceException {
			return new ResponseEntity<>(ohService.getActiveDropdowns(orgId,empId), HttpStatus.OK);
	}
	*/
	@CrossOrigin
	@GetMapping(value="/active-levels/{orgId}/{empId}")
	public ResponseEntity<?> getActiveLevels(@PathVariable(name="empId") Integer empId,
			@PathVariable(name="orgId") Integer orgId)
			throws DynamicFormsServiceException {
			return new ResponseEntity<>(ohService.getActiveLevels(orgId,empId), HttpStatus.OK);
	}
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Set<Object> seen = ConcurrentHashMap.newKeySet();
	    return t -> seen.add(keyExtractor.apply(t));
	}

	@CrossOrigin
	@GetMapping(value="/active-branches/{orgId}/{empId}")
	public ResponseEntity<?> getActiveBranches(@PathVariable(name="empId") Integer empId,
			@PathVariable(name="orgId") Integer orgId)
			throws DynamicFormsServiceException {
			return new ResponseEntity<>(ohService.getActiveBranches(orgId,empId), HttpStatus.OK);
	}

	
	@CrossOrigin
	@PostMapping(value="/active-dropdowns/{orgId}/{empId}")
	public ResponseEntity<?> getActiveDropdowns(@RequestBody  List<Integer> levelList,@PathVariable(name="empId") Integer empId,
			@PathVariable(name="orgId") Integer orgId)
			throws DynamicFormsServiceException {
			Map<String, Object> dataMap = ohService.getActiveDropdownsV2(levelList,orgId,empId);
			Map<String, Object> formattedMap = new LinkedHashMap<>();
			dataMap.forEach((k,v)->{
				Map<String, Object> innerMap=(Map<String, Object>)v;
				innerMap.forEach((x,y)->{
					List<TargetDropDownV2> ddList = (List<TargetDropDownV2>)y;
					ddList=	ddList.stream().distinct().collect(Collectors.toList());
					if(formattedMap.containsKey(x)) {
						List<TargetDropDownV2> l = (List<TargetDropDownV2>)formattedMap.get(x);
						
						l.addAll(ddList);
						l=	l.stream().distinct().collect(Collectors.toList());
						formattedMap.put(x, l);
					}else {
						formattedMap.put(x, ddList);
					}
				});
			});
			
			return new ResponseEntity<>(formattedMap, HttpStatus.OK);
	}
}
