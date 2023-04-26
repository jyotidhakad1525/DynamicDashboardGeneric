package com.automate.df.dao.oh;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.automate.df.entity.oh.LocationNodeDef;


public interface LocationNodeDefDao extends JpaRepository<LocationNodeDef, Integer>{

	@Query(value = "SELECT 'parent_id','location_node_def_name' FROM location_node_def where location_node_def_name in (:levelList) and org_id=:orgId and active = 'Y'", nativeQuery = true)
	List<LocationNodeDef> getEmpLevelDataByOrder(
			@Param(value = "orgId") Integer orgId,
			@Param(value = "levelList") List<String> levelList);

	@Query(value = "SELECT min(parent_id),location_node_def_name FROM location_node_def where location_node_def_name in (:levelList) and org_id=:orgId order by parent_id asc", nativeQuery = true)
	String getTopEmpLevel(
			@Param(value = "orgId") Integer orgId,
			@Param(value = "levelList") List<String> levelList);
	
	
	@Query(value = "SELECT * FROM location_node_def where org_id=:orgId and location_node_def_type in (:levelList) and active = 'Y' order by parent_id asc", nativeQuery = true)
	List<LocationNodeDef> getNodeDefData(
			@Param(value = "orgId") Integer orgId,
			@Param(value = "levelList") List<String> levelList);
	
	@Query(value = "select * from location_node_def where org_id=:orgId and location_node_def_type = :levelType and active = 'Y'", nativeQuery = true)
	Optional<LocationNodeDef> verifyNodeDef(
			@Param(value = "orgId") Integer orgId,
			@Param(value = "levelType") String levelType);

	@Query(value = "select * from location_node_def where org_id=:orgId and active='Y'", nativeQuery = true)
	List<LocationNodeDef> getLevelByOrgID(@Param(value = "orgId") Integer orgId);

	@Modifying
	@Transactional
	@Query(value="update location_node_def set active='N' where org_id=:orgId and location_node_def_type=:levelcode",nativeQuery = true)
	void removeLevel(@Param(value = "orgId") Integer orgId, 
			@Param(value = "levelcode") String levelCodeToRemove);

	@Modifying
	@Transactional
	@Query(value="update location_node_def set display_name=:displayName where org_id=:orgId and location_node_def_type=:levelCode",nativeQuery = true)
	void updateDisplayName(@Param(value = "orgId") Integer orgId, 
			@Param(value = "levelCode") String levelCode, 
			@Param(value = "displayName") String displayName);
	
	@Query(value="SELECT display_name FROM location_node_def where org_id=:orgId and active='Y' and location_node_def_type=:level",nativeQuery = true)
	String getLevelnameByType(@Param(value = "level") String level,
			@Param(value="orgId") Integer orgId);


}
