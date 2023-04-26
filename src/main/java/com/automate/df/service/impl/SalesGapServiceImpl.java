package com.automate.df.service.impl;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.automate.df.constants.GsAppConstants;
import com.automate.df.dao.oh.DmsBranchDao;
import com.automate.df.dao.oh.DmsDesignationRepo;
import com.automate.df.dao.salesgap.DmsEmployeeRepo;
import com.automate.df.dao.salesgap.TargetSettingRepo;
import com.automate.df.dao.salesgap.TargetUserRepo;
import com.automate.df.entity.oh.DmsBranch;
import com.automate.df.entity.oh.DmsDesignation;
import com.automate.df.entity.salesgap.DmsEmployee;
import com.automate.df.entity.salesgap.TSAdminUpdateReq;
import com.automate.df.entity.salesgap.TargetEntity;
import com.automate.df.entity.salesgap.TargetEntityUser;
import com.automate.df.entity.salesgap.TargetRoleReq;
import com.automate.df.exception.DynamicFormsServiceException;
import com.automate.df.model.salesgap.Target;
import com.automate.df.model.salesgap.TargetDropDown;
import com.automate.df.model.salesgap.TargetMappingAddReq;
import com.automate.df.model.salesgap.TargetMappingReq;
import com.automate.df.model.salesgap.TargetParamReq;
import com.automate.df.model.salesgap.TargetRoleRes;
import com.automate.df.model.salesgap.TargetSearch;
import com.automate.df.model.salesgap.TargetSettingReq;
import com.automate.df.model.salesgap.TargetSettingRes;
import com.automate.df.service.SalesGapService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author sruja
 *
 */
@Slf4j
@Service
public class SalesGapServiceImpl implements SalesGapService {

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
	private EntityManager entityManager;

	@Value("${targetsetting.params}")
	List<String> paramList;

	@Autowired
	DmsEmployeeRepo dmsEmployeeRepo;

	@Value("${dse.roles}")
	List<String> dseDesignationList;

	@Value("${tl.roles}")
	List<String> tlDesignationList;

	@Value("${manager.roles}")
	List<String> mgrDesignationList;

	@Value("${branchmgr.roles}")
	List<String> branchMgrDesignationList;

	@Value("${gm.roles}")
	List<String> GMDesignationList;

	public static final String RETAIL_TARGET = "retailTarget";
	public static final String INVOICE = "invoice";

	public static final String PERCENTAGE = "percentage";
	public static final String NUMBER = "number";

	final String getReportingEmp = "SELECT emp_id,emp_name FROM dms_employee where reporting_to=<ID>";
	final String getEmpUnderTLQuery = "SELECT emp_id FROM dms_employee where reporting_to=<ID>";

	final String roleMapQuery = " SELECT "
			+ " rolemap.organization_id, rolemap.branch_id, rolemap.emp_id, role.role_name, role.role_id, role.precedence "
			+ " FROM dms_role role " + " INNER JOIN dms_employee_role_mapping rolemap ON rolemap.role_id=role.role_id "
			+ " AND rolemap.emp_id=<EMP_ID> " + " ORDER BY role.precedence ";
	final String dmsEmpByidQuery = "SELECT * FROM dms_employee where emp_id=<EMP_ID>";
	final String getSalForEmp = "select salary from dms_emp_sal_mapping where emp_id=<ID>";

	@Override
	public List<TargetSettingRes> getTargetSettingData(int pageNo, int size) {
		log.debug("Inside getTargetSettingData()");
		List<TargetSettingRes> list = new ArrayList<>();
		try {
			Pageable pageable = PageRequest.of(pageNo, size);
			List<TargetEntity> dbList = targetSettingRepo.findAll(pageable).toList();
			// list = dbList.stream().map(x -> modelMapper.map(x,
			// TargetSettingRes.class)).collect(Collectors.toList());
			dbList = dbList.stream().filter(x -> x.getActive().equalsIgnoreCase(GsAppConstants.ACTIVE))
					.collect(Collectors.toList());
			for (TargetEntity te : dbList) {
				TargetSettingRes res = modelMapper.map(te, TargetSettingRes.class);
				String json = te.getTargets();
				if (null != json && !json.isEmpty()) {
					JsonParser parser = new JsonParser();
					JsonArray arr = parser.parse(json).getAsJsonArray();
					for (JsonElement je : arr) {
						JsonObject obj = je.getAsJsonObject();
						String paramName = obj.get("parameter").getAsString();
						res = convertJsonToStr(res, paramName, obj);
					}
				}
				// branch,location,department,designation,experience,salaryrange,branchmanager,manager,teamlead,employee
				res.setEmpName(te.getEmpName());

				res.setBranchName(getBranchName(te.getBranch()));
				// res.setLocationName(getLocationName(te.getLocation()));
				res.setDepartmentName(getDeptName(te.getDepartment()));
				res.setDesignationName(getDesignationName(te.getDesignation()));
				res.setExperience(te.getExperience());
				res.setSalrayRange(te.getSalrayRange());
				list.add(res);
			}

		} catch (Exception e) {
			log.error("getTargetSettingData() ", e);
		}
		return list;
	}

	private TargetSettingRes convertJsonToStr(TargetSettingRes res, String paramName, JsonObject obj) {
		if (null != paramName && paramName.equalsIgnoreCase("retailTarget")) {
			if (obj.has("target"))
				res.setRetailTarget(obj.get("target").getAsString());
		}
		if (null != paramName && paramName.equalsIgnoreCase("enquiry")) {
			if (obj.has("target"))
				res.setEnquiry(obj.get("target").getAsString());
		}
		if (null != paramName && paramName.equalsIgnoreCase("testDrive")) {
			if (obj.has("target"))
				res.setTestDrive(obj.get("target").getAsString());
		}
		if (null != paramName && paramName.equalsIgnoreCase("homeVisit")) {
			if (obj.has("target"))
				res.setHomeVisit(obj.get("target").getAsString());
		}

		if (null != paramName && paramName.equalsIgnoreCase("videoConference")) {
			if (obj.has("target"))
				res.setVideoConference(obj.get("target").getAsString());
		}

		if (null != paramName && paramName.equalsIgnoreCase("booking")) {
			if (obj.has("target"))
				res.setBooking(obj.get("target").getAsString());
		}
		if (null != paramName && paramName.equalsIgnoreCase("exchange")) {
			if (obj.has("target"))
				res.setExchange(obj.get("target").getAsString());
		}
		if (null != paramName && paramName.equalsIgnoreCase("finance")) {
			if (obj.has("target"))
				res.setFinance(obj.get("target").getAsString());
		}
		if (null != paramName && paramName.equalsIgnoreCase("insurance")) {
			if (obj.has("target"))
				res.setInsurance(obj.get("target").getAsString());
		}
		if (null != paramName && paramName.equalsIgnoreCase("exWarranty")) {
			if (obj.has("target"))
				res.setExWarranty(obj.get("target").getAsString());
		}
		if (null != paramName && paramName.equalsIgnoreCase("accessories")) {
			if (obj.has("target"))
				res.setAccessories(obj.get("target").getAsString());
		}
		if (null != paramName && paramName.equalsIgnoreCase("events")) {
			if (obj.has("target"))
				res.setEvents(obj.get("target").getAsString());
		}
		if (null != paramName && paramName.equalsIgnoreCase("other")) {
			if (obj.has("target"))
				res.setOther(obj.get("target").getAsString());
		}
		if (null != paramName && paramName.equalsIgnoreCase("enquiry")) {
			if (obj.has("target"))
				res.setEnquiry(obj.get("target").getAsString());
		}

		if (null != paramName && paramName.equalsIgnoreCase(INVOICE)) {
			if (obj.has("target"))
				res.setInvoice(obj.get("target").getAsString());
		}

		return res;
	}

	private TargetSettingRes convertJsonToStrV3(TargetSettingRes res, String paramName, JsonObject obj) {
		if (null != paramName && paramName.equalsIgnoreCase("retailTarget")) {
			if (obj.has("target"))
				res.setRetailTarget("0");
		}
		if (null != paramName && paramName.equalsIgnoreCase("enquiry")) {
			if (obj.has("target"))
				res.setEnquiry("0");
		}
		if (null != paramName && paramName.equalsIgnoreCase("testDrive")) {
			if (obj.has("target"))
				res.setTestDrive("0");
		}
		if (null != paramName && paramName.equalsIgnoreCase("homeVisit")) {
			if (obj.has("target"))
				res.setHomeVisit("0");
		}

		if (null != paramName && paramName.equalsIgnoreCase("videoConference")) {
			if (obj.has("target"))
				res.setVideoConference("0");
		}

		if (null != paramName && paramName.equalsIgnoreCase("booking")) {
			if (obj.has("target"))
				res.setBooking("0");
		}
		if (null != paramName && paramName.equalsIgnoreCase("exchange")) {
			if (obj.has("target"))
				res.setExchange("0");
		}
		if (null != paramName && paramName.equalsIgnoreCase("finance")) {
			if (obj.has("target"))
				res.setFinance("0");
		}
		if (null != paramName && paramName.equalsIgnoreCase("insurance")) {
			if (obj.has("target"))
				res.setInsurance("0");
		}
		if (null != paramName && paramName.equalsIgnoreCase("exWarranty")) {
			if (obj.has("target"))
				res.setExWarranty("0");
		}
		if (null != paramName && paramName.equalsIgnoreCase("accessories")) {
			if (obj.has("target"))
				res.setAccessories("0");
		}
		if (null != paramName && paramName.equalsIgnoreCase("events")) {
			if (obj.has("target"))
				res.setEvents("0");
		}
		if (null != paramName && paramName.equalsIgnoreCase("other")) {
			if (obj.has("target"))
				res.setOther("0");
		}
		if (null != paramName && paramName.equalsIgnoreCase("enquiry")) {
			if (obj.has("target"))
				res.setEnquiry("0");
		}

		if (null != paramName && paramName.equalsIgnoreCase(INVOICE)) {
			if (obj.has("target"))
				res.setInvoice("0");
		}

		return res;
	}

	private TargetSettingRes convertJsonToStrV2(TargetSettingRes res, String paramName, JsonObject obj,
			String retailTarget) {
		if (null != paramName && paramName.equalsIgnoreCase("retailTarget")) {
			res.setRetailTarget(retailTarget);
		}
		if (null != paramName && paramName.equalsIgnoreCase("enquiry")) {
			if (obj.has("target"))
				res.setEnquiry(obj.get("target").getAsString());
		}
		if (null != paramName && paramName.equalsIgnoreCase("testDrive")) {
			if (obj.has("target"))
				res.setTestDrive(obj.get("target").getAsString());
		}
		if (null != paramName && paramName.equalsIgnoreCase("homeVisit")) {
			if (obj.has("target"))
				res.setHomeVisit(obj.get("target").getAsString());
		}

		if (null != paramName && paramName.equalsIgnoreCase("videoConference")) {
			if (obj.has("target"))
				res.setVideoConference(obj.get("target").getAsString());
		}

		if (null != paramName && paramName.equalsIgnoreCase("booking")) {
			if (obj.has("target"))
				res.setBooking(obj.get("target").getAsString());
		}
		if (null != paramName && paramName.equalsIgnoreCase("exchange")) {
			if (obj.has("target"))
				res.setExchange(obj.get("target").getAsString());
		}
		if (null != paramName && paramName.equalsIgnoreCase("finance")) {
			if (obj.has("target"))
				res.setFinance(obj.get("target").getAsString());
		}
		if (null != paramName && paramName.equalsIgnoreCase("insurance")) {
			if (obj.has("target"))
				res.setInsurance(obj.get("target").getAsString());
		}
		if (null != paramName && paramName.equalsIgnoreCase("exWarranty")) {
			if (obj.has("target"))
				res.setExWarranty(obj.get("target").getAsString());
		}
		if (null != paramName && paramName.equalsIgnoreCase("accessories")) {
			if (obj.has("target"))
				res.setAccessories(obj.get("target").getAsString());
		}
		if (null != paramName && paramName.equalsIgnoreCase("events")) {
			if (obj.has("target"))
				res.setEvents(obj.get("target").getAsString());
		}
		if (null != paramName && paramName.equalsIgnoreCase("other")) {
			if (obj.has("target"))
				res.setOther(obj.get("target").getAsString());
		}
		if (null != paramName && paramName.equalsIgnoreCase("enquiry")) {
			if (obj.has("target"))
				res.setEnquiry(obj.get("target").getAsString());
		}
		if (null != paramName && paramName.equalsIgnoreCase(INVOICE)) {
			if (obj.has("target"))
				res.setInvoice(obj.get("target").getAsString());
		}
		return res;
	}

	@Override
	public TargetSettingRes saveTargetSettingData(TargetSettingReq request) throws DynamicFormsServiceException {
		log.debug("Inside saveTargetSettingData()");
		TargetEntity dbRes = null;
		TargetSettingRes ts = null;
		try {
			String minSal = "";
			String maxSal = "";
			TargetEntity te = new TargetEntity();
			te.setBranch(request.getBranch());
			// te.setLocation(request.getLocation());
			te.setDepartment(request.getDepartment());
			te.setDesignation(request.getDesignation());
			String exp = request.getExperience();
			String salRange = request.getSalrayRange();
			te.setExperience(request.getExperience());
			if (null != salRange) {
				te.setSalrayRange(salRange);
				if (salRange.contains("-")) {
					String tmp[] = salRange.split("-");
					minSal = tmp[0];
					minSal = StringUtils.replaceIgnoreCase(minSal, "k", "").trim();
					maxSal = tmp[1];
					maxSal = StringUtils.replaceIgnoreCase(maxSal, "k", "").trim();

				} else {
					minSal = salRange;
					minSal = StringUtils.replaceIgnoreCase(minSal, "k", "").trim();
				}
			}
			te.setOrgId(request.getOrgId());

			te.setMaxSalary(maxSal);
			te.setMinSalary(minSal);
			List<Target> list = request.getTargets();
			// log.debug("Before Targets "+list);
			// list = updatedTargetValues(list);
			// log.debug("After updating Targets "+list);
			if (null != list) {
				te.setTargets(gson.toJson(list));
			}
			if (!validateTargetAdminData(te)) {
				log.debug("TARGET ADMIN DATA DOES NOT EXISTS IN DB");
				te.setActive(GsAppConstants.ACTIVE);
				dbRes = targetSettingRepo.save(te);
				String targets = dbRes.getTargets();

				ts = modelMapper.map(dbRes, TargetSettingRes.class);
				ts = convertTargetStrToObj(targets, ts);
				ts.setBranchName(getBranchName(te.getBranch()));
				// ts.setLocationName(getLocationName(te.getLocation()));
				ts.setDepartmentName(getDeptName(te.getDepartment()));
				ts.setDesignationName(getDesignationName(te.getDesignation()));
				ts.setExperience(te.getExperience());
				ts.setSalrayRange(te.getSalrayRange());

			} else {
				throw new DynamicFormsServiceException("TARGET ADMIN DATA  EXISTS IN DB",
						HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (DynamicFormsServiceException e) {
			log.error("saveTargetSettingData() ", e);
			e.printStackTrace();
			throw new DynamicFormsServiceException("TARGET ADMIN DATA  EXISTS IN DB", HttpStatus.INTERNAL_SERVER_ERROR);

		} catch (Exception e) {
			log.error("saveTargetSettingData() ", e);
			e.printStackTrace();
			throw new DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return ts;
	}

	private boolean validateTargetAdminData(TargetEntity te) {
		List<TargetEntity> dbList = new ArrayList<>();

		String salRange = te.getSalrayRange();
		String exp = te.getExperience();

		if (null != salRange && null != exp) {
			dbList = targetSettingRepo.getTargetmappingData(te.getOrgId(), te.getBranch(), te.getDepartment(),
					te.getExperience(), te.getSalrayRange(), te.getDesignation());
		}
		if (null != salRange && null == exp) {
			dbList = targetSettingRepo.getTargetmappingDataWithOutExp(te.getOrgId(), te.getBranch(), te.getDepartment(),
					te.getSalrayRange(), te.getDesignation());
		}
		if (null == salRange && null != exp) {
			dbList = targetSettingRepo.getTargetmappingDataWithOutSal(te.getOrgId(), te.getBranch(), te.getDepartment(),
					te.getExperience(), te.getDesignation());
		}
		if (null == salRange && null == exp) {
			dbList = targetSettingRepo.getTargetmappingDataWithOutExpSal(te.getOrgId(), te.getBranch(),
					te.getDepartment(), te.getDesignation());
		}

		if (null != dbList && !dbList.isEmpty()) {
			return true;
		}
		return false;
	}

	private List<Target> updatedTargetValues(List<Target> list) {
		Integer retailTarget = null;
		String unitType = null;
		for (Target target : list) {

			if (null != target && target.getParameter().equalsIgnoreCase(RETAIL_TARGET)) {
				unitType = target.getUnit();
				retailTarget = Integer.valueOf(target.getTarget());

			}
		}
		log.debug("retailTarget " + retailTarget + " unitType " + unitType);
		for (Target target : list) {
			if (null != target && !target.getParameter().equalsIgnoreCase(RETAIL_TARGET)
					&& unitType.equalsIgnoreCase(PERCENTAGE)) {
				Integer paramTarget = 0;
				if (null != target.getTarget()) {
					paramTarget = Integer.valueOf(target.getTarget());
				}

				log.debug("paramTarget::" + paramTarget);
				if (paramTarget != 0) {
					Integer updatedTarget = (retailTarget * paramTarget * 100) / 100;
					target.setTarget(String.valueOf(updatedTarget));
				}
			}
		}
		return list;
	}

	/*
	 * 
	 * @Override public String saveTargetMappingData(TargetMappingReq request) {
	 * log.debug("Inside saveTargetMappingData()"); String res = null; try {
	 * Optional<TargetEntity> opt = targetSettingRepo.findById(request.getId());
	 * if(opt.isPresent()) { TargetEntity dbRes = opt.get(); dbRes =
	 * updateTargetMappingData(request,dbRes); targetSettingRepo.save(dbRes); res =
	 * objectMapper.writeValueAsString(dbRes); }else { res = "NO Record found"; } }
	 * catch (Exception e) { log.error("saveTargetSettingData() ", e);
	 * 
	 * } return res; }
	 * 
	 */

	@Override
	public TargetMappingReq getTargetMappingData(Integer id) {
		log.debug("Inside searchTargetMappingData()");
		TargetMappingReq res = null;
		try {
			Optional<TargetEntity> opt = targetSettingRepo.findById(id);
			if (opt.isPresent()) {
				res = modelMapper.map(opt.get(), TargetMappingReq.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("searchTargetMappingData() ", e);

		}
		return res;
	}

	@Override
	public String updateTargetSettingData(TargetSettingRes request) {
		log.debug("Inside updateTargetSettingData()");
		String res = null;
		try {

			Optional<TargetEntity> opt = targetSettingRepo.findById(request.getId());
			if (opt.isPresent()) {
				TargetEntity dbRes = opt.get();
				dbRes = updateTargetMappingData(request, dbRes);
				targetSettingRepo.save(dbRes);
				res = objectMapper.writeValueAsString(dbRes);
			} else {
				res = "NO Record found";
			}
		} catch (Exception e) {
			log.error("saveTargetSettingData() ", e);

		}
		return res;
	}

	/*
	 * @Override public List<TargetSettingRes> searchTargetMappingData(TargetSearch
	 * request) { log.debug("Inside searchTargetMappingData()");
	 * List<TargetSettingRes> res = null; try { List<TargetEntity> dbList =
	 * targetSettingRepo.getDataByEmpNameId(request.getEmpId(),request.getEmpName())
	 * ; res = dbList.stream().map(x -> modelMapper.map(x,
	 * TargetSettingRes.class)).collect(Collectors.toList()); } catch (Exception e)
	 * { e.printStackTrace(); log.error("searchTargetMappingData() ", e);
	 * 
	 * } return res; }
	 */
	private TargetEntity updateTargetMappingData(TargetSettingRes req, TargetEntity dbRes) {

		try {
			dbRes.setBranch(req.getBranch());
			// dbRes.setLocation(req.getLocation());
			dbRes.setEmployeeId(req.getEmployeeId());
			dbRes.setEmpName(getEmpIdByName(req.getEmployeeId()));
			dbRes.setManagerId(req.getManagerId());
			dbRes.setTeamLeadId(req.getTeamLeadId());

			TargetParamReq[] params = objectMapper.readValue(dbRes.getTargets(), TargetParamReq[].class);
			String retailUnitType = null;
			for (TargetParamReq param : params) {
				if (null != param && param.getParameter().equalsIgnoreCase(RETAIL_TARGET)) {
					retailUnitType = param.getUnit();
				}
			}
			List<TargetParamReq> list = new ArrayList<>();
			Map<String, String> unitsMap = getUnitsFromDbIfExists(dbRes.getTargets());
			for (String param : paramList) {
				String methodName = "get" + StringUtils.capitalize(param);
				Method getNameMethod = req.getClass().getMethod(methodName);
				String name = (String) getNameMethod.invoke(req); // explicit cast
				list.add(new TargetParamReq(param, name, unitsMap.get(param)));
			}
			dbRes.setTargets(new Gson().toJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dbRes;
	}

	private String getDropDownValueById(String id, String type) {
		log.info("getDropDownValueById ,id :" + id + " ,type " + type);
		List<TargetDropDown> list = getTargetDropdown(type);
		if (null != id && type != "experience") {
			Optional<TargetDropDown> opt = list.stream().filter(x -> x.getId() == id).findAny();
			if (opt.isPresent()) {
				return opt.get().getValue();
			}
		}
		return "";
	}

	@Override
	public List<TargetDropDown> getTargetDropdown(String type) {
		List<TargetDropDown> list = new ArrayList<>();
		/*
		 * if(type.equalsIgnoreCase("branch")) { TargetDropDown td1 = new
		 * TargetDropDown(1,"BharathHyundai"); TargetDropDown td2 = new
		 * TargetDropDown(2,"Renalut"); list.add(td1); list.add(td2); }
		 * if(type.equalsIgnoreCase("location")) { TargetDropDown td1 = new
		 * TargetDropDown(1,"Gachibowli"); TargetDropDown td2 = new
		 * TargetDropDown(2,"HitechCity"); list.add(td1); list.add(td2); }
		 * if(type.equalsIgnoreCase("department")) { TargetDropDown td1 = new
		 * TargetDropDown(1,"Sales"); TargetDropDown td2 = new
		 * TargetDropDown(2,"Finance"); list.add(td1); list.add(td2); }
		 * if(type.equalsIgnoreCase("designation")) { TargetDropDown td1 = new
		 * TargetDropDown(1,"Sales Executive"); TargetDropDown td2 = new
		 * TargetDropDown(2,"Team Lead"); list.add(td1); list.add(td2); }
		 * if(type.equalsIgnoreCase("experience")) { TargetDropDown td1 = new
		 * TargetDropDown(1,"5Y"); TargetDropDown td2 = new TargetDropDown(2,"3Y");
		 * list.add(td1); list.add(td2); } if(type.equalsIgnoreCase("salaryrange")) {
		 * TargetDropDown td1 = new TargetDropDown(1,"20K"); TargetDropDown td2 = new
		 * TargetDropDown(2,"30k"); list.add(td1); list.add(td2);
		 * }if(type.equalsIgnoreCase("branchmanager")) { TargetDropDown td1 = new
		 * TargetDropDown(1,"BranchManager1"); TargetDropDown td2 = new
		 * TargetDropDown(2,"BranchManager2"); list.add(td1); list.add(td2); }
		 * if(type.equalsIgnoreCase("manager")) { TargetDropDown td1 = new
		 * TargetDropDown(1,"Manager1"); TargetDropDown td2 = new
		 * TargetDropDown(2,"Manager2"); list.add(td1); list.add(td2); }
		 * if(type.equalsIgnoreCase("teamlead")) { TargetDropDown td1 = new
		 * TargetDropDown(1,"teamlead1"); TargetDropDown td2 = new
		 * TargetDropDown(2,"teamlead2"); list.add(td1); list.add(td2); }
		 * if(type.equalsIgnoreCase("employee")) { TargetDropDown td1 = new
		 * TargetDropDown(1,"employee1"); TargetDropDown td2 = new
		 * TargetDropDown(2,"employee2"); list.add(td1); list.add(td2); }
		 */
		return list;
	}

	@Override
	public Map<String, Object> getTargetDataWithRole(TargetRoleReq req) throws DynamicFormsServiceException {
		log.debug(dmsEmpByidQuery);
		List<TargetSettingRes> outputList = new ArrayList<>();
		Map<String, Object> map = new LinkedHashMap<>();
		int pageNo = req.getPageNo();
		int size = req.getSize();
		try {
			int empId = req.getEmpId();

			TargetRoleRes trRoot = getEmpRoleData(empId);

			if (validateDSE(trRoot.getDesignationName())) {
				log.info("Generating Data for DSE");
				outputList.addAll(getTSDataForRoleV2(trRoot, null, null, null, null));
			}

			else if (validateTL(trRoot.getDesignationName())) {
				log.info("Generating Data for TL of ID " + empId);
				outputList = getTLData(String.valueOf(empId), outputList, null, null, null);
			}

			else if (validateMgr(trRoot.getDesignationName())) {
				log.info("Generating Data for MANAGER of ID " + empId);
				outputList = getManagerData(String.valueOf(empId), outputList, null, null);
			} else if (validateBranchMgr(trRoot.getDesignationName())) {
				log.info("Generating Data for Branch Mgr of ID " + empId);
				outputList = getBranchMgrData(String.valueOf(empId), outputList, null);
			} else if (validateGeneralMgr(trRoot.getDesignationName())) {
				log.info("Generating Data for General Mgr of ID " + empId);
				outputList = getGeneralMgrData(String.valueOf(empId), outputList);
			}
			outputList = outputList.stream().distinct().collect(Collectors.toList());
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
			map.put("totalCnt", totalCnt);
			map.put("pageNo", pageNo);
			map.put("size", size);
			map.put("data", outputList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * @param empId
	 * @return
	 */

	public TargetRoleRes getEmpRoleData(int empId) throws DynamicFormsServiceException {

		String tmpQuery = dmsEmpByidQuery.replaceAll("<EMP_ID>", String.valueOf(empId));

		tmpQuery = roleMapQuery.replaceAll("<EMP_ID>", String.valueOf(empId));
		List<Object[]> data = entityManager.createNativeQuery(tmpQuery).getResultList();
		TargetRoleRes trRoot = new TargetRoleRes();
		
		for (Object[] arr : data) {
			trRoot.setOrgId(String.valueOf(arr[0]));
			trRoot.setBranchId(String.valueOf(arr[1]));
			trRoot.setEmpId(String.valueOf(arr[2]));
			trRoot.setRoleName(String.valueOf(arr[3]));
			trRoot.setRoleId(String.valueOf(arr[4]));
			trRoot.setPrecedence(Integer.parseInt(arr[5].toString()));
			String empid = trRoot.getEmpId();
			if(null!=empid) {
				TargetRoleRes tm = getEmpRoleDataV3(Integer.parseInt(empid));
				List<String> l = tm.getOrgMapBranches();
				trRoot.setOrgMapBranches(l);;
				if(l!=null) {
					trRoot.setBranchId(l.get(0));
					
				}
			}
		}
	
		Optional<DmsEmployee> empOpt = dmsEmployeeRepo.findById(empId);
		DmsEmployee emp = null;
		if (empOpt.isPresent()) {
			emp = empOpt.get();
			trRoot = buildTargetRoleRes(trRoot, emp);
		} else {
			throw new DynamicFormsServiceException("No Empoloyee with given empId in DB", HttpStatus.BAD_REQUEST);

		}
		log.debug("trRoot " + trRoot);

		return trRoot;
	}

	public List<TargetRoleRes> getEmpRoles(int empId) {

		List<TargetRoleRes> empRoles = new ArrayList<>();
		String tmpQuery = roleMapQuery.replaceAll("<EMP_ID>", String.valueOf(empId));
		List<Object[]> data = entityManager.createNativeQuery(tmpQuery).getResultList();

		for (final Object[] arr : data) {
			final TargetRoleRes trRoot = new TargetRoleRes();
			trRoot.setOrgId(String.valueOf(arr[0]));
			trRoot.setBranchId(String.valueOf(arr[1]));
			trRoot.setEmpId(String.valueOf(arr[2]));
			trRoot.setRoleName(String.valueOf(arr[3]));
			trRoot.setRoleId(String.valueOf(arr[4]));
			trRoot.setPrecedence(Integer.valueOf(arr[5].toString()));
			empRoles.add(trRoot);
		}
		return empRoles;
	}

	private List<TargetSettingRes> getGeneralMgrData(String empId, List<TargetSettingRes> outputList) {
		List<Object> branchMgrReportiesEmpIds = entityManager
				.createNativeQuery(getEmpUnderTLQuery.replaceAll("<ID>", empId)).getResultList();

		for (Object id : branchMgrReportiesEmpIds) {
			String eId = String.valueOf(id);
			log.info("Executing for General MANAGER ID " + eId);

			outputList = getBranchMgrData(eId, outputList, empId);
		}

		return outputList;
	}

	private List<TargetSettingRes> getBranchMgrData(String empId, List<TargetSettingRes> outputList,
			String generalMgrId) {

		List<Object> branchMgrReportiesEmpIds = entityManager
				.createNativeQuery(getEmpUnderTLQuery.replaceAll("<ID>", empId)).getResultList();

		for (Object id : branchMgrReportiesEmpIds) {
			String eId = String.valueOf(id);
			log.info("Executing for Branch MANAGER ID " + eId);

			outputList = getManagerData(eId, outputList, empId, generalMgrId);
		}

		return outputList;
	}

	private List<TargetSettingRes> getManagerData(String empId, List<TargetSettingRes> outputList, String branchMgrId,
			String generalMgrId) {

		List<Object> managerTLEmpIds = entityManager.createNativeQuery(getEmpUnderTLQuery.replaceAll("<ID>", empId))
				.getResultList();
		for (Object id : managerTLEmpIds) {
			String eId = String.valueOf(id);
			log.info("Executing for TL ID " + eId);

			outputList = getTLData(eId, outputList, empId, branchMgrId, generalMgrId);
		}
		return outputList;
	}

	private List<TargetSettingRes> getTLData(String empId, List<TargetSettingRes> outputList, String managerId,
			String branchMgrId, String generalMgrId) {

		try {
			List<Object> tlEmpIDData = entityManager
					.createNativeQuery(getEmpUnderTLQuery.replaceAll("<ID>", String.valueOf(empId))).getResultList();

			for (Object id : tlEmpIDData) {
				String eId = String.valueOf(id);
				log.info("Executing for EMP ID " + eId);

				String tmpQuery = roleMapQuery.replaceAll("<EMP_ID>", eId);
				List<Object[]> tlData = entityManager.createNativeQuery(tmpQuery).getResultList();
				List<TargetRoleRes> tlDataResList = new ArrayList<>();

				for (Object[] arr : tlData) {
					TargetRoleRes tlDataRole = new TargetRoleRes();
					tlDataRole.setOrgId(String.valueOf(arr[0]));
					tlDataRole.setBranchId(String.valueOf(arr[1]));
					tlDataRole.setEmpId(String.valueOf(arr[2]));
					tlDataRole.setRoleName(String.valueOf(arr[3]));
					tlDataRole.setRoleId(String.valueOf(arr[4]));
					tlDataResList.add(tlDataRole);
					
					String tEmpId = String.valueOf(arr[2]);
					Integer emp = Integer.parseInt(tEmpId);
					tlDataResList.add(getEmpRoleDataV3(emp));
				}
				if (null != tlDataResList) {
					log.info("Size of tlDataResList " + tlDataResList.size() + " tlDataResList " + tlDataResList);

					for (TargetRoleRes tr : tlDataResList) {
						Optional<DmsEmployee> empOpt = dmsEmployeeRepo.findById(Integer.valueOf(tr.getEmpId()));
						if (empOpt.isPresent()) {
							DmsEmployee emp = empOpt.get();
							// outputList.add(getTSDataForRole(buildTargetRoleRes(tr,
							// emp),String.valueOf(empId),managerId,branchMgrId,generalMgrId));
							outputList.addAll(getTSDataForRoleV2(buildTargetRoleRes(tr, emp), String.valueOf(empId),
									managerId, branchMgrId, generalMgrId));

						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return outputList;
	}

	private TargetRoleRes buildTargetRoleRes(TargetRoleRes trRoot, DmsEmployee emp) {
		try {
			trRoot.setSalary(getEmpSal(emp.getEmp_id()));
		
			// trRoot.setSalary(emp.getBasicSal());
			trRoot.setLocationId(emp.getLocationId());
			trRoot.setDesignationId(emp.getDesignationId());
			trRoot.setDesignationName(getDesignationName(emp.getDesignationId())); // trRoot.setDesignationName(getDesignationName(emp.getDesignationId()));
			trRoot.setExperience(calcualteExperience(emp.getPrevExperience(), emp.getJoiningDate()));
			trRoot.setDeptId(emp.getDeptId());
			trRoot.setDeptName(getDeptName(emp.getDeptId()));
			trRoot.setBranchId(emp.getBranch());
			//trRoot.setBranchId(trRoot.getOrgMapBranches());
			List<String> l = trRoot.getOrgMapBranches();
			log.debug("ORG MAP BRANCHES in buildTargetRoleRes "+trRoot.getOrgMapBranches());
			if(null!=l) {
				trRoot.setBranchId(l.get(0));
			}

			trRoot.setHrmsRole(emp.getHrmsRole());
			// trRoot.setLevel(getEmpLevel(emp.getEmp_id()));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return trRoot;
	}

	@Autowired
	DmsDesignationRepo dmsDesignationRepo;

	private Integer getEmpLevel(Integer empId) throws DynamicFormsServiceException {
		Integer empLevel = 0;
		Optional<DmsDesignation> desOpt = dmsDesignationRepo.findById(empId);
		if (desOpt.isPresent()) {
			empLevel = desOpt.get().getLevel();
		} else {
			throw new DynamicFormsServiceException("Given emp does not have valid designation in dms_designation",
					HttpStatus.BAD_REQUEST);
		}

		log.debug("Given emp level is " + empLevel);
		return empLevel;
	}

	public TargetSettingRes getTSDataForRole(TargetRoleRes tRole, String teamLeadId, String managerId,
			String branchMgrId, String generalMgrId) {
		log.debug("Inside getTSDataForRole()");
		TargetSettingRes res = new TargetSettingRes();
		try {
			List<TargetEntity> dbList = targetSettingRepo.getTargetmappingDataWithRole(tRole.getOrgId(),
					tRole.getBranchId(), tRole.getDeptId(), tRole.getExperience(), tRole.getSalary());
			for (TargetEntity te : dbList) {
				res = modelMapper.map(te, TargetSettingRes.class);

				Optional<TargetEntityUser> tesOpt = targetUserRepo.findByEmpId(tRole.getEmpId());
				String target = null;
				if (tesOpt.isPresent()) {
					TargetEntityUser tes = tesOpt.get();
					target = tesOpt.get().getTargets();
					res.setStartDate(tes.getStartDate());
					res.setEndDate(tes.getEndDate());
				} else {
					target = te.getTargets();
				}
				System.out.println("target::" + target);
				res = convertTargetStrToObj(target, res);
				res.setEmpName(getEmpName(tRole.getEmpId()));
				res.setEmployeeId(tRole.getEmpId());
				if (null != teamLeadId) {
					res.setTeamLead(getTeamLeadName(teamLeadId));
					res.setTeamLeadId(teamLeadId);
				}
				if (null != managerId) {
					res.setManager(getEmpName(managerId));
					res.setManagerId(managerId);
				}
				if (null != branchMgrId) {
					res.setBranchManagerId(branchMgrId);
					res.setBranchmanger(getEmpName(branchMgrId));
				}
				if (null != generalMgrId) {
					res.setGeneralManagerId(generalMgrId);
					res.setGeneralManager(getEmpName(generalMgrId));
				}
				// res.setManager(getDropDownValueById(te.getManagerId(),"manager"));
				// res.setBranchmanger(getDropDownValueById(te.getBranchmangerId(),"branchmanager"));
				// res.setBranch(getDropDownValueById(te.getBranch(),"branch"));
				// res.setLocation(getDropDownValueById(te.getLocation(), "location"));
				res.setDepartment(getDropDownValueById(te.getDepartment(), "department"));
				res.setDesignation(getDropDownValueById(te.getDesignation(), "designation"));
				res.setExperience(getDropDownValueById(te.getExperience(), "experience"));
				res.setSalrayRange(getDropDownValueById(te.getSalrayRange(), "salaryrange"));

			}

		} catch (Exception e) {
			log.error("getTargetSettingData() ", e);
		}
		return res;
	}

	public List<TargetSettingRes> getTSDataForRoleV2(TargetRoleRes tRole, String teamLeadId, String managerId,
			String branchMgrId, String generalMgrId) {
		log.debug("Inside getTSDataForRoleV2(),TROLE "+tRole);
		List<TargetSettingRes> list = new ArrayList<>();
		try {

			for (TargetEntity te : getTargetSettingMasterDataForGivenRole(tRole)) {
				List<TargetEntityUser> tesUserList = targetUserRepo.findAllEmpIds(tRole.getEmpId());
				if (null != tesUserList && !tesUserList.isEmpty()) {
					log.info("tesUserList is not empty " + tesUserList.size());
					for (TargetEntityUser teUser : tesUserList) {
						modelMapper.getConfiguration().setAmbiguityIgnored(true);
						TargetSettingRes tsRes = modelMapper.map(teUser, TargetSettingRes.class);
						tsRes = convertTargetStrToObj(teUser.getTargets(), tsRes);
						tsRes.setEmpName(getEmpName(tRole.getEmpId()));
						tsRes.setEmployeeId(tRole.getEmpId());
						tsRes.setId(teUser.getGeneratedId());
						if (null != teamLeadId) {
							tsRes.setTeamLead(getTeamLeadName(teamLeadId));
							tsRes.setTeamLeadId(teamLeadId);
						}
						if (null != managerId) {
							tsRes.setManager(getEmpName(managerId));
							tsRes.setManagerId(managerId);
						}
						if (null != branchMgrId) {
							tsRes.setBranchManagerId(branchMgrId);
							tsRes.setBranchmanger(getEmpName(branchMgrId));
						}
						if (null != generalMgrId) {
							tsRes.setGeneralManagerId(generalMgrId);
							tsRes.setGeneralManager(getEmpName(generalMgrId));
						}

						if (null != tRole.getLocationId()) {
							tsRes.setLocationName(getLocationName(tRole.getLocationId()));
						}
						if (null != tRole.getBranchId()) {
							tsRes.setBranchName(getBranchName(tRole.getBranchId()));
						}
						if (null != tRole.getDeptId()) {
							tsRes.setDepartmentName(getDeptName(tRole.getDeptId()));
						}
						if (null != tRole.getDesignationId()) {
							tsRes.setDesignationName(getDesignationName(tRole.getDesignationId()));
						}
						list.add(tsRes);
					}

				}

				else {
					log.debug("tesUserList is  empty ");
					modelMapper.getConfiguration().setAmbiguityIgnored(true);

					TargetSettingRes res = modelMapper.map(te, TargetSettingRes.class);
					res.setStartDate(getFirstDayOfQurter());
					res.setEndDate(getLastDayOfQurter());

					TargetEntityUser teUserToSave = modelMapper.map(res, TargetEntityUser.class);
					teUserToSave.setEmployeeId(tRole.getEmpId());
					teUserToSave.setTargets(te.getTargets());
					teUserToSave.setType("default");
					teUserToSave.setActive(GsAppConstants.ACTIVE);
					teUserToSave.setExperience(tRole.getExperience());

					Optional<TargetEntityUser> defaultTeUserOpt = targetUserRepo
							.checkDefaultDataInTargetUser(tRole.getEmpId());

					if (!defaultTeUserOpt.isPresent()) {
						log.debug("Default data is empty for " + tRole.getEmpId());
						TargetEntityUser dbRes = targetUserRepo.save(teUserToSave);
						res.setId(dbRes.getGeneratedId());
					}

					res = convertTargetStrToObjV3(te.getTargets(), res);
					res.setEmpName(getEmpName(tRole.getEmpId()));
					res.setEmployeeId(tRole.getEmpId());
					res.setExperience(tRole.getExperience() != null ? tRole.getExperience() : "");
					if (null != teamLeadId) {
						res.setTeamLead(getTeamLeadName(teamLeadId));
						res.setTeamLeadId(teamLeadId);
					}
					if (null != managerId) {
						res.setManager(getEmpName(managerId));
						res.setManagerId(managerId);
					}
					if (null != branchMgrId) {
						res.setBranchManagerId(branchMgrId);
						res.setBranchmanger(getEmpName(branchMgrId));
					}
					if (null != generalMgrId) {
						res.setGeneralManagerId(generalMgrId);
						res.setGeneralManager(getEmpName(generalMgrId));
					}

					if (null != tRole.getLocationId()) {
						res.setLocationName(getLocationName(tRole.getLocationId()));
					}
					if (null != tRole.getBranchId()) {
						res.setBranchName(getBranchName(tRole.getBranchId()));
					}
					if (null != tRole.getDeptId()) {
						res.setDepartmentName(getDeptName(tRole.getDeptId()));
					}
					if (null != tRole.getDesignationId()) {
						res.setDesignationName(getDesignationName(tRole.getDesignationId()));
					}
					list.add(res);

				}
			}
			System.out.println("list in getTSDataForRoleV2  " + list);

		} catch (Exception e) {
			log.error("getTargetSettingData() ", e);
		}
		return list;
	}

	private List<TargetEntity> getTargetSettingMasterDataForGivenRole(TargetRoleRes tRole)
			throws DynamicFormsServiceException {
		log.debug("Inside getTargetSettingMasterDataForGivenRole,for tRole");
		log.debug("tRole:::" + tRole);
		List<TargetEntity> finalList = new ArrayList<>();
		System.out.println("tRole.getDesignationId() " + tRole.getDesignationId());
		System.out.println("tRole.getOrgId()::" + tRole.getOrgId());
		System.out.println("tRole.getBranchId()::" + tRole.getBranchId());
		System.out.println("tRole.getLocationId()::" + tRole.getLocationId());
		System.out.println("tRole.getDeptId()::" + tRole.getDeptId());
		System.out.println("tRole.getDesignationId()::" + tRole.getDesignationId());

		// List<TargetEntityUser> userTargetList =
		// targetUserRepo.getUserTargetData(tRole.getOrgId(),tRole.getDeptId(),tRole.getDesignationId(),tRole.getBranchId());
		// List<TargetEntity> dbList =
		// targetSettingRepo.getTargetmappingDataWithOutExpSalV2(tRole.getOrgId(),
		// tRole.getBranchId(), tRole.getLocationId(), tRole.getDeptId(),
		// tRole.getDesignationId());
		List<TargetEntity> dbList = targetSettingRepo.getTargetmappingDataWithOutExpSalV2(tRole.getOrgId(),
				tRole.getDeptId(), tRole.getDesignationId(), tRole.getBranchId());
		// tRole.getBranchId(), tRole.getLocationId(), tRole.getDeptId(),
		// tRole.getDesignationId());

		log.debug("dbList size::::::: :" + dbList.size());
		log.debug("dbList " + dbList);
		String salRange = tRole.getSalary();
		if (null != salRange) {
			// throw new DynamicFormsServiceException("Salary Details of Employees are
			// missing", HttpStatus.INTERNAL_SERVER_ERROR);
			salRange = StringUtils.replaceIgnoreCase(salRange, "k", "");
			salRange = salRange.trim();
			Integer sal = Integer.valueOf(salRange);
			log.info("Sal range of emp " + tRole.getEmpId() + " is " + salRange);

			for (TargetEntity te : dbList) {
				if (null != te.getSalrayRange() &&te.getSalrayRange().length()>0
						&& null != te.getExperience() &&  te.getExperience().length()>0
						&& null != te.getMinSalary() && te.getMinSalary().length()>0
						&& null != te.getMaxSalary() && te.getMaxSalary().length()>0) {
					Integer minSal = Integer.valueOf(te.getMinSalary());
					Integer maxSal = Integer.valueOf(te.getMaxSalary());
					log.info("minSal::" + minSal + " maxSal " + maxSal);
					if ((minSal <= sal) && (sal <= maxSal)) {
						finalList.add(te);
					}
				}
			}
		}
		if (finalList.isEmpty()) {

			log.debug("FinalList is empty,Fetching adming config from NO Sal & Exp");
			for (TargetEntity te : dbList) {
				if (null == te.getSalrayRange() || null == te.getExperience()) {
					finalList.add(te);
				}
			}
		}
		if (finalList.size() > 1) {
			finalList = finalList.subList(0, 1);
		}
		log.debug("finalList " + finalList);
		log.debug("finalList size " + finalList.size());
		return finalList;
	}
	
	public TargetSettingRes getTSDataForRoleWithTarget(TargetRoleRes tRole, String teamLeadId, String managerId,
			String branchMgrId, String generalMgrId, String retailTarget) {
		log.debug("Inside getTSDataForRole()");
		TargetSettingRes res = new TargetSettingRes();
		try {
			List<TargetEntity> dbList = targetSettingRepo.getTargetmappingDataWithRole(tRole.getOrgId(),
					tRole.getBranchId(), tRole.getDeptId(), tRole.getExperience(), tRole.getSalary());
			for (TargetEntity te : dbList) {
				res = modelMapper.map(te, TargetSettingRes.class);
				String target = te.getTargets();
				System.out.println("target::" + target);
				res = convertTargetStrToObjV2(target, res, retailTarget);
				res.setEmpName(getEmpName(tRole.getEmpId()));
				res.setEmployeeId(tRole.getEmpId());
				if (null != teamLeadId) {
					res.setTeamLead(getTeamLeadName(teamLeadId));
					res.setTeamLeadId(teamLeadId);
				}
				if (null != managerId) {
					res.setManager(getEmpName(managerId));
					res.setManagerId(managerId);
				}
				if (null != branchMgrId) {
					res.setBranchManagerId(branchMgrId);
					res.setBranchmanger(getEmpName(branchMgrId));
				}
				if (null != generalMgrId) {
					res.setGeneralManagerId(generalMgrId);
					res.setGeneralManager(getEmpName(generalMgrId));
				}
				// res.setManager(getDropDownValueById(te.getManagerId(),"manager"));
				// res.setBranchmanger(getDropDownValueById(te.getBranchmangerId(),"branchmanager"));
				// res.setBranch(getDropDownValueById(te.getBranch(),"branch"));
				// res.setLocation(getDropDownValueById(te.getLocation(), "location"));
				res.setDepartment(getDropDownValueById(te.getDepartment(), "department"));
				res.setDesignation(getDropDownValueById(te.getDesignation(), "designation"));
				res.setExperience(getDropDownValueById(te.getExperience(), "experience"));
				res.setSalrayRange(getDropDownValueById(te.getSalrayRange(), "salaryrange"));

			}

		} catch (Exception e) {
			log.error("getTargetSettingData() ", e);
		}
		return res;
	}

	private TargetSettingRes convertTargetStrToObjV3(String json, TargetSettingRes res) {
		if (null != json && !json.isEmpty()) {
			JsonParser parser = new JsonParser();
			JsonArray arr = parser.parse(json).getAsJsonArray();
			for (JsonElement je : arr) {
				JsonObject obj = je.getAsJsonObject();
				String paramName = obj.get("parameter").getAsString();
				res = convertJsonToStrV3(res, paramName, obj);
			}
		}
		return res;
	}

	private TargetSettingRes convertTargetStrToObj(String json, TargetSettingRes res) {
		if (null != json && !json.isEmpty()) {
			JsonParser parser = new JsonParser();
			JsonArray arr = parser.parse(json).getAsJsonArray();
			for (JsonElement je : arr) {
				JsonObject obj = je.getAsJsonObject();
				String paramName = obj.get("parameter").getAsString();
				res = convertJsonToStr(res, paramName, obj);
			}
		}
		return res;
	}

	private TargetSettingRes convertTargetStrToObjV2(String json, TargetSettingRes res, String retailTarget) {
		if (null != json && !json.isEmpty()) {
			JsonParser parser = new JsonParser();
			JsonArray arr = parser.parse(json).getAsJsonArray();
			for (JsonElement je : arr) {
				JsonObject obj = je.getAsJsonObject();
				String paramName = obj.get("parameter").getAsString();
				res = convertJsonToStrV2(res, paramName, obj, retailTarget);
			}
		}
		return res;
	}

	public String getTeamLeadName(String id) {
		String res = null;
		log.info("TeamLead ID " + id);
		String empNameQuery = "SELECT emp_name FROM dms_employee where emp_id=<ID>;";
		try {
			Object obj = entityManager.createNativeQuery(empNameQuery.replaceAll("<ID>", id)).getSingleResult();
			res = (String) obj;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public String getEmpName(String id) {
		String res = null;
		String empNameQuery = "SELECT emp_name FROM dms_employee where emp_id=<ID>;";
		try {
			if (null != id && !id.equalsIgnoreCase("string")) {
				Object obj = entityManager.createNativeQuery(empNameQuery.replaceAll("<ID>", id)).getSingleResult();
				res = (String) obj;
			} else {
				res = "";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	private String getBranchName(String branchId) {
		log.info("Inside getBranchName,Given Branch ID : " + branchId);
		String res = null;
		String deptQuery = "SELECT name FROM dms_branch where branch_id=<ID>;";
		try {
			if (null != branchId) {
				Object obj = entityManager.createNativeQuery(deptQuery.replaceAll("<ID>", branchId)).getSingleResult();
				res = (String) obj;
			} else {
				res = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	private String getEmpSal(Integer eId) {
		log.info("Inside getEmpSal,Given Branch ID : " + eId);
		String res = null;
		try {
			if (null != eId) {
				Object obj = entityManager.createNativeQuery(getSalForEmp.replaceAll("<ID>", String.valueOf(eId)))
						.getSingleResult();
				res = (String) obj;
			} else {
				res = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	private String getLocationName(String locationId) {
		String res = null;
		String deptQuery = "SELECT location_name FROM dms_location where id=<ID>;";
		try {
			Object obj = entityManager.createNativeQuery(deptQuery.replaceAll("<ID>", locationId)).getSingleResult();
			res = (String) obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	private String getDeptName(String deptId) {

		String res = null;
		String deptQuery = "SELECT hrms_department_id FROM dms_department where dms_department_id=<ID>;";
		try {
			Object obj = entityManager.createNativeQuery(deptQuery.replaceAll("<ID>", deptId)).getSingleResult();
			res = (String) obj;
			System.out.println("Dept ID " + deptId + " is : " + res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	private String calcualteExperience(String prevExperience, String joiningDate)
			throws ParseException, DynamicFormsServiceException {
		log.debug("Inside calcualteExperience ,prevExperience :" + prevExperience + " and joiningDate " + joiningDate);

		// if(prevExperience==null && joiningDate ==null) {
		// throw new DynamicFormsServiceException("Joining Date and Previous experience
		// is NULL for the given employee ", HttpStatus.INTERNAL_SERVER_ERROR);
		// }

		if (!StringUtils.isEmpty(prevExperience) && !StringUtils.isEmpty(joiningDate)) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			Date joining = dateFormat.parse(joiningDate);
			log.debug("todaysDate::" + date + " joining " + joining);
			int diff = getDiffYears(joining, date);
			log.info("Diff date" + diff);
			prevExperience = prevExperience.replaceAll("Y", "").trim();
			Integer totalExp = diff + Integer.valueOf(prevExperience);
			return totalExp + "Y";
		} else {
			return null;
		}
	}

	public static int getDiffYears(Date first, Date last) {
		Calendar a = getCalendar(first);
		Calendar b = getCalendar(last);
		System.out.println("b.get(Calendar.YEAR) " + b.get(Calendar.YEAR) + ":::, " + a.get(Calendar.YEAR));
		int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
		if (a.get(Calendar.DAY_OF_YEAR) > b.get(Calendar.DAY_OF_YEAR)) {
			diff--;
		}

		return diff;
	}

	public static Calendar getCalendar(Date date) {
		Calendar cal = Calendar.getInstance(Locale.US);
		cal.setTime(date);
		return cal;
	}

	public String getDesignationName(String designationId) {
		String res = null;
		String designationQuery = "SELECT designation_name FROM dms_designation where dms_designation_id=<ID>";
		try {
			Object obj = entityManager.createNativeQuery(designationQuery.replaceAll("<ID>", designationId))
					.getSingleResult();
			res = (String) obj;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public TargetSettingRes updateTargetDataWithRole(TargetSettingRes req) {
		log.info("Inside updateTargetDataWithRole()");
		TargetSettingRes res = null;
		try {
			Optional<TargetEntity> teOpt = targetSettingRepo.findById(req.getId());
			if (teOpt.isPresent()) {
				TargetEntity te = teOpt.get();
				Map<String, String> unitsMap = getUnitsFromDbIfExists(te.getTargets());
				log.info("unitsMap::" + unitsMap);
				List<TargetParamReq> list = new ArrayList<>();
				for (String param : paramList) {
					String methodName = "get" + StringUtils.capitalize(param);
					Method getNameMethod = req.getClass().getMethod(methodName);
					String name = (String) getNameMethod.invoke(req); // explicit cast
					list.add(new TargetParamReq(param, name, unitsMap.get(param)));
				}
				TargetEntityUser teUser = modelMapper.map(te, TargetEntityUser.class);
				teUser.setTargets(new Gson().toJson(list));
				teUser.setEmployeeId(req.getEmployeeId());

				log.debug("Emp ID " + req.getEmployeeId() + " StartDate " + req.getStartDate() + " endData: "
						+ req.getEndDate());
				Optional<TargetEntityUser> tesOpt = targetUserRepo.findByEmpIdWithDate(req.getEmployeeId(),
						req.getStartDate(), req.getEndDate());
				if (tesOpt.isPresent()) {
					log.debug("Record present in user ts table");
					TargetEntityUser tes = tesOpt.get();
					teUser.setGeneratedId(tes.getGeneratedId());
				}

				modelMapper.getConfiguration().setAmbiguityIgnored(true);
				teUser.setTeamLeadId(req.getTeamLead());
				teUser.setManagerId(req.getManager());
				teUser.setBranch(req.getBranch());
				teUser.setBranchmangerId(req.getBranchmanger());
				teUser.setGeneralManager(req.getGeneralManagerId());
				teUser.setStartDate(req.getStartDate());
				teUser.setEndDate(req.getEndDate());

				res = modelMapper.map(targetUserRepo.save(teUser), TargetSettingRes.class);
				res.setId(teUser.getGeneratedId());
				res = convertTargetStrToObj(teUser.getTargets(), res);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@Autowired
	DmsBranchDao dmsBranchDao;
	
	@Override
	public TargetSettingRes addTargetDataWithRole(TargetMappingAddReq req) throws DynamicFormsServiceException {
		log.info("Inside addTargetDataWithRole()");
		TargetSettingRes res = null;

		try {
			String empId = req.getEmployeeId();
			Integer retailTarget = parseRetailTarget(req);

			String tmpQuery = dmsEmpByidQuery.replaceAll("<EMP_ID>", String.valueOf(empId));
			tmpQuery = roleMapQuery.replaceAll("<EMP_ID>", String.valueOf(empId));
			List<Object[]> data = entityManager.createNativeQuery(tmpQuery).getResultList();
			TargetRoleRes trRoot = new TargetRoleRes();
			for (Object[] arr : data) {
				trRoot.setOrgId(String.valueOf(arr[0]));
				trRoot.setBranchId(String.valueOf(arr[1]));
				trRoot.setEmpId(String.valueOf(arr[2]));
				trRoot.setRoleName(String.valueOf(arr[3]));
				trRoot.setRoleId(String.valueOf(arr[4]));
			}
			
			TargetSettingRes userDefaultTsRes = null;
			log.debug("addTargetDataWithRole::::"+empId);
			Optional<DmsEmployee> empOpt = dmsEmployeeRepo.findById(Integer.valueOf(empId));
			TargetRoleRes tRole = null;

			if (empOpt.isPresent()) {
				userDefaultTsRes = new TargetSettingRes();
				DmsEmployee emp = empOpt.get();
				tRole = buildTargetRoleRes(trRoot, emp);

			}
			String adminTargets = getAdminTargetString(Integer.parseInt(req.getEmployeeId()));
			log.debug("adminTargets :" + adminTargets);
			String calculatedTargetString = calculateTargets(adminTargets, retailTarget);
			log.debug("calculatedTargetString in add " + calculatedTargetString);

			if (null != calculatedTargetString) {
				TargetEntityUser teUser = new TargetEntityUser();
				teUser.setTargets(calculatedTargetString);
				teUser.setOrgId(trRoot.getOrgId());

				teUser.setStartDate(req.getStartDate());
				teUser.setEndDate(req.getEndDate());

				teUser.setEmployeeId(req.getEmployeeId());

				modelMapper.getConfiguration().setAmbiguityIgnored(true);
				teUser.setTeamLeadId(req.getTeamLeadId());
				teUser.setManagerId(req.getManagerId());
				
				String branchId = req.getBranch();
				log.debug("Input branch ID ,orgmapid "+branchId);
				if (null != branchId) {
					//DmsBranch branch = dmsBranchDao.getBranchByOrgMpId(Integer.parseInt(branchId));
					DmsBranch branch = dmsBranchDao.findById(Integer.parseInt(branchId)).get();
					log.debug("branch:::"+branch);
					if (branch != null) {
						branchId = String.valueOf(branch.getBranchId());
					} else {

						throw new DynamicFormsServiceException("NO VALID BRANCH EXISTS IN DB",
								HttpStatus.INTERNAL_SERVER_ERROR);
					}
					log.debug("branchId::::"+branchId);
					teUser.setBranch(branchId);
				}else {
					throw new DynamicFormsServiceException("NO VALID BRANCH EXISTS IN DB",
							HttpStatus.INTERNAL_SERVER_ERROR);
				}
				
				
				
				teUser.setLocation(tRole.getLocationId());
				teUser.setDesignation(tRole.getDesignationId());
				teUser.setDepartment(tRole.getDeptId());
				teUser.setExperience(tRole.getExperience());
				teUser.setBranchmangerId(req.getBranchmangerId());
				teUser.setGeneralManager(getEmpName(req.getGeneralManagerId()));
				teUser.setStartDate(req.getStartDate());
				teUser.setEndDate(req.getEndDate());
				teUser.setRetailTarget(retailTarget);

				List<TargetEntityUser> list = targetUserRepo.findAllQ3(empId);
				log.debug("Data list for emp id " + list.size());
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

				Date inputStartDate = dateFormat.parse(req.getStartDate());
				Date inputEndDate = dateFormat.parse(req.getEndDate());
				if (inputEndDate.before(inputStartDate)) {
					throw new DynamicFormsServiceException(
							"Date Validation Fails, please verify start date and end date",
							HttpStatus.INTERNAL_SERVER_ERROR);
				}
				boolean startFlag = false;
				boolean endFlag = false;
				log.debug("Inputstartdate " + inputStartDate + ",inputenddate:" + inputEndDate);
				for (TargetEntityUser te : list) {

					Date dbStartDate = dateFormat.parse(te.getStartDate());
					Date dbEndDate = dateFormat.parse(te.getEndDate());
					log.debug("dbStartDate::" + dbStartDate + " ,dbEndDate " + dbEndDate);

					startFlag = dateoverlapvalidation(inputStartDate, dbStartDate, dbEndDate);
					endFlag = dateoverlapvalidation(inputEndDate, dbStartDate, dbEndDate);
					if (startFlag || endFlag)
						break;
					// log.debug("startFlag "+startFlag+", endFlag:"+endFlag);

				}
				;
				log.debug("startFlag:: " + startFlag + ", endFlag:" + endFlag);
				// if (!validateTargetMappingRole(teUser)) {
				if (!startFlag && !endFlag) {
					log.debug("TARGET ROLE DATA DOESNOT EXISTS IN DB");
					teUser.setActive(GsAppConstants.ACTIVE);
					res = modelMapper.map(targetUserRepo.save(teUser), TargetSettingRes.class);

					res.setEmpName(getEmpName(res.getEmployeeId()));
					res.setTeamLead(getEmpName(res.getTeamLeadId()));
					res.setManager(getEmpName(res.getManagerId()));
					res.setBranchmanger(getEmpName(req.getBranchmangerId()));
					res.setGeneralManager(getEmpName(req.getGeneralManagerId()));
					res.setBranchManagerId(req.getBranchmangerId());
					// res.setLocation(tRole.getLocationId());
					res.setBranchName(getBranchName(res.getBranch()));
					res.setLocationName(getLocationName(trRoot.getLocationId()));
					res.setDepartmentName(getDeptName(res.getDepartment()));
					res.setDesignationName(getDesignationName(res.getDesignation()));
					res.setExperience(res.getExperience());
					res.setSalrayRange(res.getSalrayRange());
					res = convertTargetStrToObj(teUser.getTargets(), res);
				}

				else {
					throw new DynamicFormsServiceException("TARGET ROLE DATA  EXISTS IN DB",
							HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}

		} catch (DynamicFormsServiceException e) {
			log.error("saveTargetSettingData() ", e);
			throw e;

		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return res;
	}

	public static boolean dateoverlapvalidation(Date date, Date dateStart, Date dateEnd) {
		if (date != null && dateStart != null && dateEnd != null) {
			if (date.after(dateStart) && date.before(dateEnd)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	private boolean validateTargetMappingRole(TargetEntityUser teUser) {

		List<TargetEntityUser> list = targetUserRepo.findAllQ1(teUser.getEmployeeId(), teUser.getStartDate(),
				teUser.getEndDate());
		if (list != null && !list.isEmpty()) {
			return true;
		}
		return false;
	}

	private Map<String, String> getUnitsFromDbIfExists(String targets) {
		Map<String, String> map = new HashMap<>();
		JsonParser parser = new JsonParser();
		JsonArray arr = parser.parse(targets).getAsJsonArray();
		for (JsonElement je : arr) {
			JsonObject obj = je.getAsJsonObject();
			String paramName = obj.get("parameter").getAsString();
			for (String param : paramList) {
				if (null != paramName && paramName.equalsIgnoreCase(param)) {
					if (obj.has("unit")) {
						map.put(param, obj.get("unit").getAsString());
					}
				}
			}
		}
		return map;
	}

	@Override
	public List<TargetDropDown> getTargetDropdownV2(String orgId, String branchId, String parent, String child,
			String parentId) {
		List<TargetDropDown> list = new ArrayList<>();
		if ((null != parent && parent.equalsIgnoreCase("location"))
				&& (null != child && child.equalsIgnoreCase("branchmanager")) && (null != parentId)) {

			String branchMgrStr = StringUtils.join(branchMgrDesignationList, "\", \"");// Join with ", "
			branchMgrStr = StringUtils.wrap(branchMgrStr, "\"");// Wrap step1 with "

			log.debug("branchMgrStr ::" + branchMgrStr);
			String query = "SELECT emp_id,emp_name FROM dms_employee where location = " + parentId
					+ " and emp_id in \r\n"
					+ "(select rolemap.emp_id FROM dms_employee_role_mapping rolemap where rolemap.role_id in (\r\n"
					+ "SELECT role_id FROM dms_role where role_name in (" + branchMgrStr + ") and org_id=" + orgId
					+ " and branch_id=" + branchId + "));";

			list = buildDropDown(query);
		} else if ((null != parent && parent.equalsIgnoreCase("organization"))
				&& (null != child && child.equalsIgnoreCase("branch")) && (null != parentId)) {
			list = buildDropDown("SELECT branch_id,name FROM dms_branch where organization_id=" + orgId + ";");
		} 
		
		/*else if ((null != parent && parent.equalsIgnoreCase("branch"))
				&& (null != child && child.equalsIgnoreCase("location")) && (null != parentId)) {
			list = buildDropDown("SELECT id,location_name FROM dms_location where branch_id=" + parentId + ";");
		}*/

		else if ((null != parent && parent.equalsIgnoreCase("branch")) && (null != child && child.equalsIgnoreCase("department")) && (null != parentId)) {
			list = buildDropDown("select dms_department_id,department_name from dms_department where branch_id = "+parentId+" and org_id="+orgId);
							
		} else if ((null != parent && parent.equalsIgnoreCase("department"))
				&& (null != child && child.equalsIgnoreCase("designation")) && (null != parentId)) {
			list = buildDropDown(
					"select dms_designation_id,designation_name from dms_designation where dms_designation_id in \r\n"
							+ "(select designation_id from dms_dept_designation_mapping where dept_id = " + parentId
							+ ");");
		} else if ((null != parent && parent.equalsIgnoreCase("designation"))
				&& (null != child && child.equalsIgnoreCase("experience")) && (null != parentId)) {
			list = buildDropDown(
					"select dms_designation_id,designation_name from dms_designation where dms_designation_id in \r\n"
							+ "(select designation_id from dms_dept_designation_mapping where dept_id = " + parentId
							+ ");");
		} else {
			list = buildDropDown(getReportingEmp.replaceAll("<ID>", parentId));

		}
		return list;
	}

	private List<TargetDropDown> buildDropDown(String query) {
		List<TargetDropDown> list = new ArrayList<>();
		List<Object[]> data = entityManager.createNativeQuery(query).getResultList();

		for (Object[] arr : data) {
			TargetDropDown trRoot = new TargetDropDown();
			trRoot.setId(String.valueOf(arr[0]));
			trRoot.setValue(String.valueOf(arr[1]));
			list.add(trRoot);
		}
		return list;
	}

	@Override
	public String deleteTSData(String recordId, String empId) {
		String res = null;
		Optional<TargetEntityUser> opt = targetUserRepo.findByEmpIdWithRecordId(recordId, empId);
		if (opt.isPresent()) {
			TargetEntityUser te = opt.get();
			te.setActive(GsAppConstants.INACTIVE);
			targetUserRepo.save(te);
			res = "{\"SUCCESS\": \"Deleted Succesfully\"}";
		} else {
			res = "{\"INVALID_REQUEST\": \"No data found in DB\"}";
		}
		return res;
	}

	public String getFirstDayOfQurter() {
		// LocalDate localDate = LocalDate.now();
		// LocalDate firstDayOfQuarter =
		// localDate.with(localDate.getMonth().firstMonthOfQuarter()).with(TemporalAdjusters.firstDayOfMonth());
		return LocalDate.ofEpochDay(System.currentTimeMillis() / (24 * 60 * 60 * 1000)).withDayOfMonth(1).toString();
	}

	public String getLastDayOfQurter() {
		/*
		 * LocalDate localDate = LocalDate.now(); LocalDate firstDayOfQuarter =
		 * localDate.with(localDate.getMonth().firstMonthOfQuarter())
		 * .with(TemporalAdjusters.firstDayOfMonth());
		 * 
		 * LocalDate lastDayOfQuarter = firstDayOfQuarter.plusMonths(2)
		 * .with(TemporalAdjusters.lastDayOfMonth());
		 */
		return LocalDate.ofEpochDay(System.currentTimeMillis() / (24 * 60 * 60 * 1000)).plusMonths(1).withDayOfMonth(1)
				.minusDays(1).toString();
	}

	@Override
	public TargetSettingRes editTargetDataWithRoleV2(TargetMappingAddReq req) throws DynamicFormsServiceException {
		log.info("Inside editTargetDataWithRoleV2()");
		TargetSettingRes res = null;
		try {
			Integer retailTarget = parseRetailTarget(req);

			Optional<TargetEntityUser> opt = targetUserRepo.findByEmpIdWithDate(req.getEmployeeId(), req.getStartDate(),
					req.getEndDate());

			String adminTargets = getAdminTargetString(Integer.parseInt(req.getEmployeeId()));
			log.debug("adminTargets :" + adminTargets);
			if (opt.isPresent()) {

				TargetEntityUser te = opt.get();
				String target = calculateTargets(adminTargets, retailTarget);

				te.setTargets(target);

				modelMapper.getConfiguration().setAmbiguityIgnored(true);
				te.setTeamLeadId(req.getTeamLeadId());
				te.setManagerId(req.getManagerId());
				te.setBranch(req.getBranch());
				te.setBranchmangerId(req.getBranchmangerId());
				te.setGeneralManager(getEmpName(req.getGeneralManagerId()));
				te.setStartDate(req.getStartDate());
				te.setEndDate(req.getEndDate());
				te.setRetailTarget(retailTarget);
				res = modelMapper.map(targetUserRepo.save(te), TargetSettingRes.class);

				res.setEmpName(getEmpName(res.getEmployeeId()));
				res.setTeamLead(getEmpName(res.getTeamLeadId()));
				res.setManager(getEmpName(res.getManagerId()));
				res.setBranchmanger(getEmpName(req.getBranchmangerId()));
				res.setGeneralManager(getEmpName(req.getGeneralManagerId()));

				res.setBranchManagerId(req.getBranchmangerId());
				// res.setLocation(req.getLocation());
				res = convertTargetStrToObj(te.getTargets(), res);
			}

		} catch (DynamicFormsServiceException e) {
			log.error("saveTargetSettingData() ", e);
			throw e;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	private Integer parseRetailTarget(TargetMappingAddReq req) throws DynamicFormsServiceException {
		Integer retailTarget = 0;
		if (null != req.getRetailTarget()) {
			retailTarget = Integer.parseInt(req.getRetailTarget());
		} else {
			throw new DynamicFormsServiceException("Retail Target is missing", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.debug("retailTarget to EDIT Is :" + retailTarget);
		return retailTarget;
	}

	private String getAdminTargetString(Integer empId) throws ParseException, DynamicFormsServiceException {
		String adminTargets = null;
		log.debug(dmsEmpByidQuery);
		//TargetRoleRes trRoot = getEmpRoleData(empId);
		TargetRoleRes trRoot = getEmpRoleDataV3(empId);
		TargetRoleRes tRole = new TargetRoleRes();
		
		
		log.debug("TARGET ROLE "+trRoot);
		List<String> orgMapBranchList = trRoot.getOrgMapBranches();
		Optional<DmsEmployee> empOpt = dmsEmployeeRepo.findById(empId);
		if (empOpt.isPresent()) {
			DmsEmployee emp = empOpt.get();
			tRole = buildTargetRoleRes(trRoot, emp);
			
		}
		if(orgMapBranchList!=null) {
			tRole.setBranchId(orgMapBranchList.get(0))
			;
		}

		log.debug("tRole in getAdminTargets:::" + tRole);

		for (TargetEntity te : getTargetSettingMasterDataForGivenRole(tRole)) {
			adminTargets = te.getTargets();
		}
		return adminTargets;
	}

	private String calculateTargets(String adminTargets, Integer retailTarget)
			throws JsonMappingException, JsonProcessingException, DynamicFormsServiceException {
		TargetParamReq[] paramArr=null;
		if(null!=adminTargets) {
		 paramArr = objectMapper.readValue(adminTargets, TargetParamReq[].class);
		 String enquiry = null;
		 for (TargetParamReq param : paramArr) {
			
				if (param.getParameter().equalsIgnoreCase("enquiry")) {
					enquiry = calculateEnquiry(retailTarget, param.getTarget(), param.getUnit());
					param.setTarget(enquiry);
				}
			 
		 }
		for (TargetParamReq param : paramArr) {
			
			if (param.getParameter().equalsIgnoreCase("testDrive")) {
				param.setTarget(calculateBooking(enquiry, param.getTarget(), param.getUnit()));
			}
			if (param.getParameter().equalsIgnoreCase("homeVisit")) {
				param.setTarget(calculateBooking(enquiry, param.getTarget(), param.getUnit()));
			}
			if (param.getParameter().equalsIgnoreCase("booking")) {
				param.setTarget(calculateBooking(enquiry, param.getTarget(), param.getUnit()));
			}
			if (param.getParameter().equalsIgnoreCase("exchange")) {
				param.setTarget(calculateEnquiry(retailTarget, param.getTarget(), param.getUnit()));
			}
			if (param.getParameter().equalsIgnoreCase("finance")) {
				param.setTarget(calculateEnquiry(retailTarget, param.getTarget(), param.getUnit()));
			}
			if (param.getParameter().equalsIgnoreCase("insurance")) {
				param.setTarget(calculateEnquiry(retailTarget, param.getTarget(), param.getUnit()));
			}
			if (param.getParameter().equalsIgnoreCase("exWarranty")) {
				param.setTarget(calculateEnquiry(retailTarget, param.getTarget(), param.getUnit()));
			}
			if (param.getParameter().equalsIgnoreCase("accessories")) {
				param.setTarget(calculateAccessories(retailTarget, param.getTarget(), param.getUnit()));
			}
			if (param.getParameter().equalsIgnoreCase("events")) {
				param.setTarget(calculateEnquiry(retailTarget, param.getTarget(), param.getUnit()));
			}
		}
		}else {
			throw new DynamicFormsServiceException("Target Admin data with for Given user does not exists in DB",
					HttpStatus.BAD_REQUEST);
		}
		return new Gson().toJson(paramArr);
	}

	private String calculateHomeVisit(String enquiry, String target, String unit) {
		if (null != enquiry && unit.equalsIgnoreCase(PERCENTAGE)) {
			Integer t = Integer.parseInt(target);
			Integer enq = Integer.parseInt(enquiry);
			Double perc = 0D;
			if (t > 0) {
				perc = (double) enq * (t / 100);
			}
			log.debug("Calculated TestDrive for ENQUIRY " + enquiry + " and HOMEVISIT is  " + perc);
			return String.format("%.1f", perc);
		} else {
			return target;
		}
	}
	private String calculateAccessories(Integer retailTarget, String target, String unit) {
		if (null != retailTarget) {
			Integer t = Integer.parseInt(target);
			Integer perc = t*retailTarget;
			return String.valueOf(perc);
		} else {
			return target;
		}
	}

	private String calculateTestDrive(String enquiry, String target, String unit) {
		if (null != enquiry && unit.equalsIgnoreCase(PERCENTAGE)) {
			Integer t = Integer.parseInt(target);
			Integer enq = Integer.parseInt(enquiry);
			Integer perc = 0;
			if (t > 0) {
				perc = enq * (t / 100);
			}
			log.debug("Calculated TestDrive for ENQUIRY " + enquiry + " and Testdrive is  " + perc);
			return String.format("%.0f", perc);
		} else {
			return target;
		}
	}

	private String calculateEnquiry(Integer retailTarget, String target, String unit) {
		if (unit.equalsIgnoreCase(PERCENTAGE)) {
			Double t = Double.valueOf(target);
			Double perc = 0D;
			System.out.println("t " + t + " retailTarget " + retailTarget);
			if (t > 0) {
				Double p = t / 100;
				System.out.println(" P :::" + p);
				perc = (t / 100) * retailTarget;
			}
			log.debug("Calculated Enquiry for target " + target + " and retailTarget " + retailTarget + " is " + perc);
			return String.format("%.0f", perc);
		} else {
			return target;
		}
	}
	
	private String calculateBooking( String enqTarget,String bookingTarget,String unit) {
		log.debug("callingg calculateBooking");;
		if (unit.equalsIgnoreCase(PERCENTAGE) && bookingTarget!=null && enqTarget!=null) {
			Double t = Double.valueOf(bookingTarget);
			Double e = Double.valueOf(enqTarget);
			Double perc = 0D;
			//System.out.println("t " + t + " retailTarget " + retailTarget);
			if (t > 0) {
				Double p = t / 100;
				System.out.println(" P :::" + p);
				perc = (t / 100) * e;
			}
		log.debug("Calculated Enquiry for target " + bookingTarget + " and enqTarget " + enqTarget + " is " + perc);
			return String.format("%.0f", perc);
		} else {
			return bookingTarget;
		}
	}

	@Override
	public List<TargetSettingRes> searchTargetMappingData(TargetSearch request) {
		log.debug("Inside searchTargetMappingData()");
		List<TargetSettingRes> list = new ArrayList<>();
		try {
			String empId = null;
			if (null != request.getEmpId()) {
				empId = request.getEmpId();
			} else if (null == request.getEmpId() && null != request.getEmpName()) {
				empId = getEmpIdByName(request.getEmpName());
			}

			String startDate = request.getStartDate();
			String endDate = request.getEndDate();

			log.info("empId " + empId + " starDate " + startDate + " endDate " + endDate);
			List<TargetEntityUser> teUserDbList = new ArrayList<>();
			if (null != empId && null != startDate && null != endDate) {
				teUserDbList = targetUserRepo.findAllQ1(empId, startDate, endDate);
			}
			if (null != empId && null != startDate && null == endDate) {
				teUserDbList = targetUserRepo.findAllQ2(empId, startDate);
			}
			if (null != empId && null == startDate && null == endDate) {
				teUserDbList = targetUserRepo.findAllQ3(empId);
			}

			for (TargetEntityUser te : teUserDbList) {
				modelMapper.getConfiguration().setAmbiguityIgnored(true);
				TargetSettingRes tsres = modelMapper.map(te, TargetSettingRes.class);
				tsres.setId(te.getGeneratedId());
				tsres = convertTargetStrToObj(te.getTargets(), tsres);

				if (null != request.getTeamLeadId()) {
					tsres.setTeamLead(getTeamLeadName(request.getTeamLeadId()));
					tsres.setTeamLeadId(request.getTeamLeadId());
				}
				if (null != request.getManagerId()) {
					tsres.setManager(getEmpName(request.getManagerId()));
					tsres.setManagerId(request.getManagerId());
				}
				if (null != request.getBranchmangerId()) {
					tsres.setBranchManagerId(request.getBranchmangerId());
					tsres.setBranchmanger(getEmpName(request.getBranchmangerId()));
				}
				if (null != request.getGeneralManagerId()) {
					tsres.setGeneralManagerId(request.getGeneralManagerId());
					tsres.setGeneralManager(getEmpName(request.getGeneralManagerId()));
				}
				list.add(tsres);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("searchTargetMappingData() ", e);

		}
		return list;
	}

	private String getEmpIdByName(String id) {
		String res = null;
		String empNameQuery = "SELECT emp_id FROM dms_employee where emp_name=<ID>;";
		try {
			if (null != id && !id.equalsIgnoreCase("string")) {
				Object obj = entityManager.createNativeQuery(empNameQuery.replaceAll("<ID>", id)).getSingleResult();
				res = (String) obj;
			} else {
				res = "";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public boolean validateTL(String roleName) {
		boolean flag = false;
		if (tlDesignationList.contains(roleName)) {
			flag = true;
		}
		return flag;
	}

	public boolean validateDSE(String roleName) {
		boolean flag = false;
		log.debug("dseDesignationList " + dseDesignationList);
		if (dseDesignationList.contains(roleName)) {
			flag = true;
		}
		return flag;
	}

	public boolean validateMgr(String roleName) {
		boolean flag = false;
		if (mgrDesignationList.contains(roleName)) {
			flag = true;
		}
		return flag;
	}

	public boolean validateBranchMgr(String roleName) {
		boolean flag = false;
		if (branchMgrDesignationList.contains(roleName)) {
			flag = true;
		}
		return flag;
	}

	public boolean validateGeneralMgr(String roleName) {
		boolean flag = false;
		if (GMDesignationList.contains(roleName)) {
			flag = true;
		}
		return flag;
	}

	@Autowired
	OHServiceImpl ohServiceImpl;
	
	@Override
	public Map<String, String> getEmployeeRole(Integer empId) throws DynamicFormsServiceException {
		//TargetRoleRes res = getEmpRoleData(empId);
		Map<String, String> map = new HashMap<>();
		
		Optional<DmsEmployee> empOpt = dmsEmployeeRepo.findEmpById(empId);
		if(empOpt.isPresent()) {
		DmsEmployee emp = empOpt.get();
		Integer empDesId = Integer.parseInt(emp.getDesignationId());
		log.debug("empDesigntaion:::" + empDesId);
		Optional<DmsDesignation> desOpt = dmsDesignationRepo.findById(empDesId);
		Integer empLevel=0;
		if (desOpt.isPresent()) {
			empLevel = desOpt.get().getLevel();
		} 

		log.debug("Given emp level is " + empLevel);
		map.put("role", ohServiceImpl.getLevelName(empLevel));
		
		}else {
			map.put("role", "");
		}
		
	
		return map;
	}

	public String getEmployeeRoleV2(Integer empId) {
		TargetRoleRes res = null;
		try {
			res = getEmpRoleData(empId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getFormattedRole(res.getRoleName());
	}

	private String getFormattedRole(String roleName) {
		log.debug("input role " + roleName);
		if (validateDSE(roleName))
			return "DSE";
		else if (validateTL(roleName))
			return "TL";
		else if (validateMgr(roleName))
			return "Manager";
		else if (validateBranchMgr(roleName))
			return "Dept Heads";
		else if (validateGeneralMgr(roleName))
			return "President , Vice President";
		else
			return roleName;
	
	}

	@Override
	public String deleteAdminTargetMapping(Integer recordId) {
		String res = null;
		Optional<TargetEntity> opt = targetSettingRepo.findById(recordId);
		if (opt.isPresent()) {
			TargetEntity te = opt.get();
			te.setActive(GsAppConstants.INACTIVE);
			targetSettingRepo.save(te);
			res = "{\"SUCCESS\": \"Deleted Succesfully\"}";
		} else {
			res = "{\"INVALID_REQUEST\": \"No data found in DB\"}";
		}
		return res;
	}

	@Override
	public TargetSettingRes updateTargetSettingDataV2(TSAdminUpdateReq request) throws DynamicFormsServiceException {

		log.debug("Inside updateTargetSettingDataV2()");
		TargetEntity dbRes = null;
		TargetSettingRes ts = null;
		try {
			TargetEntity te = new TargetEntity();
			te.setBranch(request.getBranch());
			// te.setLocation(request.getLocation());
			te.setDepartment(request.getDepartment());
			te.setDesignation(request.getDesignation());
			te.setExperience(request.getExperience());
			te.setSalrayRange(request.getSalrayRange());
			te.setOrgId(request.getOrgId());
			String salRange = request.getSalrayRange();

			String minSal = "";
			String maxSal = "";
			if (salRange.contains("-")) {
				String tmp[] = salRange.split("-");
				minSal = tmp[0];
				minSal = StringUtils.replaceIgnoreCase(minSal, "k", "").trim();
				maxSal = tmp[1];
				maxSal = StringUtils.replaceIgnoreCase(maxSal, "k", "").trim();

			} else {
				minSal = salRange;
				minSal = StringUtils.replaceIgnoreCase(minSal, "k", "").trim();
			}
			te.setMaxSalary(maxSal);
			te.setMinSalary(minSal);
			List<Target> list = request.getTargets();
			log.debug("Before Targets " + list);
			// list = updatedTargetValues(list);
			// log.debug("After updating Targets "+list);
			if (null != list) {
				te.setTargets(gson.toJson(list));
			}
			if (validateTargetAdminData(te)) {
				log.debug("TARGET ADMIN DATA EXISTS IN DB");

				int recordId = request.getId();
				Optional<TargetEntity> dbRecordOpt = targetSettingRepo.findById(recordId);

				if (dbRecordOpt.isPresent()) {

					TargetEntity dbRecord = dbRecordOpt.get();
					te.setId(dbRecord.getId());
					dbRes = targetSettingRepo.save(te);
					String targets = dbRes.getTargets();

					ts = modelMapper.map(dbRes, TargetSettingRes.class);
					ts = convertTargetStrToObj(targets, ts);
					ts.setBranchName(getBranchName(te.getBranch()));
					// ts.setLocationName(getLocationName(te.getLocation()));
					ts.setDepartmentName(getDeptName(te.getDepartment()));
					ts.setDesignationName(getDesignationName(te.getDesignation()));
					ts.setExperience(te.getExperience());
					ts.setSalrayRange(te.getSalrayRange());
				}

				else {
					throw new DynamicFormsServiceException("Target Admin data with Given ID does not exists in DB",
							HttpStatus.BAD_REQUEST);
				}

			} else {
				throw new DynamicFormsServiceException("TARGET ADMIN DOES NOT DATA  EXISTS IN DB",
						HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} catch (DynamicFormsServiceException e) {
			log.error("saveTargetSettingData() ", e);
			throw e;

		} catch (Exception e) {
			log.error("saveTargetSettingData() ", e);
			throw new DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return ts;

	}

	@Override
	public TSAdminUpdateReq getTargetSettingAdminById(int id) throws DynamicFormsServiceException {
		TargetEntity dbRecord = null;
		TSAdminUpdateReq res = null;
		try {

			Optional<TargetEntity> dbRecordOpt = targetSettingRepo.findById(id);

			if (dbRecordOpt.isPresent()) {
				dbRecord = dbRecordOpt.get();
				res = modelMapper.map(dbRecord, TSAdminUpdateReq.class);
				TargetParamReq[] params = objectMapper.readValue(dbRecord.getTargets(), TargetParamReq[].class);
				List list = Arrays.asList(params);
				res.setTargets(list);
			} else {
				throw new DynamicFormsServiceException("Target Admin data with Given data does not exists in DB",
						HttpStatus.BAD_REQUEST);
			}
		} catch (DynamicFormsServiceException e) {
			log.error("getTargetSettingAdminById() ", e);
			throw e;

		} catch (Exception e) {
			log.error("getTargetSettingAdminById() ", e);
			throw new DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return res;
	}

	public String getOrgName(String id) {
		String res = null;
		String designationQuery = "select name from dms_organization where org_id=<ID>";
		try {
			if (null != id) {
				Object obj = entityManager.createNativeQuery(designationQuery.replaceAll("<ID>", id)).getSingleResult();
				res = (String) obj;
			} else {
				res = "";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public TargetRoleRes getEmpRoleDataV2(int empId) throws DynamicFormsServiceException {

		String tmpQuery = dmsEmpByidQuery.replaceAll("<EMP_ID>", String.valueOf(empId));

		tmpQuery = roleMapQuery.replaceAll("<EMP_ID>", String.valueOf(empId));
		List<Object[]> data = entityManager.createNativeQuery(tmpQuery).getResultList();
		TargetRoleRes trRoot = new TargetRoleRes();
		for (Object[] arr : data) {
			trRoot.setOrgId(String.valueOf(arr[0]));
			trRoot.setRoleName(String.valueOf(arr[3]));
			trRoot.setRoleId(String.valueOf(arr[4]));
			trRoot.setBranchId(String.valueOf(arr[1]));
		}

		return trRoot;
	}

	private List<TargetEntity> getUserTargetData(TargetRoleRes tRole) throws DynamicFormsServiceException {
		log.debug("Inside getTargetSettingMasterDataForGivenRole,for tRole");
		log.debug("tRole:::" + tRole);
		List<TargetEntity> finalList = new ArrayList<>();
		System.out.println("tRole.getDesignationId() " + tRole.getDesignationId());
		System.out.println("tRole.getOrgId()::" + tRole.getOrgId());
		System.out.println("tRole.getBranchId()::" + tRole.getBranchId());
		System.out.println("tRole.getLocationId()::" + tRole.getLocationId());
		System.out.println("tRole.getDeptId()::" + tRole.getDeptId());
		System.out.println("tRole.getDesignationId()::" + tRole.getDesignationId());

		List<TargetEntityUser> userTargetList = targetUserRepo.getUserTargetData(tRole.getOrgId(), tRole.getDeptId(),
				tRole.getDesignationId(), tRole.getBranchId());
		// List<TargetEntity> dbList =
		// targetSettingRepo.getTargetmappingDataWithOutExpSalV2(tRole.getOrgId(),
		// tRole.getBranchId(), tRole.getLocationId(), tRole.getDeptId(),
		// tRole.getDesignationId());
		List<TargetEntity> dbList = targetSettingRepo.getTargetmappingDataWithOutExpSalV2(tRole.getOrgId(),
				tRole.getDeptId(), tRole.getDesignationId(), tRole.getBranchId());
		// tRole.getBranchId(), tRole.getLocationId(), tRole.getDeptId(),
		// tRole.getDesignationId());

		log.debug("dbList size::::::: :" + dbList.size());
		log.debug("dbList " + dbList);
		String salRange = tRole.getSalary();
		if (null != salRange) {
			// throw new DynamicFormsServiceException("Salary Details of Employees are
			// missing", HttpStatus.INTERNAL_SERVER_ERROR);
			salRange = StringUtils.replaceIgnoreCase(salRange, "k", "");
			salRange = salRange.trim();
			Integer sal = Integer.valueOf(salRange);
			log.info("Sal range of emp " + tRole.getEmpId() + " is " + salRange);

			for (TargetEntity te : dbList) {
				if (null != te.getSalrayRange() && null != te.getExperience() && null != te.getMinSalary()
						&& null != te.getMaxSalary()) {
					Integer minSal = Integer.valueOf(te.getMinSalary());
					Integer maxSal = Integer.valueOf(te.getMaxSalary());
					log.info("minSal::" + minSal + " maxSal " + maxSal);
					if ((minSal <= sal) && (sal <= maxSal)) {
						finalList.add(te);
					}
				}
			}
		}
		if (finalList.isEmpty()) {

			log.debug("FinalList is empty,Fetching adming config from NO Sal & Exp");
			for (TargetEntity te : dbList) {
				if (null == te.getSalrayRange() || null == te.getExperience()) {
					finalList.add(te);
				}
			}
		}
		if (finalList.size() > 1) {
			finalList = finalList.subList(0, 1);
		}
		log.debug("finalList " + finalList);
		log.debug("finalList size " + finalList.size());
		return finalList;
	}

	public List<TargetSettingRes> getTSDataForRoleV3(Integer empId) throws DynamicFormsServiceException {
		log.debug("Inside getTSDataForRoleV3()");
		List<TargetSettingRes> list = new ArrayList<>();
		TargetRoleRes tRole = getEmpRoleDataV3(empId);
		try {
			log.debug("tRole.getOrgMapBranches()::"+tRole.getOrgMapBranches());			

			for (String orgMapBranchID : tRole.getOrgMapBranches()) {
				log.debug("orgMapBranchID::"+orgMapBranchID);
				
				log.debug("tRole.getDesignationId() " + tRole.getDesignationId());
				log.debug("tRole.getOrgId()::" + tRole.getOrgId());
				log.debug("tRole.getBranchId()::" + tRole.getBranchId());
				log.debug("tRole.getLocationId()::" + tRole.getLocationId());
				
				
				List<TargetEntityUser> userTargetList = targetUserRepo.getUserTargetData(tRole.getOrgId(),
						tRole.getDeptId(), tRole.getDesignationId(), orgMapBranchID);
				tRole.setBranchId(orgMapBranchID);
				tRole.setLocationId(orgMapBranchID);
				log.info("userTargetListis not empty " + userTargetList.size());
				for (TargetEntityUser teUser : userTargetList) {
					log.debug("TargetEntityUser:::"+teUser);
					log.debug("user targets "+teUser.getTargets());
					modelMapper.getConfiguration().setAmbiguityIgnored(true);
					TargetSettingRes tsRes = modelMapper.map(teUser, TargetSettingRes.class);
					log.debug("tsRes:::"+tsRes);
					tsRes = convertTargetStrToObj(teUser.getTargets(), tsRes);
					tsRes.setEmpName(getEmpName(tRole.getEmpId()));
					tsRes.setEmployeeId(tRole.getEmpId());
					tsRes.setId(teUser.getGeneratedId());

					if (null != tRole.getLocationId()) {
						tsRes.setLocationName(getLocationName(tRole.getLocationId()));
					}
					if (null != tRole.getBranchId()) {
						tsRes.setBranchName(getBranchName(tRole.getBranchId()));
					}
					if (null != tRole.getDeptId()) {
						tsRes.setDepartmentName(getDeptName(tRole.getDeptId()));
					}
					if (null != tRole.getDesignationId()) {
						tsRes.setDesignationName(getDesignationName(tRole.getDesignationId()));
					}
					list.add(tsRes);
				}

			}
			System.out.println("list in getTSDataForRoleV2  " + list);

		} catch (Exception e) {
			log.error("getTargetSettingData() ", e);
		}
		return list;
	}
	/*
	 * public List<TargetSettingRes> getTSDataForRoleV3(Integer empId) throws
	 * DynamicFormsServiceException { log.debug("Inside getTSDataForRoleV2()");
	 * TargetRoleRes tRole = getEmpRoleDataV3(empId); List<TargetSettingRes> list =
	 * new ArrayList<>(); try {
	 * 
	 * for(String orgMapBranchID : tRole.getOrgMapBranches()) {
	 * tRole.setBranchId(orgMapBranchID); tRole.setLocationId(orgMapBranchID);
	 * log.debug("trole :::"+tRole.toString());
	 * list.addAll(getTSDataForRoleV2(tRole,null,null,null,null)); /* for
	 * (TargetEntity te : getTargetSettingMasterDataForGivenRole(tRole)) {
	 * List<TargetEntityUser> tesUserList =
	 * targetUserRepo.findAllEmpIds(tRole.getEmpId()); if (null != tesUserList &&
	 * !tesUserList.isEmpty()) { log.info("tesUserList is not empty " +
	 * tesUserList.size()); for (TargetEntityUser teUser : tesUserList) {
	 * modelMapper.getConfiguration().setAmbiguityIgnored(true); TargetSettingRes
	 * tsRes = modelMapper.map(teUser, TargetSettingRes.class); tsRes =
	 * convertTargetStrToObj(teUser.getTargets(), tsRes);
	 * tsRes.setEmpName(getEmpName(String.valueOf(empId)));
	 * tsRes.setEmployeeId(String.valueOf(empId));
	 * tsRes.setId(teUser.getGeneratedId());
	 * 
	 * list.add(tsRes); }
	 * 
	 * }
	 * 
	 * } }
	 * 
	 * System.out.println("list in getTSDataForRoleV2  " + list);
	 * 
	 * } catch (Exception e) { log.error("getTargetSettingData() ", e); } return
	 * list;
	 * 
	 * }
	 */

	public TargetRoleRes getEmpRoleDataV3(int empId) throws DynamicFormsServiceException {

		String tmpQuery = dmsEmpByidQuery.replaceAll("<EMP_ID>", String.valueOf(empId));

		tmpQuery = roleMapQuery.replaceAll("<EMP_ID>", String.valueOf(empId));
		List<Object[]> data = entityManager.createNativeQuery(tmpQuery).getResultList();
		TargetRoleRes trRoot = new TargetRoleRes();
		for (Object[] arr : data) {
			trRoot.setOrgId(String.valueOf(arr[0]));
			trRoot.setBranchId(String.valueOf(arr[1]));
			trRoot.setEmpId(String.valueOf(arr[2]));
			trRoot.setRoleName(String.valueOf(arr[3]));
			trRoot.setRoleId(String.valueOf(arr[4]));
			trRoot.setPrecedence(Integer.parseInt(arr[5].toString()));
		}

		String branchQuery = "select branch_id,name from dms_branch where org_map_id in  (select location_node_data_id from emp_location_mapping where emp_id="
				+ empId + ");";
		
		//String branchQuery = "select location_node_data_id,org_id from emp_location_mapping where emp_id="+empId;
		List<Object[]> branchdata = entityManager.createNativeQuery(branchQuery).getResultList();
		List<String> orgMapBranchIds = new ArrayList<>();

		for (Object[] arr : branchdata) {

			orgMapBranchIds.add(String.valueOf(arr[0]));
		}
		log.debug("orgMapBranchIds::" + orgMapBranchIds);
		trRoot.setOrgMapBranches(orgMapBranchIds);

		Optional<DmsEmployee> empOpt = dmsEmployeeRepo.findById(empId);
		DmsEmployee emp = null;
		if (empOpt.isPresent()) {
			emp = empOpt.get();
			trRoot = buildTargetRoleRes(trRoot, emp);
		} else {
			throw new DynamicFormsServiceException("No Empoloyee with given empId in DB", HttpStatus.BAD_REQUEST);

		}
		log.debug("trRoot " + trRoot);

		return trRoot;
	}

}
