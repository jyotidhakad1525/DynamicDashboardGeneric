package com.automate.df.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.automate.df.dao.AuditEmpMappingDAO;
import com.automate.df.dao.AuditHistoryDao;
import com.automate.df.dao.AuditMasterDAO;
import com.automate.df.entity.AuditEmpMapping;
import com.automate.df.entity.AuditHistory;
import com.automate.df.entity.AuditMaster;
import com.automate.df.exception.DynamicFormsServiceException;
import com.automate.df.model.AuditHistoryRes;
import com.automate.df.model.AuditMasterRes;
import com.automate.df.model.AuditMasterSaveReq;
import com.automate.df.model.AuditSearchReq;
import com.automate.df.model.AuditTrailReq;
import com.automate.df.service.AuditService;
import com.automate.df.util.FlatMapUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuditServiceImpl implements AuditService {

	@Autowired
	AuditMasterDAO auditMasterDAO;

	@Autowired
	AuditEmpMappingDAO auditEmpMappingDAO;

	@Autowired
	AuditHistoryDao auditHistoryDao;

	@Override
	public List<AuditMasterRes> getAuditMasterDataWithMapping(String orgId)
			throws DynamicFormsServiceException {
		List<AuditMasterRes> list = new ArrayList<>();
		try {
			if (null != orgId ) {
				log.debug("In IF loop of getAuditMasterDataWithMapping");
				Map<Integer, AuditMaster> m = getActivityName();
				List<AuditEmpMapping> dbList = auditEmpMappingDAO.getMappingsByOrgEmpId(Integer.parseInt(orgId));

				dbList.forEach(x -> {
					AuditMasterRes res = new AuditMasterRes();
					res.setId(x.getAuditWfId());
					AuditMaster audit = m.get(x.getAuditWfId());
					res.setAuditWfName(audit.getActivityName());
					res.setActivityType(audit.getActivityType());
					res.setTaskName(audit.getTaskName());
					res.setEnabled(true);
					list.add(res);
				});

				List<AuditMasterRes> tmpList = new ArrayList<>();
				m.forEach((k, v) -> {
					AuditMasterRes res = new AuditMasterRes();
					res.setId(k);
					AuditMaster audit = m.get(k);
					res.setAuditWfName(audit.getActivityName());
					res.setActivityType(audit.getActivityType());
					res.setTaskName(audit.getTaskName());
					res.setEnabled(false);
					List<Integer> l = list.stream().map(AuditMasterRes::getId).collect(Collectors.toList());
					if (!l.contains(k)) {
						tmpList.add(res);
					}
				});

				list.addAll(tmpList);

			} else {
				log.debug("In Else loop of getAuditMasterDataWithMapping");
				List<AuditMaster> dbList = auditMasterDAO.findAll();
				dbList.forEach(x -> {
					AuditMasterRes res = new AuditMasterRes();
					res.setId(x.getId());
					res.setAuditWfName(x.getActivityName());
					res.setActivityType(x.getActivityType());
					res.setTaskName(x.getTaskName());
					res.setEnabled(false);
					list.add(res);
				});
			}
			list.sort(Comparator.comparing(AuditMasterRes::getId));
		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return list;
	}

	private Map<Integer, AuditMaster> getActivityName() {
		Map<Integer, AuditMaster> map = new HashMap<>();
		auditMasterDAO.findAll().forEach(x -> {
			map.put(x.getId(), x);
		});
		return map;
	}

	@Override
	public List<AuditEmpMapping> mapEmployes(List<AuditMasterSaveReq> req) throws DynamicFormsServiceException {
		List<AuditEmpMapping> list = new ArrayList<>();
		try {
			req.forEach(x -> {
				AuditEmpMapping empMap = new AuditEmpMapping();
				empMap.setOrgId(x.getOrgId());
				//empMap.setEmpId(x.getEmpId());
				empMap.setAuditWfId(x.getAuditWfId());
				empMap.setUpdatedAt(LocalDateTime.now().toString());
				boolean flag = x.isEnabled();
				log.debug("Enable flag " + flag + " for wf " + x.getAuditWfId());
				List<AuditEmpMapping> dbList = auditEmpMappingDAO.verifyMapping(x.getOrgId(), x.getAuditWfId());
				log.debug("dbList in audit " + dbList);
				if (!flag) {
					auditEmpMappingDAO.deleteAllById(dbList.stream().map(f -> f.getId()).collect(Collectors.toList()));
				} else {
					if (null != dbList && dbList.isEmpty()) {
						list.add(auditEmpMappingDAO.save(empMap));
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return list;
	}

	@Override
	public Object saveAuditData(AuditTrailReq req) throws DynamicFormsServiceException {
		AuditHistory res = new AuditHistory();
		try {

			res = compareJson(req);

		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return res;
	}

	private AuditHistory compareJson(AuditTrailReq req) {
		AuditHistory res = new AuditHistory();
		try {

			String leftJson = new Gson().toJson(req.getBeforeJson());
			String rightJson = new Gson().toJson(req.getAfterJson());

			JsonElement element = new Gson().fromJson(leftJson, JsonElement.class);
			JsonObject jsonObj = element.getAsJsonObject();
			String universalId = "";
			String leadId = "";
			if (jsonObj.has("dmsLeadDto")) {
				JsonElement ele = jsonObj.get("dmsLeadDto");
				if (null != ele) {
					JsonObject leadJsonObj = ele.getAsJsonObject();
					if (leadJsonObj.has("crmUniversalId")) {
						universalId = leadJsonObj.get("crmUniversalId").toString();
						if (null != universalId && universalId.startsWith("\"") && universalId.endsWith("\"")) {
							universalId = universalId.replaceAll("^\"|\"$", "");
						}
					}
					if (leadJsonObj.has("id")) {
						leadId = leadJsonObj.get("id").toString();
					}
					log.debug("Given JSON has universalID and LeadId");

				}
			}
			log.debug("universalId:" + universalId + ",leadId:" + leadId);

			ObjectMapper mapper = new ObjectMapper();
			TypeReference<HashMap<String, Object>> type = new TypeReference<HashMap<String, Object>>() {
			};

			Map<String, Object> leftMap = mapper.readValue(leftJson, type);
			Map<String, Object> rightMap = mapper.readValue(rightJson, type);

			Map<String, Object> leftFlatMap = FlatMapUtil.flatten(leftMap);
			Map<String, Object> rightFlatMap = FlatMapUtil.flatten(rightMap);

			MapDifference<String, Object> difference = Maps.difference(leftFlatMap, rightFlatMap);

			/*
			 * System.out.println("Entries only on the left\n--------------------------");
			 * difference.entriesOnlyOnLeft() .forEach((key, value) ->
			 * System.out.println(key + ": " + value));
			 * 
			 * System.out.
			 * println("\n\nEntries only on the right\n--------------------------");
			 * difference.entriesOnlyOnRight() .forEach((key, value) ->
			 * System.out.println(key + ": " + value));
			 */
			log.debug("\n\nEntries differing\n--------------------------");
			difference.entriesDiffering().forEach((key, value) -> log.debug(key + ": " + value));

			ObjectMapper om = new ObjectMapper();
			om.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			JsonNode jsonDifference = om.valueToTree(difference.entriesDiffering());
			String diff = jsonDifference.toPrettyString();

			log.debug("JSON Diff " + diff);

			AuditHistory aud = new AuditHistory();

			aud.setStageName(req.getStageName());
			aud.setTaskName(req.getTaskName());
			aud.setUpdatedEmpID(req.getEmpId());
			aud.setUpdatedEmpName(req.getEmpName());
			aud.setOrgId(req.getOrgId());
			aud.setLeadId(leadId);
			aud.setUniversalId(universalId);
			java.util.Date dt = new java.util.Date();
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			aud.setUpdatedAt(sdf.format(dt));
			aud.setData(diff);
			res = auditHistoryDao.save(aud);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@Autowired
	Gson gson;

	@Override
	public List<AuditHistoryRes>  getAuditDataById(AuditSearchReq req)
			throws DynamicFormsServiceException {
		log.debug("Inside getAuditDataById() {}");
		List<AuditHistoryRes> resList = new ArrayList<>();
		
		String leadId = req.getLeadId();
		String universalId = req.getUniversalId();
		String startDate =req.getStartDate();
		String endDate = req.getEndDate();
		String empId = req.getEmpId();
		try {
			if(validateString(startDate)) {
			startDate = startDate +" 00:00:00";
			}
			if(validateString(endDate)) {
			endDate = endDate+" 23:59:59";
			}
			List<AuditHistory> auditHistoryList = new ArrayList<>();
			if ((validateString(universalId) || validateString(leadId)) && validateString(startDate) && validateString(endDate)) {
				log.debug("In first loop of audit search");
				if(validateString(universalId)) {
					auditHistoryList = auditHistoryDao.getAuditDataById(universalId,startDate,endDate);
				}else {
					auditHistoryList = auditHistoryDao.getAuditDataByIdLead(leadId,startDate,endDate);
				}
				log.debug("Size of auditHistoryList in first loop "+auditHistoryList.size());;
			}
			else if ((validateString(universalId) || validateString(leadId)) && (validateString(startDate) && !validateString(endDate))) {
				log.debug("In second loop of audit search");
				if(validateString(universalId)) {
					auditHistoryList = auditHistoryDao.getAuditDataByIdV2(universalId,startDate);
				}else {
					auditHistoryList = auditHistoryDao.getAuditDataByIdV2Lead(leadId,startDate);
				}
			}
			else if ((validateString(universalId) || validateString(leadId)) && !validateString(startDate) && !validateString(endDate) ) {
				log.debug("In third loop of audit search");
				auditHistoryList = auditHistoryDao.getAuditDataByIdV3(leadId,universalId);
			}
			else if (!validateString(universalId) && !validateString(leadId) && validateString(startDate) && validateString(endDate) ) {
				log.debug("In fourth loop of audit search");
				auditHistoryList = auditHistoryDao.getAuditDataByIdV4(startDate,endDate);
			}
			else if (!validateString(universalId) && !validateString(leadId) && validateString(startDate) && !validateString(endDate) ) {
				log.debug("In fifth loop of audit search");
				auditHistoryList = auditHistoryDao.getAuditDataByIdV5(startDate);
			}
			else if (!validateString(universalId) && !validateString(leadId) && !validateString(startDate) && !validateString(endDate) ) {
				log.debug("In SIXTH loop of audit search");
				auditHistoryList = auditHistoryDao.findAll();
			}
			
			if(validateString(empId)) {
				log.debug("In employee Loop");
				auditHistoryList = auditHistoryList.stream().filter(x->
				(StringUtils.containsIgnoreCase(x.getUpdatedEmpID(), empId) || StringUtils.containsIgnoreCase(x.getUpdatedEmpName(), empId))).
				collect(Collectors.toList());
			}
			
			if(validateString(req.getStage())) {
				log.debug("In Stage Loop");
				auditHistoryList = auditHistoryList.stream().filter(x->(validateString(x.getStageName()) && x.getStageName().equalsIgnoreCase(req.getStage()))).collect(Collectors.toList());
			}
			
			resList = buildAuditHistory(auditHistoryList);
			//map = resList.stream().collect(Collectors.groupingBy(AuditHistoryRes::getUpdatedBy,Collectors.toList()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return resList;
	}

	private boolean validateString(String leadId) {
		if(null!=leadId && !leadId.isEmpty()) {
			return true;
		}
			
		return false;
	}

	@Override
	public Object getAuditDataByTaskStage(String stageName, String task) throws DynamicFormsServiceException {
		List<AuditHistoryRes> resList = new ArrayList<>();
		try {
			List<AuditHistory> auditHistoryList = new ArrayList<>();
			if (null != stageName || null != task) {
				auditHistoryList = auditHistoryDao.getAuditDataByTaskStage(stageName, task);
				resList = buildAuditHistory(auditHistoryList);

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return resList;
	}

	private List<AuditHistoryRes> buildAuditHistory(List<AuditHistory> auditHistoryList)
			throws DynamicFormsServiceException {
		List<AuditHistoryRes> resList = new ArrayList<>();
	
		if (null != auditHistoryList && !auditHistoryList.isEmpty()) {
			for (AuditHistory auditHistory : auditHistoryList) {
				AuditHistoryRes res = new AuditHistoryRes();
				String json = auditHistory.getData();
				JsonElement jelem = gson.fromJson(json, JsonElement.class);
				JsonObject jobj = jelem.getAsJsonObject();
				Iterator<String> keys = jobj.keySet().iterator();
				while (keys.hasNext()) {
					String key = keys.next();
					AuditHistoryRes historyObj = new AuditHistoryRes();
					historyObj.setFieldName(key);
					historyObj.setUpdatedEmpId(auditHistory.getUpdatedEmpID());
					historyObj.setUpdatedEmpName(auditHistory.getUpdatedEmpName());
					historyObj.setUpdatedAt(auditHistory.getUpdatedAt());
					historyObj.setLeadId(auditHistory.getLeadId());
					historyObj.setUniversalId(auditHistory.getUniversalId());
					if (jobj.has(key)) {
						JsonElement innerEle = jobj.get(key);
						JsonObject innerObj = innerEle.getAsJsonObject();
						historyObj.setOldValue(innerObj.get("left").getAsString());
						historyObj.setUpdatedValue(innerObj.get("right").getAsString());
					}
					resList.add(historyObj);
				}

			}
		} else {
			throw new DynamicFormsServiceException("No Audit data found for given Lead ID/Universal ID",
					HttpStatus.NOT_FOUND);
		}
		return resList;
	}

	@Override
	public Object getAuditDataBasedOnTime(String leadId, String universalId) throws DynamicFormsServiceException {
		List<AuditHistoryRes> resList = new ArrayList<>();
		Map<String,List<AuditHistoryRes>> map = new LinkedHashMap<>();
		try {
			/*
			 * List<AuditHistory> auditHistoryList = new ArrayList<>(); if (null != leadId
			 * || null != universalId) { auditHistoryList =
			 * auditHistoryDao.getAuditDataById(leadId, universalId); } else if
			 * ((null==leadId || leadId.isEmpty()) && (null==universalId ||
			 * universalId.isEmpty())) { auditHistoryList = auditHistoryDao.findAll(); }
			 * resList = buildAuditHistory(auditHistoryList); map =
			 * resList.stream().collect(Collectors.groupingBy(AuditHistoryRes::getUpdatedAt,
			 * Collectors.toList()));
			 */
		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return map;
	}

}
