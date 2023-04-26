package com.automate.df.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.automate.df.entity.ReportQueries;



public interface ReportQueriesDAO extends JpaRepository<ReportQueries, Integer> {

	@Query(value="SELECT * FROM report_queries where report_identifier=:reportId and role_identifier=:roleId",nativeQuery=true)
	public Optional<ReportQueries> findQuery(@Param(value="reportId") String reportId,@Param(value="roleId") String roleId);
}
