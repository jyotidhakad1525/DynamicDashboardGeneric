package com.automate.df.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.automate.df.entity.AuditEmpMapping;

public interface AuditEmpMappingDAO extends JpaRepository<AuditEmpMapping, Integer> {
	
	@Query(value="select  * from audit_emp_mapping where ORG_ID = :orgId",nativeQuery = true)
	public List<AuditEmpMapping> getMappingsByOrgEmpId(@Param(value="orgId") Integer orgId
			);

	@Query(value="select  * from audit_emp_mapping where ORG_ID = :orgId and AUDIT_WF_ID = :auditWfId",nativeQuery = true)
	public List<AuditEmpMapping> verifyMapping(@Param(value="orgId") Integer orgId,@Param(value="auditWfId") Integer auditWfId);

}
