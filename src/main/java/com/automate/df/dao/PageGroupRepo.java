package com.automate.df.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.automate.df.entity.PageGroupFieldRoleAccess;

public interface PageGroupRepo extends JpaRepository<PageGroupFieldRoleAccess, Integer>{

	@Query(value="SELECT * FROM page_group_field_role_access where org_ver_loc_page_id = :pageId ", nativeQuery = true)
	public List<PageGroupFieldRoleAccess> findByPageId(@Param(value="pageId") int pageId);
	
	@Query(value="SELECT * FROM page_group_field_role_access where org_ver_loc_page_group_id = 0 and org_ver_loc_page_group_field_id = 0 and  org_ver_loc_page_id=:pageId and org_ver_loc_role_id=:roleId and mode=:mode",nativeQuery = true)
	public List<PageGroupFieldRoleAccess> findForPageLevel(@Param(value="pageId") int pageId,@Param(value="roleId") int roleId,@Param(value="mode") String mode);
	
	@Query(value="SELECT * FROM page_group_field_role_access where org_ver_loc_page_group_id !=0  and  org_ver_loc_page_id=:pageId and org_ver_loc_role_id=:roleId and mode=:mode",nativeQuery = true)
	public List<PageGroupFieldRoleAccess> findForGroupLevel(@Param(value="pageId") int pageId,@Param(value="roleId") int roleId,@Param(value="mode") String mode);
	
	@Query(value="SELECT * FROM page_group_field_role_access where org_ver_loc_page_group_field_id !=0 and org_ver_loc_page_id=:pageId and org_ver_loc_role_id=:roleId and mode=:mode",nativeQuery = true)
	public List<PageGroupFieldRoleAccess> findForFieldLevel(@Param(value="pageId") int pageId,@Param(value="roleId") int roleId,@Param(value="mode") String mode);

	
	
	@Query(value="SELECT * FROM page_group_field_role_access where org_ver_loc_page_id=:pageId and org_ver_loc_role_id=:roleId and mode=:mode",nativeQuery=true)
	public List<PageGroupFieldRoleAccess> findForFieldGroupLevel(@Param(value="pageId") int pageId,@Param(value="roleId") int roleId,@Param(value="mode") String mode); 


	public List<PageGroupFieldRoleAccess> findAllByPageId(int pageId);
	

}
