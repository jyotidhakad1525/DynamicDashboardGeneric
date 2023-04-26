package com.automate.df.model;

import java.util.Set;

import com.automate.df.entity.CheckListItems;
import com.automate.df.entity.CheckListType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ListOFCheckListTypeWithItems {


	private int id;
	private String checkListType;
	private String checkListSubType;
	private String createdAt;
	private String updatedAt;
	private String status;
	private String createdBy;
	private String updatedBy;
	private String orgId;
	private Set<CheckListItems> checkListItems;
	
	public ListOFCheckListTypeWithItems(CheckListType checkListType,Set<CheckListItems> checkListItems) {
		super();
		this.id = checkListType.getId();
		this.checkListType = checkListType.getCheckListType();
		this.checkListSubType = checkListType.getCheckListSubType();
		this.createdAt = checkListType.getCreatedAt();
		this.updatedAt = checkListType.getUpdatedAt();
		this.status = checkListType.getStatus();
		this.createdBy = checkListType.getCreatedBy();
		this.updatedBy = checkListType.getUpdatedBy();
		this.orgId = checkListType.getOrgId();
		this.checkListItems = checkListItems;
	}
	
	
	
}
