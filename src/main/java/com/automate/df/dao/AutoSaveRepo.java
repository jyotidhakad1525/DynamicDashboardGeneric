package com.automate.df.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.automate.df.entity.AutoSaveEntity;

public interface AutoSaveRepo extends JpaRepository<AutoSaveEntity, Integer>{
	
	@Query(value="select  * from auto_save where universal_id=:type",nativeQuery = true)
	Page<AutoSaveEntity> getAutoSaveBasedOnType(@Param(value="type") String type,Pageable pageable);
	
	@Query(value="select  * from auto_save where universal_id = :universal_id",nativeQuery = true)
	List<AutoSaveEntity> getDataByUniversalId(@Param(value="universal_id") String universalId);
	
	@Query(value="select  * from auto_save where universal_id = :universal_id",nativeQuery = true)
	Optional<AutoSaveEntity> getDataByUniversalIdV2(@Param(value="universal_id") String universalId);

}
