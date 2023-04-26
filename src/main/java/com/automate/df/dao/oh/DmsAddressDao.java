package com.automate.df.dao.oh;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.automate.df.entity.oh.DmsAddress;


public interface DmsAddressDao extends JpaRepository<DmsAddress, Integer>{
	@Modifying
	@Transactional 
	@Query(value="update dms_address set active='N' where id=:id",nativeQuery = true)
	void removeBranchMapping(@Param(value = "id") Integer id);
	
	@Query(value="select branch_id from dms_branch where org_map_id=:nodeId",nativeQuery = true)
	Integer getBranchId(@Param(value = "nodeId") Integer nodeId);
	
}
