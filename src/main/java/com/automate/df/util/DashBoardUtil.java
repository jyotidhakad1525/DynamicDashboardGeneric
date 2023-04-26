package com.automate.df.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.automate.df.dao.dashboard.DmsLeadDao;
import com.automate.df.dao.dashboard.DmsWfTaskDao;
import com.automate.df.exception.DynamicFormsServiceException;
import com.automate.df.service.SalesGapService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DashBoardUtil {
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	DmsLeadDao dmsLeadDao;
	
	@Autowired
	DmsWfTaskDao dmsWfTaskDao;
	
	@Autowired
	SalesGapService salesGapService;
	
	
	@Value("${lostdrop.modelnames.main}")
	List<String> modelNamesList;
	
	
	
	@Autowired
	RestTemplate restTemplate;
	

	String vehicleDetailQuery = "SELECT id,model FROM `vehicle-management`.vehicle_details_new where organization_id=<ID>";
	String vehicleExShowRoomPriceQuery = "SELECT ex_showroom_price FROM `vehicle-management`.vehicle_on_road_price where organization_id=<ORG_ID> and vehicle_id=<VEHICLE_ID> and varient_id=<VARIENT_ID>";
	String roleMapQuery = "SELECT rolemap.organization_id,rolemap.branch_id,rolemap.emp_id,role.role_name,role.role_id FROM dms_role role INNER JOIN dms_employee_role_mapping rolemap ON rolemap.role_id=role.role_id\r\n"
			+ "AND rolemap.emp_id=<EMP_ID>;";
	String dmsEmpByidQuery =  "SELECT * FROM dms_employee where emp_id=<EMP_ID>";
	String getEmpUnderTLQuery = "SELECT emp_id FROM dms_employee where reporting_to=<ID>";
	String getEmpUnderBranch= "SELECT emp_id FROM dms_employee_role_mapping where branch_id=<ID>";
	String getEmpUnderLocation= "SELECT emp_id FROM dms_employee where location=<ID>";
	String getVariantName = "select variant from dms_lead_product where lead_id=<ID>";
	String getVariantId="SELECT id FROM `vehicle-management`.vehicle_varient_new where name=\"<name>\"";
	
	public List<Integer> getEmployeesUnderTL(String loggedInEmpId) {
		List<Integer> empIdsUnderReporting = entityManager
				.createNativeQuery(getEmpUnderTLQuery.replaceAll("<ID>", loggedInEmpId)).getResultList();
		log.debug("Employees under " + loggedInEmpId + " are " + empIdsUnderReporting);
		empIdsUnderReporting.add(Integer.valueOf(loggedInEmpId));
		return empIdsUnderReporting;
	}

	
	public List<Integer> getEmployeesUnderMgr(String managerID) {
		log.info("Generating Data for Manager "+managerID);
		List<Integer> empIdsUnderReporting=new ArrayList<>();
		List<Integer> managerTLEmpIds = entityManager.createNativeQuery(getEmpUnderTLQuery.replaceAll("<ID>", managerID)).getResultList();
		for(Integer tlID:managerTLEmpIds ) {
			empIdsUnderReporting.add(tlID);
			empIdsUnderReporting.addAll(entityManager.createNativeQuery(getEmpUnderTLQuery.replaceAll("<ID>", String.valueOf(tlID))).getResultList());
		}
		empIdsUnderReporting.add(Integer.valueOf(managerID));
		log.info("Employees under "+managerID+" are "+empIdsUnderReporting);
		return empIdsUnderReporting;
	}

	public List<Integer> getEmployeesUnderBranchMgr(String branchMgrId) {
		
		log.info("Generating Data for BranchMgr " + branchMgrId);
		
		List<Integer> empIdsUnderReporting=new ArrayList<>();
		List<Integer> MgrEmpIds = entityManager.createNativeQuery(getEmpUnderTLQuery.replaceAll("<ID>", branchMgrId)).getResultList();
		log.debug("MgrEmpIds "+MgrEmpIds);
		for(Integer mgrId:MgrEmpIds) {
			empIdsUnderReporting.add(mgrId);
			List<Integer> teamLeadList = entityManager.createNativeQuery(getEmpUnderTLQuery.replaceAll("<ID>", String.valueOf(mgrId))).getResultList();
			log.debug("teamLeadList "+teamLeadList);
			for(Integer tlId:teamLeadList) {
				empIdsUnderReporting.add(tlId);
				empIdsUnderReporting.addAll(entityManager.createNativeQuery(getEmpUnderTLQuery.replaceAll("<ID>", String.valueOf(tlId))).getResultList());
			}
		}
		empIdsUnderReporting.add(Integer.valueOf(branchMgrId));
		log.info("Employees under " + branchMgrId + " are " + empIdsUnderReporting);
		return empIdsUnderReporting;
	}

	public List<Integer> getEmployeesUnderGeneralMgr(String generalMgrId) {
		List<Integer> empIdsUnderReporting=new ArrayList<>();
		List<Integer> BranchMgrEmpIds = entityManager.createNativeQuery(getEmpUnderTLQuery.replaceAll("<ID>", generalMgrId)).getResultList();
		log.debug("BranchMgrEmpIds "+BranchMgrEmpIds);
		for(Integer branchMgrID : BranchMgrEmpIds) {
			List<Integer> MgrEmpIds = entityManager.createNativeQuery(getEmpUnderTLQuery.replaceAll("<ID>", String.valueOf(branchMgrID))).getResultList();
			empIdsUnderReporting.add(branchMgrID);
			for(Integer mgrId:MgrEmpIds) {
				empIdsUnderReporting.add(mgrId);
				List<Integer> teamLeadList = entityManager.createNativeQuery(getEmpUnderTLQuery.replaceAll("<ID>", String.valueOf(mgrId))).getResultList();
				for(Integer tlId:teamLeadList) {
					empIdsUnderReporting.add(tlId);
					empIdsUnderReporting.addAll(entityManager.createNativeQuery(getEmpUnderTLQuery.replaceAll("<ID>", String.valueOf(tlId))).getResultList());
				}
			}
		}
		empIdsUnderReporting.add(Integer.valueOf(generalMgrId));
		log.info("Employees under " + generalMgrId + " are " + empIdsUnderReporting);
		return empIdsUnderReporting;
	}


	public List<Integer> getEmployeesUnderBranch(String branchId) {
		List<Integer> empIdsUnderReporting = entityManager
				.createNativeQuery(getEmpUnderBranch.replaceAll("<ID>", branchId)).getResultList();
		
		//empIdsUnderReporting = empIdsUnderReporting.stream().filter(x->salesGapService.getEmployeeRoleV2(x).equalsIgnoreCase("DSE")).collect(Collectors.toList());
		log.info("Employees Under branch " + branchId + " are " + empIdsUnderReporting);
		empIdsUnderReporting.add(Integer.valueOf(branchId));
		return empIdsUnderReporting;
	}

	

	public List<Integer> getEmployeesUnderLocation(String branchId, String locationId) throws DynamicFormsServiceException {
		List<Integer> empIdsUnderReporting = entityManager
				.createNativeQuery(getEmpUnderLocation.replaceAll("<ID>", locationId)).getResultList();
		try {
		empIdsUnderReporting = empIdsUnderReporting.stream().filter(x->salesGapService.getEmployeeRoleV2(x).equalsIgnoreCase("DSE")).collect(Collectors.toList());
		log.info("Employees with DSE role under " + branchId + " are " + empIdsUnderReporting);
		empIdsUnderReporting.add(Integer.valueOf(branchId));
		}catch(Exception e) {
			e.printStackTrace();
		}
		return empIdsUnderReporting;
	}


	public String getEventNameFromCode(String code) {
		return code;
	}
	
	public Map<String,Map<Integer,String>> getVehilceDetails(String orgId) {
		List<Object[]> list = entityManager
				.createNativeQuery(vehicleDetailQuery.replaceAll("<ID>",orgId)).getResultList();
		Map<String,Map<Integer,String>> resMap = new HashMap<>();
		Map<Integer,String> map = new LinkedHashMap<>();
		Map<Integer,String> otherMap = new LinkedHashMap<>();
		
		int cnt=1;
		for(Object[] arr :list){
				if(cnt>5) {
					otherMap.put((Integer)arr[0], (String)arr[1]);
				}else {
					map.put((Integer)arr[0], (String)arr[1]);
				}
				cnt++;
		}
		resMap.put("main",map);
		resMap.put("others",otherMap);
		log.debug("resMap in getVehilceDetails:"+resMap);
		return resMap;
	}
	
	public Map<String,Integer> getVehilceDetailsByOrgId(String orgId) {
		List<Object[]> list = entityManager
				.createNativeQuery(vehicleDetailQuery.replaceAll("<ID>",orgId)).getResultList();
		Map<String,Integer> resMap = new HashMap<>();
		for (Object[] arr : list) {
			resMap.put((String) arr[1], (Integer) arr[0]);
		}
		return resMap;
	}
	
	
	public BigDecimal getVehiclePrice(String orgId,Integer vehicleId,Integer variantId) {
		String tmp = vehicleExShowRoomPriceQuery;
		tmp = tmp.replaceAll("<ORG_ID>", orgId);
		tmp = tmp.replaceAll("<VEHICLE_ID>", String.valueOf(vehicleId));
		tmp = tmp.replaceAll("<VARIENT_ID>", String.valueOf(variantId));
		List<BigDecimal> list = entityManager
				.createNativeQuery(tmp).getResultList();
		System.out.println("list "+list);
		BigDecimal bd = null;
		for(BigDecimal arr :list){
			bd = arr;
		}
		if(null!=bd) {
			return bd;
		}
		return new BigDecimal(0);
	
	}
	
	public Integer getVariantId(Integer id) {
		List<String> list = entityManager
				.createNativeQuery(getVariantName.replaceAll("<ID>",String.valueOf(id))).getResultList();
		String res =null;
		for(String arr :list){
			res=arr;
		}
		Integer variantId=0;
		if(null!=res && !res.isEmpty()) {
			log.debug("Variant name is "+res);
			List<Integer> idList = entityManager
					.createNativeQuery(getVariantId.replaceAll("<name>",res)).getResultList();
		
	
		for(Integer i :idList){
			variantId=i;
		}
		}
		log.debug("variantId is "+variantId+" for given Lead ID  "+id);
		return variantId;
	}
	
	public List<Integer> getPaginatedList(List<Integer> outputList,int pageNo,int size){
		pageNo = pageNo+1;
		if(null!=outputList) {
			int totalCnt = outputList.size();
			int fromIndex = size * (pageNo - 1);
			int toIndex = size * pageNo;
	
			if (toIndex > totalCnt) {
				toIndex = totalCnt;
			}
			if (fromIndex > toIndex) {
				fromIndex = toIndex;
			}
			outputList = outputList.subList(fromIndex, toIndex);
		}
		return outputList;
	}
	
	
}
