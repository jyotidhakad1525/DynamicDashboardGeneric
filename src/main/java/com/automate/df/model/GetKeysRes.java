package com.automate.df.model;

import java.util.List;
import java.util.Map;

import com.automate.df.entity.OrgVerticalLocationPageGroupField;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetKeysRes {
	
	int pageId;
	String UUID;
	PageConfiguration pageConfig;
	List<OrgVerticalLocationPageGroupField> fieldList;

}
