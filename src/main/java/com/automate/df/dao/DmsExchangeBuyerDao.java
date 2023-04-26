package com.automate.df.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.automate.df.entity.sales.employee.DmsExchangeBuyer;

public interface DmsExchangeBuyerDao extends JpaRepository<DmsExchangeBuyer, Integer> {
	
	
	@Query(value="select  * from dms_exchange_buyer where lead_id = :leadId",nativeQuery = true)
	public List<DmsExchangeBuyer> getDmsExchangeBuyersByLeadId(@Param(value="leadId") Integer leadId);

}
