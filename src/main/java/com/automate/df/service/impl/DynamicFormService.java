package com.automate.df.service.impl;


import java.util.List;

import com.automate.df.entity.OrgVerticalLocationRoleMenu;
import com.automate.df.exception.DynamicFormsServiceException;
import com.automate.df.exception.DynmFormExcelException;
import com.automate.df.model.AutModuleMenuExcelBO;
import com.automate.df.model.ButtonControl;
import com.automate.df.model.ButtonReq;
import com.automate.df.model.DFFieldRes;
import com.automate.df.model.DropDownRes;
import com.automate.df.model.DropdownReq;
import com.automate.df.model.DynamicFormFieldBO;
import com.automate.df.model.DynamicRoleFormBO;
import com.automate.df.model.DynmFormExcelResponseDO;
import com.automate.df.model.DynmMenuExcelResponseDO;
import com.automate.df.model.Mappings;
import com.automate.df.model.MenuMappings;
import com.automate.df.model.PageRes;

public interface DynamicFormService {
	
	public PageRes getBusinessFormKeyMappings(Mappings mappings) throws DynamicFormsServiceException;

	public DFFieldRes getMasterDataBasedOnFormKeyMapId(String id) throws DynamicFormsServiceException;

	DynmFormExcelResponseDO createDynamicFormFieldsExcel(List<DynamicFormFieldBO> dynamicFormFieldBOList) throws DynmFormExcelException;

	public String getPojoName(int id) throws DynamicFormsServiceException;
	
	public String getPojoNameV2(int id, String UUID) throws DynamicFormsServiceException;

	DynmFormExcelResponseDO createDynamicRoleFormsExcel(List<DynamicRoleFormBO> dynamicRoleFormBOList)
			throws DynmFormExcelException;


	public List<ButtonControl> getButtonControls(ButtonReq buttonReq) throws DynamicFormsServiceException;

	public List<OrgVerticalLocationRoleMenu> getMenuList(MenuMappings mappings);

	DynmMenuExcelResponseDO createAutoModuleMenuExcel(List<AutModuleMenuExcelBO> dynamicMenuBOList);

	public List<DropDownRes> getDropDownData(DropdownReq dropdownReq) throws DynamicFormsServiceException;

}
