package com.automate.df.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.automate.df.entity.OrgVerticalLocationRoleMenu;

public interface OrgVerLocMenuRepo extends JpaRepository<OrgVerticalLocationRoleMenu, Integer> {
 
	OrgVerticalLocationRoleMenu findByName(String name);
	
	OrgVerticalLocationRoleMenu findByUUIDAndPageIdentifier(String uuid,String pageIdentifier);
	
	List<OrgVerticalLocationRoleMenu> findAllByUUID(String uuid);
	
	List<OrgVerticalLocationRoleMenu> findAllByUUIDAndRoleId(String uuid,int roleId);
	
	List<OrgVerticalLocationRoleMenu> findAllByUUIDAndPageIdentifier(String uuid,String pageIdentifier);
		
}
