package com.automate.df.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.automate.df.entity.sales.lead.DmsInvoice;




public interface DmsInvoiceDao extends JpaRepository<DmsInvoice, Integer>{

	@Query(value="select  * from dms_invoice where lead_id = :id",nativeQuery = true)
	public List<DmsInvoice> getInvoiceDataWithLeadId(@Param(value="id") int leadId);

}
