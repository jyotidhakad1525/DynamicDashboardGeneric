package com.automate.df.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.automate.df.dao.oh.DmsAddressDao;
import com.automate.df.dao.oh.DmsBranchDao;
import com.automate.df.dao.oh.DmsDesignationRepo;
import com.automate.df.dao.oh.DmsGradeDao;
import com.automate.df.dao.oh.EmpLocationMappingDao;
import com.automate.df.dao.oh.LocationNodeDataDao;
import com.automate.df.dao.oh.LocationNodeDefDao;
import com.automate.df.dao.salesgap.DmsEmployeeRepo;
import com.automate.df.entity.oh.DmsAddress;
import com.automate.df.entity.oh.DmsBranch;
import com.automate.df.entity.oh.DmsDesignation;
import com.automate.df.entity.oh.DmsGrade;
import com.automate.df.entity.oh.EmpLocationMapping;
import com.automate.df.entity.oh.LocationNodeData;
import com.automate.df.entity.oh.LocationNodeDef;
import com.automate.df.entity.salesgap.DmsEmployee;
import com.automate.df.exception.DynamicFormsServiceException;
import com.automate.df.model.AcitveMappingOrgChartRes;
import com.automate.df.model.oh.EmployeeRoot;
import com.automate.df.model.oh.EmployeeRootV2;
import com.automate.df.model.oh.LevelDDData;
import com.automate.df.model.oh.LevelDataReq;
import com.automate.df.model.oh.LevelDropDownData;
import com.automate.df.model.oh.LevelMapping;
import com.automate.df.model.oh.LevelReq;
import com.automate.df.model.oh.LocationDefNodeRes;
import com.automate.df.model.oh.LocationNodeDataV2;
import com.automate.df.model.oh.OHEmpLevelMapping;
import com.automate.df.model.oh.OHEmpLevelMappingV2;
import com.automate.df.model.oh.OHEmpLevelUpdateMapReq;
import com.automate.df.model.oh.OHLeveDeleteReq;
import com.automate.df.model.oh.OHLeveUpdateReq;
import com.automate.df.model.oh.OHLevelReq;
import com.automate.df.model.oh.OHLevelUpdateReq;
import com.automate.df.model.oh.OHNodeUpdateReq;
import com.automate.df.model.oh.OHRes;
import com.automate.df.model.oh.PostOffice;
import com.automate.df.model.oh.PostOfficeRoot;
import com.automate.df.model.salesgap.TargetDropDownV2;
import com.automate.df.model.salesgap.TargetRoleRes;
import com.automate.df.service.OHService;
import com.automate.df.util.ObjectMapperUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author sruja
 *
 */

@Service
@Slf4j
public class OHServiceImpl implements OHService {

	@Autowired
	Environment env;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	LocationNodeDataDao locationNodeDataDao;

	@Autowired
	EmpLocationMappingDao empLocationMappingDao;

	@Autowired
	LocationNodeDefDao locationNodeDefDao;

	@Autowired
	DmsAddressDao dmsAddressDao;

	@Autowired
	DmsBranchDao dmsBranchDao;

	@Autowired
	DmsDesignationRepo dmsDesignationRepo;

	@Override
	public List<OHRes> getOHDropdown(Integer orgId, Integer empId, Integer id) throws DynamicFormsServiceException {
		List<OHRes> list = new ArrayList<>();
		try {
			List<LocationNodeData> nodeList = locationNodeDataDao.getLocationNodeData(id,
					empLocationMappingDao.getLeads(orgId, empId));
			nodeList.forEach(x -> {
				OHRes ohRes = new OHRes();
				ohRes.setKey(String.valueOf(x.getId()));
				ohRes.setValue(x.getCode());
				list.add(ohRes);
			});
			log.debug("nodeList " + nodeList);
		}
		/*
		 * catch (DynamicFormsServiceException e) {
		 * log.error("saveTargetSettingData() ", e); throw e;
		 * 
		 * }
		 */
		catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return list;
	}

	@Override
	public List<String> getLevelData(Integer orgId, Integer empId) throws DynamicFormsServiceException {
		List<String> list = new ArrayList<>();
		try {
			list = locationNodeDataDao.getEmpLevelData(empLocationMappingDao.getLeads(orgId, empId));
			log.debug("getLevelData ,outputlist :: " + list);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return list;
	}

	@Override
	public List<OHRes> getEmpParentDropdown(Integer orgId, Integer empId) throws DynamicFormsServiceException {
		List<OHRes> list = new ArrayList<>();
		try {
			List<Integer> leadIdList = empLocationMappingDao.getLeads(orgId, empId);
			List<LocationNodeDef> opList = locationNodeDefDao.getNodeDefData(orgId,
					locationNodeDataDao.getEmpLevelData(leadIdList));
			String topLevel = null;
			System.out.println("opList " + opList);
			if (null != opList && !opList.isEmpty()) {
				topLevel = opList.get(0).getLocationNodeDefType();
			} else {
				throw new DynamicFormsServiceException("No Level Data found for the given empId and OrgId",
						HttpStatus.UNPROCESSABLE_ENTITY);
			}
			log.debug("orgID:" + orgId + ",empId:" + empId + ",topLevel:" + topLevel);
			List<LocationNodeData> nodeList = locationNodeDataDao.getParentEmpDropdown(topLevel, leadIdList);

			nodeList.forEach(x -> {
				OHRes ohRes = new OHRes();
				ohRes.setKey(String.valueOf(x.getId()));
				ohRes.setValue(x.getCode());
				list.add(ohRes);
			});
			log.debug("nodeList " + nodeList);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return list;
	}

	@Override
	public String addOHMapping(LevelDataReq req) throws DynamicFormsServiceException {
		try {
			boolean isRoot = req.isRootLevel();
			log.debug("isRoot Level " + isRoot);
			Integer orgId = req.getOrgId();
			Integer empId = req.getEmpId();
			log.debug("orgId ::" + orgId + ", empId " + empId);
			if (isRoot) {

				for (LevelDropDownData data : req.getData()) {

					LocationNodeData nodeData = new LocationNodeData();
					// nodeData.set

				}

			} else {

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return null;
	}

	@Override
	public List<OHRes> getEmpBranches(Integer orgId, Integer empId) throws DynamicFormsServiceException {
		List<OHRes> list = new ArrayList<>();
		try {
			List<Integer> leadIdList = empLocationMappingDao.getLeads(orgId, empId);
			List<LocationNodeDef> opList = locationNodeDefDao.getNodeDefData(orgId,
					locationNodeDataDao.getEmpLevelData(leadIdList));
			String leastLevelMinusOne = null;
			if (null != opList && !opList.isEmpty()) {
				System.out.println("opList " + opList);
				leastLevelMinusOne = opList.get(opList.size() - 2).getLocationNodeDefType();
			} else {
				throw new DynamicFormsServiceException("No Level Data found for the given empId and OrgId",
						HttpStatus.UNPROCESSABLE_ENTITY);
			}
			log.debug("orgID:" + orgId + ",empId:" + empId + ",leastLevelMinusOne:" + leastLevelMinusOne);
			List<LocationNodeData> nodeList = locationNodeDataDao.getParentEmpDropdown(leastLevelMinusOne, leadIdList);

			nodeList.forEach(x -> {
				OHRes ohRes = new OHRes();
				ohRes.setKey(String.valueOf(x.getId()));
				ohRes.setValue(x.getCode());
				list.add(ohRes);
			});
			log.debug("nodeList " + nodeList);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return list;
	}

	public String getLevelType(Integer order) {

		Map<Integer, String> map = new HashMap<>();
		map.put(1, "Level1");
		map.put(2, "Level2");
		map.put(3, "Level3");
		map.put(4, "Level4");
		map.put(5, "Level5");
		map.put(6, "Level6");
		map.put(7, "Level7");
		map.put(8, "Level8");
		map.put(9, "Level9");
		map.put(10, "Level10");
		return map.get(order);

	}

	@Override
	public List<LocationNodeData> createLevels(OHLevelReq req) throws DynamicFormsServiceException {
		List<LocationNodeData> resList = new ArrayList<>();
		List<LocationNodeDef> defList = new ArrayList<>();
		try {
			int orgId = req.getOrgId();
			int empId = req.getEmpId();
			for (LevelReq lr : req.getLevelList()) {

				LocationNodeDef locationNodeDef = new LocationNodeDef();
				locationNodeDef.setOrgId(orgId);
				locationNodeDef.setCreatedBy(empId);
				locationNodeDef.setCreatedOn(getCurrentTmeStamp());
				locationNodeDef.setModifiedBy(empId);
				locationNodeDef.setModifiedOn(getCurrentTmeStamp());
				locationNodeDef.setLocationNodeDefName(lr.getLevelDefName());
				locationNodeDef.setLocationNodeDefType(getLevelType(lr.getLevelOrder()));
				locationNodeDef.setParentId(lr.getLevelOrder());
				locationNodeDef.setDisplayName(lr.getLevelDefName());
				locationNodeDef.setActive("Y");
				Optional<LocationNodeDef> nodeDefOpt = locationNodeDefDao.verifyNodeDef(orgId, getLevelType(lr.getLevelOrder()));
				if (!nodeDefOpt.isPresent()) {
					locationNodeDefDao.save(locationNodeDef);
				}
			}
			defList = getOrgLevels(req.getOrgId());
			for (LevelReq lr : req.getLevelList()) {
				List<LevelDDData> levelMappings = lr.getLevelData();

				int levelOrder = lr.getLevelOrder();
				log.debug("levelOrder::" + levelOrder);
				if (levelOrder == 1) {
					for (LevelDDData levelDDData : levelMappings) {
						LocationNodeData lData = new LocationNodeData();
						lData.setCananicalName(levelDDData.getCode());
						lData.setCode(levelDDData.getCode());
						lData.setName(levelDDData.getName());
						lData.setCreatedOn(getCurrentTmeStamp());
						lData.setCreatedBy(req.getEmpId());
						lData.setModifiedOn(getCurrentTmeStamp());
						lData.setModifiedBy(req.getEmpId());
						lData.setParentId("0");
						lData.setRefParentId("0");
						lData.setLeafNode("NO");
						String levelType = getLevelType(levelOrder);
						lData.setType(levelType);
						lData.setOrgId(orgId);
						lData.setActive("Y");
						Integer levelDefId = defList.stream()
								.filter(x -> (x.getLocationNodeDefType().equalsIgnoreCase(levelType)
										&& x.getOrgId() == req.getOrgId()))
								.map(x -> x.getId()).findFirst().get();
						lData.setLocationNodeDefId(levelDefId);
						Optional<LocationNodeData> dbNodeParentlevelData = locationNodeDataDao
								.verifyLevelDataRecord(orgId, lr.getLevelDefType(), levelDDData.getCode());
						if (!dbNodeParentlevelData.isPresent()) {
							log.debug("Level 1 is not there, so inserting into DB");
							resList.add(locationNodeDataDao.save(lData));
						}

					}
					log.debug("Level Data Mapping insertion is done for level1");
				} else {

					Integer locatioNodeId = 0;
					String parentLevel = getParentLevel(levelOrder);
					log.debug("Level Data Mapping insertion started for level: " + levelOrder + ",parentLevel: "
							+ parentLevel);
					if (null != levelMappings) {
						for (LevelDDData levelDDData : levelMappings) {

							String parentMappingCode = levelDDData.getParentMappingCode();
							log.debug("parentMappingCode " + parentMappingCode);
							Optional<LocationNodeData> dbNodeParetDataOpt = locationNodeDataDao
									.verifyLevelDataRecord(orgId, parentLevel, levelDDData.getParentMappingCode());
							log.debug("dbNodeParetDataOpt " + dbNodeParetDataOpt.isPresent());
							if (dbNodeParetDataOpt.isPresent()) {
								LocationNodeData parentData = dbNodeParetDataOpt.get();
								log.debug("dbNodeParentlevelData is presne");
								int parentMappingId = parentData.getId();

								LocationNodeData lData = new LocationNodeData();

								String levelCode = levelDDData.getCode();
								String levelName = levelDDData.getName();
								log.debug("levelcode " + levelCode + " levelName " + levelName);
								boolean postalFlag = isValidPinCode(levelName);
								List<PostOffice> postOfficeObj = new ArrayList<>();
								if (postalFlag) {
									log.debug("Givne data contains valid pincode");
									lData.setCananicalName(parentData.getCananicalName() + "/" + levelName);
									lData.setLeafNode("YES");
									levelName = levelName.replaceAll(" ", "");
									ResponseEntity<String> resEntity = restTemplate.getForEntity(
											"https://api.postalpincode.in/pincode/" + levelName, String.class);
									System.out.println("root " + resEntity.getBody());
									if (resEntity.getStatusCodeValue() == 200) {
										ObjectMapper om = new ObjectMapper();
										PostOfficeRoot[] proot = om.readValue(resEntity.getBody(),
												PostOfficeRoot[].class);
										List<PostOfficeRoot> pList = Arrays.asList(proot);
										System.out.println("plist " + pList);
										if (null != pList && !pList.isEmpty()) {
											postOfficeObj = proot[0].getPostOffice();
											if (null != postOfficeObj && !postOfficeObj.isEmpty()) {
												PostOffice po = postOfficeObj.get(0);
												lData.setName(po.getName());

											}
										}

									}
								} else {
									lData.setCananicalName(parentData.getCananicalName() + "/" + levelCode);
									lData.setName(levelName);
									lData.setLeafNode("NO");
								}
								lData.setCode(levelDDData.getCode());

								lData.setCreatedOn(getCurrentTmeStamp());
								lData.setCreatedBy(req.getEmpId());
								lData.setModifiedOn(getCurrentTmeStamp());
								lData.setModifiedBy(req.getEmpId());
								lData.setParentId(String.valueOf(parentMappingId));
								lData.setRefParentId(String.valueOf(parentMappingId));
								String levelType = getLevelType(levelOrder);
								lData.setType(levelType);
								lData.setOrgId(orgId);
								lData.setActive("Y");
								System.out.println("defList ::" + defList);
								Integer levelDefId = defList.stream()
										.filter(x -> (x.getLocationNodeDefType().equalsIgnoreCase(levelType)
												&& x.getOrgId() == req.getOrgId()))
										.map(x -> x.getId()).findFirst().get();
								lData.setLocationNodeDefId(levelDefId);

								Optional<LocationNodeData> dbNodeParentlevelData = locationNodeDataDao
										.verifyLevelDataRecord(orgId, lr.getLevelDefType(), levelDDData.getCode());
								if (!dbNodeParentlevelData.isPresent()) {
									log.debug("Data is not there, so inserting into DB");
									LocationNodeData dbData = locationNodeDataDao.save(lData);
									locatioNodeId = dbData.getId();
									resList.add(dbData);
								}

								if (postalFlag && null != postOfficeObj && !postOfficeObj.isEmpty()) {
									PostOffice po = postOfficeObj.get(0);
									Integer addressId = createAddress(po);
									createBranch(po, addressId, req.getOrgId(), locatioNodeId);
									
								}

							}
						}
						log.debug("Level Data Mapping insertion ended for level: " + levelOrder + ",parentLevel: "
								+ parentLevel);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resList;
	}

	private void createBranch(PostOffice po, Integer addressId, Integer orgId, Integer locatioNodeId)
			throws DynamicFormsServiceException {
		// TODO Auto-generated method stub
		try {
			DmsBranch obj = new DmsBranch();
			obj.setActive("Y");
			obj.setAdress(addressId);
			obj.setAdress(addressId);
			obj.setName(po.getName());
			obj.setOrgMapId(locatioNodeId);
			dmsBranchDao.save(obj);
			log.debug("Branch creted ::: " + dmsBranchDao);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private Integer createAddress(PostOffice po) throws DynamicFormsServiceException {
		Integer id = 0;
		try {
			DmsAddress obj = new DmsAddress();
			obj.setCity(po.getRegion());
			obj.setCountry(po.getCountry());
			obj.setState(po.getState());
			obj.setDistrict(po.getDistrict());
			obj.setPincode(po.getPincode());
			obj.setActive("Y");
			id = dmsAddressDao.save(obj).getId();
			log.debug("Address creted with ID " + id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return id;
	}

	private String getParentLevel(int levelOrder) {
		levelOrder = levelOrder - 1;
		return "Level" + levelOrder;
	}

	public Timestamp getCurrentTmeStamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	@Override
	public List<?> removeDataLevels(OHLeveDeleteReq req) throws DynamicFormsServiceException {
		log.debug("removeDataLevels(){}");
		List<LocationNodeData> list = new ArrayList<>();
		try {
			List<String> levels = getOrgLevels(req.getOrgId()).stream().map(x -> x.getLocationNodeDefType())
					.collect(Collectors.toList());
			log.debug("levels before in removeDataLevels" + levels);
			levels = levels.subList(levels.indexOf(req.getLevelCodeToRemove()), levels.size());
			log.debug("levels afterin removeDataLevels" + levels);
			String leafLevel = levels.get(levels.size() - 1);
			log.debug("Leaf level in remove mappings " + leafLevel);
			List<LocationNodeData> leafData = getLevelDataNodes(req.getOrgId(), leafLevel);

			for (String level : levels) {
				locationNodeDefDao.removeLevel(req.getOrgId(), level);
				locationNodeDataDao.removeLevel(req.getOrgId(), level);
			}

			empLocationMappingDao.removeActiveMappings(req.getOrgId(), levels);

			System.out.println("leafData" + leafData);
			for (LocationNodeData data : leafData) {

				String cName = data.getCananicalName();
				System.out.println("cName " + cName);
				if (null != cName) {

					String[] tmp = cName.split("/");
					cName = tmp[tmp.length - 1];
					log.debug("Pincode " + cName);

					if (isValidPinCode(cName)) {
						DmsBranch branch = dmsBranchDao.getBranchByOrgMpId(data.getId());
						branch.setActive("N");

						dmsBranchDao.save(branch);
						DmsAddress addr = dmsAddressDao.getById(branch.getAdress());
						addr.setActive("N");
						dmsAddressDao.save(addr);
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return list;

	}

	@Override
	public List<LocationNodeData> setEmpLevelMapping(OHEmpLevelMapping req, String active)
			throws DynamicFormsServiceException {
		List<LocationNodeData> list = new ArrayList<>();
		try {
			List<String> levels = getOrgLevels(req.getOrgId()).stream().map(x -> x.getLocationNodeDefType())
					.collect(Collectors.toList());
			log.debug("levels before " + levels);
			levels = levels.subList(levels.indexOf(req.getLevelCode()), levels.size());
			levels = levels.stream().sorted().collect(Collectors.toList());
			log.debug("levels after " + levels);
			Map<String, List<Integer>> levelIdmap = new LinkedHashMap<>();
			for (int i = 0; i < levels.size(); i++) {
				String level = levels.get(i);
				if (i == 0) {
					levelIdmap.put(level, req.getNodesIds());
				} else {
					if (!levelIdmap.isEmpty()) {
						String previousLevel = getPreviousLevel(level);
						log.debug("level " + level + ",previousLevel:" + previousLevel);
						if (levelIdmap.containsKey(previousLevel)) {
							log.debug("LevelIdMap contains previous level " + level);
							List<LocationNodeData> nodeData = locationNodeDataDao.getNodeDataByParentId(req.getOrgId(),
									level, levelIdmap.get(previousLevel));
							List<Integer> idLists = nodeData.stream().map(x -> x.getId()).collect(Collectors.toList());
							System.out.println("idLists  " + idLists);
							levelIdmap.put(level, idLists);
							list.addAll(nodeData);
						}
					}

				}

			}
			log.debug("levelIdmap ::" + levelIdmap);
			List<Integer> reqNodeIds = new ArrayList<>();
			levelIdmap.forEach((k, v) -> {
				reqNodeIds.addAll(v);
			});
			log.debug("reqNodeIds:::" + reqNodeIds);
			if (active.equalsIgnoreCase("Y")) {
				for (Integer empId : req.getEmpId()) {

					List<EmpLocationMapping> empLocationMapList = new ArrayList<>();
					for (Integer nodeId : reqNodeIds) {

						EmpLocationMapping emp = new EmpLocationMapping();
						emp.setActive(active);
						emp.setEmpId(String.valueOf(empId));
						emp.setLocationNodeDataId(String.valueOf(nodeId));
						emp.setOrgId(String.valueOf(req.getOrgId()));
						empLocationMapList.add(emp);
					}
					empLocationMappingDao.saveAll(empLocationMapList);
				}
			} else {
				for (Integer empId : req.getEmpId()) {

					List<EmpLocationMapping> empLocationMapList = empLocationMappingDao
							.getSelectedMappingsForEmp(req.getOrgId(), empId, reqNodeIds);
					for (EmpLocationMapping map : empLocationMapList) {
						map.setActive(active);
					}
					empLocationMappingDao.saveAll(empLocationMapList);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return list;

	}


	public String setEmpLevelMappingV2(OHEmpLevelMapping req, String active)
			throws DynamicFormsServiceException {
		List<LocationNodeData> list = new ArrayList<>();
		String res=null;
		try {
			List<String> levels = getOrgLevels(req.getOrgId()).stream().map(x -> x.getLocationNodeDefType())
					.collect(Collectors.toList());
			log.debug("levels before " + levels);
			log.debug("req.getLevelCode():::"+req.getLevelCode());
			levels = levels.subList(levels.indexOf(req.getLevelCode()), levels.size());
			levels = levels.stream().sorted().collect(Collectors.toList());
			log.debug("levels after " + levels);
			Map<String, List<Integer>> levelIdmap = new LinkedHashMap<>();
			for (int i = 0; i < levels.size(); i++) {
				String level = levels.get(i);
				if (i == 0) {
					levelIdmap.put(level, req.getNodesIds());
				} else {
					if (!levelIdmap.isEmpty()) {
						String previousLevel = getPreviousLevel(level);
						log.debug("level " + level + ",previousLevel:" + previousLevel);
						if (levelIdmap.containsKey(previousLevel)) {
							log.debug("LevelIdMap contains previous level " + level);
							List<LocationNodeData> nodeData = locationNodeDataDao.getNodeDataByParentId(req.getOrgId(),
									level, levelIdmap.get(previousLevel));
							List<Integer> idLists = nodeData.stream().map(x -> x.getId()).collect(Collectors.toList());
							System.out.println("idLists  " + idLists);
							levelIdmap.put(level, idLists);
							list.addAll(nodeData);
						}
					}

				}

			}
			log.debug("levelIdmap ::" + levelIdmap);
			List<Integer> reqNodeIds = new ArrayList<>();
			levelIdmap.forEach((k, v) -> {
				reqNodeIds.addAll(v);
			});
			log.debug("reqNodeIds:::" + reqNodeIds);
			
			List<Integer> leafNodes  = new ArrayList<>();
			reqNodeIds.forEach(x->{
				String leafNode = locationNodeDataDao.verifyLeafNode(x);
				if(leafNode.equalsIgnoreCase("yes")) {
					leafNodes.add(x);
				}
			});
			
			Map<Integer,Boolean> taskMap = new HashMap<>();
			for(Integer node: leafNodes) {
				taskMap.put(node, validateOpenTasks(node,req.getEmpId(),req.getOrgId()));
			};
			
			log.debug("taskMap:"+taskMap);
			boolean taskFlag = false;
			for(Map.Entry<Integer,Boolean> e : taskMap.entrySet()) {
				if(e.getValue().equals(true)) {
					taskFlag = true;
					break;
				}
			}
			log.debug("taskFlag:: "+taskFlag);
			
			if (!taskFlag) {
				log.debug("NO Open tasks for the given emp " + req.getEmpId());
				if (active.equalsIgnoreCase("Y")) {
					for (Integer empId : req.getEmpId()) {

						List<EmpLocationMapping> empLocationMapList = new ArrayList<>();
						for (Integer nodeId : reqNodeIds) {

							EmpLocationMapping emp = new EmpLocationMapping();
							emp.setActive(active);
							emp.setEmpId(String.valueOf(empId));
							emp.setLocationNodeDataId(String.valueOf(nodeId));
							emp.setOrgId(String.valueOf(req.getOrgId()));
							empLocationMapList.add(emp);
						}
						empLocationMappingDao.saveAll(empLocationMapList);
					}
				} else {
					for (Integer empId : req.getEmpId()) {

						List<EmpLocationMapping> empLocationMapList = empLocationMappingDao
								.getSelectedMappingsForEmp(req.getOrgId(), empId, reqNodeIds);
						for (EmpLocationMapping map : empLocationMapList) {
							map.setActive(active);

						}
						empLocationMappingDao.saveAll(empLocationMapList);
					}

				}	
				res = "Successfully removed all active mappings for selected Node";
			} else {
				log.debug("Open tasks are there for the given emp " + req.getEmpId());
				String removed="";
				String notRemoved="";
				for (Integer empId : req.getEmpId()) {
					for (Map.Entry<Integer, Boolean> e : taskMap.entrySet()) {
						if (e.getValue().equals(false)) {
							List<Integer> nodeIdsToRemove = new ArrayList<>();
							nodeIdsToRemove.add(e.getKey());
							List<EmpLocationMapping> empLocationMapList = empLocationMappingDao
									.getSelectedMappingsForEmp(req.getOrgId(), empId, nodeIdsToRemove);
							for (EmpLocationMapping map : empLocationMapList) {
								map.setActive(active);
							}
							empLocationMappingDao.saveAll(empLocationMapList);
							String tmp = " Removed Mapping for Node "+e.getKey() ;
							removed = removed+tmp+System.lineSeparator();
						}else {
							String tmp = " Open tasks are available for branch "+e.getKey() +" for the given employee" ;
							notRemoved = notRemoved+tmp+System.lineSeparator();
						}
					}

				}
				res = removed +notRemoved;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return res;

	}

	
	private boolean validateOpenTasks(Integer nodeId, List<Integer> empList, Integer orgId) throws DynamicFormsServiceException {

		/*
		 * SELECT * FROM salesDataSetup.dms_workflow_task where task_status='ASSIGNED'
		 * AND universal_id in (SELECT crm_universal_id FROM salesDataSetup.dms_lead
		 * where organization_id=11 and sales_consultant='emp1' and branch_id=21 and
		 * lead_stage!='DROPPED');
		 * 
		 * sample query
		 */
		boolean flag =false;
		try {
			for (Integer empId : empList) {
				DmsBranch branch = dmsBranchDao.getBranchByOrgMpId(nodeId);
				if (null != branch) {
					Integer branchId = branch.getBranchId();
					Optional<DmsEmployee> opt = dmsEmployeeRepo.findById(empId);
					if(opt.isPresent()) {
						log.debug("Getting open tasks for branch :" + branchId);
						String empName = opt.get().getEmpName();
						String query = "SELECT * FROM dms_workflow_task where task_status='ASSIGNED' AND universal_id\r\n"
								+ " in (SELECT crm_universal_id FROM dms_lead where organization_id=" + orgId + " \r\n"
								+ " and sales_consultant='"+empName+"' and branch_id=" + branchId + " and lead_stage!='DROPPED')";
						log.debug("QUERY TO CHECK OPEN TASKS "+query);
						List<Object[]> data = entityManager.createNativeQuery(query).getResultList();
						if(null!=data && !data.isEmpty()) {
							flag =true;
						}
					}else {
						throw new DynamicFormsServiceException("No Employee found in Dms Employee table for employee Id: "+empId, HttpStatus.INTERNAL_SERVER_ERROR);
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			
			throw new DynamicFormsServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return flag;
	}

	private String getPreviousLevel(String level) {

		if (level.equalsIgnoreCase("Level2")) {
			return "Level1";
		}

		if (level.equalsIgnoreCase("Level3")) {
			return "Level2";
		}

		if (level.equalsIgnoreCase("Level4")) {
			return "Level3";
		}

		if (level.equalsIgnoreCase("Level5")) {
			return "Level4";
		}
		if (level.equalsIgnoreCase("Level6")) {
			return "Level5";
		}
		if (level.equalsIgnoreCase("Level7")) {
			return "Level6";
		}
		if (level.equalsIgnoreCase("Level8")) {
			return "Level7";
		}
		if (level.equalsIgnoreCase("Level9")) {
			return "Level8";
		}
		return null;
	}

	@Override
	public List<LocationNodeDef> getOrgLevels(Integer orgId) throws DynamicFormsServiceException {
		List<LocationNodeDef> list = new ArrayList<>();
		try {
			list = locationNodeDefDao.getLevelByOrgID(orgId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return list;
	}

	@Override
	public List<LocationNodeData> getLevelDataNodes(Integer orgId, String levelCode)
			throws DynamicFormsServiceException {
		List<LocationNodeData> list = new ArrayList<>();
		try {
			list = locationNodeDataDao.getNodeDataByLevel(orgId, levelCode);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return list;
	}

	@Override
	public List<LocationNodeData> updateOrgLevels(OHLeveUpdateReq req) throws DynamicFormsServiceException {
		List<LocationNodeData> resList = new ArrayList<>();
		try {
			int orgId = req.getOrgId();
			int empId = req.getEmpId();
			List<String> levels = getOrgLevels(req.getOrgId()).stream().map(x -> x.getLocationNodeDefType())
					.collect(Collectors.toList());
			log.debug("levels for orgId " + orgId + " is " + levels);

			if (levels.isEmpty()) {
				throw new DynamicFormsServiceException(
						"No levels exists for this Organization, please use /level-data-creation api to create levels",
						HttpStatus.INTERNAL_SERVER_ERROR);
			} else {

				for (LevelReq lr : req.getLevelList()) {
					LocationNodeDef locationNodeDef = new LocationNodeDef();
					locationNodeDef.setOrgId(orgId);
					locationNodeDef.setCreatedBy(empId);
					locationNodeDef.setCreatedOn(getCurrentTmeStamp());
					locationNodeDef.setModifiedBy(empId);
					locationNodeDef.setModifiedOn(getCurrentTmeStamp());
					locationNodeDef.setLocationNodeDefName(lr.getLevelDefName());
					locationNodeDef.setLocationNodeDefType(lr.getLevelDefType());
					locationNodeDef.setParentId(lr.getLevelOrder());
					locationNodeDef.setActive("Y");
					Optional<LocationNodeDef> nodeDefOpt = locationNodeDefDao.verifyNodeDef(orgId,
							lr.getLevelDefType());
					if (!nodeDefOpt.isPresent()) {
						locationNodeDefDao.save(locationNodeDef);
					}
				}
				for (LevelReq lr : req.getLevelList()) {
					List<LevelDDData> levelMappings = lr.getLevelData();
					int levelOrder = lr.getLevelOrder();
					log.debug("levelOrder::" + levelOrder);
					if (levelOrder == 1) {
						for (LevelDDData levelDDData : levelMappings) {
							LocationNodeData lData = new LocationNodeData();
							lData.setCananicalName(levelDDData.getCode());
							lData.setCode(levelDDData.getCode());
							lData.setName(levelDDData.getName());
							lData.setCreatedOn(getCurrentTmeStamp());
							lData.setCreatedBy(req.getEmpId());
							lData.setModifiedOn(getCurrentTmeStamp());
							lData.setModifiedBy(req.getEmpId());
							lData.setParentId("0");
							lData.setRefParentId("0");
							lData.setType(lr.getLevelDefType());
							lData.setOrgId(orgId);
							lData.setActive("Y");
							System.out.println("saving node data");
							Optional<LocationNodeData> dbNodeParentlevelData = locationNodeDataDao
									.verifyLevelDataRecord(orgId, lr.getLevelDefType(), levelDDData.getCode());
							if (!dbNodeParentlevelData.isPresent()) {
								log.debug("Level 1 is not there, so inserting into DB");
								resList.add(locationNodeDataDao.save(lData));
							}

						}
						log.debug("Level Data Mapping insertion is done for level1");
					} else {
						String parentLevel = getParentLevel(levelOrder);
						log.debug("Level Data Mapping insertion started for level: " + levelOrder + ",parentLevel: "
								+ parentLevel);
						for (LevelDDData levelDDData : levelMappings) {

							String parentMappingCode = levelDDData.getParentMappingCode();
							log.debug("parentMappingCode " + parentMappingCode);
							System.out.println(
									"levelDDData.getParentMappingCode() " + levelDDData.getParentMappingCode());
							Optional<LocationNodeData> dbNodeParetDataOpt = locationNodeDataDao
									.verifyLevelDataRecord(orgId, parentLevel, levelDDData.getParentMappingCode());
							log.debug("dbNodeParetDataOpt " + dbNodeParetDataOpt.isPresent());
							if (dbNodeParetDataOpt.isPresent()) {
								LocationNodeData parentData = dbNodeParetDataOpt.get();
								log.debug("dbNodeParentlevelData is presne");
								int parentMappingId = parentData.getId();

								LocationNodeData lData = new LocationNodeData();
								lData.setCananicalName(parentData.getCananicalName() + "/" + levelDDData.getCode());
								lData.setCode(levelDDData.getCode());
								lData.setName(levelDDData.getName());
								lData.setCreatedOn(getCurrentTmeStamp());
								lData.setCreatedBy(req.getEmpId());
								lData.setModifiedOn(getCurrentTmeStamp());
								lData.setModifiedBy(req.getEmpId());
								lData.setParentId(String.valueOf(parentMappingId));
								lData.setRefParentId(String.valueOf(parentMappingId));
								lData.setType(lr.getLevelDefType());
								lData.setOrgId(orgId);
								lData.setActive("Y");
								// LocationNodeData nodeData = locationNodeDataDao.save(lData);

								Optional<LocationNodeData> dbNodeParentlevelData = locationNodeDataDao
										.verifyLevelDataRecord(orgId, lr.getLevelDefType(), levelDDData.getCode());
								if (!dbNodeParentlevelData.isPresent()) {
									log.debug("Data is not there, so inserting into DB");
									resList.add(locationNodeDataDao.save(lData));
								}
							}
						}
						log.debug("Level Data Mapping insertion ended for level: " + levelOrder + ",parentLevel: "
								+ parentLevel);

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return resList;

	}

	@Override
	public String updateEmpLevelMapping(OHEmpLevelUpdateMapReq req) throws DynamicFormsServiceException {
		String res = null;
		try {
			Integer orgId = req.getOrgId();
			boolean removeFlag = req.isRemoveCurrentActiveLevel();
			log.debug("Remove Current Flag is ::" + removeFlag);

			if (removeFlag) {
				try {
					for (Integer empId : req.getEmpId()) {
						log.debug("removing active level for emp id " + empId);
						empLocationMappingDao.updateActiveEmpMapStatus(orgId, empId);
						res = "Successfully removed active mappings for given employee list " + req.getEmpId();
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new DynamicFormsServiceException(
							"Error ocurred while removing the active mappings for given employees",
							HttpStatus.INTERNAL_SERVER_ERROR);
				}
				res = "removed successfully";
			}
			List<Integer> removeNodeIdsList = req.getRemoveActiveNodeIds();
			if (null != removeNodeIdsList && !removeNodeIdsList.isEmpty()) {
				log.debug("removeNodeIdsList is :" + removeNodeIdsList);
				try {
					for (Integer empId : req.getEmpId()) {
						for (Integer nodeIdToRemove : removeNodeIdsList) {
							List<LocationNodeData> activeParentNodeList = getActiveEmpMappings(orgId, empId);
							List<String> levels = activeParentNodeList.stream().map(x -> x.getType())
									.collect(Collectors.toList());
							levels = levels.stream().sorted().collect(Collectors.toList());
							log.debug("Levels list " + levels);
							OHEmpLevelMapping rq = new OHEmpLevelMapping();
							List<Integer> eList = new ArrayList<>();
							eList.add(empId);
							rq.setEmpId(eList);
							rq.setOrgId(orgId);
							rq.setLevelCode(locationNodeDataDao.getLevelname(nodeIdToRemove));
							rq.setNodesIds(removeNodeIdsList);
							res = setEmpLevelMappingV2(rq, "N");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new DynamicFormsServiceException(
							"Error ocurred while removing the selected nodes for given employees",
							HttpStatus.INTERNAL_SERVER_ERROR);
				}
			
			}
			List<Integer> updatesNodesInCurrentLevel = req.getUpdateNodesInCurrentLevel();
			if (null != updatesNodesInCurrentLevel && !updatesNodesInCurrentLevel.isEmpty()) {
				log.debug("updatesNodesInCurrentLevel is :" + removeNodeIdsList);
				try {
					for (Integer empId : req.getEmpId()) {
						List<LocationNodeData> activeParentNodeList = getActiveEmpMappings(orgId, empId);
						List<String> levels = activeParentNodeList.stream().map(x -> x.getType())
								.collect(Collectors.toList());
						levels = levels.stream().sorted().collect(Collectors.toList());
						log.debug("Levels list " + levels);
						OHEmpLevelMapping rq = new OHEmpLevelMapping();
						List<Integer> eList = new ArrayList<>();
						eList.add(empId);
						rq.setEmpId(eList);
						rq.setOrgId(orgId);
						rq.setLevelCode(levels.get(0));
						rq.setNodesIds(updatesNodesInCurrentLevel);

						setEmpLevelMapping(rq, "Y");

					}

				} catch (Exception e) {
					e.printStackTrace();
					throw new DynamicFormsServiceException(
							"Error ocurred while adding the active mappings for given employees",
							HttpStatus.INTERNAL_SERVER_ERROR);
				}
				res = "Successfully added selected nodes for given employees";
			}

			List<Integer> newlyaddedNodesList = req.getNewlyAddedNodeIds();
			if (null != newlyaddedNodesList && !newlyaddedNodesList.isEmpty()) {
				try {
					if (null != req.getNewLevelAdded() && validateNewLevelAdded(req.getNewLevelAdded())) {
						log.debug("newlyaddedNodesList is :" + newlyaddedNodesList + " and level :"
								+ req.getNewLevelAdded());
						OHEmpLevelMapping rq = new OHEmpLevelMapping();
						rq.setEmpId(req.getEmpId());
						rq.setOrgId(orgId);
						rq.setLevelCode(req.getNewLevelAdded());
						rq.setNodesIds(newlyaddedNodesList);
						setEmpLevelMapping(rq, "Y");
					} else {
						throw new DynamicFormsServiceException(
								"Please check the level entered,given level is not valid one", HttpStatus.BAD_REQUEST);
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new DynamicFormsServiceException(
							"Error ocurred while adding the active mappings for given employees",
							HttpStatus.INTERNAL_SERVER_ERROR);
				}
				res = "Successfully added given level with mappings for given employees";
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return res;
	}

	private boolean validateNewLevelAdded(String newLevelAdded) {

		if (newLevelAdded.startsWith("Level")) {
			return true;
		}
		return false;
	}

	@Override
	public List<LocationNodeData> getActiveEmpMappings(Integer orgId, Integer empId)
			throws DynamicFormsServiceException {
		List<LocationNodeData> list = new ArrayList<>();
		try {
			list = locationNodeDataDao.getActiveLevelsForEmp(orgId, empId);
			List<String> levels = list.stream().map(x -> x.getType()).sorted().collect(Collectors.toList());
			log.debug("Levels list " + levels);
			if (null != levels && !levels.isEmpty()) {
				list = list.stream().filter(x -> x.getType().equalsIgnoreCase(levels.get(0)))
						.collect(Collectors.toList());
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return list;
	}

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	DmsEmployeeRepo dmsEmployeeRepo;

	@Autowired
	SalesGapServiceImpl salesGapServiceImpl;

	@Override
	public Map<String, Object> getEmployeesListWithMapping(Integer pageNo, Integer size,Integer orgId)
			throws DynamicFormsServiceException {
		List<EmployeeRoot> empRootList = new ArrayList<>();
		Map<String, Object> map = new LinkedHashMap<>();
		try {
	

			List<DmsEmployee> empList =dmsEmployeeRepo.getEmployeesByOrg(orgId);
			for (DmsEmployee dmsEmp : empList) {
				EmployeeRoot emp = new EmployeeRoot();

				emp.setEmpId(dmsEmp.getEmp_id());
				emp.setOrgId(dmsEmp.getOrg());
				emp.setBranchId(dmsEmp.getBranch());
				emp.setCognitoName(dmsEmp.getCogintoName());
				emp.setDesignationName(salesGapServiceImpl.getDesignationName(dmsEmp.getDesignationId()));
				emp.setDesignation(dmsEmp.getDesignationId());
			;
				emp.setReportingTo(salesGapServiceImpl.getEmpName(dmsEmp.getReportingTo()));
				emp.setEmpName(dmsEmp.getEmpName());

				if (null != dmsEmp.getOrg()) {
					List<LocationNodeData> activeParentNodeList = getActiveEmpMappings(
							Integer.parseInt(dmsEmp.getOrg()), dmsEmp.getEmp_id());

					List<String> levels = activeParentNodeList.stream().map(x -> x.getType())
							.collect(Collectors.toList());
					levels = levels.stream().sorted().collect(Collectors.toList());

					if (null != levels && !levels.isEmpty()) {
						emp.setMappedLevel(levels.get(0));
					}
				}
				emp.setLevelDisplayName(locationNodeDefDao.getLevelnameByType(emp.getMappedLevel(), orgId));
				
				
				Map<String, Object> mappings = getMappingByEmpId(dmsEmp.getEmp_id() );
				List<String> mapList = new ArrayList<>();
				mappings.forEach((k, v) -> {
					List<LocationNodeData> dataList = (List<LocationNodeData>) v;
					if (null != dataList) {

						dataList.forEach(x -> {

							LocationNodeData data = (LocationNodeData) x;
							mapList.add(data.getCananicalName());
						});
					}

				});;
				TargetRoleRes roleres = salesGapServiceImpl.getEmpRoleDataV2(dmsEmp.getEmp_id());
				if (roleres != null) {
					emp.setHrmsRole(roleres.getRoleName());
					emp.setHrmsRoleId(roleres.getRoleId());
				}
				emp.setMappings(mapList);
				emp.setOrgName(salesGapServiceImpl.getOrgName(emp.getOrgId()));

				empRootList.add(emp);
			}
			pageNo = pageNo + 1;
			int totalCnt = empRootList.size();
			int fromIndex = size * (pageNo - 1);
			int toIndex = size * pageNo;

			if (toIndex > totalCnt) {
				toIndex = totalCnt;
			}
			if (fromIndex > toIndex) {
				fromIndex = toIndex;
			}
			empRootList = empRootList.subList(fromIndex, toIndex);
			map.put("totalCnt", totalCnt);
			map.put("pageNo", pageNo);
			map.put("size", size);
			map.put("data", empRootList);

		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return map;
	}

	private void addBranchMappings(Integer orgId) throws DynamicFormsServiceException {
		try {
			List<LocationNodeDef> defList = getOrgLevels(orgId);

			if (null != defList && !defList.isEmpty()) {
				List<String> levels = defList.stream().map(x -> x.getLocationNodeDefType()).sorted()
						.collect(Collectors.toList());
				String leaflevel = levels.get(levels.size() - 1);
				log.debug("leaflevel for org " + orgId + " is " + leaflevel);

				List<LocationNodeData> dataList = locationNodeDataDao.getNodeDataByLevel(orgId, leaflevel);

				if (null != dataList && !dataList.isEmpty()) {

				} else {

					throw new DynamicFormsServiceException("No Data nodes present for the given org and level",
							HttpStatus.INTERNAL_SERVER_ERROR);
				}

			} else {
				throw new DynamicFormsServiceException("No Levels present for the given org",
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	boolean isValidPinCode(String pinCode) {

		String regex = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$";
		Pattern p = Pattern.compile(regex);
		if (pinCode == null) {
			return false;
		}
		java.util.regex.Matcher m = p.matcher(pinCode);

		return m.matches();
	}

	@Override
	public List<LocationDefNodeRes> getActiveEmpMappingsAll(Integer orgId) throws DynamicFormsServiceException {
		List<LocationNodeData> list = new ArrayList<>();
		List<LocationDefNodeRes> defList = new ArrayList<>();
		AcitveMappingOrgChartRes res = new AcitveMappingOrgChartRes();

		try {
			defList = ObjectMapperUtils.mapAll(locationNodeDefDao.getLevelByOrgID(orgId), LocationDefNodeRes.class);
			list = locationNodeDataDao.getActiveLevelsForOrg(orgId);
			List<String> levels = list.stream().map(x -> x.getType()).sorted().collect(Collectors.toList());
			log.debug("Levels list " + levels);
			Map<String, List<LocationNodeData>> map = new LinkedHashMap<>();
			if (null != levels && !levels.isEmpty()) {
				for (String level : levels) {
					map.put(level, list.stream().filter(x -> x.getType().equalsIgnoreCase(level))
							.collect(Collectors.toList()));
				}
			}
			res.setMap(map);
			for (LocationDefNodeRes def : defList) {
				List<LocationNodeData> list_2 = map.get(def.getLocationNodeDefType());
				if (null != list_2 && !list_2.isEmpty()) {
					String s = list_2.stream().map(x -> x.getLeafNode()).findAny().get();
					if (s.equalsIgnoreCase("YES")) {
						def.setLeafLevel(true);
					} else {
						def.setLeafLevel(false);

					}
				} else {
					def.setLeafLevel(false);
				}
				def.setNodes(map.get(def.getLocationNodeDefType()));
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return defList;
	}

	@Override
	public List<EmployeeRootV2> getMappedEmployees(String type,String orgId) throws DynamicFormsServiceException {

		List<EmployeeRoot> empRootList = new ArrayList<>();
		Map<String, Object> map = new LinkedHashMap<>();
		List<EmployeeRootV2> v2List = new ArrayList<>();
		try {
			List<DmsEmployee> empList = dmsEmployeeRepo.getEmployeesByOrg(Integer.parseInt(orgId));
			for (DmsEmployee dmsEmp : empList) {
			
				EmployeeRoot emp = new EmployeeRoot();
				emp.setEmpId(dmsEmp.getEmp_id());
				emp.setOrgId(dmsEmp.getOrg());
				emp.setEmpName(dmsEmp.getEmpName());
				if (null != dmsEmp.getOrg()) {
					List<LocationNodeData> activeParentNodeList = getActiveEmpMappings(
							Integer.parseInt(dmsEmp.getOrg()), dmsEmp.getEmp_id());
					List<String> levels = activeParentNodeList.stream().map(x -> x.getType())
							.collect(Collectors.toList());
					levels = levels.stream().sorted().collect(Collectors.toList());
					if (null != levels && !levels.isEmpty()) {
						emp.setMappedLevel(levels.get(0));
					}
				}
				empRootList.add(emp);
				
			}
			v2List = ObjectMapperUtils.mapAll(empRootList, EmployeeRootV2.class);
			if (type.equalsIgnoreCase("mapped")) {
				v2List = v2List.stream().filter(x -> x.getMappedLevel() != null).collect(Collectors.toList());

				for (EmployeeRootV2 empv2 : v2List) {

					empv2.setNodes(locationNodeDataDao.getActiveLevelsForEmpWithOutOrg(empv2.getEmpId()));
				}

			}
			if (type.equalsIgnoreCase("unmapped")) {
				v2List = v2List.stream().filter(x -> x.getMappedLevel() == null).collect(Collectors.toList());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return v2List;

	}

	@Override
	public String updateOrgLevels(OHLevelUpdateReq req) throws DynamicFormsServiceException {

		log.debug("updateOrgLevels(){}");
		List<LocationNodeData> list = new ArrayList<>();
		try {
			Integer orgId = req.getOrgId();
			String levelCode = req.getLevelCode();
			String displayName = req.getUpdateDisplayName();

			if (null != displayName && displayName.length() > 0) {
				log.debug("Updating displayName for the given level " + levelCode + " of org " + orgId);
				locationNodeDefDao.updateDisplayName(orgId, req.getLevelCode(), displayName);
			}

			List<Integer> datanodes = req.getRemoveDataNodes();
			System.out.println("datanodes " + datanodes);
			if (null != datanodes && !datanodes.isEmpty()) {
				List<String> levels = getOrgLevels(orgId).stream().map(x -> x.getLocationNodeDefType())
						.collect(Collectors.toList());
				log.debug("levels before " + levels);
				levels = levels.subList(levels.indexOf(levelCode), levels.size());
				levels = levels.stream().sorted().collect(Collectors.toList());
				log.debug("levels after " + levels);
				Map<String, List<Integer>> levelIdmap = new LinkedHashMap<>();

				for (int i = 0; i < levels.size(); i++) {
					String level = levels.get(i);
					if (i == 0) {
						levelIdmap.put(level, datanodes);
					} else {
						if (!levelIdmap.isEmpty()) {
							String previousLevel = getPreviousLevel(level);
							log.debug("level " + level + ",previousLevel:" + previousLevel);
							if (levelIdmap.containsKey(previousLevel)) {
								log.debug("LevelIdMap contains previous level " + level);
								List<LocationNodeData> nodeData = locationNodeDataDao
										.getNodeDataByParentId(req.getOrgId(), level, levelIdmap.get(previousLevel));
								List<Integer> idLists = nodeData.stream().map(x -> x.getId())
										.collect(Collectors.toList());
								levelIdmap.put(level, idLists);
								list.addAll(nodeData);
							}
						}

					}

				}
				log.debug("levelIdmap ::" + levelIdmap);
				List<Integer> reqNodeIds = new ArrayList<>();
				levelIdmap.forEach((k, v) -> {
					reqNodeIds.addAll(v);
				});
				log.debug("reqNodeIds to delete:::" + reqNodeIds);

				locationNodeDataDao.removeOrgLevelNodes(orgId, reqNodeIds);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return "Data Updated Successfully";

	}

	@Override
	public String updateNodes(OHNodeUpdateReq req) throws DynamicFormsServiceException {

		try {
			List<LocationNodeDataV2> activeParentNodeList = ObjectMapperUtils
					.mapAll(locationNodeDataDao.getActiveLevelsForOrg(req.getOrgId()), LocationNodeDataV2.class);
			List<String> levels = activeParentNodeList.stream().map(x -> x.getType()).collect(Collectors.toList());
			levels = levels.stream().sorted().collect(Collectors.toList());
			log.debug("USER LEVELS " + levels + " and size:" + levels.size());
			String orgLeafLevel = null;
			if (null != levels && levels.size() > 0) {
				orgLeafLevel = levels.get(levels.size() - 1);
				log.debug("orgLeafLevel:::" + orgLeafLevel);
			}
			String inputLeafLevel = locationNodeDataDao.getLevelname(req.getDataNodeId());
			log.debug("inputLeafLevel:::"+inputLeafLevel);
			
			if(inputLeafLevel.equalsIgnoreCase(orgLeafLevel)) {
				log.debug("Updating for leaf level");
				locationNodeDataDao.updateNodeDisplayName(req.getOrgId(), req.getDataNodeId(), req.getUpdateNodeNameTo());
				dmsBranchDao.updateBranchName(req.getDataNodeId(),req.getUpdateNodeNameTo());				
			}else {
				log.debug("Updating for non leaf level");
				locationNodeDataDao.updateNodeDisplayName(req.getOrgId(), req.getDataNodeId(), req.getUpdateNodeNameTo());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return "Updated node name Successfully";
	}

	@Override
	public Map<String, Object> getMappingByEmpId(Integer empId) throws DynamicFormsServiceException {
		Map<String, Object> map = new LinkedHashMap<>();
		try {
			List<LocationNodeData> activeParentNodeList = locationNodeDataDao.getActiveLevelsForEmpWithOutOrg(empId);
			List<String> levels = activeParentNodeList.stream().map(x -> x.getType()).collect(Collectors.toList());
			levels = levels.stream().sorted().collect(Collectors.toList());
			if (null != levels && !levels.isEmpty()) {
				for (String level : levels) {
					map.put(level, activeParentNodeList.stream().filter(x -> x.getType().equalsIgnoreCase(level))
							.collect(Collectors.toList()));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return map;
	}


	private List<TargetDropDownV2> buildDropDown(Integer id, Integer branch, Integer orgId) {
		//String query = "SELECT emp_id,emp_name FROM dms_employee where reporting_to=<ID> and branch=<BRANCH> and org=<ORGID>";
		String query = "\r\n"
				+ "select emp_id,emp_name from dms_employee where org = <ORGID> "
				+ "and reporting_to=<ID> and emp_id in (select emp_id from emp_location_mapping where org_id=<ORGID>"
				+ " and location_node_data_id ='<BRANCH>')";
		query = query.replaceAll("<ID>", String.valueOf(id));
		query = query.replaceAll("<BRANCH>", String.valueOf(branch));
		query = query.replaceAll("<ORGID>", String.valueOf(orgId));
		log.debug("buildDropDown query "+query);
		List<TargetDropDownV2> list = new ArrayList<>();
		List<Object[]> data = entityManager.createNativeQuery(query).getResultList();

		for (Object[] arr : data) {
			TargetDropDownV2 trRoot = new TargetDropDownV2();
			trRoot.setCode(String.valueOf(arr[0]));
			trRoot.setName(String.valueOf(arr[1]));
			
			list.add(trRoot);
		}
		list = list.stream().distinct().collect(Collectors.toList());
		

		return list;
	}
	
	private List<TargetDropDownV2> buildDropDownV2(Integer id, Integer branch, Integer orgId) {
		//String query = "SELECT emp_id,emp_name FROM dms_employee where reporting_to=<ID> and branch=<BRANCH> and org=<ORGID>";
		String query = "\r\n"
				+ "select emp_id,emp_name from dms_employee where org = <ORGID> "
				+ "and reporting_to=<ID> and emp_id in (select emp_id from emp_location_mapping where org_id=<ORGID>"
				+ " and location_node_data_id in (select org_map_id from dms_branch where branch_id=<BRANCH>))";
		query = query.replaceAll("<ID>", String.valueOf(id));
		query = query.replaceAll("<BRANCH>", String.valueOf(branch));
		query = query.replaceAll("<ORGID>", String.valueOf(orgId));
		log.debug("buildDropDown query "+query);
		List<TargetDropDownV2> list = new ArrayList<>();
		List<Object[]> data = entityManager.createNativeQuery(query).getResultList();

		for (Object[] arr : data) {
			TargetDropDownV2 trRoot = new TargetDropDownV2();
			trRoot.setCode(String.valueOf(arr[0]));
			trRoot.setName(String.valueOf(arr[1]));
			
			list.add(trRoot);
		}
		list = list.stream().distinct().collect(Collectors.toList());
		

		return list;
	}



	@Override
	public Map<String, Object> getActiveDropdownsV2(List<Integer> levelList, Integer orgId, Integer empId)
			throws DynamicFormsServiceException {
		List<LocationNodeData> list = new ArrayList<>();
		List<Integer> reqNodeIds = new ArrayList<>();
		Map<String, Object> resMap = new LinkedHashMap<>();
		try {
			for (Integer nodeId : levelList) {
				log.debug("nodeId::::"+nodeId);
				String levelName = locationNodeDataDao.getLevelname(nodeId);
				log.debug("Given node level " + levelName + " and nodeId " + nodeId);
				List<String> levels = getOrgLevels(orgId).stream().map(x -> x.getLocationNodeDefType()).collect(Collectors.toList());
				log.debug("levels before " + levels);
				log.debug("levelName::"+levelName);
				levels = levels.subList(levels.indexOf(levelName), levels.size());
				levels = levels.stream().sorted().collect(Collectors.toList());
				log.debug("levels after " + levels);
				Map<String, List<Integer>> levelIdmap = new LinkedHashMap<>();
				for (int i = 0; i < levels.size(); i++) {
					String level = levels.get(i);
					if (i == 0) {
						List<Integer> tmp = new ArrayList<>();
						tmp.add(nodeId);
						levelIdmap.put(level, tmp);
					} else {
						if (!levelIdmap.isEmpty()) {
							String previousLevel = getPreviousLevel(level);
							log.debug("level " + level + ",previousLevel:" + previousLevel);
							if (levelIdmap.containsKey(previousLevel)) {
								log.debug("LevelIdMap contains previous level " + level);
								List<LocationNodeData> nodeData = locationNodeDataDao.getNodeDataByParentId(orgId,
										level, levelIdmap.get(previousLevel));
								List<Integer> idLists = nodeData.stream().map(x -> x.getId())
										.collect(Collectors.toList());
								levelIdmap.put(level, idLists);
								list.addAll(nodeData);
							}
						}

					}
				}
				log.debug("levelIdmap ::" + levelIdmap);

				levelIdmap.forEach((k, v) -> {
					reqNodeIds.addAll(v);
				});
			}

			log.debug("reqNodeIds:::" + reqNodeIds);
			List<LocationNodeDataV2> activeParentNodeList = ObjectMapperUtils
					.mapAll(locationNodeDataDao.getActiveLevelsForEmpWithOutOrg(empId), LocationNodeDataV2.class);
			List<String> levels = activeParentNodeList.stream().map(x -> x.getType()).collect(Collectors.toList());
			levels = levels.stream().sorted().collect(Collectors.toList());
			log.debug("USER LEVELS " + levels + " and size:" + levels.size());
			String leafLevel = null;
			if (null != levels && levels.size() > 0) {
				leafLevel = levels.get(levels.size() - 1);
				log.debug("leafLevel:::" + leafLevel);
			}
			List<LocationNodeData> leafNodeList = locationNodeDataDao.getNodeDataByLevel(orgId, leafLevel);

			List<Integer> leafNodeIdList = leafNodeList.stream().map(x -> x.getId()).collect(Collectors.toList());
			List<Integer> finalleafNodeIdList = new ArrayList<>();
			for (Integer i : reqNodeIds) {
				if (leafNodeIdList.contains(i)) {
					finalleafNodeIdList.add(i);
				}
			}
			log.debug("finalleafNodeIdList::" + finalleafNodeIdList);

			for (Integer nodeId : finalleafNodeIdList) {
				log.debug("Getting data for nodeId::"+nodeId);
				DmsBranch branch = dmsBranchDao.getBranchByOrgMpId(nodeId);
				int branchId = branch.getBranchId();
				log.debug("branchId::" + branchId);
				List<DmsEmployee> branchEmpList = dmsEmployeeRepo.getEmployeesByOrg(orgId);
				Optional<DmsEmployee> empOpt = branchEmpList.stream().filter(x -> x.getEmp_id() == empId).findAny();
				System.out.println("empOpt ::" + empOpt.isPresent());
				if (empOpt.isPresent()) {
					DmsEmployee emp = empOpt.get();
					//Map<String, Object> reportingHierarchyMap = getReportingHierarchy(emp, branchId, orgId);
					Map<String, Object> reportingHierarchyMap = getReportingHierarchy(emp, nodeId, orgId);
					reportingHierarchyMap = formatReportingHierarchy(reportingHierarchyMap);
					resMap.put(String.valueOf(branchId), reportingHierarchyMap);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return resMap;
	}

	@Override
	public Map<String, Object> getActiveDropdowns(Integer orgId, Integer empId) throws DynamicFormsServiceException {
		Map<String, Object> map = new LinkedHashMap<>();
		try {
			List<LocationNodeDataV2> activeParentNodeList = ObjectMapperUtils
					.mapAll(locationNodeDataDao.getActiveLevelsForEmpWithOutOrg(empId), LocationNodeDataV2.class);
			List<String> levels = activeParentNodeList.stream().map(x -> x.getType()).collect(Collectors.toList());
			levels = levels.stream().sorted().collect(Collectors.toList());
			log.debug("USER LEVELS " + levels + " and size:" + levels.size());
			if (null != levels && levels.size() > 0) {
				String leafLevel = levels.get(levels.size() - 1);
				log.debug("leafLevel:::" + leafLevel);
				Integer maxLevel = getMaxLevel();
				log.debug("maxLevel:::" + maxLevel);
				if (null != levels && !levels.isEmpty()) {
					for (String level : levels) {
						if (level.equalsIgnoreCase(leafLevel)) {
							List<LocationNodeDataV2> leafNodes = activeParentNodeList.stream()
									.filter(x -> x.getType().equalsIgnoreCase(level)).collect(Collectors.toList());

							for (LocationNodeDataV2 ld : leafNodes) {
								int nodeId = ld.getId();
								log.debug("Node Id " + nodeId);
								DmsBranch branch = dmsBranchDao.getBranchByOrgMpId(nodeId);
								int branchId = branch.getBranchId();
								log.debug("branchId::" + branchId);
								List<DmsEmployee> branchEmpList = dmsEmployeeRepo.getEmployeesByOrgBranch(orgId,
										branchId);
								log.debug("branchEmpList size ::" + branchEmpList.size());
								Optional<DmsEmployee> empOpt = branchEmpList.stream()
										.filter(x -> x.getEmp_id() == empId).findAny();
								System.out.println("empOpt ::" + empOpt.isPresent());
								if (empOpt.isPresent()) {
									DmsEmployee emp = empOpt.get();
									Map<String, Object> reportingHierarchyMap = getReportingHierarchy(emp, branchId,
											orgId);
									reportingHierarchyMap = formatReportingHierarchy(reportingHierarchyMap);
									ld.setChilds(reportingHierarchyMap);
								}
							}

							map.put(level, leafNodes);
						} else {

							map.put(level, activeParentNodeList.stream()
									.filter(x -> x.getType().equalsIgnoreCase(level)).collect(Collectors.toList()));
						}
					}
				}
			} else {
				throw new DynamicFormsServiceException("No Org Hierarchy Data is present for the given empId",
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return map;
	}

	private Map<String, Object> formatReportingHierarchy(Map<String, Object> hMap) {
		if (null != hMap) {
			AtomicInteger a = new AtomicInteger(0);
			for (Map.Entry<String, Object> mapentry : hMap.entrySet()) {
				Map<String, Object> map2 = (Map<String, Object>) mapentry.getValue();
				a.incrementAndGet();
				for (Map.Entry<String, Object> mapentry_1 : map2.entrySet()) {
					List<TargetDropDownV2> ddList = (List<TargetDropDownV2>) mapentry_1.getValue();
					ddList=	ddList.stream().distinct().collect(Collectors.toList());
					ddList.forEach(x -> {
						x.setParentId(mapentry_1.getKey());
						x.setOrder(a.intValue());
					});

				}
			}

			for (Map.Entry<String, Object> mapentry : hMap.entrySet()) {
				Map<String, Object> map2 = (Map<String, Object>) mapentry.getValue();
				List<TargetDropDownV2> list = new ArrayList<>();

				for (Map.Entry<String, Object> mapentry_1 : map2.entrySet()) {
					List<TargetDropDownV2> ddList = (List<TargetDropDownV2>) mapentry_1.getValue();
					ddList=	ddList.stream().distinct().collect(Collectors.toList());
					list.addAll(ddList);
				}
				list=	list.stream().distinct().collect(Collectors.toList());
				hMap.put(mapentry.getKey(), list);
			}
		}
		return hMap;
	}

	public Map<String, Object> getReportingHierarchy(DmsEmployee emp, int branchId, Integer orgId) {
		log.debug("Inside getReportingHierarchy,branchId: "+branchId+",orgId:"+orgId);
		Map<String, Object> empDtaMap = new LinkedHashMap<>();
		try {
			Integer maxLevel = getMaxLevel();
			log.debug("maxLevel:::" + maxLevel);
			Integer empLevel = 0;
			if (null != emp.getDesignationId()) {
				Integer empDesId = Integer.parseInt(emp.getDesignationId());
				log.debug("empDesigntaion:::" + empDesId);
				Optional<DmsDesignation> desOpt = dmsDesignationRepo.findById(empDesId);
				if (desOpt.isPresent()) {
					empLevel = desOpt.get().getLevel();
				} else {
					throw new DynamicFormsServiceException(
							"Given emp does not have valid designation in dms_designation", HttpStatus.BAD_REQUEST);
				}

				log.debug("Given emp level is " + empLevel);

				List<TargetDropDownV2> empList = buildDropDown(emp.getEmp_id(), branchId, orgId);
				if (!empList.isEmpty() && maxLevel >= (empLevel + 1)) {
					Map<String, Object> map = new LinkedHashMap<>();
					map.put(String.valueOf(emp.getEmp_id()), empList);
					empDtaMap.put(getLevelName(empLevel + 1), map);
				}
				Map<String, Object> map1 = new LinkedHashMap<>();
				Map<String, Object> map2 = new LinkedHashMap<>();
				Map<String, Object> map3 = new LinkedHashMap<>();
				Map<String, Object> map4 = new LinkedHashMap<>();
				Map<String, Object> map5 = new LinkedHashMap<>();
				for (TargetDropDownV2 td : empList) {
					List<TargetDropDownV2> empList1 = buildDropDown(Integer.parseInt(td.getCode()), branchId, orgId);
					map1.put(td.getCode(), empList1);

					for (TargetDropDownV2 td1 : empList1) {
						List<TargetDropDownV2> empList2 = buildDropDown(Integer.parseInt(td1.getCode()), branchId,
								orgId);
						map2.put(td1.getCode(), empList2);

						for (TargetDropDownV2 td2 : empList2) {
							List<TargetDropDownV2> empList3 = buildDropDown(Integer.parseInt(td2.getCode()), branchId,
									orgId);
							map3.put(td2.getCode(), empList3);

							for (TargetDropDownV2 td3 : empList3) {
								List<TargetDropDownV2> empList4 = buildDropDown(Integer.parseInt(td3.getCode()),
										branchId, orgId);
								map4.put(td3.getCode(), empList4);

								for (TargetDropDownV2 td4 : empList4) {
									List<TargetDropDownV2> empList5 = buildDropDown(Integer.parseInt(td4.getCode()),
											branchId, orgId);
									map5.put(td4.getCode(), empList5);
								}
							}
						}
					}
				}
				if (maxLevel >= (empLevel + 2) && !validateMap(map1)) {
					empDtaMap.put(getLevelName(empLevel + 2), map1);
				}
				if (maxLevel >= (empLevel + 3) && !validateMap(map2)) {
					empDtaMap.put(getLevelName(empLevel + 3), map2);
				}
				if (maxLevel >= (empLevel + 4) && !validateMap(map3)) {
					empDtaMap.put(getLevelName(empLevel + 4), map3);
				}
				if (maxLevel >= (empLevel + 5) && !validateMap(map4)) {
					empDtaMap.put(getLevelName(empLevel + 5), map4);
				}

			}
			log.debug("empDtaMap in getReportingHierarchy:::"+empDtaMap);		
		} catch (Exception e) {
			e.printStackTrace();
		}

		return empDtaMap;
	}

	
	

	public boolean validateMap(Map<String, Object> map) {
		// TODO Auto-generated method stub
		boolean flag = false;

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			List<TargetDropDownV2> tmp = (List<TargetDropDownV2>) entry.getValue();
			if (!tmp.isEmpty()) {
				flag = false;
				break;
			} else {
				flag = true;
			}
		}
		System.out.println("flag:::" + flag);
		return flag;

	}

	@Autowired
	DmsGradeDao dmsGradeDao;

	private Integer getMaxLevel() {

		List<DmsGrade> list = dmsGradeDao.findAll();
		return list.size();

	}

	public String getLevelName(int level) throws DynamicFormsServiceException {

		List<DmsGrade> list = dmsGradeDao.findAll();
		String levelName = null;
		Optional<DmsGrade> opt = list.stream().filter(x -> x.getLevel() == level).findFirst();
		if (opt.isPresent()) {
			levelName = opt.get().getLevelName();
			levelName.replaceAll(",", "/");
		} else {
			throw new DynamicFormsServiceException(
					"There is no valid levelname for given level " + level + " in dms_grade",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return levelName;
	}
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Set<Object> seen = ConcurrentHashMap.newKeySet();
	    return t -> seen.add(keyExtractor.apply(t));
	}

	

	@Override
	public Map<String, Object> getActiveLevels(Integer orgId, Integer empId) throws DynamicFormsServiceException {
		
		Map<String, Object> finalMap = new LinkedHashMap<>();
		Map<String, Object> map = new LinkedHashMap<>();
		try {
			List<LocationNodeDataV2> activeParentNodeList = ObjectMapperUtils.mapAll(locationNodeDataDao.getActiveLevelsForEmpWithOutOrg(empId), LocationNodeDataV2.class);
			List<LocationNodeDef> list = locationNodeDefDao.getLevelByOrgID(orgId);
			List<String> orgLevels = list.stream().map(x->x.getLocationNodeDefType()).collect(Collectors.toList());
			List<String> activeLevels = activeParentNodeList.stream().map(x->x.getType()).distinct().collect(Collectors.toList());
			List<String> disabledLevels = new ArrayList<>(orgLevels);
			disabledLevels.removeAll(activeLevels);
			log.debug("disabledLevels "+disabledLevels);
			log.debug("activeLevels "+activeLevels);
			for(LocationNodeDataV2 d : activeParentNodeList) {
				d.setDisabled("N");
			}
			
			if(null!=disabledLevels && !disabledLevels.isEmpty()) {
				for(String disabledLevel: disabledLevels) {
					List<LocationNodeData> levelDataList = locationNodeDataDao.getNodeDataByLevel(orgId, disabledLevel);
					List<LocationNodeDataV2> levelDataListv2 = ObjectMapperUtils.mapAll(levelDataList, LocationNodeDataV2.class);
					for(LocationNodeDataV2 d : levelDataListv2) {
						d.setDisabled("Y");
					}
					activeParentNodeList.addAll(levelDataListv2);
				}
			}
			List<String> levels = activeParentNodeList.stream().map(x -> x.getType()).collect(Collectors.toList());
			levels = levels.stream().sorted().distinct().collect(Collectors.toList());
			log.debug("USER LEVELS " + levels + " and size:" + levels.size());
			if (null != levels && levels.size() > 0) {
				String leafLevel = levels.get(levels.size() - 1);
				log.debug("leafLevel:::" + leafLevel);
				Integer maxLevel = getMaxLevel();
				log.debug("maxLevel:::" + maxLevel);
			
					for (String level : levels) {
						if (level.equalsIgnoreCase(leafLevel)) {
							List<LocationNodeDataV2> leafNodes = activeParentNodeList.stream().filter(x -> x.getType().equalsIgnoreCase(level)).collect(Collectors.toList());

							for (LocationNodeDataV2 ld : leafNodes) {
								Map<String, Object> empDtaMap = new LinkedHashMap<>();
								int nodeId = ld.getId();
								log.debug("Node Id " + nodeId);
								DmsBranch branch = dmsBranchDao.getBranchByOrgMpId(nodeId);
								int branchId = branch.getBranchId();
								log.debug("branchId::" + branchId);
								List<DmsEmployee> branchEmpList = dmsEmployeeRepo.getEmployeesByOrgBranch(orgId,
										branchId);
								log.debug("branchEmpList size ::" + branchEmpList.size());
						
							}

							map.put(level, leafNodes);
							List<LocationNodeData> levelDataList = locationNodeDataDao.getNodeDataByLevel(orgId, level);
							List<LocationNodeDataV2> levelDataListv2 = ObjectMapperUtils.mapAll(levelDataList, LocationNodeDataV2.class);
							List<LocationNodeDataV2> mapList = (List<LocationNodeDataV2>)map.get(level);
							
							levelDataListv2.forEach(x->{
							
							
								if(!mapList.contains(x)) {
									x.setDisabled("Y");
									mapList.add(x);
								}
							});
								map.put(level, mapList);
								
						} else {

							map.put(level, activeParentNodeList.stream().filter(x -> x.getType().equalsIgnoreCase(level)).collect(Collectors.toList()));
							
							List<LocationNodeData> levelDataList = locationNodeDataDao.getNodeDataByLevel(orgId, level);
							List<LocationNodeDataV2> levelDataListv2 = ObjectMapperUtils.mapAll(levelDataList, LocationNodeDataV2.class);
							List<LocationNodeDataV2> mapList = (List<LocationNodeDataV2>)map.get(level);
							
							levelDataListv2.forEach(x->{
							
							
								if(!mapList.contains(x)) {
									x.setDisabled("Y");
									mapList.add(x);
								}
							});
							map.put(level, mapList);
						//	map.put(getLevelName(level,orgId), mapList);
							
						}
					}
				
			} else {
				throw new DynamicFormsServiceException("No Org Hierarchy Data is present for the given empId",
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
			AtomicInteger atomicInt = new AtomicInteger(0);
			map.forEach((k,v)->{
				atomicInt.getAndIncrement();
				List<LocationNodeDataV2> l = (List<LocationNodeDataV2>)v;
				l.forEach(z->{
					z.setOrder(atomicInt.intValue());
				
				});
			});
			
			map.forEach((k,v)->{
				List<LocationNodeDataV2> l = (List<LocationNodeDataV2>)v;
				Map<String,List<LocationNodeDataV2>> innerMap = new LinkedHashMap<>();
				innerMap.put("sublevels",l);
				map.put(k, innerMap);
				finalMap.put(getLevelName(k, orgId), innerMap);
			});
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return finalMap;

	}
	
	
	private String getLevelName(String level, Integer orgId) {
		System.out.println("level:"+level+",orgId:"+orgId);
		return locationNodeDefDao.getLevelnameByType(level,orgId);
	}

	@Override
	public List<LocationNodeData> setEmpLevelMappingMultiple(OHEmpLevelMappingV2 req, String active)
			throws DynamicFormsServiceException {
		List<LocationNodeData> list = new ArrayList<>();
		try {
			List<LevelMapping> levelMappings = req.getLevels();
			log.debug("levelMappings:::" + levelMappings);
			for (LevelMapping levelMap : levelMappings) {
				log.debug("Started Mapping for " + levelMap.toString());
				List<String> levels = getOrgLevels(req.getOrgId()).stream().map(x -> x.getLocationNodeDefType())
						.collect(Collectors.toList());
				log.debug("levels before " + levels);
				levels = levels.subList(levels.indexOf(levelMap.getLevelCode()), levels.size());
				levels = levels.stream().sorted().collect(Collectors.toList());
				log.debug("levels after " + levels);
				Map<String, List<Integer>> levelIdmap = new LinkedHashMap<>();
				for (int i = 0; i < levels.size(); i++) {
					String level = levels.get(i);
					if (i == 0) {
						levelIdmap.put(level, levelMap.getNodesIds());
					} else {
						if (!levelIdmap.isEmpty()) {
							String previousLevel = getPreviousLevel(level);
							log.debug("level " + level + ",previousLevel:" + previousLevel);
							if (levelIdmap.containsKey(previousLevel)) {
								log.debug("LevelIdMap contains previous level " + level);
								List<LocationNodeData> nodeData = locationNodeDataDao
										.getNodeDataByParentId(req.getOrgId(), level, levelIdmap.get(previousLevel));
								List<Integer> idLists = nodeData.stream().map(x -> x.getId())
										.collect(Collectors.toList());
								System.out.println("idLists  " + idLists);
								levelIdmap.put(level, idLists);
								list.addAll(nodeData);
							}
						}

					}

				}
				log.debug("levelIdmap ::" + levelIdmap);
				List<Integer> reqNodeIds = new ArrayList<>();
				levelIdmap.forEach((k, v) -> {
					reqNodeIds.addAll(v);
				});
				log.debug("reqNodeIds:::" + reqNodeIds);
				if (active.equalsIgnoreCase("Y")) {
					List<EmpLocationMapping> empLocationMapList = new ArrayList<>();
					for (Integer nodeId : reqNodeIds) {
						EmpLocationMapping emp = new EmpLocationMapping();
						emp.setActive(active);
						emp.setEmpId(String.valueOf(levelMap.getEmpId()));
						emp.setLocationNodeDataId(String.valueOf(nodeId));
						emp.setBranchId(dmsAddressDao.getBranchId(nodeId));
						emp.setOrgId(String.valueOf(req.getOrgId()));
						List<Integer> tmpList = new ArrayList<>();
						tmpList.add(nodeId);
						List<EmpLocationMapping> dbList = empLocationMappingDao.getSelectedMappingsForEmp(req.getOrgId(), levelMap.getEmpId(), tmpList);
						if (null != dbList && dbList.isEmpty()) {
							empLocationMapList.add(emp);
						}
					}
					empLocationMappingDao.saveAll(empLocationMapList);
				} else {
					List<EmpLocationMapping> empLocationMapList = empLocationMappingDao.getSelectedMappingsForEmp(req.getOrgId(), levelMap.getEmpId(), reqNodeIds);
					for (EmpLocationMapping map : empLocationMapList) {
						map.setActive(active);
					}
					empLocationMappingDao.saveAll(empLocationMapList);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return list;

	}

	@Override
	public  List<LocationNodeDataV2>  getActiveBranches(Integer orgId, Integer empId) throws DynamicFormsServiceException {
		List<LocationNodeDataV2> leafNodes = new ArrayList<>();
		try {
			List<LocationNodeDataV2> activeParentNodeList = ObjectMapperUtils.mapAll(locationNodeDataDao.getActiveLevelsForEmpWithOutOrg(empId), LocationNodeDataV2.class);
			List<LocationNodeDef> list = locationNodeDefDao.getLevelByOrgID(orgId);
			List<String> orgLevels = list.stream().map(x->x.getLocationNodeDefType()).collect(Collectors.toList());
			List<String> activeLevels = activeParentNodeList.stream().map(x->x.getType()).distinct().collect(Collectors.toList());
			
			List<String> disabledLevels = new ArrayList<>(orgLevels);
			disabledLevels.removeAll(activeLevels);
			
			log.debug("disabledLevels "+disabledLevels);
			log.debug("activeLevels "+activeLevels);
			for(LocationNodeDataV2 d : activeParentNodeList) {
				d.setDisabled("N");
			}
			
			
			

			List<String> levels = activeParentNodeList.stream().map(x -> x.getType()).collect(Collectors.toList());
			levels = levels.stream().sorted().distinct().collect(Collectors.toList());
			log.debug("USER LEVELS " + levels + " and size:" + levels.size());
			if (null != levels && levels.size() > 0) {
				String leafLevel = levels.get(levels.size() - 1);
				log.debug("leafLevel:::" + leafLevel);
				Integer maxLevel = getMaxLevel();
				log.debug("maxLevel:::" + maxLevel);
			
					for (String level : levels) {
						if (level.equalsIgnoreCase(leafLevel)) {
							leafNodes = activeParentNodeList.stream().filter(x -> x.getType().equalsIgnoreCase(level)).collect(Collectors.toList());
						} 
					}
				
			} else {
				throw new DynamicFormsServiceException("No Org Hierarchy Data is present for the given empId",
						HttpStatus.INTERNAL_SERVER_ERROR);
			}
			for(LocationNodeDataV2 l: leafNodes) {
				log.debug("Node Def ID: "+l.getId());
				l.setBranch(dmsBranchDao.getBranchByOrgMpId(l.getId()).getBranchId());
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return leafNodes;

	
	}
	
	
	public Map<String, Object> getReportingHierarchyV2(DmsEmployee emp, int branchId, Integer orgId) {
		log.debug("Inside getReportingHierarchyV2,branchId: "+branchId+",orgId:"+orgId);
		Map<String, Object> empDtaMap = new LinkedHashMap<>();
		try {
			Integer maxLevel = getMaxLevel();
			log.debug("maxLevel:::" + maxLevel);
			Integer empLevel = 0;
			if (null != emp.getDesignationId()) {
				Integer empDesId = Integer.parseInt(emp.getDesignationId());
				log.debug("empDesigntaion:::" + empDesId);
				Optional<DmsDesignation> desOpt = dmsDesignationRepo.findById(empDesId);
				if (desOpt.isPresent()) {
					empLevel = desOpt.get().getLevel();
				} else {
					throw new DynamicFormsServiceException(
							"Given emp does not have valid designation in dms_designation", HttpStatus.BAD_REQUEST);
				}

				log.debug("Given emp level is " + empLevel);

				List<TargetDropDownV2> empList = buildDropDownV2(emp.getEmp_id(), branchId, orgId);
				if (!empList.isEmpty() && maxLevel >= (empLevel + 1)) {
					Map<String, Object> map = new LinkedHashMap<>();
					map.put(String.valueOf(emp.getEmp_id()), empList);
					empDtaMap.put(getLevelName(empLevel + 1), map);
				}
				Map<String, Object> map1 = new LinkedHashMap<>();
				Map<String, Object> map2 = new LinkedHashMap<>();
				Map<String, Object> map3 = new LinkedHashMap<>();
				Map<String, Object> map4 = new LinkedHashMap<>();
				Map<String, Object> map5 = new LinkedHashMap<>();
				for (TargetDropDownV2 td : empList) {
					List<TargetDropDownV2> empList1 = buildDropDownV2(Integer.parseInt(td.getCode()), branchId, orgId);
					map1.put(td.getCode(), empList1);

					for (TargetDropDownV2 td1 : empList1) {
						List<TargetDropDownV2> empList2 = buildDropDownV2(Integer.parseInt(td1.getCode()), branchId,
								orgId);
						map2.put(td1.getCode(), empList2);

						for (TargetDropDownV2 td2 : empList2) {
							List<TargetDropDownV2> empList3 = buildDropDownV2(Integer.parseInt(td2.getCode()), branchId,
									orgId);
							map3.put(td2.getCode(), empList3);

							for (TargetDropDownV2 td3 : empList3) {
								List<TargetDropDownV2> empList4 = buildDropDownV2(Integer.parseInt(td3.getCode()),
										branchId, orgId);
								map4.put(td3.getCode(), empList4);

								for (TargetDropDownV2 td4 : empList4) {
									List<TargetDropDownV2> empList5 = buildDropDownV2(Integer.parseInt(td4.getCode()),
											branchId, orgId);
									map5.put(td4.getCode(), empList5);
								}
							}
						}
					}
				}
				if (maxLevel >= (empLevel + 2) && !validateMap(map1)) {
					empDtaMap.put(getLevelName(empLevel + 2), map1);
				}
				if (maxLevel >= (empLevel + 3) && !validateMap(map2)) {
					empDtaMap.put(getLevelName(empLevel + 3), map2);
				}
				if (maxLevel >= (empLevel + 4) && !validateMap(map3)) {
					empDtaMap.put(getLevelName(empLevel + 4), map3);
				}
				if (maxLevel >= (empLevel + 5) && !validateMap(map4)) {
					empDtaMap.put(getLevelName(empLevel + 5), map4);
				}

			}
			log.debug("empDtaMap in getReportingHierarchy:::"+empDtaMap);		
		} catch (Exception e) {
			e.printStackTrace();
		}

		return empDtaMap;
	}
}
