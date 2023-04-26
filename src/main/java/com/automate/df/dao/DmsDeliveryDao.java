package com.automate.df.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.automate.df.entity.sales.lead.DmsDelivery;



public interface DmsDeliveryDao extends JpaRepository<DmsDelivery, Integer>{
	
	
	@Query(value="select  * from dms_delivery where lead_id = :id",nativeQuery = true)
	public List<DmsDelivery> getDeliveriesWithLeadId(@Param(value="id") int leadId);


}
