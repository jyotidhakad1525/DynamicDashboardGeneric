package com.automate.df.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.automate.df.dao.dashboard.DmsLeadDao;
import com.automate.df.dao.dashboard.DmsLeadDropDao;
import com.automate.df.dao.dashboard.DmsWfTaskDao;
import com.automate.df.dao.salesgap.DmsEmployeeRepo;
import com.automate.df.dao.salesgap.TargetSettingRepo;
import com.automate.df.dao.salesgap.TargetUserRepo;
import com.automate.df.entity.dashboard.DmsLead;
import com.automate.df.entity.dashboard.DmsWFTask;
import com.automate.df.entity.salesgap.DmsEmployee;
import com.automate.df.entity.salesgap.TargetRoleReq;
import com.automate.df.exception.DynamicFormsServiceException;
import com.automate.df.model.df.dashboard.DashBoardReq;
import com.automate.df.model.df.dashboard.DropRes;
import com.automate.df.model.df.dashboard.EventDataRes;
import com.automate.df.model.df.dashboard.LeadSourceRes;
import com.automate.df.model.df.dashboard.LostRes;
import com.automate.df.model.df.dashboard.TargetAchivement;
import com.automate.df.model.df.dashboard.TodaysRes;
import com.automate.df.model.df.dashboard.VehicleModelRes;
import com.automate.df.model.salesgap.TargetRoleRes;
import com.automate.df.model.salesgap.TargetSettingRes;
import com.automate.df.service.DashBoardService;
import com.automate.df.service.SalesGapService;
import com.automate.df.util.DashBoardUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author srujan
 *
 */
@Slf4j
@Service
public class DashBoardServiceImpl implements DashBoardService{
	
	@Autowired
	Environment env;
	
	@Autowired
	TargetSettingRepo targetSettingRepo;
	
	@Autowired
	TargetUserRepo targetUserRepo;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	Gson gson;
	
	@Autowired
	SalesGapServiceImpl salesGapServiceImpl;
	
	@Autowired
	DmsEmployeeRepo dmsEmployeeRepo;
	
	@Autowired
	DashBoardUtil dashBoardUtil;

	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	DmsLeadDao dmsLeadDao;
	
	@Autowired
	DmsWfTaskDao dmsWfTaskDao;
	
	@Autowired
	DmsLeadDropDao dmsLeadDropDao;
	
	
	public static final String ENQUIRY = "Enquiry";
	public static final String DROPPED = "DROPPED";
	public static final String TEST_DRIVE= "Test Drive";
	public static final String HOME_VISIT= "Home Visit";
	public static final String FINANCE= "Finance";
	public static final String INSURANCE= "Insurance";
	public static final String VIDEO_CONFERENCE= "Video Conference";
	public static final String PROCEED_TO_BOOKING= "Proceed to Booking";
	public static final String BOOKING_FOLLOWUP_DSE= "Booking Follow Up - DSE";
	public static final String BOOKING_FOLLOWUP_CRM= "Booking Follow Up - CRM";
	public static final String BOOKING_FOLLOWUP_ACCCESSORIES= "Booking Follow Up - Accessories";
	public static final String VERIFY_EXCHANGE_APPROVAL = "Verify Exchange Approval";
	
	
	public static final String READY_FOR_INVOICE= "Ready for invoice - Accounts";
	public static final String PROCEED_TO_INVOICE= "Proceed to Invoice";
	public static final String INVOICE_FOLLOWUP_DSE = "Invoice Follow Up - DSE";
	public static final String INVOICE = "INVOICE";
	
	
	public static final String PRE_BOOKING = "Pre Booking";
	public static final String DELIVERY = "Delivery";
	
	public static final String BOOKING = "Booking";
	public static final String EXCHANGE = "Exchange";
	public static final String ACCCESSORIES = "Accessories";
	public static final String EVENTS = "Events";
	
	String roleMapQuery = "SELECT rolemap.organization_id,rolemap.branch_id,rolemap.emp_id,role.role_name,role.role_id FROM dms_role role INNER JOIN dms_employee_role_mapping rolemap ON rolemap.role_id=role.role_id\r\n"
			+ "AND rolemap.emp_id=<EMP_ID>;";
	String dmsEmpByidQuery =  "SELECT * FROM dms_employee where emp_id=<EMP_ID>";
	String getEmpUnderTLQuery = "SELECT emp_id FROM dms_employee where reporting_to=<ID>";

	@Override
	public List<TargetAchivement> getTargetAchivementParams(DashBoardReq req) {
		log.info("Inside getTargetAchivementParams(){}");
		List<TargetAchivement> list = new ArrayList<>();
		try {
			
			String empId = req.getLoggedInEmpId();
			TargetRoleRes tRole = salesGapServiceImpl.getEmpRoleData(Integer.valueOf(empId));
			String designation = tRole.getDesignationName();
			log.info("LOGGED IN EMP Designation "+designation);
			if(salesGapServiceImpl.validateDSE(designation)) {
				log.info("Getting date for DSE "+empId);
				list=buildTargetAchivementForDSE(tRole.getEmpId(),req);
			}
			if(salesGapServiceImpl.validateTL(designation)) {
				
				if(null!=req && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("TL - Page Load");
					list=buildTargetAchivementForTL(empId,req);
				}
				else if(null!=req && null != req.getEmployeeId()) {
					log.info("Emp ID is present");
					list = buildTargetAchivementForDSE(req.getEmployeeId(),req);
				}
			}
		
			if(salesGapServiceImpl.validateMgr(designation)) {
				if(null!=req && null==req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info(" Manager - Page Load");
					list=buildTargetAchivementForMgr(empId,req);
				}
				else if(null!=req && null!=req.getTeamLeadId() && null == req.getEmployeeId()) {
					log.info("TeamLead ID is present");
					list = buildTargetAchivementForTL(req.getTeamLeadId(),req);
				}
				
				else if(null!=req && null!=req.getTeamLeadId() && null != req.getEmployeeId()) {
					log.info("TeamLead ID and Emp ID is present");
					list = buildTargetAchivementForDSE(req.getEmployeeId(),req);
				}
			}
			
			if(salesGapServiceImpl.validateBranchMgr(designation)) {
				
				if(null!=req && null==req.getBranchmangerId() && null==req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Branch Manager PageLoad");
					list=buildTargetAchivementForBranchMgr(empId,req);
				}
				else if(null!=req && null!=req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId()) {
					log.info("Only Manager ID and Manager ID is present");
					list=buildTargetAchivementForMgr(req.getManagerId(),req);
				}
				
				else if(null!=req &&  null!=req.getManagerId() && null!=req.getTeamLeadId() && null == req.getEmployeeId()) {
					log.info("Only Manager ID and Team LeadID is present");
					list = buildTargetAchivementForTL(req.getTeamLeadId(),req);
				}
				else if(null!=req && null!=req.getManagerId() && null!=req.getTeamLeadId() && null != req.getEmployeeId()) {
					log.info("Manager ID , TeamLead ID and Emp ID is present");
					list = buildTargetAchivementForDSE(req.getEmployeeId(),req);
				}
			}
			
			if(salesGapServiceImpl.validateGeneralMgr(designation)  && null!=req) {
	
				if(null== req.getBranchId() && null==req.getLocationId() && null==req.getBranchmangerId() && null==req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("General Manager PageLoad");
					list=buildTargetAchivementForGeneralMgr(empId,req);
				}
				else if(null != req.getBranchId() && null==req.getLocationId() && null==req.getBranchmangerId() && null==req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID is present");
					list=buildTargetAchivementForBranch(empId,req);
				}
				else if(null != req.getBranchId() && null!=req.getLocationId() && null==req.getBranchmangerId() && null==req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID and Location ID is present");
					list=buildTargetAchivementForLocation(empId,req);
				}
				
				else if(null != req.getBranchId() && null!=req.getLocationId() && null!=req.getBranchmangerId() && null==req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID,Location ID and BranchMgr ID is present");
					list=buildTargetAchivementForBranchMgr(req.getBranchmangerId(),req);
				}
				else if(null != req.getBranchId() && null!=req.getLocationId() && null!=req.getBranchmangerId() && null!=req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID,Location ID ,BranchMgr ID and Manager ID is present");
					list=buildTargetAchivementForBranchMgr(req.getManagerId(),req);
				}
				else if(null != req.getBranchId() && null!=req.getLocationId() && null!=req.getBranchmangerId() && null!=req.getManagerId() && null!=req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID,Location ID ,BranchMgr ID and Manager ID is present");
					list=buildTargetAchivementForBranchMgr(req.getTeamLeadId(),req);
				}
				else if(null != req.getBranchId() && null!=req.getLocationId() && null!=req.getBranchmangerId() && null!=req.getManagerId() && null!=req.getTeamLeadId() && null != req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID,Location ID ,BranchMgr ID , Manager ID and employeeID is present");
					list = buildTargetAchivementForDSE(req.getEmployeeId(),req);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}


	private List<TargetAchivement> buildTargetAchivementForLocation(String empId, DashBoardReq req) {
		List<TargetAchivement> resList = new ArrayList<>();
		try {
			String branchId = req.getBranchId();
			String locationId = req.getLocationId();
			log.info("Generating Data for Branch " + branchId+" and Location "+locationId);
			List<Integer> empIdsUnderReporting=dashBoardUtil.getEmployeesUnderLocation(branchId, locationId);
			List<String> empNamesList = dmsEmployeeRepo.findEmpNamesById(empIdsUnderReporting);
			log.info("empNamesList::" + empNamesList);
			resList = getTargetAchivementParamsForMultipleEmp(empId, empIdsUnderReporting, req);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resList;
	}


	private List<TargetAchivement> buildTargetAchivementForBranch(String empId, DashBoardReq req) {
		List<TargetAchivement> resList = new ArrayList<>();
		try {
			String branchId = req.getBranchId();
			log.info("Generating Data for Branch " + branchId);
			List<Integer> empIdsUnderReporting=dashBoardUtil.getEmployeesUnderBranch(branchId);
			
			List<String> empNamesList = dmsEmployeeRepo.findEmpNamesById(empIdsUnderReporting);
			log.info("empNamesList::" + empNamesList);
			resList = getTargetAchivementParamsForMultipleEmp(empId, empIdsUnderReporting, req);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resList;
	}


	


	private List<TargetAchivement> buildTargetAchivementForGeneralMgr(String generalMgrId, DashBoardReq req) {
		List<TargetAchivement> resList = new ArrayList<>();
		try {
			log.info("Generating Data for GeneralMgr " + generalMgrId);
			
			List<Integer> empIdsUnderReporting=dashBoardUtil.getEmployeesUnderGeneralMgr(generalMgrId);
			List<String> empNamesList = dmsEmployeeRepo.findEmpNamesById(empIdsUnderReporting);
			log.info("empNamesList::" + empNamesList);
			resList = getTargetAchivementParamsForMultipleEmp(generalMgrId, empIdsUnderReporting, req);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resList;
	}


	private List<TargetAchivement> buildTargetAchivementForBranchMgr(String branchMgrId, DashBoardReq req) {
		List<TargetAchivement> resList = new ArrayList<>();
		try {
			log.info("Generating Data for BranchMgr " + branchMgrId);
				List<Integer> empIdsUnderReporting=dashBoardUtil.getEmployeesUnderBranchMgr(branchMgrId);
				List<String> empNamesList = dmsEmployeeRepo.findEmpNamesById(empIdsUnderReporting);
			log.info("empNamesList::" + empNamesList);
			resList = getTargetAchivementParamsForMultipleEmp(branchMgrId, empIdsUnderReporting, req);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resList;
	}


	private List<TargetAchivement> buildTargetAchivementForTL(String teamLeadEmpId, DashBoardReq req) {
		List<TargetAchivement> resList = new ArrayList<>();
		try {
			log.info("Generating Data for TL " + teamLeadEmpId);
			List<Integer> empIdsUnderReporting = dashBoardUtil.getEmployeesUnderTL(teamLeadEmpId);
			List<String> empNamesList = dmsEmployeeRepo.findEmpNamesById(empIdsUnderReporting);
			log.info("empNamesList::" + empNamesList);
			resList = getTargetAchivementParamsForMultipleEmp(teamLeadEmpId, empIdsUnderReporting, req);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resList;
	}

	private List<TargetAchivement> buildTargetAchivementForMgr(String managerID, DashBoardReq req) {
		   List<TargetAchivement> resList = new ArrayList<>();
		   try {
			log.info("Generating Data for Manager "+managerID);
			List<Integer> empIdsUnderReporting=dashBoardUtil.getEmployeesUnderMgr(managerID);
			resList = getTargetAchivementParamsForMultipleEmp(managerID,empIdsUnderReporting,req);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		
		return resList;
	
	}



	private List<TargetAchivement> getTargetAchivementParamsForMultipleEmp(String targetParamId,
			List<Integer> empIdsUnderReporting, DashBoardReq req) throws ParseException, DynamicFormsServiceException {
		List<TargetAchivement> resList = new ArrayList<>();
		List<String> empNamesList = dmsEmployeeRepo.findEmpNamesById(empIdsUnderReporting);
		log.info("empNamesList::" + empNamesList);
		String startDate = null;
		String endDate = null;
		if (null == req.getStartDate() && null == req.getEndDate()) {
			startDate = getFirstDayOfMonth();
			endDate = getLastDayOfMonth();
		} else {
			startDate = req.getStartDate();
			endDate = req.getEndDate();
		}

		log.info("StartDate " + startDate + ", EndDate " + endDate);
		Map<String, Integer> targetParamMapTL = getTargetParams(targetParamId, startDate, endDate);
		List<DmsLead> dmsLeadList = dmsLeadDao.getAllEmployeeLeads(empNamesList, startDate, endDate, ENQUIRY);
		Long enqLeadCnt = 0L;
		if (null != dmsLeadList) {
			enqLeadCnt = getEnqLeadCount(dmsLeadList);
			log.info("Total Enquiry Leads for given time period is " + enqLeadCnt);
		}
		List<DmsWFTask> wfTaskList = dmsWfTaskDao.getWfTaskByAssigneeIdList(empIdsUnderReporting, startDate, endDate);
		return buildTargetAchivements(resList, targetParamMapTL, enqLeadCnt, wfTaskList);
	}




	private List<TargetAchivement> buildTargetAchivementForDSE(String empId, DashBoardReq req) {
		   List<TargetAchivement> resList = new ArrayList<>();
		   try {
			log.info("Generating Data for DSE");
			Optional<DmsEmployee> empOpt = dmsEmployeeRepo.findById(Integer.valueOf(empId));
			if(empOpt.isPresent()) {
				String empName = salesGapServiceImpl.getTeamLeadName(empId);
				log.info("Emp ID "+empId + " empName "+empName); 
				String startDate = null;
				String endDate = null;
				if(null==req.getStartDate() && null == req.getEndDate()) {
					startDate = getFirstDayOfMonth();
					endDate = getLastDayOfMonth();
				}else {
					startDate = req.getStartDate();
					endDate = req.getEndDate();
				}
				
				log.info("StartDate "+startDate+", EndDate "+endDate);;
				Map<String,Integer> targetParamMap = getTargetParams(empId,startDate,endDate);
				// getting Enquiry Count
				List<DmsLead> dmsLeadList = dmsLeadDao.getLeads(empName, startDate, endDate, ENQUIRY);
				Long enqLeadCnt =0L;
				if(null!=dmsLeadList) {
					enqLeadCnt = getEnqLeadCount(dmsLeadList);
					log.info("Total Enquiry Leads for given time period is "+enqLeadCnt);
				}
				
				List<DmsWFTask> wfTaskList = dmsWfTaskDao.getWfTaskByAssigneeId(empId, startDate, endDate);
				resList = buildTargetAchivements(resList,targetParamMap,enqLeadCnt,wfTaskList);
			}
		
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		
		return resList;
	}

	private List<TargetAchivement> buildTargetAchivements(List<TargetAchivement> resList,
			Map<String, Integer> targetParamMap, Long enqLeadCnt,List<DmsWFTask> wfTaskList) {
		

		// Getting Test Drive Cnt
		Long testDriveCnt = getTestDriveCount(wfTaskList);
		Long financeCnt = getFinanceCount(wfTaskList);
		Long insuranceCnt =getInsuranceCount(wfTaskList);
		Long accessoriesCnt = getAccessoriesCount(wfTaskList);
		Long bookingCnt = getBookingCount(wfTaskList);
		Long homeVistCnt = getHomeVisitCount(wfTaskList);
		//Long videoConfCnt = wfTaskList.stream().filter(x->x.getTaskName().equalsIgnoreCase(VIDEO_CONFERENCE)).count();
		Long exchangeCnt = getExchangeCount(wfTaskList);
		Long invoceCnt = getInvoiceCountTarget(wfTaskList);
		//Long retailCnt = 0L;
		
		
		TargetAchivement enqTargetAchivement = new TargetAchivement();
		enqTargetAchivement.setTarget(String.valueOf(targetParamMap.get(ENQUIRY)));
		enqTargetAchivement.setParamName(ENQUIRY);
		enqTargetAchivement.setParamShortName("E");
		enqTargetAchivement.setAchievment(String.valueOf(enqLeadCnt));;
		enqTargetAchivement.setAchivementPerc(getAchievmentPercentage(enqLeadCnt,targetParamMap.get(ENQUIRY)));
		enqTargetAchivement.setShortfall(getShortFallCount(enqLeadCnt,targetParamMap.get(ENQUIRY)));
		enqTargetAchivement.setShortFallPerc(getShortFallPercentage(enqLeadCnt,targetParamMap.get(ENQUIRY)));;
		resList.add(enqTargetAchivement);
		
		TargetAchivement testDriveTA = new TargetAchivement();
		testDriveTA.setTarget(String.valueOf(targetParamMap.get(TEST_DRIVE)));
		testDriveTA.setParamName(TEST_DRIVE);
		testDriveTA.setParamShortName("TD");
		testDriveTA.setAchievment(String.valueOf(testDriveCnt));
		testDriveTA.setAchivementPerc(getAchievmentPercentage(testDriveCnt,targetParamMap.get(TEST_DRIVE)));
		testDriveTA.setShortfall(getShortFallCount(testDriveCnt,targetParamMap.get(TEST_DRIVE)));
		testDriveTA.setShortFallPerc(getShortFallPercentage(testDriveCnt,targetParamMap.get(TEST_DRIVE)));
		resList.add(testDriveTA);
		
		
		TargetAchivement financeTA = new TargetAchivement();
		financeTA.setTarget(String.valueOf(targetParamMap.get(FINANCE)));
		financeTA.setParamName(FINANCE);
		financeTA.setParamShortName("F");
		financeTA.setAchievment(String.valueOf(financeCnt));
		financeTA.setAchivementPerc(getAchievmentPercentage(financeCnt,targetParamMap.get(FINANCE)));
		financeTA.setShortfall(getShortFallCount(financeCnt,targetParamMap.get(FINANCE)));
		financeTA.setShortFallPerc(getShortFallPercentage(financeCnt,targetParamMap.get(FINANCE)));
		resList.add(financeTA);
		
		TargetAchivement insuranceTA = new TargetAchivement();
		insuranceTA.setTarget(String.valueOf(targetParamMap.get(INSURANCE)));
		insuranceTA.setParamName(INSURANCE);
		insuranceTA.setParamShortName("I");
		insuranceTA.setAchievment(String.valueOf(insuranceCnt));
		insuranceTA.setAchivementPerc(getAchievmentPercentage(insuranceCnt,targetParamMap.get(FINANCE)));
		insuranceTA.setShortfall(getShortFallCount(insuranceCnt,targetParamMap.get(FINANCE)));
		insuranceTA.setShortFallPerc(getShortFallPercentage(insuranceCnt,targetParamMap.get(FINANCE)));
		resList.add(insuranceTA);
		
		
		TargetAchivement accessoriesTA = new TargetAchivement();
		accessoriesTA.setTarget(String.valueOf(targetParamMap.get(ACCCESSORIES)));
		accessoriesTA.setParamName(ACCCESSORIES);
		accessoriesTA.setParamShortName("Acc.");
		accessoriesTA.setAchievment(String.valueOf(accessoriesCnt));
		accessoriesTA.setAchivementPerc(getAchievmentPercentage(accessoriesCnt,targetParamMap.get(ACCCESSORIES)));
		accessoriesTA.setShortfall(getShortFallCount(accessoriesCnt,targetParamMap.get(ACCCESSORIES)));
		accessoriesTA.setShortFallPerc(getShortFallPercentage(accessoriesCnt,targetParamMap.get(ACCCESSORIES)));
		resList.add(accessoriesTA);
		
		
		TargetAchivement bookingTA = new TargetAchivement();
		bookingTA.setTarget(String.valueOf(targetParamMap.get(BOOKING)));
		bookingTA.setParamName(BOOKING);
		bookingTA.setParamShortName("B");
		bookingTA.setAchievment(String.valueOf(bookingCnt));
		bookingTA.setAchivementPerc(getAchievmentPercentage(bookingCnt,targetParamMap.get(BOOKING)));
		bookingTA.setShortfall(getShortFallCount(bookingCnt,targetParamMap.get(BOOKING)));
		bookingTA.setShortFallPerc(getShortFallPercentage(bookingCnt,targetParamMap.get(BOOKING)));
		resList.add(bookingTA);
		
		TargetAchivement homeVisitTA = new TargetAchivement();
		homeVisitTA.setTarget(String.valueOf(targetParamMap.get(HOME_VISIT)));
		homeVisitTA.setParamName(HOME_VISIT);
		homeVisitTA.setParamShortName("HV");
		homeVisitTA.setAchievment(String.valueOf(homeVistCnt));
		homeVisitTA.setAchivementPerc(getAchievmentPercentage(homeVistCnt,targetParamMap.get(HOME_VISIT)));
		homeVisitTA.setShortfall(getShortFallCount(homeVistCnt,targetParamMap.get(HOME_VISIT)));
		homeVisitTA.setShortFallPerc(getShortFallPercentage(homeVistCnt,targetParamMap.get(HOME_VISIT)));
		resList.add(homeVisitTA);
		
		TargetAchivement exchangeTA = new TargetAchivement();
		exchangeTA.setTarget(String.valueOf(targetParamMap.get(EXCHANGE)));
		exchangeTA.setParamName(EXCHANGE);
		exchangeTA.setParamShortName("Ex");
		exchangeTA.setAchievment(String.valueOf(exchangeCnt));
		exchangeTA.setAchivementPerc(getAchievmentPercentage(exchangeCnt,targetParamMap.get(EXCHANGE)));
		exchangeTA.setShortfall(getShortFallCount(exchangeCnt,targetParamMap.get(EXCHANGE)));
		exchangeTA.setShortFallPerc(getShortFallPercentage(exchangeCnt,targetParamMap.get(EXCHANGE)));
		resList.add(exchangeTA);
		
		
		TargetAchivement vcTA = new TargetAchivement();
		vcTA.setTarget(String.valueOf(targetParamMap.get(VIDEO_CONFERENCE)));
		vcTA.setParamName(VIDEO_CONFERENCE);
		vcTA.setParamShortName("VC");
		vcTA.setAchievment(String.valueOf(0));
		vcTA.setAchivementPerc(String.valueOf(0));
		vcTA.setShortfall(String.valueOf(0));
		vcTA.setShortFallPerc(String.valueOf(0));
		resList.add(vcTA);
		
		TargetAchivement rTa = new TargetAchivement();
		rTa.setTarget(String.valueOf(targetParamMap.get(INVOICE)));
		rTa.setParamName(INVOICE);
		rTa.setParamShortName("R");
		rTa.setAchievment(String.valueOf(invoceCnt));
		rTa.setAchivementPerc(getAchievmentPercentage(invoceCnt,targetParamMap.get(INVOICE)));
		rTa.setShortfall(getShortFallCount(invoceCnt,targetParamMap.get(INVOICE)));
		rTa.setShortFallPerc(getShortFallPercentage(invoceCnt,targetParamMap.get(INVOICE)));
		resList.add(rTa);
		return resList;
	}

	

	


	private Map<String,Integer> getTargetParams(String empId, String start, String end) throws ParseException, DynamicFormsServiceException {
		TargetRoleReq targetRoleReq = new TargetRoleReq();
		targetRoleReq.setEmpId(Integer.valueOf(empId));
		targetRoleReq.setPageNo(1);
		targetRoleReq.setSize(10);
		Map<String, Object> tagetAdminMap = salesGapServiceImpl.getTargetDataWithRole(targetRoleReq);
		List<TargetSettingRes> adminTargetSettingData = (List<TargetSettingRes>)tagetAdminMap.get("data");
		log.info("size of adminTargetSettingData "+adminTargetSettingData.size());
		Date startDate = parseDate(start);  
		Date endDate = parseDate(end); 
		log.info("startDate:"+startDate+",endDate:"+endDate);
		List<TargetSettingRes> filteredList = new ArrayList<>();
		for(TargetSettingRes res :adminTargetSettingData) {
			
			Date resStartDate = parseDate(res.getStartDate());
			Date resEndDate = parseDate(res.getEndDate());
			log.info("resStartDate:"+resStartDate+",resEndDate:"+resEndDate);
			System.out.println("startDate equals "+startDate.equals(resStartDate));
			if((startDate.equals(resStartDate)||startDate.after(resStartDate)) 
					&&(endDate.equals(resEndDate) || endDate.before(resEndDate))) {
				filteredList.add(res);
			}
		}
		log.info("filteredList for given date range "+filteredList.size());
		
		Integer retailTarget = 0;
		Integer enquiry=0;
		Integer testdrive=0;
		Integer homeVisit=0;
		Integer videoConf=0;
		Integer booking=0;
		Integer exchange=0;
		Integer finance=0;
		Integer insurance=0;
		Integer accessories=0;
		Integer events = 0;
		Integer invoice=0;
		
		for(TargetSettingRes res :filteredList) {
			retailTarget += validateNumber(res.getRetailTarget());
			enquiry += validateNumber(res.getEnquiry());
			testdrive += validateNumber(res.getTestDrive());
			homeVisit += validateNumber(res.getHomeVisit());
			videoConf += validateNumber(res.getVideoConference());
			booking += validateNumber(res.getBooking());
			exchange += validateNumber(res.getExchange());
			finance += validateNumber(res.getFinance());
			insurance += validateNumber(res.getInsurance());
			accessories += validateNumber(res.getAccessories());
			//exW += Integer.valueOf(res.getExchange());
			events += validateNumber(res.getEvents());
			invoice += validateNumber(res.getInvoice());
		}
		
		Map<String,Integer> map = new HashMap<>();
		map.put(ENQUIRY,enquiry);
		map.put(TEST_DRIVE, testdrive);
		map.put(HOME_VISIT, homeVisit);
		map.put(VIDEO_CONFERENCE, videoConf);
		map.put(BOOKING, booking);
		map.put(EXCHANGE, exchange);
		map.put(FINANCE, finance);
		map.put(INSURANCE, insurance);
		map.put(ACCCESSORIES, accessories);
		map.put(EVENTS, events);
		map.put(INVOICE, invoice);
		return map;
	}

	private Integer validateNumber(String retailTarget) {
		if(null!=retailTarget) {
			return Integer.valueOf(retailTarget);
		}
		return 0;
	}

	private Date parseDate(String date) throws ParseException {
		return new SimpleDateFormat("yyyy-MM-dd").parse(date);
	}

	private String getShortFallPercentage(Long cnt, Integer target) {
		Double perc = 0D;
		Double shortfall = Double.valueOf(target)- Double.valueOf(cnt);
		if(target>0) {
		 perc = (shortfall/Double.valueOf(target))*100;
		}
		return String.format("%.2f", perc);
	}

	private String getShortFallCount(Long cnt, Integer target) {
		Long shorfall = Long.valueOf(target)- Long.valueOf(cnt);
		return String.valueOf(shorfall);
	}

	private String getAchievmentPercentage(Long cnt, Integer target) {
		Double perc = 0D;
		if(target>0) {
			perc = (Double.valueOf(cnt)/Double.valueOf(target))*100;
		}
		return String.format("%.2f", perc);
	}

	
	private TargetRoleRes getEmployeeRoleInfo(String empId) {
		String tmpQuery = dmsEmpByidQuery.replaceAll("<EMP_ID>",String.valueOf(empId));
		
		tmpQuery = roleMapQuery.replaceAll("<EMP_ID>",String.valueOf(empId));
		List<Object[]> data = entityManager.createNativeQuery(tmpQuery).getResultList();
		TargetRoleRes trRoot = new TargetRoleRes();
		for(Object[] arr : data) {
			trRoot.setOrgId(String.valueOf(arr[0]));
			trRoot.setBranchId(String.valueOf(arr[1]));
			trRoot.setEmpId(String.valueOf(arr[2]));
			trRoot.setRoleName(String.valueOf(arr[3]));
			trRoot.setRoleId(String.valueOf(arr[4]));
		}
		
		return trRoot;
	}
	
	
	public String getFirstDayOfMonth() {
			return LocalDate.ofEpochDay(System.currentTimeMillis() / (24 * 60 * 60 * 1000) ).withDayOfMonth(1).toString()+" 00:00:00";
	}
	public String getLastDayOfMonth() {
			return LocalDate.ofEpochDay(System.currentTimeMillis() / (24 * 60 * 60 * 1000) ).plusMonths(1).withDayOfMonth(1).minusDays(1).toString()+" 00:00:00";
	}
	
	

	@Override
	public List<VehicleModelRes> getVehicleModelData(DashBoardReq req) {

		log.info("Inside getVehicleModelData(){}");
		List<VehicleModelRes> list = new ArrayList<>();
		try {
			
			
			
			String empId = req.getLoggedInEmpId();
			TargetRoleRes tRole = salesGapServiceImpl.getEmpRoleData(Integer.valueOf(empId));
			String designation = tRole.getDesignationName();
			log.info("LOGGED IN EMP Designation "+designation);	
		
			 Map<Integer,String> vehicleDataMap = dashBoardUtil.getVehilceDetails(tRole.getOrgId()).get("main");
			 //List<String> vehicleModelList = dmsLeadDao.getModelNames();
			 List<String> vehicleModelList = new ArrayList<>();
			 vehicleDataMap.forEach((k,v)->{
				 vehicleModelList.add(v);
			 });
			 
			if(salesGapServiceImpl.validateDSE(designation)) {
				log.info("Getting date for DSE "+empId);
				list=buildVehicleModelForDSE(tRole,req,vehicleModelList,empId);
			}
			
			if(salesGapServiceImpl.validateTL(designation)) {
				
				if(null!=req && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("TL - Page Load");
					list=buildVehicleModelForTL(tRole,req,vehicleModelList,req.getLoggedInEmpId());
				}
				else if(null!=req && null != req.getEmployeeId() && null!=empId) {
					log.info("Emp ID is present");
					list=buildVehicleModelForDSE(tRole,req,vehicleModelList,req.getEmployeeId());
				}
			}
			
			if(salesGapServiceImpl.validateMgr(designation)) {
				if(null!=req && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info(" Manager - Page Load");
					list=buildVehicleModelForMgr(tRole,req,vehicleModelList,req.getLoggedInEmpId());
				}
				else if(null!=req && null!=req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("TeamLead ID is present");
					list=buildVehicleModelForTL(tRole,req,vehicleModelList,req.getTeamLeadId());
				}
				
				else if(null!=req && null!=req.getTeamLeadId() && null != req.getEmployeeId() && empId!=null) {
					log.info("TeamLead ID and Emp ID is present");
					list=buildVehicleModelForDSE(tRole,req,vehicleModelList,req.getEmployeeId());
				}
			}
		
			if(salesGapServiceImpl.validateBranchMgr(designation)) {
				
				if(null!=req && null==req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Branch Manager PageLoad");
					list=buildVehicleModelForBranchMgr(tRole,req,vehicleModelList,req.getLoggedInEmpId());
				}
				else if(null!=req && null!=req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Manager ID and Manager ID is present");
					list=buildVehicleModelForMgr(tRole,req,vehicleModelList,req.getManagerId());
				}
				
				else if(null!=req &&  null!=req.getManagerId() && null!=req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Manager ID and Team LeadID is present");
					list=buildVehicleModelForTL(tRole,req,vehicleModelList,req.getTeamLeadId());
				}
				else if(null!=req && null!=req.getManagerId() && null!=req.getTeamLeadId() && null != req.getEmployeeId() && empId!=null) {
					log.info("Manager ID , TeamLead ID and Emp ID is present");
					list=buildVehicleModelForDSE(tRole,req,vehicleModelList,req.getEmployeeId());
				}
			}
			
			if(salesGapServiceImpl.validateGeneralMgr(designation) && null!=req) {
				System.out.println("req.getBranchmangerId() "+req.getBranchmangerId());
				if(null== req.getBranchId() && null==req.getLocationId() && null==req.getBranchmangerId() && null==req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("General Manager PageLoad");
					list=buildVehicleModelForGeneralMgr(tRole,req,vehicleModelList);
				}
				else if(null != req.getBranchId() && null==req.getLocationId() && null==req.getBranchmangerId() && null==req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID is present");
					list=buildVehicleModelForBranch(tRole,req,vehicleModelList);
				}
				else if(null != req.getBranchId() && null!=req.getLocationId() && null==req.getBranchmangerId() && null==req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID and Location ID is present");
					list=buildVehicleModelForLocation(tRole,req,vehicleModelList);
				}
				
				else if(null != req.getBranchId() && null!=req.getLocationId() && null!=req.getBranchmangerId() && null==req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID,Location ID and BranchMgr ID is present");
					list=buildVehicleModelForBranchMgr(tRole,req,vehicleModelList,req.getBranchmangerId());
					}
				else if(null != req.getBranchId() && null!=req.getLocationId() && null!=req.getBranchmangerId() && null!=req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID,Location ID ,BranchMgr ID and Manager ID is present");
					list=buildVehicleModelForMgr(tRole,req,vehicleModelList,req.getManagerId());
				}
				else if(null != req.getBranchId() && null!=req.getLocationId() && null!=req.getBranchmangerId() && null!=req.getManagerId() && null!=req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID,Location ID ,BranchMgr ID and Manager ID is present");
					list=buildVehicleModelForTL(tRole,req,vehicleModelList,req.getTeamLeadId());
					}
				else if(null != req.getBranchId() && null!=req.getLocationId() && null!=req.getBranchmangerId() && null!=req.getManagerId() && null!=req.getTeamLeadId() && null != req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID,Location ID ,BranchMgr ID , Manager ID and Employeed ID is present");
					list=buildVehicleModelForDSE(tRole,req,vehicleModelList,req.getEmployeeId());
				}
				
				
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	
	}



	private List<VehicleModelRes> buildVehicleModelForDSE(TargetRoleRes loggedInUserRoleData, DashBoardReq req, List<String> vehicleModelList, String empId) {
		List<VehicleModelRes> resList = new ArrayList<>();
		   try {
			log.info("Generating Vehcile Data for DSE "+empId);
			List<Integer> empIdsUnderReporting=new ArrayList<>();
			empIdsUnderReporting.add(Integer.valueOf(empId));
			resList = getVehicleModelData(empIdsUnderReporting,req,loggedInUserRoleData,vehicleModelList);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		
		return resList;
	}
	
	
	@Override
	public List<VehicleModelRes> getVehicleModelDataByBranch(DashBoardReq req) {
		log.info("Inside getVehicleModelDataByBranch(){}");
		List<VehicleModelRes> list = new ArrayList<>();
		try {
			String empId = req.getLoggedInEmpId();
			TargetRoleRes tRole = getEmployeeRoleInfo(empId);
			List<String> vehicleModelList = dmsLeadDao.getModelNames();
			list=buildVehicleModelForBranch(tRole,req,vehicleModelList);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}


	private List<VehicleModelRes> buildVehicleModelForLocation(TargetRoleRes loggedInUserRoleData, DashBoardReq req,
			List<String> vehicleModelList) {
		List<VehicleModelRes> resList = new ArrayList<>();
		   try {
			String branch = req.getBranchId();
			String location = req.getLocationId();
			log.info("Generating Data for Branch " + branch+" and location "+location);
			List<Integer> empIdsUnderReporting = dashBoardUtil.getEmployeesUnderLocation(req.getBranchId(),req.getLocationId());
			resList = getVehicleModelData(empIdsUnderReporting,req,loggedInUserRoleData,vehicleModelList);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		return resList;
	}


	private List<VehicleModelRes> buildVehicleModelForBranch(TargetRoleRes loggedInUserRoleData, DashBoardReq req,
			List<String> vehicleModelList) {
			List<VehicleModelRes> resList = new ArrayList<>();
		   try {
			String branch = req.getBranchSelectionInEvents();
			log.info("Generating Data for Branch " + branch);
			List<Integer> empIdsUnderReporting = dashBoardUtil.getEmployeesUnderBranch(branch);
			log.debug("empIdsUnderReporting under branch: "+empIdsUnderReporting);
			resList = getVehicleModelData(empIdsUnderReporting,req,loggedInUserRoleData,vehicleModelList);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		return resList;
	}



	private List<VehicleModelRes> buildVehicleModelForTL(TargetRoleRes loggedInUserRoleData, DashBoardReq req,
			List<String> vehicleModelList,String empId) {
		List<VehicleModelRes> resList = new ArrayList<>();
		   try {
			
			log.info("Generating Data for TL " + empId);
			List<Integer> empIdsUnderReporting = dashBoardUtil.getEmployeesUnderTL(empId);
			resList = getVehicleModelData(empIdsUnderReporting,req,loggedInUserRoleData,vehicleModelList);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		return resList;
	}

	
	private List<VehicleModelRes> buildVehicleModelForMgr(TargetRoleRes loggedInUserRoleData, DashBoardReq req,
			List<String> vehicleModelList,String empId) {
		List<VehicleModelRes> resList = new ArrayList<>();
		   try {
			
			log.info("Generating Data for Manager " + empId);
			List<Integer> empIdsUnderReporting = dashBoardUtil.getEmployeesUnderMgr(req.getManagerId());
			resList = getVehicleModelData(empIdsUnderReporting,req,loggedInUserRoleData,vehicleModelList);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		return resList;
	}

	private List<VehicleModelRes> buildVehicleModelForBranchMgr(TargetRoleRes loggedInUserRoleData, DashBoardReq req,
			List<String> vehicleModelList,String empId) {
		List<VehicleModelRes> resList = new ArrayList<>();
		   try {
			log.info("Generating Data for Branch Manager " + empId);
			List<Integer> empIdsUnderReporting = dashBoardUtil.getEmployeesUnderBranchMgr(empId);
			resList = getVehicleModelData(empIdsUnderReporting,req,loggedInUserRoleData,vehicleModelList);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		return resList;
	}
	
	private List<VehicleModelRes> buildVehicleModelForGeneralMgr(TargetRoleRes loggedInUserRoleData, DashBoardReq req,
			List<String> vehicleModelList) {
			List<VehicleModelRes> resList = new ArrayList<>();
		   try {
			String empId = req.getLoggedInEmpId();
			log.info("Generating Data for General Manager " + empId);
			List<Integer> empIdsUnderReporting = dashBoardUtil.getEmployeesUnderGeneralMgr(empId);
			resList = getVehicleModelData(empIdsUnderReporting,req,loggedInUserRoleData,vehicleModelList);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		return resList;
	}
	


	private List<VehicleModelRes> getVehicleModelData(List<Integer> empIdsUnderReporting, DashBoardReq req,TargetRoleRes loggedInUserRoleData,
			List<String> vehicleModelList) {
		List<VehicleModelRes> resList = new ArrayList<>();

		List<String> empNamesList = dmsEmployeeRepo.findEmpNamesById(empIdsUnderReporting);
		log.info("empNamesList::" + empNamesList);

		String startDate = getStartDate(req.getStartDate());
		String endDate = getEndDate(req.getEndDate());
		log.info("StartDate " + startDate + ", EndDate " + endDate);

		log.info("vehicleModelList ::" + vehicleModelList);

		for (String model : vehicleModelList) {
			if (null != model) {
				VehicleModelRes vehicleRes = new VehicleModelRes();
				log.info("Generating data for model " + model);
				List<DmsLead> dmsLeadList = dmsLeadDao.getAllEmployeeLeadsWithModel(loggedInUserRoleData.getOrgId(),
						empNamesList, startDate, endDate, model);

				Long enqLeadCnt = 0L;
				Long droppedCnt = 0L;
				if (null != dmsLeadList) {
					log.info("size of dmsLeadList " + dmsLeadList.size());
					enqLeadCnt = getEnqLeadCount(dmsLeadList);
					droppedCnt = getDroppedCount(dmsLeadList);
					vehicleRes.setR(getInvoiceCount(dmsLeadList));

					log.info("enqLeadCnt: " + enqLeadCnt + " ,droppedCnt : " + droppedCnt);
				}
				vehicleRes.setModel(model);
				vehicleRes.setE(enqLeadCnt);
				vehicleRes.setL(droppedCnt);

				List<String> leadUniversalIdList = dmsLeadList.stream().map(DmsLead::getCrmUniversalId)
						.collect(Collectors.toList());
				log.info("leadUniversalIdList " + leadUniversalIdList);

				List<DmsWFTask> wfTaskList = dmsWfTaskDao.getWfTaskByAssigneeIdListByModel(empIdsUnderReporting,
						leadUniversalIdList, startDate, endDate);

				vehicleRes.setT(getTestDriveCount(wfTaskList));
				vehicleRes.setV(getHomeVisitCount(wfTaskList));
				vehicleRes.setB(getBookingCount(wfTaskList));
				
				resList.add(vehicleRes);
			}
		}
		return resList;
	}
	
	


	public String getStartDate(String inputStartDate) {
		if (null == inputStartDate && null == inputStartDate) {
			return getFirstDayOfMonth();
		} else {
			return inputStartDate;
		}
	
	}
	public String getEndDate(String inputEndDate) {
		if (null == inputEndDate && null == inputEndDate) {
			return getLastDayOfMonth();
		} else {
			return inputEndDate;
		}
	}


	@Override
	public List<LeadSourceRes> getLeadSourceData(DashBoardReq req) {


		log.info("Inside getLeadSourceData(){}");
		List<LeadSourceRes> list = new ArrayList<>();
		try {
			
			String empId = req.getLoggedInEmpId();
			TargetRoleRes tRole = salesGapServiceImpl.getEmpRoleData(Integer.valueOf(empId));
			String designation = tRole.getDesignationName();
			log.info("LOGGED IN EMP Designation "+designation);	
			
			if(salesGapServiceImpl.validateDSE(designation)) {
				log.info("Getting date for DSE "+empId);
				list=buildLeadSourceForDSE(tRole,req,empId);
			}
			
			if(salesGapServiceImpl.validateTL(designation)) {
				
				if(null!=req && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("TL - Page Load");
					list=buildLeadSourceForTL(tRole,req,req.getLoggedInEmpId());
				}
				else if(null!=req && null != req.getEmployeeId() && null!=empId) {
					log.info("Emp ID is present");
					list=buildLeadSourceForDSE(tRole,req,req.getEmployeeId());
				}
			}
			
			if(salesGapServiceImpl.validateMgr(designation)) {
				if(null!=req && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info(" Manager - Page Load");
					list=buildLeadSourceForMgr(tRole,req,req.getLoggedInEmpId());
				}
				else if(null!=req && null!=req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("TeamLead ID is present");
					list=buildLeadSourceForTL(tRole,req,req.getTeamLeadId());
				}
				
				else if(null!=req && null!=req.getTeamLeadId() && null != req.getEmployeeId() && empId!=null) {
					log.info("TeamLead ID and Emp ID is present");
					list=buildLeadSourceForDSE(tRole,req,req.getEmployeeId());
				}
			}
		
			if(salesGapServiceImpl.validateBranchMgr(designation)) {
				
				if(null!=req && null==req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Branch Manager PageLoad");
					list=buildLeadSourceForBranchMgr(tRole,req,req.getLoggedInEmpId());
				}
				else if(null!=req && null!=req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Manager ID and Manager ID is present");
					list=buildLeadSourceForMgr(tRole,req,req.getManagerId());
				}
				
				else if(null!=req &&  null!=req.getManagerId() && null!=req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Manager ID and Team LeadID is present");
					list=buildLeadSourceForTL(tRole,req,req.getTeamLeadId());
				}
				else if(null!=req && null!=req.getManagerId() && null!=req.getTeamLeadId() && null != req.getEmployeeId() && empId!=null) {
					log.info("Manager ID , TeamLead ID and Emp ID is present");
					list=buildLeadSourceForDSE(tRole,req,req.getEmployeeId());
				}
			}
			
			if(salesGapServiceImpl.validateGeneralMgr(designation) && null!=req) {
				System.out.println("req.getBranchmangerId() "+req.getBranchmangerId());
				if(null== req.getBranchId() && null==req.getLocationId() && null==req.getBranchmangerId() && null==req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("General Manager PageLoad");
					list=buildLeadSourceForGeneralMgr(tRole,req);
				}
				else if(null != req.getBranchId() && null==req.getLocationId() && null==req.getBranchmangerId() && null==req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID is present");
					list=buildLeadSourceForBranch(tRole,req);
				}
				else if(null != req.getBranchId() && null!=req.getLocationId() && null==req.getBranchmangerId() && null==req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID and Location ID is present");
					list=buildLeadSourceForLocation(tRole,req);
				}
				
				else if(null != req.getBranchId() && null!=req.getLocationId() && null!=req.getBranchmangerId() && null==req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID,Location ID and BranchMgr ID is present");
					list=buildLeadSourceForBranchMgr(tRole,req,req.getBranchmangerId());
					}
				else if(null != req.getBranchId() && null!=req.getLocationId() && null!=req.getBranchmangerId() && null!=req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID,Location ID ,BranchMgr ID and Manager ID is present");
					list=buildLeadSourceForMgr(tRole,req,req.getManagerId());
				}
				else if(null != req.getBranchId() && null!=req.getLocationId() && null!=req.getBranchmangerId() && null!=req.getManagerId() && null!=req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID,Location ID ,BranchMgr ID and Manager ID is present");
					list=buildLeadSourceForTL(tRole,req,req.getTeamLeadId());
					}
				else if(null != req.getBranchId() && null!=req.getLocationId() && null!=req.getBranchmangerId() && null!=req.getManagerId() && null!=req.getTeamLeadId() && null != req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID,Location ID ,BranchMgr ID , Manager ID and Employeed ID is present");
					list=buildLeadSourceForDSE(tRole,req,req.getEmployeeId());
				}
			}
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	
	
	}


	private List<LeadSourceRes> buildLeadSourceForDSE(TargetRoleRes tRole, DashBoardReq req,String empId) {
		List<LeadSourceRes> resList = new ArrayList<>();
		   try {
			log.info("Generating LeadSourceRes Data for DSE "+empId);
			List<Integer> empIdsUnderReporting=new ArrayList<>();
			empIdsUnderReporting.add(Integer.valueOf(empId));
			resList = getLeadSourceData(empIdsUnderReporting,req,tRole);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		
		return resList;
	}
	
	private List<LeadSourceRes> buildLeadSourceForLocation(TargetRoleRes loggedInUserRoleData, DashBoardReq req) {
		List<LeadSourceRes> resList = new ArrayList<>();
		   try {
			String branch = req.getBranchId();
			String location = req.getLocationId();
			log.info("Generating Data for Branch " + branch+" and location "+location);
			List<Integer> empIdsUnderReporting = dashBoardUtil.getEmployeesUnderLocation(req.getBranchId(),req.getLocationId());
			resList = getLeadSourceData(empIdsUnderReporting,req,loggedInUserRoleData);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		return resList;
	}


	private List<LeadSourceRes> buildLeadSourceForBranch(TargetRoleRes loggedInUserRoleData, DashBoardReq req) {
			List<LeadSourceRes> resList = new ArrayList<>();
		   try {
			String branch = req.getBranchSelectionInEvents();
			log.info("Generating Data for Branch " + branch);
			List<Integer> empIdsUnderReporting = dashBoardUtil.getEmployeesUnderBranch(branch);
			log.debug("empIdsUnderReporting under branch: "+empIdsUnderReporting);
			resList = getLeadSourceData(empIdsUnderReporting,req,loggedInUserRoleData);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		return resList;
	}



	private List<LeadSourceRes> buildLeadSourceForTL(TargetRoleRes loggedInUserRoleData, DashBoardReq req,
			String empId) {
		List<LeadSourceRes> resList = new ArrayList<>();
		   try {
			
			log.info("Generating Data for TL " + empId);
			List<Integer> empIdsUnderReporting = dashBoardUtil.getEmployeesUnderTL(empId);
			resList = getLeadSourceData(empIdsUnderReporting,req,loggedInUserRoleData);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		return resList;
	}

	
	private List<LeadSourceRes> buildLeadSourceForMgr(TargetRoleRes loggedInUserRoleData, DashBoardReq req,
			String empId) {
		List<LeadSourceRes> resList = new ArrayList<>();
		   try {
			
			log.info("Generating Data for Manager " + empId);
			List<Integer> empIdsUnderReporting = dashBoardUtil.getEmployeesUnderMgr(req.getManagerId());
			resList = getLeadSourceData(empIdsUnderReporting,req,loggedInUserRoleData);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		return resList;
	}

	private List<LeadSourceRes> buildLeadSourceForBranchMgr(TargetRoleRes loggedInUserRoleData, DashBoardReq req,
			String empId) {
		List<LeadSourceRes> resList = new ArrayList<>();
		   try {
			log.info("Generating Data for Branch Manager " + empId);
			List<Integer> empIdsUnderReporting = dashBoardUtil.getEmployeesUnderBranchMgr(empId);
			resList = getLeadSourceData(empIdsUnderReporting,req,loggedInUserRoleData);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		return resList;
	}
	
	private List<LeadSourceRes> buildLeadSourceForGeneralMgr(TargetRoleRes loggedInUserRoleData, DashBoardReq req) {
			List<LeadSourceRes> resList = new ArrayList<>();
		   try {
			String empId = req.getLoggedInEmpId();
			log.info("Generating Data for General Manager " + empId);
			List<Integer> empIdsUnderReporting = dashBoardUtil.getEmployeesUnderGeneralMgr(empId);
			resList = getLeadSourceData(empIdsUnderReporting,req,loggedInUserRoleData);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		return resList;
	}
	
	@Override
	public List<LeadSourceRes> getLeadSourceDataByBranch(DashBoardReq req) {
		log.info("Inside getLeadSourceDataByBranch(){}");
		List<LeadSourceRes> list = new ArrayList<>();
		try {
			String empId = req.getLoggedInEmpId();
			TargetRoleRes tRole = getEmployeeRoleInfo(empId);
			
			list=buildLeadSourceForBranch(tRole,req);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}


	




	private List<LeadSourceRes> getLeadSourceData(List<Integer> empIdsUnderReporting, DashBoardReq req,
			TargetRoleRes tRole) {
		List<LeadSourceRes> resList = new ArrayList<>();

		List<String> empNamesList = dmsEmployeeRepo.findEmpNamesById(empIdsUnderReporting);
		log.info("empNamesList::" + empNamesList);

		String startDate = getStartDate(req.getStartDate());
		String endDate = getEndDate(req.getEndDate());
		log.info("StartDate " + startDate + ", EndDate " + endDate);

		getLeadTypes().forEach((k, v) -> {
			LeadSourceRes leadSource = new LeadSourceRes();
			log.debug("Generating data for Leadsource " + k + " and enq id " + v);
			List<DmsLead> dmsLeadList = dmsLeadDao.getAllEmployeeLeadsBasedOnEnquiry(tRole.getOrgId(),
					 empNamesList, startDate, endDate, v);

			Long enqLeadCnt = 0L;
			Long droppedCnt = 0L;
			if (null != dmsLeadList) {
				log.info("size of dmsLeadList " + dmsLeadList.size());
				enqLeadCnt = getEnqLeadCount(dmsLeadList);
				droppedCnt = getDroppedCount(dmsLeadList);
				leadSource.setR(getInvoiceCount(dmsLeadList));
				log.info("enqLeadCnt: " + enqLeadCnt + " ,droppedCnt : " + droppedCnt);
			}
			leadSource.setLead(k);
			leadSource.setE(enqLeadCnt);
			leadSource.setL(droppedCnt);

			List<String> leadUniversalIdList = dmsLeadList.stream().map(DmsLead::getCrmUniversalId)
					.collect(Collectors.toList());
			log.debug("leadUniversalIdList " + leadUniversalIdList);

			List<DmsWFTask> wfTaskList = dmsWfTaskDao.getWfTaskByAssigneeIdListByModel(empIdsUnderReporting,
					leadUniversalIdList, startDate, endDate);

			leadSource.setT(getTestDriveCount(wfTaskList));
			leadSource.setV(getHomeVisitCount(wfTaskList));
			leadSource.setB(getBookingCount(wfTaskList));
			
		

			resList.add(leadSource);

		});

		return resList;

	}



	private Map<String,Integer> getLeadTypes() {
		Map<String,Integer> map = new LinkedHashMap<>();
		map.put("Reference",6);//6
		map.put("Showroom",1); //1
		map.put("Field",2);  //2
		map.put("Social Network",5);//5
		return map;
	}


	
	@Override
	public List<EventDataRes> getEventSourceData(DashBoardReq req) {
	log.info("Inside getEventSourceData(){}");
		List<EventDataRes> list = new ArrayList<>();
		try {
			
			String empId = req.getLoggedInEmpId();
			TargetRoleRes tRole = salesGapServiceImpl.getEmpRoleData(Integer.valueOf(empId));
			String designation = tRole.getDesignationName();
			log.info("LOGGED IN EMP Designation "+designation);	
			
			if(salesGapServiceImpl.validateDSE(designation)) {
				log.info("Getting date for DSE "+empId);
				list=buildEventSourceForDSE(tRole,req,empId);
			}
			
			if(salesGapServiceImpl.validateTL(designation)) {
				
				if(null!=req && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("TL - Page Load");
					list=buildEventSourceForTL(tRole,req,req.getLoggedInEmpId());
				}
				else if(null!=req && null != req.getEmployeeId() && null!=empId) {
					log.info("Emp ID is present");
					list=buildEventSourceForDSE(tRole,req,req.getEmployeeId());
				}
			}
			
			if(salesGapServiceImpl.validateMgr(designation)) {
				if(null!=req && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info(" Manager - Page Load");
					list=buildEventSourceForMgr(tRole,req,req.getLoggedInEmpId());
				}
				else if(null!=req && null!=req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("TeamLead ID is present");
					list=buildEventSourceForTL(tRole,req,req.getTeamLeadId());
				}
				
				else if(null!=req && null!=req.getTeamLeadId() && null != req.getEmployeeId() && empId!=null) {
					log.info("TeamLead ID and Emp ID is present");
					list=buildEventSourceForDSE(tRole,req,req.getEmployeeId());
				}
			}
		
			if(salesGapServiceImpl.validateBranchMgr(designation)) {
				
				if(null!=req && null==req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Branch Manager PageLoad");
					list=buildEventSourceForBranchMgr(tRole,req,req.getLoggedInEmpId());
				}
				else if(null!=req && null!=req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Manager ID and Manager ID is present");
					list=buildEventSourceForMgr(tRole,req,req.getManagerId());
				}
				
				else if(null!=req &&  null!=req.getManagerId() && null!=req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Manager ID and Team LeadID is present");
					list=buildEventSourceForTL(tRole,req,req.getTeamLeadId());
				}
				else if(null!=req && null!=req.getManagerId() && null!=req.getTeamLeadId() && null != req.getEmployeeId() && empId!=null) {
					log.info("Manager ID , TeamLead ID and Emp ID is present");
					list=buildEventSourceForDSE(tRole,req,req.getEmployeeId());
				}
			}
			
			if(salesGapServiceImpl.validateGeneralMgr(designation) && null!=req) {
				System.out.println("req.getBranchmangerId() "+req.getBranchmangerId());
				if(null== req.getBranchId() && null==req.getLocationId() && null==req.getBranchmangerId() && null==req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("General Manager PageLoad");
					list=buildEventSourceForGeneralMgr(tRole,req);
				}
				else if(null != req.getBranchId() && null==req.getLocationId() && null==req.getBranchmangerId() && null==req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID is present");
					list=buildEventSourceForBranch(tRole,req);
				}
				else if(null != req.getBranchId() && null!=req.getLocationId() && null==req.getBranchmangerId() && null==req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID and Location ID is present");
					list=buildEventSourceForLocation(tRole,req);
				}
				
				else if(null != req.getBranchId() && null!=req.getLocationId() && null!=req.getBranchmangerId() && null==req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID,Location ID and BranchMgr ID is present");
					list=buildEventSourceForBranchMgr(tRole,req,req.getBranchmangerId());
					}
				else if(null != req.getBranchId() && null!=req.getLocationId() && null!=req.getBranchmangerId() && null!=req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID,Location ID ,BranchMgr ID and Manager ID is present");
					list=buildEventSourceForMgr(tRole,req,req.getManagerId());
				}
				else if(null != req.getBranchId() && null!=req.getLocationId() && null!=req.getBranchmangerId() && null!=req.getManagerId() && null!=req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID,Location ID ,BranchMgr ID and Manager ID is present");
					list=buildEventSourceForTL(tRole,req,req.getTeamLeadId());
					}
				else if(null != req.getBranchId() && null!=req.getLocationId() && null!=req.getBranchmangerId() && null!=req.getManagerId() && null!=req.getTeamLeadId() && null != req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID,Location ID ,BranchMgr ID , Manager ID and Employeed ID is present");
					list=buildEventSourceForDSE(tRole,req,req.getEmployeeId());
				}
			}
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	
	
	}


	private List<EventDataRes> buildEventSourceForDSE(TargetRoleRes tRole, DashBoardReq req,String empId) {
		List<EventDataRes> resList = new ArrayList<>();
		   try {
			log.info("Generating EventDataRes Data for DSE "+empId);
			List<Integer> empIdsUnderReporting=new ArrayList<>();
			empIdsUnderReporting.add(Integer.valueOf(empId));
			resList = getEventSourceData(empIdsUnderReporting,req,tRole);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		
		return resList;
	}
	
	private List<EventDataRes> buildEventSourceForLocation(TargetRoleRes loggedInUserRoleData, DashBoardReq req) {
		List<EventDataRes> resList = new ArrayList<>();
		   try {
			String branch = req.getBranchId();
			String location = req.getLocationId();
			log.info("Generating Data for Branch " + branch+" and location "+location);
			List<Integer> empIdsUnderReporting = dashBoardUtil.getEmployeesUnderLocation(req.getBranchId(),req.getLocationId());
			resList = getEventSourceData(empIdsUnderReporting,req,loggedInUserRoleData);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		return resList;
	}


	private List<EventDataRes> buildEventSourceForBranch(TargetRoleRes loggedInUserRoleData, DashBoardReq req) {
			List<EventDataRes> resList = new ArrayList<>();
		   try {
			String branch = req.getBranchSelectionInEvents();
			log.info("Generating Data for Branch " + branch);
			List<Integer> empIdsUnderReporting = dashBoardUtil.getEmployeesUnderBranch(branch);
			log.debug("empIdsUnderReporting under branch: "+empIdsUnderReporting);
			resList = getEventSourceData(empIdsUnderReporting,req,loggedInUserRoleData);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		return resList;
	}



	private List<EventDataRes> buildEventSourceForTL(TargetRoleRes loggedInUserRoleData, DashBoardReq req,
			String empId) {
		List<EventDataRes> resList = new ArrayList<>();
		   try {
			
			log.info("Generating Data for TL " + empId);
			List<Integer> empIdsUnderReporting = dashBoardUtil.getEmployeesUnderTL(empId);
			resList = getEventSourceData(empIdsUnderReporting,req,loggedInUserRoleData);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		return resList;
	}

	
	private List<EventDataRes> buildEventSourceForMgr(TargetRoleRes loggedInUserRoleData, DashBoardReq req,
			String empId) {
		List<EventDataRes> resList = new ArrayList<>();
		   try {
			
			log.info("Generating Data for Manager " + empId);
			List<Integer> empIdsUnderReporting = dashBoardUtil.getEmployeesUnderMgr(req.getManagerId());
			resList = getEventSourceData(empIdsUnderReporting,req,loggedInUserRoleData);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		return resList;
	}

	private List<EventDataRes> buildEventSourceForBranchMgr(TargetRoleRes loggedInUserRoleData, DashBoardReq req,
			String empId) {
		List<EventDataRes> resList = new ArrayList<>();
		   try {
			log.info("Generating Data for Branch Manager " + empId);
			List<Integer> empIdsUnderReporting = dashBoardUtil.getEmployeesUnderBranchMgr(empId);
			resList = getEventSourceData(empIdsUnderReporting,req,loggedInUserRoleData);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		return resList;
	}
	
	private List<EventDataRes> buildEventSourceForGeneralMgr(TargetRoleRes loggedInUserRoleData, DashBoardReq req) {
			List<EventDataRes> resList = new ArrayList<>();
		   try {
			String empId = req.getLoggedInEmpId();
			log.info("Generating Data for General Manager " + empId);
			List<Integer> empIdsUnderReporting = dashBoardUtil.getEmployeesUnderGeneralMgr(empId);
			resList = getEventSourceData(empIdsUnderReporting,req,loggedInUserRoleData);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		return resList;
	}
	
	@Override
	public List<EventDataRes> getEventSourceDataByBranch(DashBoardReq req) {
		log.info("Inside getEventSourceDataByBranch(){}");
		List<EventDataRes> list = new ArrayList<>();
		try {
			String empId = req.getLoggedInEmpId();
			TargetRoleRes tRole = getEmployeeRoleInfo(empId);
			
			list=buildEventSourceForBranch(tRole,req);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}


	
	private List<EventDataRes> getEventSourceData(List<Integer> empIdsUnderReporting, DashBoardReq req,
			TargetRoleRes tRole) {
		log.info("Inside getEventSourceData()");
		List<EventDataRes> resList = new ArrayList<>();
		List<String> empNamesList = dmsEmployeeRepo.findEmpNamesById(empIdsUnderReporting);
		log.info("empNamesList::" + empNamesList);
		String startDate = getStartDate(req.getStartDate());
		String endDate = getEndDate(req.getEndDate());
		log.info("StartDate " + startDate + ", EndDate " + endDate);
	
		List<DmsLead> dmsLeadList = dmsLeadDao.getAllEmployeeLeadsBasedOnEnquiry(tRole.getOrgId(), 
				empNamesList, startDate, endDate, 4);

		Set<String> eventCodeSet = dmsLeadList.stream().map(DmsLead::getEventCode).collect(Collectors.toSet());
		log.debug("eventCodeSet :" + eventCodeSet);

		eventCodeSet.forEach(x -> {
			log.debug("Generating data for EventSource " + x);
			EventDataRes EventSource = new EventDataRes();
			Long enqLeadCnt = 0L;
			Long droppedCnt = 0L;

			if (null != dmsLeadList) {
				log.info("size of dmsLeadList " + dmsLeadList.size());
				enqLeadCnt = getEnqLeadCount(dmsLeadList);
				droppedCnt = getDroppedCount(dmsLeadList);
				EventSource.setR(getInvoiceCount(dmsLeadList));
				log.info("enqLeadCnt: " + enqLeadCnt + " ,droppedCnt : " + droppedCnt);
			}
			EventSource.setEventName(dashBoardUtil.getEventNameFromCode(x));
			EventSource.setE(enqLeadCnt);
			EventSource.setL(droppedCnt);

			List<String> leadUniversalIdList = dmsLeadList.stream().map(DmsLead::getCrmUniversalId)
					.collect(Collectors.toList());
			log.debug("leadUniversalIdList " + leadUniversalIdList);

			List<DmsWFTask> wfTaskList = dmsWfTaskDao.getWfTaskByAssigneeIdListByModel(empIdsUnderReporting,
					leadUniversalIdList, startDate, endDate);

			EventSource.setT(getTestDriveCount(wfTaskList));
			EventSource.setB(getBookingCount(wfTaskList));
			EventSource.setV(getHomeVisitCount(wfTaskList));
			
			resList.add(EventSource);

		});
		return resList;
	}






	
	private Long getExchangeCount(List<DmsWFTask> wfTaskList) {
		return wfTaskList.stream().filter(x->x.getTaskName().equalsIgnoreCase(VERIFY_EXCHANGE_APPROVAL)).count();
	}


	private Long getHomeVisitCount(List<DmsWFTask> wfTaskList) {
		return wfTaskList.stream().filter(x->x.getTaskName().equalsIgnoreCase(HOME_VISIT)).count();
	}

	private Long getInvoiceCount(List<DmsLead> dmsLeadList) {
		/*return wfTaskList.stream().
				filter(x->(x.getTaskName().equalsIgnoreCase(READY_FOR_INVOICE)
						|| x.getTaskName().equalsIgnoreCase(PROCEED_TO_INVOICE)
						|| x.getTaskName().equalsIgnoreCase(INVOICE_FOLLOWUP_DSE))).count();*/
		return dmsLeadList.stream().filter(x->x.getLeadStage().equalsIgnoreCase(INVOICE)).count();
	}
	
	private Long getInvoiceCountTarget(List<DmsWFTask> wfTaskList) {
		/*return wfTaskList.stream().
				filter(x->(x.getTaskName().equalsIgnoreCase(READY_FOR_INVOICE)
						|| x.getTaskName().equalsIgnoreCase(PROCEED_TO_INVOICE)
						|| x.getTaskName().equalsIgnoreCase(INVOICE_FOLLOWUP_DSE))).count();*/
		return wfTaskList.stream().filter(x->x.getTaskName().equalsIgnoreCase(INVOICE)).count();
	}
	private Long getBookingCount(List<DmsWFTask> wfTaskList) {
		return wfTaskList.stream().
				filter(x->(x.getTaskName().equalsIgnoreCase(PROCEED_TO_BOOKING)
						|| x.getTaskName().equalsIgnoreCase(BOOKING_FOLLOWUP_DSE)
						|| x.getTaskName().equalsIgnoreCase(BOOKING_FOLLOWUP_CRM))).count();
	}


	private Long getAccessoriesCount(List<DmsWFTask> wfTaskList) {
		return wfTaskList.stream().filter(x->x.getTaskName().equalsIgnoreCase(BOOKING_FOLLOWUP_ACCCESSORIES)).count();
	}


	private Long getInsuranceCount(List<DmsWFTask> wfTaskList) {
			return  wfTaskList.stream().filter(x->x.getTaskName().equalsIgnoreCase(INSURANCE)).count();
	}


	private Long getFinanceCount(List<DmsWFTask> wfTaskList) {
			return wfTaskList.stream().filter(x->x.getTaskName().equalsIgnoreCase(FINANCE)).count();
	}

	
	
	private Long getVideoConfCount(List<DmsWFTask> wfTaskList) {
		return wfTaskList.stream().filter(x->x.getTaskName().equalsIgnoreCase(VIDEO_CONFERENCE)).count();
	}


	private Long getDroppedCount(List<DmsLead> dmsLeadList) {
		return dmsLeadList.stream().filter(x->x.getLeadStage().equalsIgnoreCase(DROPPED)).count();
	}


	private Long getEnqLeadCount(List<DmsLead> dmsLeadList) {
		return dmsLeadList.stream().filter(x->x.getLeadStage().equalsIgnoreCase(ENQUIRY)).count();
	}


	public Long getTestDriveCount(List<DmsWFTask> wfTaskList) {
		return wfTaskList.stream().filter(x->x.getTaskName().equalsIgnoreCase(TEST_DRIVE)).count();
	}


	@Override
	public Map<String,Object> getLostDropData(DashBoardReq req) {
	log.info("Inside getLostDropData(){}");
	Map<String,Object>  list = new LinkedHashMap<>();
		try {
			String empId = req.getLoggedInEmpId();
			TargetRoleRes tRole = salesGapServiceImpl.getEmpRoleData(Integer.valueOf(empId));
			String designation = tRole.getDesignationName();
			log.info("LOGGED IN EMP Designation "+designation);	
			
			
			if(salesGapServiceImpl.validateDSE(designation)) {
				log.info("Getting date for DSE "+empId);
				list=buildLostDropForDSE(tRole,req,empId);
			}
		
			if(salesGapServiceImpl.validateTL(designation)) {
				
				if(null!=req && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("TL - Page Load");
					list=buildLostDropForTL(tRole,req,req.getLoggedInEmpId());
				}
				else if(null!=req && null != req.getEmployeeId() && null!=empId) {
					log.info("Emp ID is present");
					list=buildLostDropForDSE(tRole,req,req.getEmployeeId());
				}
			}
			
			if(salesGapServiceImpl.validateMgr(designation)) {
				if(null!=req && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info(" Manager - Page Load");
					list=buildLostDropForMgr(tRole,req,req.getLoggedInEmpId());
				}
				else if(null!=req && null!=req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("TeamLead ID is present");
					list=buildLostDropForTL(tRole,req,req.getTeamLeadId());
				}
				
				else if(null!=req && null!=req.getTeamLeadId() && null != req.getEmployeeId() && empId!=null) {
					log.info("TeamLead ID and Emp ID is present");
					list=buildLostDropForDSE(tRole,req,req.getEmployeeId());
				}
			}
		
			if(salesGapServiceImpl.validateBranchMgr(designation)) {
				
				if(null!=req && null==req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Branch Manager PageLoad");
					list=buildLostDropForBranchMgr(tRole,req,req.getLoggedInEmpId());
				}
				else if(null!=req && null!=req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Manager ID and Manager ID is present");
					list=buildLostDropForMgr(tRole,req,req.getManagerId());
				}
				
				else if(null!=req &&  null!=req.getManagerId() && null!=req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Manager ID and Team LeadID is present");
					list=buildLostDropForTL(tRole,req,req.getTeamLeadId());
				}
				else if(null!=req && null!=req.getManagerId() && null!=req.getTeamLeadId() && null != req.getEmployeeId() && empId!=null) {
					log.info("Manager ID , TeamLead ID and Emp ID is present");
					list=buildLostDropForDSE(tRole,req,req.getEmployeeId());
				}
			}
			
			if(salesGapServiceImpl.validateGeneralMgr(designation) && null!=req) {
				System.out.println("req.getBranchmangerId() "+req.getBranchmangerId());
				if(null== req.getBranchId() && null==req.getLocationId() && null==req.getBranchmangerId() && null==req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("General Manager PageLoad");
					list=buildLostDropForGeneralMgr(tRole,req);
				}
				else if(null != req.getBranchId() && null==req.getLocationId() && null==req.getBranchmangerId() && null==req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID is present");
					list=buildLostDropForBranch(tRole,req);
				}
				else if(null != req.getBranchId() && null!=req.getLocationId() && null==req.getBranchmangerId() && null==req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID and Location ID is present");
					list=buildLostDropForLocation(tRole,req);
				}
				
				else if(null != req.getBranchId() && null!=req.getLocationId() && null!=req.getBranchmangerId() && null==req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID,Location ID and BranchMgr ID is present");
					list=buildLostDropForBranchMgr(tRole,req,req.getBranchmangerId());
					}
				else if(null != req.getBranchId() && null!=req.getLocationId() && null!=req.getBranchmangerId() && null!=req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID,Location ID ,BranchMgr ID and Manager ID is present");
					list=buildLostDropForMgr(tRole,req,req.getManagerId());
				}
				else if(null != req.getBranchId() && null!=req.getLocationId() && null!=req.getBranchmangerId() && null!=req.getManagerId() && null!=req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID,Location ID ,BranchMgr ID and Manager ID is present");
					list=buildLostDropForTL(tRole,req,req.getTeamLeadId());
					}
				else if(null != req.getBranchId() && null!=req.getLocationId() && null!=req.getBranchmangerId() && null!=req.getManagerId() && null!=req.getTeamLeadId() && null != req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID,Location ID ,BranchMgr ID , Manager ID and Employeed ID is present");
					list=buildLostDropForDSE(tRole,req,req.getEmployeeId());
				}
				
			}
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	
	
	}


	private Map<String,Object> buildLostDropForDSE(TargetRoleRes tRole, DashBoardReq req,String empId) {
		Map<String,Object> map = new HashMap<>();
		   try {
			log.info("Generating EventDataRes Data for DSE "+empId);
			List<Integer> empIdsUnderReporting=new ArrayList<>();
			empIdsUnderReporting.add(Integer.valueOf(empId));
			map = getLostDropData(empIdsUnderReporting,req,tRole);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		
		return map;
	}

	private Map<String,Object> buildLostDropForLocation(TargetRoleRes tRole, DashBoardReq req) {
		Map<String,Object> map = new HashMap<>();
		   try {
			String branch = req.getBranchId();
			String location = req.getLocationId();
			log.info("Generating Data for Branch " + branch+" and location "+location);
			List<Integer> empIdsUnderReporting = dashBoardUtil.getEmployeesUnderLocation(req.getBranchId(),req.getLocationId());
			log.debug("empIdsUnderReporting in buildLostDropForLocation "+empIdsUnderReporting);
			map = getLostDropData(empIdsUnderReporting,req,tRole);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		return map;
	}


	private Map<String,Object> buildLostDropForBranch(TargetRoleRes tRole, DashBoardReq req) {
		Map<String,Object> map = new HashMap<>();
		   try {
			String branch = req.getBranchId();
			log.info("Generating Data for Branch " + branch);
			List<Integer> empIdsUnderReporting = dashBoardUtil.getEmployeesUnderBranch(branch);
			log.debug("empIdsUnderReporting in buildLostDropForBranch "+empIdsUnderReporting);
			map = getLostDropData(empIdsUnderReporting,req,tRole);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		return map;
	}



	private Map<String,Object> buildLostDropForTL(TargetRoleRes tRole, DashBoardReq req,
			String empId) {
		Map<String, Object> map = new HashMap<>();
		try {
			log.info("Generating Data for TL " + empId);
			List<Integer> empIdsUnderReporting = dashBoardUtil.getEmployeesUnderTL(empId);
			map = getLostDropData(empIdsUnderReporting,req,tRole);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	
	private Map<String, Object> buildLostDropForMgr(TargetRoleRes tRole, DashBoardReq req,
			String empId) {
		Map<String, Object> map = new HashMap<>();
		   try {
			
			log.info("Generating Data for Manager " + empId);
			List<Integer> empIdsUnderReporting = dashBoardUtil.getEmployeesUnderMgr(empId);
			map = getLostDropData(empIdsUnderReporting,req,tRole);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		return map;
	}

	private Map<String, Object> buildLostDropForBranchMgr(TargetRoleRes tRole, DashBoardReq req,
			String empId) {
		Map<String, Object> map = new HashMap<>();
		   try {
			log.info("Generating Data for Branch Manager " + empId);
			List<Integer> empIdsUnderReporting = dashBoardUtil.getEmployeesUnderBranchMgr(empId);
			map = getLostDropData(empIdsUnderReporting,req,tRole);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		return map;
	}
	
	private Map<String, Object> buildLostDropForGeneralMgr(TargetRoleRes tRole, DashBoardReq req) {
		Map<String, Object> map = new HashMap<>();
		   try {
			String empId = req.getLoggedInEmpId();
			log.info("Generating Data for General Manager " + empId);
			List<Integer> empIdsUnderReporting = dashBoardUtil.getEmployeesUnderGeneralMgr(empId);
			map = getLostDropData(empIdsUnderReporting,req,tRole);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		return map;
	}
	
	@Override
	public Map<String, Object> getLostDropDataByBranch(DashBoardReq req) {
		log.info("Inside getLostDropDataByBranch(){}");
		Map<String, Object> map = new HashMap<>();
		try {
			String empId = req.getLoggedInEmpId();
			TargetRoleRes tRole = getEmployeeRoleInfo(empId);
			map = buildLostDropForBranch(tRole, req);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return map;
	}


	
	private Map<String,Object> getLostDropData(List<Integer> empIdsUnderReporting, DashBoardReq req,
			TargetRoleRes tRole) {
		log.info("Inside getLostDropData()");
		Map<String,Object> map = new LinkedHashMap<>();
		List<String> empNamesList = dmsEmployeeRepo.findEmpNamesById(empIdsUnderReporting);
		log.info("empNamesList::" + empNamesList);
		String startDate = getStartDate(req.getStartDate());
		String endDate = getEndDate(req.getEndDate());
		log.info("StartDate " + startDate + ", EndDate " + endDate);
		Map<String,Map<Integer,String>> vehicleDetails = dashBoardUtil.getVehilceDetails(tRole.getOrgId());
		
		map.putAll(getLostData(vehicleDetails,tRole,startDate,endDate,empNamesList,empIdsUnderReporting));
		map.putAll(getDropData(vehicleDetails,tRole,startDate,endDate,empNamesList,empIdsUnderReporting));
		return map;
	}


	private Map<String, Object> getDropData(Map<String, Map<Integer, String>> vehicleDetails, TargetRoleRes tRole,
			String startDate, String endDate, List<String> empNamesList, List<Integer> empIdsUnderReporting) {
	
		Map<String, Object> responseMap = new LinkedHashMap<>();
		List<DropRes> dropResList = new ArrayList<>();
		vehicleDetails.get("main").forEach((k, v) -> {
			log.debug("Generating data for " + v);
			List<String> list = new ArrayList<>();
			list.add(v);
			dropResList.add(buildDropData(tRole,startDate,endDate,empNamesList,v,k,list));
		});
		List<DropRes> dropResOthersList = new ArrayList<>();
		vehicleDetails.get("others").forEach((k, v) -> {
			log.debug("Generating Others data for"+v);
			List<String> list = new ArrayList<>();
			list.add(v);
			dropResOthersList.add(buildDropData(tRole,startDate,endDate,empNamesList,v,k,list));
		});
		
		log.debug("dropResList "+dropResList);
		log.debug("dropResOthersList "+dropResOthersList);
		DropRes otherDropRes = new DropRes();
		int otherDropCount =0; 
		Long otherDropAmt = 0L;
		for(DropRes lr : dropResOthersList) {
			otherDropCount = otherDropCount+lr.getDropCount();
			otherDropAmt = otherDropAmt+lr.getDropAmount();
		}
		otherDropRes.setModelName("others");
		otherDropRes.setDropAmount(otherDropAmt);
		otherDropRes.setDropCount(otherDropCount);
		log.debug("otherDropRes "+otherDropRes);
		
		dropResList.add(otherDropRes);		
		
		Integer totalDropCnt = dropResList.stream().collect(Collectors.summingInt(DropRes::getDropCount));
		log.debug("totalDropCnt ::"+totalDropCnt);
		
		for(DropRes lr : dropResList) {
			Double perc = 0D;
			if(totalDropCnt!=0) {
				perc = (lr.getDropCount()/Double.valueOf(totalDropCnt))*100;
			}
			lr.setDropPercentage(String.format("%.2f", perc));
		}
		responseMap.put("dropData", dropResList);
		responseMap.put("totalDropRevenue", dropResList.stream().collect(Collectors.summingLong(DropRes::getDropAmount)));
		return responseMap;
	}


	private DropRes buildDropData(TargetRoleRes tRole, String startDate, String endDate, List<String> empNamesList,String model,Integer
			modelId,List<String> list) {
		List<DmsLead> dmsLeadList = dmsLeadDao.getAllEmployeeLeadsWithModelandStage(tRole.getOrgId(), empNamesList, startDate, endDate, list, DROPPED);
		
		List<Integer> dropList = dmsLeadDropDao.getLeads(dmsLeadList.stream().map(DmsLead::getId).collect(Collectors.toList()), ENQUIRY);
		
		int lostCnt = 0;
		List<DmsLead> filteredDmsList = new ArrayList<>();
		if (null != dropList) {
			lostCnt = dropList.size();
			log.debug("lostCnt " + lostCnt);
			for(DmsLead lead : dmsLeadList) {
				if(dropList.contains(lead.getId())) {
					filteredDmsList.add(lead);
				}
			}
			log.debug("filteredDmsList " + filteredDmsList.size());
		}
		//Map<Integer,Long> variantMap = new HashMap<>();
		Long dropAmt=0L;
		for(DmsLead l: filteredDmsList) {
			int variantId = dashBoardUtil.getVariantId(l.getId());
			Long price = dashBoardUtil.getVehiclePrice(tRole.getOrgId(),modelId,variantId).longValue();
			dropAmt = dropAmt+price;
			log.debug("variantId "+variantId+",price "+price+" Lead Id :"+l.getId());
			
		}
		log.debug("dropAmt:: "+dropAmt);
		DropRes dropRes = new DropRes();
		dropRes.setModelName(model);
		dropRes.setDropCount(lostCnt);
		dropRes.setModelId(modelId);
		dropRes.setDropAmount(dropAmt);
		return dropRes;
	}


	private Map<String, Object> getLostData(Map<String, Map<Integer, String>> vehicleDetails, TargetRoleRes tRole,
			String startDate, String endDate, List<String> empNamesList, List<Integer> empIdsUnderReporting) {
		Map<String, Object> responseMap = new LinkedHashMap<>();
		List<LostRes> lostResList = new ArrayList<>();
		vehicleDetails.get("main").forEach((k, v) -> {
			log.debug("Generating data for " + v);
			List<String> list = new ArrayList<>();
			list.add(v);
			lostResList.add(buildLostData(tRole,startDate,endDate,empNamesList,v,k,list));
		});
		List<LostRes> lostResOthersList = new ArrayList<>();
		vehicleDetails.get("others").forEach((k, v) -> {
			log.debug("Generating Others data for"+v);
			List<String> list = new ArrayList<>();
			list.add(v);
			lostResOthersList.add(buildLostData(tRole,startDate,endDate,empNamesList,v,k,list));
		});
		
		log.debug("lostResList "+lostResList);
		log.debug("lostResOthersList "+lostResOthersList);
		LostRes otherLostRes = new LostRes();
		int otherLostCount =0; 
		Long otherLostAmt = 0L;
		for(LostRes lr : lostResOthersList) {
			otherLostCount = otherLostCount+lr.getLostCount();
			otherLostAmt = otherLostAmt+lr.getLostAmount();
		}
		otherLostRes.setModelName("others");
		otherLostRes.setLostAmount(otherLostAmt);
		otherLostRes.setLostCount(otherLostCount);
		log.debug("otherLostRes "+otherLostRes);
		
		lostResList.add(otherLostRes);		
		
		Integer totalLostCnt = lostResList.stream().collect(Collectors.summingInt(LostRes::getLostCount));
		log.debug("totalLostCnt ::"+totalLostCnt);
		
		for(LostRes lr : lostResList) {
			Double perc =0D;
			if(totalLostCnt!=0) {
				perc = (lr.getLostCount()/Double.valueOf(totalLostCnt))*100;
			}
			lr.setLostPercentage(String.format("%.2f", perc));
		}
		responseMap.put("lostData", lostResList);
		responseMap.put("totalLostRevenue", lostResList.stream().collect(Collectors.summingLong(LostRes::getLostAmount)));
		return responseMap;
	}
	
	private LostRes buildLostData(TargetRoleRes tRole, String startDate, String endDate, List<String> empNamesList,String model,Integer modelId,List<String> list) {
		List<DmsLead> dmsLeadList = dmsLeadDao.getAllEmployeeLeadsWithModelandStage(tRole.getOrgId(), empNamesList, startDate, endDate, list, DROPPED);
		List<Integer> dropList = dmsLeadDropDao.getLeads(dmsLeadList.stream().map(DmsLead::getId).collect(Collectors.toList()), ENQUIRY);
		
		int lostCnt = 0;
		List<DmsLead> filteredDmsList = new ArrayList<>();
		if (null != dropList) {
			lostCnt = dropList.size();
			log.debug("lostCnt " + lostCnt);
			for(DmsLead lead : dmsLeadList) {
				if(dropList.contains(lead.getId())) {
					filteredDmsList.add(lead);
				}
			}
			log.debug("filteredDmsList " + filteredDmsList.size());
		}
		//Map<Integer,Long> variantMap = new HashMap<>();
		Long lostAmt=0L;
		for(DmsLead l: filteredDmsList) {
			int variantId = dashBoardUtil.getVariantId(l.getId());
			Long price = dashBoardUtil.getVehiclePrice(tRole.getOrgId(),modelId,variantId).longValue();
			lostAmt = lostAmt+price;
			log.debug("variantId "+variantId+",price "+price+" Lead Id :"+l.getId());
			
		}
		log.debug("variantMap "+lostAmt);
		LostRes lostRes = new LostRes();
		lostRes.setModelName(model);
		lostRes.setLostCount(lostCnt);
		lostRes.setModelId(modelId);
		lostRes.setLostAmount(lostAmt);
		return lostRes;
	}
	
	

	@Override
	public Map<String, Object> getTodaysPendingUpcomingData(DashBoardReq req) {
	log.info("Inside getTodaysPendingUpcomingData(){},pageNo "+req.getPageNo()+" and size:"+req.getSize());
	Map<String, Object>  list = new LinkedHashMap<>();
		try {
			String empId = req.getLoggedInEmpId();
			TargetRoleRes tRole = salesGapServiceImpl.getEmpRoleData(Integer.valueOf(empId));
			String designation = tRole.getDesignationName();
			log.info("LOGGED IN EMP Designation "+designation);	
			
			if(salesGapServiceImpl.validateDSE(designation)) {
				log.info("Getting date for DSE "+empId);
				list=buildTodaysDataForDSE(tRole,req,empId);
			}
		
			if(salesGapServiceImpl.validateTL(designation)) {
				
				if(null!=req && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("TL - Page Load");
					list=buildTodaysDataForTL(tRole,req,req.getLoggedInEmpId());
				}
				else if(null!=req && null != req.getEmployeeId() && null!=empId) {
					log.info("Emp ID is present");
					list=buildTodaysDataForDSE(tRole,req,req.getEmployeeId());
				}
			}
			
			if(salesGapServiceImpl.validateMgr(designation)) {
				if(null!=req && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info(" Manager - Page Load");
					list=buildTodaysDataForMgr(tRole,req,req.getLoggedInEmpId());
				}
				else if(null!=req && null!=req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("TeamLead ID is present");
					list=buildTodaysDataForTL(tRole,req,req.getTeamLeadId());
				}
				
				else if(null!=req && null!=req.getTeamLeadId() && null != req.getEmployeeId() && empId!=null) {
					log.info("TeamLead ID and Emp ID is present");
					list=buildTodaysDataForDSE(tRole,req,req.getEmployeeId());
				}
			}
		
			if(salesGapServiceImpl.validateBranchMgr(designation)) {
				
				if(null!=req && null==req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Branch Manager PageLoad");
					list=buildTodaysDataForBranchMgr(tRole,req,req.getLoggedInEmpId());
				}
				else if(null!=req && null!=req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Manager ID and Manager ID is present");
					list=buildTodaysDataForMgr(tRole,req,req.getManagerId());
				}
				
				else if(null!=req &&  null!=req.getManagerId() && null!=req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Manager ID and Team LeadID is present");
					list=buildTodaysDataForTL(tRole,req,req.getTeamLeadId());
				}
				else if(null!=req && null!=req.getManagerId() && null!=req.getTeamLeadId() && null != req.getEmployeeId() && empId!=null) {
					log.info("Manager ID , TeamLead ID and Emp ID is present");
					list=buildTodaysDataForDSE(tRole,req,req.getEmployeeId());
				}
			}
			
			if(salesGapServiceImpl.validateGeneralMgr(designation) && null!=req) {
				System.out.println("req.getBranchmangerId() "+req.getBranchmangerId());
				if(null== req.getBranchId() && null==req.getLocationId() && null==req.getBranchmangerId() && null==req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("General Manager PageLoad");
					list=buildTodaysDataForGeneralMgr(tRole,req);
				}
				else if(null != req.getBranchId() && null==req.getLocationId() && null==req.getBranchmangerId() && null==req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID is present");
					list=buildTodaysDataForBranch(tRole,req);
				}
				else if(null != req.getBranchId() && null!=req.getLocationId() && null==req.getBranchmangerId() && null==req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID and Location ID is present");
					list=buildTodaysDataForLocation(tRole,req);
				}
				
				else if(null != req.getBranchId() && null!=req.getLocationId() && null!=req.getBranchmangerId() && null==req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID,Location ID and BranchMgr ID is present");
					list=buildTodaysDataForBranchMgr(tRole,req,req.getBranchmangerId());
					}
				else if(null != req.getBranchId() && null!=req.getLocationId() && null!=req.getBranchmangerId() && null!=req.getManagerId() && null==req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID,Location ID ,BranchMgr ID and Manager ID is present");
					list=buildTodaysDataForMgr(tRole,req,req.getManagerId());
				}
				else if(null != req.getBranchId() && null!=req.getLocationId() && null!=req.getBranchmangerId() && null!=req.getManagerId() && null!=req.getTeamLeadId() && null == req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID,Location ID ,BranchMgr ID and Manager ID is present");
					list=buildTodaysDataForTL(tRole,req,req.getTeamLeadId());
					}
				else if(null != req.getBranchId() && null!=req.getLocationId() && null!=req.getBranchmangerId() && null!=req.getManagerId() && null!=req.getTeamLeadId() && null != req.getEmployeeId() && empId!=null) {
					log.info("Only Branch ID,Location ID ,BranchMgr ID , Manager ID and Employeed ID is present");
					list=buildTodaysDataForDSE(tRole,req,req.getEmployeeId());
				}
				
			}
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	
	
	}


	private Map<String, Object> buildTodaysDataForDSE(TargetRoleRes tRole, DashBoardReq req,String empId) {
		Map<String, Object> map = new HashMap<>();
		   try {
			log.info("Generating EventDataRes Data for DSE "+empId);
			List<Integer> empIdsUnderReporting=new ArrayList<>();
			empIdsUnderReporting.add(Integer.valueOf(empId));
			map = getTodaysData(empIdsUnderReporting,req,tRole);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		
		return map;
	}

	private Map<String, Object> buildTodaysDataForLocation(TargetRoleRes tRole, DashBoardReq req) {
		Map<String, Object> map = new HashMap<>();
		   try {
			String branch = req.getBranchId();
			String location = req.getLocationId();
			log.info("Generating Data for Branch " + branch+" and location "+location);
			List<Integer> empIdsUnderReporting = dashBoardUtil.getEmployeesUnderLocation(req.getBranchId(),req.getLocationId());
			log.debug("empIdsUnderReporting in buildTodaysDataForLocation "+empIdsUnderReporting);
			map = getTodaysData(empIdsUnderReporting,req,tRole);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		return map;
	}


	private Map<String, Object> buildTodaysDataForBranch(TargetRoleRes tRole, DashBoardReq req) {
		Map<String, Object> map = new HashMap<>();
		   try {
			String branch = req.getBranchId();
			log.info("Generating Data for Branch " + branch);
			List<Integer> empIdsUnderReporting = dashBoardUtil.getEmployeesUnderBranch(branch);
			log.debug("empIdsUnderReporting in buildTodaysDataForBranch "+empIdsUnderReporting);
			map = getTodaysData(empIdsUnderReporting,req,tRole);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		return map;
	}



	private Map<String, Object> buildTodaysDataForTL(TargetRoleRes tRole, DashBoardReq req,
			String empId) {
		Map<String, Object> map = new HashMap<>();
		try {
			log.info("Generating Data for TL " + empId);
			List<Integer> empIdsUnderReporting = dashBoardUtil.getEmployeesUnderTL(empId);
			map = getTodaysData(empIdsUnderReporting,req,tRole);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	
	private Map<String, Object> buildTodaysDataForMgr(TargetRoleRes tRole, DashBoardReq req,
			String empId) {
		Map<String, Object> map = new HashMap<>();
		   try {
			
			log.info("Generating Data for Manager " + empId);
			List<Integer> empIdsUnderReporting = dashBoardUtil.getEmployeesUnderMgr(empId);
			map = getTodaysData(empIdsUnderReporting,req,tRole);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		return map;
	}

	private Map<String, Object> buildTodaysDataForBranchMgr(TargetRoleRes tRole, DashBoardReq req,
			String empId) {
		Map<String, Object> map = new HashMap<>();
		   try {
			log.info("Generating Data for Branch Manager " + empId);
			List<Integer> empIdsUnderReporting = dashBoardUtil.getEmployeesUnderBranchMgr(empId);
			map = getTodaysData(empIdsUnderReporting,req,tRole);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		return map;
	}
	
	private Map<String, Object> buildTodaysDataForGeneralMgr(TargetRoleRes tRole, DashBoardReq req) {
		Map<String, Object> map = new HashMap<>();
		   try {
			String empId = req.getLoggedInEmpId();
			log.info("Generating Data for General Manager " + empId);
			List<Integer> empIdsUnderReporting = dashBoardUtil.getEmployeesUnderGeneralMgr(empId);
			map = getTodaysData(empIdsUnderReporting,req,tRole);
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		return map;
	}


	
	public Map<String, Object> getTodaysDataByBranch(DashBoardReq req) {
		log.info("Inside getTodaysDataDataByBranch(){}");
		Map<String, Object> map = new HashMap<>();
		try {
			String empId = req.getLoggedInEmpId();
			TargetRoleRes tRole = getEmployeeRoleInfo(empId);
			map = buildTodaysDataForBranch(tRole, req);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	
	private Map<String, Object> getTodaysData(List<Integer> empIdsUnderReporting, DashBoardReq req,
			TargetRoleRes tRole) {
		Map<String, Object> map = new LinkedHashMap<>();
		try {
			int totalCnt = empIdsUnderReporting.size();
			empIdsUnderReporting = dashBoardUtil.getPaginatedList(empIdsUnderReporting, req.getPageNo(), req.getSize());
			
			Map<String, Integer> paginationMap = new LinkedHashMap<>();
			paginationMap.put("totalCnt", totalCnt);
			paginationMap.put("pageNo", req.getPageNo());
			paginationMap.put("size", req.getSize());
			
			map.put("paginationData", paginationMap);
			map.put("todaysData", getTodayData(req,empIdsUnderReporting,tRole));
			map.put("upcomingData", getUpcomingData(req,empIdsUnderReporting,tRole));
			map.put("pendingData", getPendingData(req,empIdsUnderReporting,tRole));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}


	private List<TodaysRes> getPendingData(DashBoardReq req, List<Integer> empIdsUnderReporting, TargetRoleRes tRole) {
		log.debug("Inside getPendingData()");
		List<TodaysRes> list = new ArrayList<>();
		int cnt=1;
		for (Integer empId : empIdsUnderReporting) {
			String empName = salesGapServiceImpl.getEmpName(String.valueOf(empId));
			log.debug("generating data for empId " + empId + " and empName:" + empName);
			String startDate = getStartDate(req.getStartDate());
			String endDate = getEndDate(req.getEndDate());
			String todaysDate = getTodaysDate();
			log.info("StartDate " + startDate + ", EndDate " + endDate + " ,todaysDate " + todaysDate);
			TodaysRes todaysRes = new TodaysRes();
			List<DmsWFTask> wfTaskList = dmsWfTaskDao.getTodaysUpcomingTasks(empId, startDate, endDate);
			wfTaskList = wfTaskList.stream().filter(wfTask->validatePendingTask(wfTask.getTaskUpdatedTime(), wfTask.getTaskCreatedTime())).collect(Collectors.toList());
			Long pendingCnt = 0L;
			todaysRes.setEmpName(empName);
			if(null!=wfTaskList) {
				pendingCnt = Long.valueOf(wfTaskList.size());
				todaysRes.setCall(getCallCount(wfTaskList));
				todaysRes.setV(getHomeVisitCount(wfTaskList));
				todaysRes.setTd(getTestDriveCount(wfTaskList));
				todaysRes.setD(getDeliveryCount(wfTaskList));
				todaysRes.setPb(getPreBookingCount(wfTaskList));
				todaysRes.setPending(pendingCnt);
			}
			todaysRes.setSNo(cnt++);
			list.add(todaysRes);
		}
		return list;
	}


	private String getTodaysDate() {
		return LocalDate.now().toString();
	}


	private List<TodaysRes> getUpcomingData(DashBoardReq req, List<Integer> empIdsUnderReporting, TargetRoleRes tRole) {
		log.debug("Inside getUpcomingData()");
		List<TodaysRes> list = new ArrayList<>();
		int cnt=1;
		for (Integer empId : empIdsUnderReporting) {
			String empName = salesGapServiceImpl.getEmpName(String.valueOf(empId));
			log.debug("generating data for empId " + empId + " and empName:" + empName);
			String startDate = getStartDate(req.getStartDate());
			String endDate = getEndDate(req.getEndDate());
			String todaysDate = getTodaysDate();
			log.info("StartDate " + startDate + ", EndDate " + endDate + " ,todaysDate " + todaysDate);
			
			TodaysRes todaysRes = new TodaysRes();
			List<DmsWFTask> wfTaskList = dmsWfTaskDao.getTodaysUpcomingTasks(empId, startDate, endDate);
			List<DmsWFTask> upcomingWfTaskList = wfTaskList.stream().filter(wfTask->validateUpcomingTask(wfTask.getTaskUpdatedTime(), wfTask.getTaskCreatedTime())).collect(Collectors.toList());
			List<DmsWFTask> pendingWfTaskList = wfTaskList.stream().filter(wfTask->validatePendingTask(wfTask.getTaskUpdatedTime(), wfTask.getTaskCreatedTime())).collect(Collectors.toList());
			Long pendingCnt = 0L;
			if(null!=pendingWfTaskList) {
				pendingCnt = Long.valueOf(pendingWfTaskList.size());
			}
			todaysRes.setEmpName(empName);
			if(null!=upcomingWfTaskList) {
				todaysRes.setCall(getCallCount(upcomingWfTaskList));
				todaysRes.setV(getHomeVisitCount(upcomingWfTaskList));
				todaysRes.setTd(getTestDriveCount(upcomingWfTaskList));
				todaysRes.setD(getDeliveryCount(upcomingWfTaskList));
				todaysRes.setPb(getPreBookingCount(upcomingWfTaskList));
			}
			todaysRes.setPending(pendingCnt);
			todaysRes.setSNo(cnt++);
			list.add(todaysRes);
		
		}
		return list;
	
	}



	private List<TodaysRes> getTodayData(DashBoardReq req, List<Integer> empIdsUnderReporting, TargetRoleRes tRole)
			throws ParseException {
		log.debug("Inside getTodayData()");
		int cnt=1;
		List<TodaysRes> list = new ArrayList<>();
		for (Integer empId : empIdsUnderReporting) {
			String empName = salesGapServiceImpl.getEmpName(String.valueOf(empId));
			log.debug("generating data for empId " + empId + " and empName:" + empName);
			String startDate = getStartDate(req.getStartDate());
			String endDate = getEndDate(req.getEndDate());
			String todaysDate = getTodaysDate();
			log.info("StartDate " + startDate + ", EndDate " + endDate + " ,todaysDate " + todaysDate);
			
			TodaysRes todaysRes = new TodaysRes();
			List<DmsWFTask> wfTaskList = dmsWfTaskDao.getTodaysUpcomingTasks(empId, startDate, endDate);
			List<DmsWFTask> todayWfTaskList = wfTaskList.stream().filter(wfTask->validateTodaysDate(wfTask.getTaskUpdatedTime(), wfTask.getTaskCreatedTime())).collect(Collectors.toList());
			List<DmsWFTask> pendingWfTaskList = wfTaskList.stream().filter(wfTask->validatePendingTask(wfTask.getTaskUpdatedTime(), wfTask.getTaskCreatedTime())).collect(Collectors.toList());
			Long pendingCnt = 0L;
			if(null!=pendingWfTaskList) {
				pendingCnt = Long.valueOf(pendingWfTaskList.size());
			}
			todaysRes.setEmpName(empName);
			if(null!=todayWfTaskList) {
				todaysRes.setCall(getCallCount(todayWfTaskList));
				todaysRes.setV(getHomeVisitCount(todayWfTaskList));
				todaysRes.setTd(getTestDriveCount(todayWfTaskList));
				todaysRes.setD(getDeliveryCount(todayWfTaskList));;
				todaysRes.setPb(getPreBookingCount(todayWfTaskList));
			}
			todaysRes.setPending(pendingCnt);
			todaysRes.setSNo(cnt++);;
			list.add(todaysRes);
		
		}
		return list;
	}

	private Long getPreBookingCount(List<DmsWFTask> wfTaskList) {
		return wfTaskList.stream().filter(x->StringUtils.containsIgnoreCase(x.getTaskName(), PRE_BOOKING)).count();
	}


	private Long getDeliveryCount(List<DmsWFTask> wfTaskList) {
		return wfTaskList.stream().filter(x->x.getTaskName().equalsIgnoreCase(BOOKING_FOLLOWUP_CRM)).count();
	}


	private Long getCallCount(List<DmsWFTask> wfTaskList) {
		return wfTaskList.stream().filter(x->StringUtils.containsIgnoreCase(x.getTaskName(), DELIVERY)).count();
	}


	private boolean validateTodaysDate(String taskUpdatedTime, String taskCreatedTime) {
		log.debug("inside validateTodaysDate()");
		boolean flag = false;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date updatedDate = null;
			if (taskUpdatedTime != null) {
				updatedDate = sdf.parse(taskUpdatedTime);
			} else {
				updatedDate = sdf.parse(taskCreatedTime);
			}
			Date todaysDate = sdf.parse(LocalDate.now().toString());
			flag = updatedDate.equals(todaysDate);
			log.debug("updatedDate " + updatedDate + ", and todays date "+ todaysDate+ " and flag is "+flag);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	

	private boolean validateUpcomingTask(String taskUpdatedTime, String taskCreatedTime) {
		boolean flag = false;
		try {
			log.debug("inside validateUpcomingTask");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date updatedDate = null;
			if (taskUpdatedTime != null) {
				updatedDate = sdf.parse(taskUpdatedTime);
			} else {
				updatedDate = sdf.parse(taskCreatedTime);
			}
			Date todaysDate = sdf.parse(LocalDate.now().toString());
			flag = updatedDate.after(todaysDate);
			log.debug("updatedDate " + updatedDate + ", and todays date "+ todaysDate+ " and flag is "+flag);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	
	private boolean validatePendingTask(String taskUpdatedTime, String taskCreatedTime) {
		boolean flag = false;
		try {
			log.debug("inside validatePendingTask");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date updatedDate = null;
			if (taskUpdatedTime != null) {
				updatedDate = sdf.parse(taskUpdatedTime);
			} else {
				updatedDate = sdf.parse(taskCreatedTime);
			}
			Date todaysDate = sdf.parse(LocalDate.now().toString());
			flag = updatedDate.before(todaysDate);
			log.debug("updatedDate " + updatedDate + ", and todays date "+ todaysDate+ " and flag is "+flag);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	
}
