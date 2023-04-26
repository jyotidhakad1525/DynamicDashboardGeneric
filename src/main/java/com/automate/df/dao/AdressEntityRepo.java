package com.automate.df.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.automate.df.entity.AddressEntity;


public interface AdressEntityRepo extends JpaRepository<AddressEntity, Integer> {
 
	void deleteAllByBulkUploadId(int bulkUploadId);
	
	List<AddressEntity> findAllByBulkUploadId(int bulkUploadId);
	
}
