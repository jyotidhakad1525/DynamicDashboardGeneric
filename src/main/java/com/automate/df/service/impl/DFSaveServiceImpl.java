package com.automate.df.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.automate.df.dao.LostSubLostRepository;
import com.automate.df.entity.LostReasons;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.automate.df.constants.DynamicFormConstants;
import com.automate.df.dao.BulkUploadTemplateRepository;
import com.automate.df.dao.DBUtil;
import com.automate.df.dao.OrganizationRepo;
import com.automate.df.entity.BulkUploadTemplate;
import com.automate.df.entity.OrganizationEntity;
import com.automate.df.exception.DynamicFormsServiceException;
import com.automate.df.model.Address;
import com.automate.df.model.Child1L1;
import com.automate.df.model.Child1L2;
import com.automate.df.model.Contact;
import com.automate.df.model.DFFieldRes;
import com.automate.df.model.DFSave;
import com.automate.df.model.DFUpdate;
import com.automate.df.model.DField;
import com.automate.df.model.Organization;
import com.automate.df.model.Param;
import com.automate.df.model.Root;
import com.automate.df.model.Rootparam;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DFSaveServiceImpl implements DFSaveService {

	@Autowired
	Environment env;

	@Autowired
	RestTemplate restTemplate;

	@Value("${df.module.host}")
	String dfModuleHost;

	@Value("${df.module.getpojo.api.endpoint}")
	String pojoApi;
	
	@Value("${df.module.getpojo.api.endpointv2}")
	String pojoApiV2;

	@Autowired
	DBUtil dbUtil;
	
	@Autowired
	BulkUploadTemplateRepository bulkUploadRepo;

	@Autowired
	LostSubLostRepository lostSubLostRepository;

	
	static Map<String, String> pageMap = new HashMap<>();
	 static {
		 
		 pageMap.put("intraStateTax","1198");
		 pageMap.put("interStateTax", "1199");
		 pageMap.put("unionTerritory", "1200");
		 pageMap.put("source", "1208");
		 pageMap.put("subSource", "1209");
		 pageMap.put("costomerType", "1210");
		 pageMap.put("enquerySegment", "1211");
		 pageMap.put("lostReasons", "1212");
		 pageMap.put("sublostReason", "1213");
		 pageMap.put("ComplaintFactor", "1214");
		 pageMap.put("ComplaintTracker", "1215");
		 pageMap.put("tcs", "1216");
		 pageMap.put("enqueryCategory", "1223");
		 pageMap.put("deliveryCheckList", "1224");
		 pageMap.put("bankFinancier", "1225");
		 pageMap.put("incuranceCompany", "1226");
		 pageMap.put("oem", "1227");
		 pageMap.put("oemModelMapping", "1228");
		 pageMap.put("vehicleInventory", "1229");
		 pageMap.put("rulesConfiguration", "1231");
		 pageMap.put("employe", "1232");
		 pageMap.put("otherMaker", "1233");
		 pageMap.put("otherModel", "1234");
		 pageMap.put("coDealer", "1270");
		 pageMap.put("competitor", "1271");
		 pageMap.put("otherCharges", "1283");
		 pageMap.put("paymentMode", "1329");
		 
	 }

	/*
	@Override
	public DFFieldRes saveDFFormDataWthReflection(DFSave dfSave) throws DynamicFormsServiceException {
		log.debug("saveDFFormData(){}");
		try {
			String pojoName = getPojo(dfSave.getPageId());
			log.debug("pojoName::" + pojoName);
			List<String> paramNames = dfSave.getParams().stream().map(DField::getFieldName)
					.collect(Collectors.toList());
			List<Object> paramVals = dfSave.getParams().stream().map(DField::getValue).collect(Collectors.toList());
			System.out.println("paramVals " + paramVals);

			Map<String, Class<?>> paramMapping = new HashMap<>();

			for (Object obj : paramVals) {
				if (obj instanceof String) {
					System.out.println("String");
					// paramMapping.pu
				}
				if (obj instanceof Integer) {
					System.out.println("int");
				}
			}

			for (DField dField : dfSave.getParams()) {
				Object obj = dField.getValue();
				if (obj instanceof String) {
					System.out.println("String");
					paramMapping.put(dField.getFieldName(), String.class);
				}
				if (obj instanceof Integer) {
					System.out.println("int");
					paramMapping.put(dField.getFieldName(), Integer.class);

				}
			}

			
			Class<?> clazz = PojoGenerator
					.generate("com.automate.df.model.save.Enquiry$" + UUID.randomUUID().toString(), paramMapping);

			Object obj = clazz.newInstance();

			int cnt = 0;
			for (final Method method : clazz.getDeclaredMethods()) {
				System.out.println("Method name :" + method);
				// method.in
				if (method.getName().contains("set")) {
					method.invoke(obj, paramVals.get(cnt));
					cnt++;
				}

			}

			System.out.println("After setting values ");

			
			  for (final Method method : clazz.getDeclaredMethods()) {
			  if(method.getName().contains("get")) { String value =
			  (String)method.invoke(obj);
			  System.out.println("method name "+method.getName()+" &value :"+value); } }
			 

			// validatePojo(map);
		//	dbUtil.save();

		} catch (Exception e) {
			log.error("saveDFFormData ", e);
			throw new DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	public void validatePojo(Map<String, Class<?>> props) throws Exception {


		Class<?> clazz = PojoGenerator.generate("com.automate.df.model.save.Enquiry$" + UUID.randomUUID().toString(),
				props);

		Object obj = clazz.newInstance();

		System.out.println("Clazz: " + clazz);
		System.out.println("Object: " + obj);
		System.out.println("Serializable? " + (obj instanceof Serializable));

		for (final Method method : clazz.getDeclaredMethods()) {
			System.out.println(method);
		}
	}
	*/
	private String getPojo(String pageId) {
		String tmp = dfModuleHost + pojoApi;
		tmp = tmp.replace("{id}", String.valueOf(pageId));
		return restTemplate.getForEntity(tmp, String.class).getBody();
	}

	private String getPojoV2(String pageId, String UUID) {
		String tmp = dfModuleHost + pojoApiV2;
		try {
		//String tmp = dfModuleHost + pojoApi;
		tmp = tmp.replace("{id}", String.valueOf(pageId));
		tmp = tmp.replace("{UUID}", String.valueOf(UUID));
		//return restTemplate.getForEntity(tmp, String.class).getBody();
		tmp = restTemplate.getForEntity(tmp, String.class).getBody();
		}
		catch (Exception e) {
			e.printStackTrace();
			
			// TODO: handle exception
		
		}
		return tmp;
	}
	
	@Override
	public DFFieldRes saveDFForm(DFSave dfSave) throws DynamicFormsServiceException {

		try {
			Map<String, Object> paramValMapping = new LinkedHashMap<>();
			for (DField dField : dfSave.getParams()) {
				if(dfSave.getPageId().equalsIgnoreCase("1212") || dfSave.getPageId().equalsIgnoreCase("1213")) {
					if(dField.getFieldName().equalsIgnoreCase("stage_name") &&  dField.getValue().equals("Contacts")) {
						paramValMapping.put(dField.getFieldName(), "Pre Enquiry");
					} else if(dField.getFieldName().equalsIgnoreCase("stage_name") &&  dField.getValue().equals("Booking Approval")) {
						paramValMapping.put(dField.getFieldName(), "Pre Booking");
					}else {
						paramValMapping.put(dField.getFieldName(), dField.getValue());
					}
				}else {
					paramValMapping.put(dField.getFieldName(), dField.getValue());
				}
			}

			try {
				if (dfSave.getPageId().equalsIgnoreCase("1213")) {
					if (paramValMapping.get("lostreason_id") == null && paramValMapping.get("lost_reason") != null) {
						String lostReason = (String) paramValMapping.get("lost_reason");
						Integer org_id = (Integer) paramValMapping.get("org_id");
						String stage_name = (String) paramValMapping.get("stage_name");
						List<LostReasons> lostReasons = lostSubLostRepository.getAllSubLost(lostReason, org_id, stage_name);
						if (lostReasons != null && lostReasons.size() == 1) {
							paramValMapping.put("lostreason_id", lostReasons.get(0).getId());
						}else{
							throw new DynamicFormsServiceException("Selected Lost reasons not mapped with the Stage Name",
									HttpStatus.BAD_REQUEST);
						}
					}
				}
			}catch (Exception e){
				e.printStackTrace();
			}

			log.debug("paramValMapping::"+paramValMapping);
			StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO ");
			sb.append(getPojoV2(dfSave.getPageId(), dfSave.getUuid()));
			sb.append(" (");
			//sb.append("id");
			//sb.append(DynamicFormConstants.COMMA_SEP);
			Object[] arr = new Object[paramValMapping.size()];
			//arr[0] = dfSave.getPageId();
			AtomicInteger cnt = new AtomicInteger(0);
			paramValMapping.forEach((k, v) -> {

				sb.append(k);
				sb.append(DynamicFormConstants.COMMA_SEP);
				arr[cnt.getAndIncrement()] = v;
			});
			StringBuilder query = new StringBuilder(sb.substring(0, sb.length() - 1));
			query.append(") values(");
			
			String placeholder ="";
			for(Object obj : arr ) {
				placeholder = placeholder+"?,";
			}
			placeholder = placeholder.substring(0,placeholder.length()-1);
			query.append(placeholder);
			query.append(")");
			log.debug("query ::" + query.toString());
			dbUtil.save(query.toString(), arr);
		} 
			catch(DataIntegrityViolationException sqlEx) {
				log.error("saveDFFormData ", sqlEx);
				throw new DynamicFormsServiceException("Duplicate record not allowed",
						HttpStatus.BAD_REQUEST);
			}
			catch (Exception e) {
			log.error("saveDFFormData ", e);
			throw new DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Override
	public String getAllDfFormData(String id) throws DynamicFormsServiceException {
		log.debug("getAllDfFormData(){},pageId:"+id);
		String res = null;
		try {
			String tableName = getPojo(id);
			log.debug("tableName " + tableName);
			if (tableName != null && tableName.length() > 0) {
				StringBuilder query = new StringBuilder();
				query.append("SELECT * FROM " + tableName);
				log.debug("query::" + query);
				
				List<Object[]> colnHeadersList = new ArrayList<>();
				colnHeadersList = entityManager.createNativeQuery("DESCRIBE " + tableName).getResultList();

				log.debug("colnHeadersList " + colnHeadersList);

				List<String> headers = new ArrayList<>();
				for (Object[] arr : colnHeadersList) {

					String colName = (String) arr[0];
					//colName = colName.replaceAll("_"," ");
					//colName = WordUtils.capitalize(colName);
					headers.add(colName);
				}

				log.debug("Coln Headers ::" + headers);
				List<Map<String, Object>> jObjList = new ArrayList<>();
				Query q = entityManager.createNativeQuery(query.toString());
				List<Object[]> queryResults = q.getResultList();

				for (int i = 0; i < queryResults.size(); i++) {
					Object[] objArr = queryResults.get(i);
					Map<String, Object> map = new HashMap<>();
					for (int j = 0; j < objArr.length; j++) {
						String colName = headers.get(j);
						if (objArr[j] instanceof BigDecimal) {
							BigDecimal bd = (BigDecimal) objArr[j];
							map.put(colName, bd);
						} else if (objArr[j] instanceof String) {
							map.put(colName, (String) objArr[j]);
						} else if (objArr[j] instanceof Integer) {
							Integer bd = (Integer) objArr[j];
							map.put(colName, bd);
						} else if (objArr[j] == null) {
							map.put(colName, null);
						}
					}
					jObjList.add(map);
				}
				log.debug("jObjList size " + jObjList.size());
				res = objectMapper.writeValueAsString(jObjList);
			} else {
				throw new DynamicFormsServiceException("Invalid PAGE Identifier", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(env.getProperty("InternalServerError"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return res;
	}

	@Override
	public String getDfFormData(String recordId,String pageId, String UUID) throws DynamicFormsServiceException {

		log.debug("getDfFormData(){},pageId:"+pageId+",recordId:"+recordId+",UUID:"+UUID);
		String res = null;
		try {
			String tableName = getPojoV2(pageId,UUID);
			log.debug("tableName " + tableName);
			if (tableName != null && tableName.length() > 0) {
				StringBuilder query = new StringBuilder();
				query.append("SELECT * FROM " + tableName+" where id ="+recordId);
				log.debug("query::" + query);
				
				List<Object[]> colnHeadersList = new ArrayList<>();
				colnHeadersList = entityManager.createNativeQuery("DESCRIBE " + tableName).getResultList();

				log.debug("colnHeadersList " + colnHeadersList);

				List<String> headers = new ArrayList<>();
				for (Object[] arr : colnHeadersList) {

					String colName = (String) arr[0];
					//colName = colName.replaceAll("_"," ");
					//colName = WordUtils.capitalize(colName);
					headers.add(colName);
				}

				log.debug("Coln Headers ::" + headers);
				List<Map<String, Object>> jObjList = new ArrayList<>();
				Query q = entityManager.createNativeQuery(query.toString());
				List<Object[]> queryResults = q.getResultList();

				for (int i = 0; i < queryResults.size(); i++) {
					Object[] objArr = queryResults.get(i);
					Map<String, Object> map = new HashMap<>();
					for (int j = 0; j < objArr.length; j++) {
						String colName = headers.get(j);
						if (objArr[j] instanceof BigDecimal) {
							BigDecimal bd = (BigDecimal) objArr[j];
							map.put(colName, bd);
						} else if (objArr[j] instanceof String) {
							map.put(colName, (String) objArr[j]);
						} else if (objArr[j] instanceof Integer) {
							Integer bd = (Integer) objArr[j];
							map.put(colName, bd);
						} else if (objArr[j] == null) {
							map.put(colName, null);
						}else {
							map.put(colName, objArr[j]);
						}
					}
					jObjList.add(map);
				}
				log.debug("jObjList size " + jObjList.size());
				res = objectMapper.writeValueAsString(jObjList);
			} else {
				throw new DynamicFormsServiceException("Invalid Record Identifier", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(env.getProperty("InternalServerError"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return res;
	
	}

	@Override
	public Map<String,String> updateDFFormData(DFUpdate dfUpdate) throws DynamicFormsServiceException {
		log.debug("Inside updateDFFormData(){}");
		String res=null;
		Map<String,String> map = new HashMap<>();
		try {
			Map<String, Object> paramValMapping = new LinkedHashMap<>();
			for (DField dField : dfUpdate.getParams()) {
				if(dfUpdate.getPageId().equalsIgnoreCase("1212") || dfUpdate.getPageId().equalsIgnoreCase("1213")) {
					if(dField.getFieldName().equalsIgnoreCase("stage_name") &&  dField.getValue().equals("Contacts")) {
						paramValMapping.put(dField.getFieldName(), "Pre Enquiry");	
					}else if(dField.getFieldName().equalsIgnoreCase("stage_name") &&  dField.getValue().equals("Booking Approval")) {
						paramValMapping.put(dField.getFieldName(), "Pre Booking");
					}else{
						paramValMapping.put(dField.getFieldName(), dField.getValue());
					}
				}else {
					paramValMapping.put(dField.getFieldName(), dField.getValue());
				}
			}


			try {
				if (dfUpdate.getPageId().equalsIgnoreCase("1213")) {
					if (paramValMapping.get("lostreason_id") == null && paramValMapping.get("lost_reason") != null) {
						String lostReason = (String) paramValMapping.get("lost_reason");
						Integer org_id = (Integer) paramValMapping.get("org_id");
						String stage_name = (String) paramValMapping.get("stage_name");
						List<LostReasons> lostReasons = lostSubLostRepository.getAllSubLost(lostReason, org_id, stage_name);
						if (lostReasons != null && lostReasons.size() == 1) {
							paramValMapping.put("lostreason_id", lostReasons.get(0).getId());
						}else{
							throw new DynamicFormsServiceException("Selected Lost reasons not mapped with the Stage Name",
									HttpStatus.BAD_REQUEST);
						}
					}
				}
			}catch (Exception e){
				e.printStackTrace();
			}


			log.debug("paramValMapping::"+paramValMapping);
			StringBuilder sb = new StringBuilder();
			sb.append("UPDATE");
			sb.append(DynamicFormConstants.SPACE);
			sb.append(getPojoV2(dfUpdate.getPageId(),dfUpdate.getUuid()));
			sb.append(DynamicFormConstants.SPACE);
			sb.append("SET ");
			
			Object[] arr = new Object[paramValMapping.size()];
			AtomicInteger cnt = new AtomicInteger(0);
			paramValMapping.forEach((k, v) -> {
				sb.append(k)
					.append("=")
				 	.append('\'')
			        .append(v)
			        .append('\'');
				sb.append(DynamicFormConstants.COMMA_SEP);
				arr[cnt.getAndIncrement()] = v;
			});
			StringBuilder query = new StringBuilder(sb.substring(0, sb.length() - 1));
			query.append(" WHERE ID = ");
			query.append(dfUpdate.getRecordId());
			
			
			log.debug("query ::" + query.toString());
			res = dbUtil.update(query.toString());
			map.put("success", res);
		} catch(DataIntegrityViolationException sqlEx) {
			log.error("updateDFFormData ", sqlEx);
			throw new DynamicFormsServiceException("Duplicate record not allowed",
					HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			log.error("updateDFFormData ", e);
			throw new DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return map;
	}
	
	// Mutli Entity Methods - Starts Here

	@Autowired
	OrganizationRepo organizationRepo;

	@Autowired
	ModelMapper modelMapper;
	
	
	@Override
	public String saveOrganization(String str) {
		String res = null;
		try {
			OrganizationEntity  dbRes = organizationRepo.save(modelMapper.map(buildOrganization(str), OrganizationEntity.class));
			res  = objectMapper.writeValueAsString(modelMapper.map(dbRes, Organization.class));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("saveMultiDFForm ", e);
		}
		return res;
	}
	
	private Organization buildOrganization(String str) throws JsonMappingException, JsonProcessingException {
		Root root = objectMapper.readValue(str, Root.class);
		Organization org = new Organization();
		List<Rootparam> rootParamList = root.getRootparams();
		for (Rootparam rp : rootParamList) {

			if (validateStr(rp.getFieldName(), "orgname")) {
				org.setOrgname((String) rp.getValue());
			}
			if (validateStr(rp.getFieldName(), "orgdesc")) {
				org.setOrgdesc((String) rp.getValue());
			}

			org = addChildL1(org, rp.getChild1_l1());
		}
		return org;
	}

	@Override
	public String updateOrganization(String str) throws DynamicFormsServiceException {
		String res = null;
		try {
			Root root = objectMapper.readValue(str,Root.class);
			OrganizationEntity dbRec = modelMapper.map(buildOrganization(str), OrganizationEntity.class);
			dbRec.setId(root.getRecordId());
			OrganizationEntity  dbRes = organizationRepo.save(dbRec);
			res  = objectMapper.writeValueAsString(modelMapper.map(dbRes, Organization.class));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("updateOrganization ", e);
		}
		return res;
	}
	

	private Organization addChildL1(Organization org, List<Child1L1> childL1List) {
		List<Address> addList = new ArrayList<>();
		if (null != childL1List) {
			for (Child1L1 child1L1 : childL1List) {
				Address addr = new Address();
				for (Param param : child1L1.getParams()) {
					if (validateStr(param.getFieldName(), "city"))
						addr.setCity((String) param.getValue());
					if (validateStr(param.getFieldName(), "state"))
						addr.setState((String) param.getValue());
					if (null != param.getChild1_l2()) {
						List<Contact> contactList = new ArrayList<>();
						for (Child1L2 child1L2 : param.getChild1_l2()) {
							Contact contact = new Contact();
							for (Param childParam : child1L2.getParams()) {
								if (validateStr(childParam.getFieldName(), "contact.contactNo"))
									contact.setContactNo( childParam.getValue().toString());
							}
							contactList.add(contact);
						}
						addr.setContacts(contactList);
					}
					
				}
				
				addList.add(addr);
			}
		}
		org.setAddress(addList);
		return org;
	}

	private Organization addChildL2(Organization org, List<Child1L2> childList) {
		List<Address> addList = new ArrayList<>();
		
		if (null != childList) {
			for (Child1L2 childL1 : childList) {
				Address addr = new Address();
				for (Param param : childL1.getParams()) {
					if (validateStr(param.getFieldName(), "address.city"))
						addr.setCity((String) param.getValue());
					if (validateStr(param.getFieldName(), "address.state"))
						addr.setState((String) param.getValue());
				/*	if (null != param.getChild2_l2()) {
						List<Contact> contactList = new ArrayList<>();
						for (Child2L2 childL2 : param.getChild2_l2()) {
							Contact contact = new Contact();
							for (Param childParam :  childL2.getParams()) {
								if (validateStr(childParam.getFieldName(), "contact.contactNo"))
									contact.setContactNo((Long) childParam.getValue());
							}
							contactList.add(contact);
						}
						addr.setContacts(contactList);
					}*/
					
				}
				
				addList.add(addr);
			}
		}
		org.setAddress(addList);
		return org;
	}
	


	


	public boolean validateStr(String str,String key) {
		if(null!= str && str.contains(key)) {
			return true;
		}
		return false;
	}

	
	@Override
	public String getOrganization(Integer recordId) throws DynamicFormsServiceException {
		log.debug("Inside getOrganization()");
		String res = null;
		try {
			Optional<OrganizationEntity> opt = organizationRepo.findById(recordId);
			if (opt.isPresent()) {
				res = objectMapper.writeValueAsString(modelMapper.map(opt.get(), Organization.class));
			} else {
				res = "No Data found for given recordId";
			}
		} catch (Exception e) {
			log.error("getOrganization ", e);
			throw new DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return res;
	}

	@Override
	public String getAllOrganization(int pageNo,int size) throws DynamicFormsServiceException {
		log.debug("Inside getAllOrganization()");
		String res = null;
		try {
			Pageable pageable = PageRequest.of(pageNo, size);
			List<OrganizationEntity> dbList = organizationRepo.findAll(pageable).toList();
			List<Organization> list = dbList.stream().map(x -> modelMapper.map(x, Organization.class)).collect(Collectors.toList());
			res = objectMapper.writeValueAsString(list);	  
		}catch (Exception e) {
			log.error("getAllOrganization ", e);
			throw new DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return res;
	}

	@Override
	public String deleteOrganization(int recordId) throws DynamicFormsServiceException {
		log.debug("Inside deleteOrganization()");
		String res = null;
		try {
			organizationRepo.deleteById(recordId);
			res="Delted the record sucessfully";
		}catch (Exception e) {
			res="There is an error while deleting the record, please check id ";
			log.error("getAllOrganization ", e);
			throw new DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return res;
	}

	@Override
	public String deleteDfFormData(String recordId,String pageId) throws DynamicFormsServiceException {
		log.debug("Inside deleteDfFormData()");
		String res = null;
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("DELETE FROM ")
			.append(getPojo(pageId))
			.append(" where id = ")
			 .append('\'')
			 .append(recordId)
			 .append('\'');
			dbUtil.delete(sb.toString());
			Map<String,String> map = new HashMap<>();
			map.put("success", "true");
			
			res=objectMapper.writeValueAsString(map);
		}catch (Exception e) {
			res="There is an error while deleting the record, please check id ";
			log.error("deleteDfFormData ", e);
			throw new DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return res;
	}
	
	@Override
	public String softDeleteDfFormData(String recordId,String pageId, String UUID) throws DynamicFormsServiceException {
		log.debug("Inside deleteDfFormData()");
		String res = null;
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("UPDATE");
			sb.append(DynamicFormConstants.SPACE);
			sb.append(getPojoV2(pageId, UUID));
			sb.append(DynamicFormConstants.SPACE);
			sb.append("SET ");
			sb.append("status ")
			.append("=")
		 	.append('\'')
	        .append("InActive")
	        .append('\'');
			sb.append(" where id = ")
			 .append('\'')
			 .append(recordId)
			 .append('\'');
			
			dbUtil.delete(sb.toString());
			Map<String,String> map = new HashMap<>();
			map.put("success", "true");
			
			res=objectMapper.writeValueAsString(map);
		}catch (Exception e) {
			res="There is an error while deleting the record, please check id ";
			log.error("deleteDfFormData ", e);
			throw new DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return res;
	}
	

@Override
	public BulkUploadTemplate getBulkUpoladTemplet(int pageId, int orgId) throws DynamicFormsServiceException {
		BulkUploadTemplate opt = null;
		try {
			opt = bulkUploadRepo.findByPageIdAndOrgId(pageId, orgId);

		} catch (Exception e) {
			log.error("getOrganization ", e);
			throw new DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return opt;
	}


@Override
public String getAllDfFormDataByOrg(String pageId,int orgId,String status, String UUID) throws DynamicFormsServiceException {
	String id=pageMap.get(pageId);
	log.debug("getAllDfFormDataByOrg(){},pageId:"+id,"getAllDfFormDataByOrg(){},OrgId:"+orgId,"getAllDfFormDataByOrg(){},Status:"+status);
	String res = null;
	try {
		String tableName = getPojoV2(id, UUID );
		log.debug("tableName " + tableName);
	
		if (tableName != null && tableName.length() > 0) {
			StringBuilder query = new StringBuilder();
			query.append("SELECT * FROM " + tableName+" where org_id ="+orgId+" and status ="+status);
			log.debug("query::" + query);
			
			List<Object[]> colnHeadersList = new ArrayList<>();
			colnHeadersList = entityManager.createNativeQuery("DESCRIBE " + tableName).getResultList();

			log.debug("colnHeadersList " + colnHeadersList);

			List<String> headers = new ArrayList<>();
			for (Object[] arr : colnHeadersList) {

				String colName = (String) arr[0];
				//colName = colName.replaceAll("_"," ");
				//colName = WordUtils.capitalize(colName);
				headers.add(colName);
			}

			log.debug("Coln Headers ::" + headers);
			List<Map<String, Object>> jObjList = new ArrayList<>();
			Query q = entityManager.createNativeQuery(query.toString());
			List<Object[]> queryResults = q.getResultList();

			for (int i = 0; i < queryResults.size(); i++) {
				Object[] objArr = queryResults.get(i);
				Map<String, Object> map = new HashMap<>();
				for (int j = 0; j < objArr.length; j++) {
					String colName = headers.get(j);
					if (objArr[j] instanceof BigDecimal) {
						BigDecimal bd = (BigDecimal) objArr[j];
						map.put(colName, bd);
					} else if (objArr[j] instanceof String) {
						map.put(colName, (String) objArr[j]);
					} else if (objArr[j] instanceof Integer) {
						Integer bd = (Integer) objArr[j];
						map.put(colName, bd);
					} else if (objArr[j] == null) {
						map.put(colName, null);
					}
				}
				jObjList.add(map);
			}
			log.debug("jObjList size " + jObjList.size());
			if (pageId.equals("enqueryCategory")) {
				enquiryCatagoryProcess(jObjList, "name", "Hot", "end_day", 10);
				enquiryCatagoryProcess(jObjList, "name", "Cold", "end_day", 5000);
				enquiryCatagoryProcess(jObjList, "name", "Warm", "end_day", 30);
			}
			res = objectMapper.writeValueAsString(jObjList);
		} else {
			throw new DynamicFormsServiceException("Invalid PAGE Identifier", HttpStatus.BAD_REQUEST);
		}
	} catch (Exception e) {
		e.printStackTrace();
		throw new DynamicFormsServiceException(env.getProperty("InternalServerError"),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
	return res;
}
//as per bug DMS-220 modification fixing upper limit for enquiry category
void enquiryCatagoryProcess(List<Map<String, Object>> ListOfMap, String nameFilterKey, Object nameFilterValue,
		String endDayFilterKey, Object endDayFilterValue) {
	for (Map<String, Object> tempMap : ListOfMap) {
		for (Map.Entry<String, Object> entry : tempMap.entrySet()) {
			if (entry.getKey().equalsIgnoreCase(nameFilterKey)
					&& String.valueOf(entry.getValue()).equalsIgnoreCase(String.valueOf(nameFilterValue))) {
				if (tempMap.containsKey(endDayFilterKey)) {
					tempMap.put(endDayFilterKey, endDayFilterValue);
				}
				break;
			}
		}
	}
}
	
	// Mutli Entity Methods - Ends Here

}
