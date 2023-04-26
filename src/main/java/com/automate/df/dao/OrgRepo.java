package com.automate.df.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.automate.df.entity.OrgVerticalLocationPage;

public interface OrgRepo extends JpaRepository<OrgVerticalLocationPage, Integer>{

	@Query(value="SELECT * FROM organisation_vertical_location_page where UUID=:UUIDVal and page_id=:pageId",nativeQuery = true)
	public OrgVerticalLocationPage findByUUIDValAndPage(@Param(value="UUIDVal") String UUIDVal,@Param(value="pageId") int pageId);

	@Query(value="SELECT pojo FROM organisation_vertical_location_page where page_id=:pageId",nativeQuery = true)
	public String getPojoName(@Param(value="pageId") int pageId);
	
	@Query(value="SELECT pojo FROM organisation_vertical_location_page where page_id=:pageId and UUID=:UUID",nativeQuery = true)
	public String getPojoNameV2(@Param(value="pageId") int pageId, @Param(value="UUID") String UUID);
}
