package com.automate.df.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.automate.df.model.df.OrgDropDownReq;
import com.automate.df.model.salesgap.OrgDropDown;

import com.automate.df.service.OrgHierarchyService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author srujan
 *
 */


@Slf4j
@Service
public class OrgHierarchyServiceImpl implements OrgHierarchyService {

	@Autowired
	private EntityManager entityManager;
	final String QUERY_TMPL = "SELECT id,name FROM organisation_level<CURRENLEVEL> where organisation_id=ORGID and organisation_level<PARENTLEVEL>_id=ORG_LEVEL_PARENTID";

	@Override
	public List<OrgDropDown> getOrgLevelDropdownData(OrgDropDownReq request) {
		List<OrgDropDown> list = new ArrayList<>();
		try {
			String query = "";
			if (request.getLevelId() != 1) {
				query = QUERY_TMPL;
				query = query.replace("<CURRENLEVEL>", String.valueOf(request.getLevelId()));
				query = query.replace("<PARENTLEVEL>", String.valueOf(request.getLevelId()-1));
			} else {
				query = "SELECT * FROM organisation_level1 where organisation_id=" + request.getOrgId();
			}
			list = getNativeQueryDataForDropDown(query, request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private List<OrgDropDown> getNativeQueryDataForDropDown(String query, OrgDropDownReq request) {
		log.debug("DropDown Data for " + query);
		List<OrgDropDown> list = new ArrayList<>();
		query = query.replace("ORGID", request.getOrgId());
		query = query.replace("ORG_LEVEL_PARENTID", String.valueOf(request.getParentLevelId()));
		List<Object[]> data = entityManager.createNativeQuery(query).getResultList();
		for (Object[] objArr : data) {
			OrgDropDown org = new OrgDropDown();
			org.setId((BigInteger) objArr[0]);
			org.setValue((String) objArr[1]);
			list.add(org);
		}
		return list;
	}

}
