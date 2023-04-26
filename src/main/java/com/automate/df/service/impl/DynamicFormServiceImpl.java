package com.automate.df.service.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.automate.df.constants.DynamicFormConstants;
import com.automate.df.dao.DropDownMasterRepo;
import com.automate.df.dao.MenuActionRepo;
import com.automate.df.dao.MenuRoleActionRepo;
import com.automate.df.dao.OrgRepo;
import com.automate.df.dao.OrgVerLocMenuRepo;
import com.automate.df.dao.OrgVerLocPageGroupRepo;
import com.automate.df.dao.OrgVerLocPageGrpFieldRepo;
import com.automate.df.dao.OrgVerLocPageRepo;
import com.automate.df.dao.PageGroupRepo;
import com.automate.df.entity.DropDown;
import com.automate.df.entity.MenuAction;
import com.automate.df.entity.MenuRoleAction;
import com.automate.df.entity.OrgVerticalLocationPage;
import com.automate.df.entity.OrgVerticalLocationPageGroup;
import com.automate.df.entity.OrgVerticalLocationPageGroupField;
import com.automate.df.entity.OrgVerticalLocationRoleMenu;
import com.automate.df.entity.PageGroupFieldRoleAccess;
import com.automate.df.error.DynamicUploadExcelError;
import com.automate.df.exception.DynamicFormsServiceException;
import com.automate.df.exception.DynmFormExcelException;
import com.automate.df.model.AutModuleMenuExcelBO;
import com.automate.df.model.ButtonControl;
import com.automate.df.model.ButtonReq;
import com.automate.df.model.ButtonRes;
import com.automate.df.model.DFFieldRes;
import com.automate.df.model.DropDownRes;
import com.automate.df.model.DropdownReq;
import com.automate.df.model.DynamicFormFieldBO;
import com.automate.df.model.DynamicRoleFormBO;
import com.automate.df.model.DynmFormExcelResponseDO;
import com.automate.df.model.DynmMenuExcelResponseDO;
import com.automate.df.model.FieldRes;
import com.automate.df.model.GroupRes;
import com.automate.df.model.Mappings;
import com.automate.df.model.MenuMappings;
import com.automate.df.model.OrgVerticalLocationPageGroupFieldResponse;
import com.automate.df.model.OrgVerticalLocationPageGroupResponse;
import com.automate.df.model.OrgVerticalLocationPageResponse;
import com.automate.df.model.PageRes;
import com.automate.df.model.Role;
import com.automate.df.util.ObjectMapperUtils;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class DynamicFormServiceImpl implements DynamicFormService {

	@Autowired
	Environment env;

	@Autowired
	OrgRepo orgRepo;

	@Autowired
	PageGroupRepo pageGroupRepo;

	@Autowired
	OrgVerLocPageRepo orgVerLocPageRepo;

	@Autowired
	OrgVerLocPageGroupRepo orgVerLocPageGroupRepo;

	@Autowired
	OrgVerLocPageGrpFieldRepo orgVerLocPageGrpFieldRepo;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	OrgVerLocMenuRepo orgVerLocMenuRepo;
	
	@Override
	public PageRes getBusinessFormKeyMappings(Mappings mappings) throws DynamicFormsServiceException {
		log.debug("getBusinessFormKeyMappings(){},UUID " + mappings.getOrgIdentifier() + " pageId:"
				+ mappings.getPageIdentifier() + " role " + mappings.getRoleIdentifier());
		OrgVerticalLocationPage page = null;
		PageRes pageRes = null;
		OrgVerticalLocationPageResponse res = null;
		try {
			int pageId = mappings.getPageIdentifier();
			int roleId = mappings.getRoleIdentifier();
			String mode = mappings.getMode();

			page = orgRepo.findByUUIDValAndPage(mappings.getOrgIdentifier(), mappings.getPageIdentifier());
			System.out.println("page::" + page);

			Map<String, Object> map = new HashMap<>();
			for (PageGroupFieldRoleAccess pageRole : pageGroupRepo.findForPageLevel(pageId, roleId, mode)) {
				Role role = new Role();
				role.setRoleId(pageRole.getRoleId());
				role.setRoleName(pageRole.getPermissionType());
				role.setMode(pageRole.getMode());
				map.put("page", role);
			}

			Map<Integer, Role> groupRoleMap = new HashMap<>();
			for (PageGroupFieldRoleAccess pageRole : pageGroupRepo.findForGroupLevel(pageId, roleId, mode)) {
				Role role = new Role();
				role.setRoleId(pageRole.getRoleId());
				role.setRoleName(pageRole.getPermissionType());
				role.setMode(pageRole.getMode());
				groupRoleMap.put(pageRole.getGroupId(), role);
			}
			map.put("group", groupRoleMap);

			Map<Integer, Role> fieldRoleMap = new HashMap<>();
			for (PageGroupFieldRoleAccess pageRole : pageGroupRepo.findForFieldLevel(pageId, roleId, mode)) {
				Role role = new Role();
				role.setRoleId(pageRole.getRoleId());
				role.setRoleName(pageRole.getPermissionType());
				role.setMode(pageRole.getMode());
				fieldRoleMap.put(pageRole.getFieldId(), role);
			}
			map.put("field", fieldRoleMap);

			Map<Integer, List<Role>> fieldGroupRoleMap = new HashMap<>();
			for (PageGroupFieldRoleAccess pageRole : pageGroupRepo.findForFieldGroupLevel(pageId, roleId, mode)) {
				Role role = new Role();
				role.setRoleId(pageRole.getRoleId());
				role.setRoleName(pageRole.getPermissionType());
				role.setMode(pageRole.getMode());
				role.setFieldId(pageRole.getFieldId());

				if (fieldGroupRoleMap.containsKey(pageRole.getGroupId())) {
					List<Role> roleList = (List<Role>) fieldGroupRoleMap.get(pageRole.getGroupId());
					roleList.add(role);
					fieldGroupRoleMap.put(pageRole.getGroupId(), roleList);
				} else {
					List<Role> roleList = new ArrayList();
					roleList.add(role);
					fieldGroupRoleMap.put(pageRole.getGroupId(), roleList);

				}
			}
			System.out.println("fieldGroupRoleMap " + fieldGroupRoleMap);
			map.put("field-group", fieldGroupRoleMap);

			res = buildResObj(page, map);
			res = filterObject(map, res, mappings.getMode(), mappings.getRoleIdentifier());

			pageRes = buildPageRes(res);
			System.out.println("map::" + map);
		} catch (DataAccessException e) {
			log.error("Failed to process login in request ", e);
			throw new DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return pageRes;
	}

	private PageRes buildPageRes(OrgVerticalLocationPageResponse orgRes) {
		PageRes pageRes = new PageRes();
		pageRes = modelMapper.map(orgRes, PageRes.class);

		List<GroupRes> pageGroupResList = new ArrayList<>();
		for (OrgVerticalLocationPageGroupResponse pageGroup : orgRes.getGroupList()) {
			GroupRes pageGroupRes = new GroupRes();
			pageGroupRes.setGroup(pageGroup.getGroup());
			pageGroupRes.setIconClass(pageGroup.getIconClass());
			pageGroupRes.setId(pageGroup.getId());
			pageGroupRes.setMaxItems(pageGroup.getMaxItems());
			pageGroupRes.setMinItems(pageGroup.getMinItems());
			pageGroupRes.setName(pageGroup.getName());
			pageGroupRes.setNoOfCols(pageGroup.getNoOfCols());

			List<FieldRes> pageGroupFieldResList = new ArrayList<>();
			for (OrgVerticalLocationPageGroupFieldResponse pgGrpField : pageGroup.getFieldList()) {
				FieldRes fieldRes = modelMapper.map(pgGrpField, FieldRes.class);
				if (pgGrpField.getRole().getRoleName().equalsIgnoreCase("edit")) {
					fieldRes.setEditable(true);
				} else {
					fieldRes.setEditable(false);
				}
				pageGroupFieldResList.add(fieldRes);
			}
			pageGroupRes.setFieldList(pageGroupFieldResList);
			pageGroupResList.add(pageGroupRes);
		}
		pageRes.setGroupList(pageGroupResList);
		return pageRes;
	}

	private OrgVerticalLocationPageResponse filterObject(Map<String, Object> map, OrgVerticalLocationPageResponse page,
			String mode, int roleIdentifier) {
		OrgVerticalLocationPageResponse res = new OrgVerticalLocationPageResponse();
		res = modelMapper.map(page, OrgVerticalLocationPageResponse.class);

		Object fieldObj = map.get("field");
		Map<Integer, Role> fieldMap = new HashMap<>();
		if (null != fieldObj) {
			fieldMap = (Map<Integer, Role>) fieldObj;
		}
		System.out.println("fieldMap size ::" + fieldMap);

		if (fieldMap != null && fieldMap.isEmpty()) {
			List<OrgVerticalLocationPageGroupResponse> pageGroupResList = new ArrayList<>();
			for (OrgVerticalLocationPageGroupResponse pageGroup : page.getGroupList()) {
				OrgVerticalLocationPageGroupResponse pageGroupRes = new OrgVerticalLocationPageGroupResponse();
				pageGroupRes.setGroup(pageGroup.getGroup());
				pageGroupRes.setIconClass(pageGroup.getIconClass());
				pageGroupRes.setId(pageGroup.getId());
				pageGroupRes.setMaxItems(pageGroup.getMaxItems());
				pageGroupRes.setMinItems(pageGroup.getMinItems());
				pageGroupRes.setName(pageGroup.getName());
				pageGroupRes.setNoOfCols(pageGroup.getNoOfCols());
				pageGroupRes.setRole(pageGroup.getRole());

				List<OrgVerticalLocationPageGroupFieldResponse> pageGroupFieldResList = new ArrayList<>();
				for (OrgVerticalLocationPageGroupFieldResponse pgGrpField : pageGroup.getFieldList()) {
					OrgVerticalLocationPageGroupFieldResponse fieldRes = modelMapper.map(pgGrpField,
							OrgVerticalLocationPageGroupFieldResponse.class);
					fieldRes.setRole(pgGrpField.getRole());

					// if(pgGrpField.getRole().getRoleId() == roleIdentifier) {
					pageGroupFieldResList.add(fieldRes);
					// }
				}
				pageGroupRes.setFieldList(pageGroupFieldResList);
				System.out.println("pageGroupRes.getRole() ::" + pageGroupRes.getRole());
				if (pageGroupRes.getRole() != null) {
					pageGroupResList.add(pageGroupRes);
				}

			}
			res.setGroupList(pageGroupResList);
		}

		else {

			List<OrgVerticalLocationPageGroupResponse> pageGroupResList = new ArrayList<>();
			for (OrgVerticalLocationPageGroupResponse pageGroup : page.getGroupList()) {
				OrgVerticalLocationPageGroupResponse pageGroupRes = new OrgVerticalLocationPageGroupResponse();
				pageGroupRes.setGroup(pageGroup.getGroup());
				pageGroupRes.setIconClass(pageGroup.getIconClass());
				pageGroupRes.setId(pageGroup.getId());
				pageGroupRes.setMaxItems(pageGroup.getMaxItems());
				pageGroupRes.setMinItems(pageGroup.getMinItems());
				pageGroupRes.setName(pageGroup.getName());
				pageGroupRes.setNoOfCols(pageGroup.getNoOfCols());
				pageGroupRes.setRole(pageGroup.getRole());

				List<OrgVerticalLocationPageGroupFieldResponse> pageGroupFieldResList = new ArrayList<>();
				for (OrgVerticalLocationPageGroupFieldResponse pgGrpField : pageGroup.getFieldList()) {
					OrgVerticalLocationPageGroupFieldResponse fieldRes = modelMapper.map(pgGrpField,
							OrgVerticalLocationPageGroupFieldResponse.class);
					fieldRes.setRole(pgGrpField.getRole());
					if (fieldRes.getRole() != null) {
						pageGroupFieldResList.add(fieldRes);
					}
				}
				pageGroupRes.setFieldList(pageGroupFieldResList);
				if (!pageGroupRes.getFieldList().isEmpty()) {
					pageGroupResList.add(pageGroupRes);
				}

			}
			res.setGroupList(pageGroupResList);

		}

		return res;
	}

	private OrgVerticalLocationPageResponse buildResObj(OrgVerticalLocationPage page, Map<String, Object> map) {
		OrgVerticalLocationPageResponse res = new OrgVerticalLocationPageResponse();

		res = modelMapper.map(page, OrgVerticalLocationPageResponse.class);
		Object obj = map.get("page");
		Role pageRole = (Role) map.get("page");
		if (null != obj) {
			res.setRole(pageRole);
		}

		Object groupObj = map.get("group");
		Map<Integer, Role> groupMap = new HashMap<>();
		if (null != groupObj) {
			groupMap = (Map<Integer, Role>) groupObj;
		}

		Object fieldObj = map.get("field");
		Map<Integer, Role> fieldMap = new HashMap<>();
		if (null != fieldObj) {
			fieldMap = (Map<Integer, Role>) fieldObj;
		}

		Object fieldGroupObj = map.get("field-group");
		Map<Integer, List<Role>> fieldGroupMap = new HashMap<>();
		if (null != fieldGroupObj) {
			fieldGroupMap = (Map<Integer, List<Role>>) fieldGroupObj;
		}

		System.out.println("fieldGroupMap::" + fieldGroupMap);
		System.out.println("fieldMap size ::" + fieldMap);
		System.out.println("groupMap::" + groupMap);

		Map<Integer, List<Role>> tempMap = new HashMap<>();
		List<OrgVerticalLocationPageGroupResponse> pageGroupResList = new ArrayList<>();
		for (OrgVerticalLocationPageGroup pageGroup : page.getGroupList()) {

			List<Role> filedGroupRoles = fieldGroupMap.get(pageGroup.getId());

			System.out.println("Group id " + pageGroup.getId() + " filedGroupRoles " + filedGroupRoles);
			OrgVerticalLocationPageGroupResponse pageGroupRes = new OrgVerticalLocationPageGroupResponse();
			pageGroupRes.setGroup(pageGroup.getPageGroupId());
			pageGroupRes.setIconClass(pageGroup.getIconClass());
			pageGroupRes.setId(pageGroup.getId());
			pageGroupRes.setMaxItems(pageGroup.getMaxItems());
			pageGroupRes.setMinItems(pageGroup.getMinItems());
			pageGroupRes.setName(pageGroup.getName());
			pageGroupRes.setNoOfCols(pageGroup.getNoOfCols());
			Role groupRole = groupMap.get(pageGroup.getId());
			pageGroupRes.setRole(groupRole);
			if (pageGroupRes.getRole() == null) {
				pageGroupRes.setRole(pageRole);
			}

			boolean flag = false;
			System.out.println("filedGroupRoles " + filedGroupRoles);
			if (null != filedGroupRoles) {
				for (Role x : filedGroupRoles) {
					System.out.println("x.getFieldId() " + x.getFieldId());
					if (x.getFieldId() != 0) {
						flag = true;
					}
				}
			}

			List<OrgVerticalLocationPageGroupFieldResponse> pageGroupFieldResList = new ArrayList<>();

			if (filedGroupRoles == null) {

				for (OrgVerticalLocationPageGroupField pgGrpField : pageGroup.getFieldList()) {
					OrgVerticalLocationPageGroupFieldResponse fieldRes = modelMapper.map(pgGrpField,
							OrgVerticalLocationPageGroupFieldResponse.class);
					fieldRes.setRole(fieldMap.get(pgGrpField.getId()));
					if (fieldRes.getRole() == null) {
						if (groupRole != null) {
							fieldRes.setRole(groupRole);
						} else {
							fieldRes.setRole(pageRole);
						}
					}
					pageGroupFieldResList.add(fieldRes);

				}
			} else {
				for (OrgVerticalLocationPageGroupField pgGrpField : pageGroup.getFieldList()) {
					OrgVerticalLocationPageGroupFieldResponse fieldRes = modelMapper.map(pgGrpField,
							OrgVerticalLocationPageGroupFieldResponse.class);
					System.out.println("Flag ::" + flag);
					if (flag) {
						System.out.println("Inside flag true loop ");
						System.out.println("filedGroupRoles:: " + filedGroupRoles + ", pGrp " + pgGrpField.getId());
						boolean insideFlag = false;
						for (Role r : filedGroupRoles) {
							if (r.getFieldId() == pgGrpField.getId()) {
								insideFlag = true;
							}
						}
						System.out.println("insideFlag" + insideFlag);
						//
						if (insideFlag) {
							fieldRes.setRole(fieldMap.get(pgGrpField.getId()));
							if (fieldRes.getRole() == null) {
								if (groupRole != null) {
									fieldRes.setRole(groupRole);
								} else {
									fieldRes.setRole(pageRole);
								}
							}
							pageGroupFieldResList.add(fieldRes);
						}

					} else {

						fieldRes.setRole(fieldMap.get(pgGrpField.getId()));
						if (fieldRes.getRole() == null) {
							if (groupRole != null) {
								fieldRes.setRole(groupRole);
							} else {
								fieldRes.setRole(pageRole);
							}
						}
						pageGroupFieldResList.add(fieldRes);

					}

				}
			
			}

			// tempMap
			pageGroupRes.setFieldList(pageGroupFieldResList);
			pageGroupResList.add(pageGroupRes);

		}
		res.setGroupList(pageGroupResList);
		System.out.println("pageGroupResList:: " + pageGroupResList.size());

		return res;
	}

	@Override
	public DFFieldRes getMasterDataBasedOnFormKeyMapId(String id) throws DynamicFormsServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public DynmFormExcelResponseDO createDynamicFormFieldsExcel(List<DynamicFormFieldBO> dynamicFormFieldBOList)
			throws DynmFormExcelException {

		List<DynamicUploadExcelError> errors = createDynamicFormFields(dynamicFormFieldBOList);

		if (errors.isEmpty()) {
			return new DynmFormExcelResponseDO(DynamicFormConstants.SAVE_DATA_SUCCESS, DynamicFormConstants.SUCCESS,
					errors);
		} else {
			return new DynmFormExcelResponseDO(DynamicFormConstants.BAD_REQUEST, DynamicFormConstants.FAILURE, errors);
		}

	}

	private List<DynamicUploadExcelError> createDynamicFormFields(List<DynamicFormFieldBO> dynamicFormFieldBOList) {

		List<DynamicUploadExcelError> errors = new ArrayList<>();

		int count = 0;
		for (DynamicFormFieldBO dynmFormFldDO : dynamicFormFieldBOList) {
			try {
              
				
				if(count == 0) {
				deleteExistingPage(dynmFormFldDO);
				 count++;
				}
				saveDynmFormFld(dynmFormFldDO);

				if (!ObjectUtils.isEmpty(errors)) {
					return errors;
				}
			} catch (Exception e) {

				System.out.println("Exceptionddddd"+e.getMessage());
			}
		}

		return errors;

	}

	
	private void saveDynmFormFld(DynamicFormFieldBO request) {

		List<OrgVerticalLocationPageGroupField> fieldList = new ArrayList<OrgVerticalLocationPageGroupField>();
		OrgVerticalLocationPageGroupField field = new OrgVerticalLocationPageGroupField();
		field.setName(request.getFieldName());
		field.setFieldId(StringUtils.isBlank(request.getFieldId()) ? 0 : Integer.valueOf(request.getFieldId()));
		field.setCss(request.getCss());
		field.setDefaultText(request.getDefaultText());
		field.setDependsOn(request.getDependsOn());
		field.setDomInputType(request.getDomInputType());
		field.setDomType(request.getDomType());
		field.setDtAttributes(request.getDtAttributes());
		field.setDtChoices(request.getDtChoices());
		field.setDtStatic(request.getDtStatic());
		field.setDtUrl(request.getDtUrl());
		field.setFieldDefAttributes(request.getFieldDefAttributes());
		field.setIdentifier(request.getIdentifier());
		field.setLabel(request.getLabel());
		field.setLengthValidationMsg(request.getLengthValidationMsg());
		field.setMaxLength(StringUtils.isBlank(request.getMaxLength()) ? 0 : Integer.valueOf(request.getMaxLength()));
		field.setMinLength(StringUtils.isBlank(request.getMinLength()) ? 0 : Integer.valueOf(request.getMinLength()));
		/* field.setPageGroupId(pageGroupDbRecord.getId()); */
		field.setRegExpression(request.getRegExpression());
		field.setRequired(request.getRequired().equalsIgnoreCase("T") ? true : false);
		field.setRequiredValidationMsg(request.getRequiredValidationMsg());
		field.setToolTip(request.getToolTip());
		field.setUpdated_at(getCurrentTmeStamp());
		field.setValidFuncOn(request.getValidJsFuncOn());
		field.setValidJsFunc(request.getValidJsFunc());
		field.setValidJsFuncMsg(request.getValidJsFuncMsg());

		field.setValidJsFuncMsgKey(request.getValidJsFuncMsgKey());
		//field = orgVerLocPageGrpFieldRepo.save(field);
		field.setUiIdentifier(request.getUiIdentifier());
		
		 
		
		List<OrgVerticalLocationPageGroup> groupList = new ArrayList<OrgVerticalLocationPageGroup>();
		OrgVerticalLocationPage pageDbRecord = orgVerLocPageRepo.findByNameAndUUID(request.getPageName(),request.getUuid());
		if (null == pageDbRecord) {
			OrgVerticalLocationPage page = new OrgVerticalLocationPage();
			page.setCreatedAt(getCurrentTmeStamp());
			page.setEndPoint(request.getPageEndPoint());
			page.setPojo(request.getPagePojo());
			page.setJson(request.getPageJson());
			page.setName(request.getPageName());
			page.setPosition(StringUtils.isBlank(request.getPagePosition()) ? 0
					: Integer.valueOf(request.getPagePosition()));
			page.setNoOfClns(StringUtils.isBlank(request.getPageNumOfColumns()) ? 0
					: Integer.valueOf(request.getPageNumOfColumns()));
			page.setPageId(StringUtils.isBlank(request.getPageId()) ? 0 : Integer.valueOf(request.getPageId()));
			page.setUUID(request.getUuid());
			page.setPageId(StringUtils.isBlank(request.getPageId()) ? 0
					: Integer.valueOf(request.getPageId()));
			page.setUpdated_at(getCurrentTmeStamp());
			pageDbRecord = orgVerLocPageRepo.saveAndFlush(page);
				
		
			OrgVerticalLocationPageGroup pageGroup = new OrgVerticalLocationPageGroup();
			pageGroup.setCreatedAt(getCurrentTmeStamp());
			 pageGroup.setParentPageId(pageDbRecord.getId());
			pageGroup.setPageGroupId(StringUtils.isBlank(request.getPageGroup()) ? 0
					: Integer.valueOf(request.getPageGroup()));
			pageGroup.setPosition(StringUtils.isBlank(request.getPageGroupPosition()) ? 0
					: Integer.valueOf(request.getPageGroupPosition()));
			pageGroup.setIconClass(request.getPageGroupIconCls());
			pageGroup.setMaxItems(StringUtils.isBlank(request.getPageGroupMaxItems()) ? 0
					: Integer.valueOf(request.getPageGroupMaxItems()));
			pageGroup.setMinItems(StringUtils.isBlank(request.getPageGroupMinItems()) ? 0
					: Integer.valueOf(request.getPageGroupMinItems()));
			pageGroup.setName(request.getPageGroupName());
			pageGroup.setNoOfCols(StringUtils.isBlank(request.getPageGroupNumOfColumns()) ? 0
					: Integer.valueOf(request.getPageGroupNumOfColumns()));
			pageGroup.setUpdated_at(getCurrentTmeStamp());
			
			
			pageGroup = orgVerLocPageGroupRepo.saveAndFlush(pageGroup);
			field.setPageGroupId(pageGroup.getId());
			fieldList.add(field);
			pageGroup.setFieldList(fieldList);
			//pageGroup = orgVerLocPageGroupRepo.saveAndFlush(pageGroup);
			/* pageGroup.setParentPageId(pageD
			 * bRecord.getId()); */
			groupList.add(pageGroup);
			page.setGroupList(groupList);
			pageDbRecord = orgVerLocPageRepo.saveAndFlush(page);
		    

		}else {
			OrgVerticalLocationPageGroup pageGroupDbRecord = orgVerLocPageGroupRepo
					.findByNameAndParentPageId(request.getPageGroupName(), pageDbRecord.getId());
			if (null == pageGroupDbRecord) {
				OrgVerticalLocationPageGroup pageGroup = new OrgVerticalLocationPageGroup();
				pageGroup.setCreatedAt(getCurrentTmeStamp());
				pageGroup.setParentPageId(pageDbRecord.getId());
				pageGroup.setPageGroupId(StringUtils.isBlank(request.getPageGroup()) ? 0
						: Integer.valueOf(request.getPageGroup()));
				pageGroup.setPosition(StringUtils.isBlank(request.getPageGroupPosition()) ? 0
						: Integer.valueOf(request.getPageGroupPosition()));
				pageGroup.setIconClass(request.getPageGroupIconCls());
				pageGroup.setMaxItems(StringUtils.isBlank(request.getPageGroupMaxItems()) ? 0
						: Integer.valueOf(request.getPageGroupMaxItems()));
				pageGroup.setMinItems(StringUtils.isBlank(request.getPageGroupMinItems()) ? 0
						: Integer.valueOf(request.getPageGroupMinItems()));
				pageGroup.setName(request.getPageGroupName());
				pageGroup.setNoOfCols(StringUtils.isBlank(request.getPageGroupNumOfColumns()) ? 0
						: Integer.valueOf(request.getPageGroupNumOfColumns()));
				pageGroup.setUpdated_at(getCurrentTmeStamp());
				pageGroup.setFieldList(fieldList);
				//pageGroup = orgVerLocPageGroupRepo.save(pageGroup);
				pageGroup = orgVerLocPageGroupRepo.saveAndFlush(pageGroup);
				field.setPageGroupId(pageGroup.getId());
				fieldList.add(field);
				pageGroup.setFieldList(fieldList);
				//pageGroup = orgVerLocPageGroupRepo.saveAndFlush(pageGroup);
				/* pageGroup.setParentPageId(pageD
				 * bRecord.getId()); */
				List<OrgVerticalLocationPageGroup> groupListDb = pageDbRecord.getGroupList();
				groupListDb.add(pageGroup);
				pageDbRecord.setGroupList(groupListDb);
				pageDbRecord = orgVerLocPageRepo.saveAndFlush(pageDbRecord);

			}else {
				List<OrgVerticalLocationPageGroupField> fieldListDb = pageGroupDbRecord.getFieldList();
				//pageGroupDbRecord = orgVerLocPageGroupRepo.saveAndFlush(pageGroupDbRecord);
				List<OrgVerticalLocationPageGroup> groupListDb = pageDbRecord.getGroupList();
				field.setPageGroupId(pageGroupDbRecord.getId());
				fieldListDb.add(field);
				pageGroupDbRecord.setFieldList(fieldListDb);
				//pageGroupDbRecord = orgVerLocPageGroupRepo.saveAndFlush(pageGroupDbRecord);
				/* pageGroup.setParentPageId(pageD
				 * bRecord.getId()); */
				groupListDb.add(pageGroupDbRecord);	
				pageDbRecord.setGroupList(groupListDb);
				pageDbRecord = orgVerLocPageRepo.saveAndFlush(pageDbRecord);
				/*
				 * pageGroupDbRecord.setFieldList(fieldList); pageGroupDbRecord =
				 * orgVerLocPageGroupRepo.save(pageGroupDbRecord);
				 */
			}
		}
		

	}

	private void deleteExistingPage(DynamicFormFieldBO request) {
		OrgVerticalLocationPage page = orgVerLocPageRepo.findByNameAndUUID(request.getPageName(),request.getUuid());
		if (page != null) {
			List<OrgVerticalLocationPageGroup> pageGroupList = orgVerLocPageGroupRepo
					.findAllByParentPageId(page.getId());

			
			  for (OrgVerticalLocationPageGroup pageGroup : pageGroupList) {
			  List<OrgVerticalLocationPageGroupField> fieldList = orgVerLocPageGrpFieldRepo
			  .findAllByPageGroupId(pageGroup.getId());
			  orgVerLocPageGrpFieldRepo.deleteAll(fieldList);
			  orgVerLocPageGrpFieldRepo.flush();
			  
			  }
			 
			orgVerLocPageGroupRepo.deleteAll(pageGroupList);
			orgVerLocPageGroupRepo.flush();
			orgVerLocPageRepo.delete(page);
			orgVerLocPageRepo.flush();
		}
	}

	
	@Override
	public DynmFormExcelResponseDO createDynamicRoleFormsExcel(List<DynamicRoleFormBO> dynamicRoleFormBOList)
			throws DynmFormExcelException {

		List<DynamicUploadExcelError> errors = createDynamicRoleForm
				(dynamicRoleFormBOList);

		if (errors.isEmpty()) {
			return new DynmFormExcelResponseDO(DynamicFormConstants.SAVE_DATA_SUCCESS, DynamicFormConstants.SUCCESS,
					errors);
		} else {
			return new DynmFormExcelResponseDO(DynamicFormConstants.BAD_REQUEST, DynamicFormConstants.FAILURE, errors);
		}

	}
	
	private List<DynamicUploadExcelError> createDynamicRoleForm(List<DynamicRoleFormBO> dynamicRoleFormBOList) {

		List<DynamicUploadExcelError> errors = new ArrayList<>();

		int count = 0;
		for (DynamicRoleFormBO dynmFormFldDO : dynamicRoleFormBOList) {
			try {
              
				
				if(count == 0) {
					deleteExisRoleForPage(dynmFormFldDO);
				 count++;
				}
				saveDynmRoleForm(dynmFormFldDO);

				if (!ObjectUtils.isEmpty(errors)) {
					return errors;
				}
			} catch (Exception e) {

				System.out.println("Exceptionddddd"+e.getMessage());
			}
		}

		return errors;

	}


	@Override
	public String getPojoName(int id) throws DynamicFormsServiceException {
		String res =null;
		try {
			res = orgRepo.getPojoName(id);
		}
		catch (DataAccessException e) {
			log.error("getPojoName ", e);
			throw new DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return res ;
	}
	
	@Override
	public String getPojoNameV2(int id, String UUID) throws DynamicFormsServiceException {
		String res =null;
		try {
			res = orgRepo.getPojoNameV2(id,UUID);
		}
		catch (DataAccessException e) {
			log.error("getPojoName ", e);
			throw new DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return res ;
	}
	

	private void saveDynmRoleForm(DynamicRoleFormBO request) {

		OrgVerticalLocationPage pageDbRecord = orgVerLocPageRepo.findByNameAndUUID(request.getPageName(),request.getUuid());
		PageGroupFieldRoleAccess roleAccess = new PageGroupFieldRoleAccess();
		roleAccess.setCreatedAt(getCurrentTmeStamp());
		roleAccess.setPageId(pageDbRecord.getPageId());
		OrgVerticalLocationPageGroup pageGroupDbRecord =  new OrgVerticalLocationPageGroup();
		if(StringUtils.isNotBlank(request.getPageGroupName())){
			 pageGroupDbRecord = orgVerLocPageGroupRepo
					.findByNameAndParentPageId(request.getPageGroupName(), pageDbRecord.getId());
			roleAccess.setGroupId(pageGroupDbRecord.getId());
		}else {
			roleAccess.setGroupId(0);
		}
		
		if(StringUtils.isNotBlank(request.getFieldId())){
			OrgVerticalLocationPageGroupField field = orgVerLocPageGrpFieldRepo.findByFieldIdAndPageGroupId(Integer.valueOf(request.getFieldId()),pageGroupDbRecord.getId());
			roleAccess.setFieldId(field.getId());
		}else {
			roleAccess.setFieldId(0);
		}
		roleAccess.setMode(request.getMode());
		roleAccess.setPermissionType(request.getPermissionType());
		roleAccess.setRoleId(Integer.valueOf(request.getRoleIdentifier()));
		roleAccess.setUpdated_at(getCurrentTmeStamp());
		pageGroupRepo.saveAndFlush(roleAccess);
		

	}

	private void deleteExisRoleForPage(DynamicRoleFormBO request) {
		OrgVerticalLocationPage page = orgVerLocPageRepo.findByNameAndUUID(request.getPageName(),request.getUuid());
		if (page != null) {
			List<PageGroupFieldRoleAccess> pageGrpRoleList = pageGroupRepo
					.findAllByPageId(page.getPageId());

			pageGroupRepo.deleteAll(pageGrpRoleList);
			pageGroupRepo.flush();
			
		}
	}
	

	public Date getDate() {
		java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		return date;

	}

	public Timestamp getCurrentTmeStamp() {
		return new Timestamp(System.currentTimeMillis());
	}

//	@Override

//	public String getPojoName(int id) throws DynamicFormsServiceException {
//		String res =null;
//		try {
//			res = orgRepo.getPojoName(id);
//		}
//		catch (DataAccessException e) {
//			log.error("getPojoName ", e);
//			throw new DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
//					HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//		return res ;
//	}

//	@Override
//	public DynmFormExcelResponseDO createDynamicRoleFormsExcel(List<DynamicRoleFormBO> dynamicRoleFormBOList)
//			throws DynmFormExcelException {
//		
//		return null;
//	}

	@Autowired
	MenuActionRepo menuActionRepo;
	
	@Autowired
	MenuRoleActionRepo menuRoleActionRepo;
	
	
	@Override
	public List<ButtonControl> getButtonControls(ButtonReq buttonReq) throws DynamicFormsServiceException {
		ButtonRes buttonRes = null;
		List<ButtonControl> bcList  = new ArrayList<>();
		try {
			List<MenuAction> menuActionList = menuActionRepo.getMenuRoles(buttonReq.getPageIdentifier());
			List<MenuRoleAction> rolesList = menuRoleActionRepo.getMenuRoleActions(buttonReq.getRoleIdentifier());			
			buttonRes = new ButtonRes();
		
			//buttonRes.setPageIdentifier(buttonReq.getPageIdentifier());
			//buttonRes.setRoleIdentifier(buttonReq.getRoleIdentifier());
			
			for(MenuAction menuAction : menuActionList) {
				for(MenuRoleAction menuRoleAction:rolesList) {
					if(menuRoleAction.getMenuActionIdentifier()==menuAction.getId()) {
						ButtonControl bc = new ButtonControl();
						bc.setAction(menuAction.getAction());
						bc.setUrl(menuAction.getActionUrl());
						bc.setName(menuAction.getName());
						bcList.add(bc);
					}
				}
			}
			
			buttonRes.setButtonControls(bcList);
			
		} catch (DataAccessException e) {
			log.error("Failed to process login in request ", e);
			throw new DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return bcList;
	}


	public List<OrgVerticalLocationRoleMenu> getMenuList(MenuMappings mappings) {
		List<OrgVerticalLocationRoleMenu> menuListDB = new ArrayList<OrgVerticalLocationRoleMenu>();
		menuListDB = orgVerLocMenuRepo.findAllByUUIDAndRoleId(mappings.getOrgIdentifier(), mappings.getRoleIdentifier());
		//List<Menu> menuList = new ArrayList<Menu>();
		/*
		 * List<OrgVerticalLocationPage> pageList = new
		 * ArrayList<OrgVerticalLocationPage>(); pageList =
		 * orgVerLocPageRepo.findByUUID(uuid);
		 */
		/* for(OrgVerticalLocationRoleMenu menuDb :menuListDB) { */
			
			/*
			 * if(StringUtils.isBlank(menuDb.getParentIdentifier())) { Menu menu = new
			 * Menu(menuDb.getPageIdentifier(), menuDb.getName(), menuDb.getMode());
			 * menuList.add(menu); }else { Menu menuChild = new
			 * Menu(menuDb.getPageIdentifier(), menuDb.getParentIdentifier(),
			 * menuDb.getName(), menuDb.getMode()); menuList.add(menuChild); }
			 */
		List<OrgVerticalLocationRoleMenu> menus = FlatToTree.construct(menuListDB);
			
			
		//}
		//List<Node<Menu>> values = FlatToTree.construct(menuList);
		return menus;
	}
	
	
	@Override
	public DynmMenuExcelResponseDO createAutoModuleMenuExcel(List<AutModuleMenuExcelBO> dynamicMenuBOList) {
		List<DynamicUploadExcelError> errors = createAutModuleMenuExcel(dynamicMenuBOList);

		if (errors.isEmpty()) {
			return new DynmMenuExcelResponseDO(DynamicFormConstants.SAVE_DATA_SUCCESS,DynamicFormConstants.SUCCESS,errors);
		} else {
			return new DynmMenuExcelResponseDO(DynamicFormConstants.BAD_REQUEST,DynamicFormConstants.FAILURE,errors);
		}

	}
	
	
	private List<DynamicUploadExcelError> createAutModuleMenuExcel(List<AutModuleMenuExcelBO> dynamicMenuBOList) {

		List<DynamicUploadExcelError> errors = new ArrayList<>();
		

		for (AutModuleMenuExcelBO dynmMenuDO : dynamicMenuBOList) {
			try {
			
				deleteExistingMenu(dynmMenuDO);
				 saveAutoMenus(dynmMenuDO);
						
			
			}catch(DataAccessException e){
				
			}
		}
		
		return errors;
		
	}

	private void saveAutoMenus(AutModuleMenuExcelBO dynmMenuDO) {
		OrgVerticalLocationRoleMenu menuDB = orgVerLocMenuRepo.findByUUIDAndPageIdentifier(dynmMenuDO.getUuid(), dynmMenuDO.getPageIdentifier());
		
		if(menuDB == null) {
			OrgVerticalLocationRoleMenu dfMenu = new OrgVerticalLocationRoleMenu();
		dfMenu.setName(dynmMenuDO.getName());
		dfMenu.setCreatedAt(getCurrentTmeStamp());
		dfMenu.setMenuId(StringUtils.isBlank(dynmMenuDO.getMeniId()) ? 0
				: Integer.valueOf(dynmMenuDO.getMeniId()));
		dfMenu.setMode(dynmMenuDO.getMode());
		dfMenu.setPageIdentifier(dynmMenuDO.getPageIdentifier());
		if(StringUtils.isNotBlank(dynmMenuDO.getParentIdentifier())){
		dfMenu.setParentIdentifier(Integer.valueOf(dynmMenuDO.getParentIdentifier()));
		}
		dfMenu.setRedirectUrl(dynmMenuDO.getRedirectUrl());
		dfMenu.setRoleId(StringUtils.isBlank(dynmMenuDO.getRoleIdentifier()) ? 0
				: Integer.valueOf(dynmMenuDO.getRoleIdentifier()));
		dfMenu.setUpdated_at(getCurrentTmeStamp());
		dfMenu.setUUID(dynmMenuDO.getUuid());
		orgVerLocMenuRepo.saveAndFlush(dfMenu);
		}
		

	}

	private void deleteExistingMenu(AutModuleMenuExcelBO request) {
		List<OrgVerticalLocationRoleMenu> menuListDB = new ArrayList<OrgVerticalLocationRoleMenu>();
		menuListDB = orgVerLocMenuRepo.findAllByUUIDAndPageIdentifier(request.getUuid(), request.getPageIdentifier());
		if (!CollectionUtils.isEmpty(menuListDB)) {

			orgVerLocMenuRepo.deleteAll(menuListDB);
			orgVerLocMenuRepo.flush();

		}
	}
	
	@Autowired
	DropDownMasterRepo dropDownMasterRepo;
	
	@Override
	public List<DropDownRes> getDropDownData(DropdownReq dropdownReq) throws DynamicFormsServiceException {
		log.debug("getDropDownData{}");
		List<DropDownRes> list = new ArrayList<>();
		try {
			
			List<DropDown> dbList = new ArrayList<>();
			int parentId = dropdownReq.getParentId();
			log.debug("parentId::"+parentId);
			if(parentId>0) {
				dbList = dropDownMasterRepo.getDropDownWithParent(dropdownReq.getBu(), dropdownReq.getDropdownType(),parentId);
			}else {
				dbList = dropDownMasterRepo.getDropDownData(dropdownReq.getBu(), dropdownReq.getDropdownType());
			}
			list = ObjectMapperUtils.mapAll(dbList, DropDownRes.class);
		}catch (Exception e) {
			log.error("getDropDownData:: ", e);
			throw new DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return list;
	}

}
