package com.automate.df.dao.salesgap;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.automate.df.entity.salesgap.TargetEntity;

public interface TargetSettingRepo extends JpaRepository<TargetEntity, Integer> {

	@Query(value = "SELECT * FROM dms_target_setting where emp_name=:empName or emp_id=:empId", nativeQuery = true)
	List<TargetEntity> getDataByEmpNameId(@Param(value = "empId") String empId,
			@Param(value = "empName") String empName);

	@Query(value = "SELECT * FROM dms_target_setting where org_id=:orgId and branch=:branchId and department=:departmentId and experience=:experience and salary_range =:salary", nativeQuery = true)
	List<TargetEntity> getTargetmappingDataWithRole(@Param(value = "orgId") String orgId,
			@Param(value = "branchId") String branchId,
			@Param(value = "departmentId") String department, @Param(value = "experience") String experience,
			@Param(value = "salary") String salary);

	@Query(value = "SELECT * FROM dms_target_setting where org_id=:orgId and branch=:branchId  and department=:departmentId and salary_range =:salary", nativeQuery = true)
	List<TargetEntity> getTargetmappingDataWithoutEx(@Param(value = "orgId") String orgId,
			@Param(value = "branchId") String branchId, 
			@Param(value = "departmentId") String department, @Param(value = "salary") long salary);

	@Query(value = "SELECT * FROM dms_target_setting where org_id=:orgId and branch=:branchId and department=:departmentId and experience=:experience", nativeQuery = true)
	List<TargetEntity> getTargetmappingDataWithOutSal(@Param(value = "orgId") String orgId,
			@Param(value = "branchId") String branchId, 
			@Param(value = "departmentId") String department, @Param(value = "experience") Integer experience);
	
	
	@Query(value = "SELECT * FROM dms_target_setting where org_id=:orgId and branch=:branchId and department=:departmentId and experience=:experience and designation=:designation", nativeQuery = true)
	List<TargetEntity> getTargetmappingDataWithRoleAndNoSal(@Param(value = "orgId") String orgId,
			@Param(value = "branchId") String branchId, 
			@Param(value = "departmentId") String department, @Param(value = "experience") String experience,
			@Param(value = "designation") String designation);

	@Query(value = "SELECT * FROM dms_target_setting where org_id=:orgId and branch=:branchId and department=:departmentId and salary_range =:salary and designation=:designation and experience is null", nativeQuery = true)
	List<TargetEntity> getTargetmappingDataWithOutExp(@Param(value = "orgId") String orgId,
			@Param(value = "branchId") String branchId,
			@Param(value = "departmentId") String department,
			@Param(value = "salary") String salary,
			@Param(value = "designation") String designation);

	@Query(value = "SELECT * FROM dms_target_setting where org_id=:orgId and branch=:branchId and department=:departmentId and experience=:experience and designation=:designation  and salary_range is null", nativeQuery = true)
	List<TargetEntity> getTargetmappingDataWithOutSal(@Param(value = "orgId") String orgId,
			@Param(value = "branchId") String branchId,
			@Param(value = "departmentId") String department,
			@Param(value = "experience") String experience
			,@Param(value = "designation") String designation);

	@Query(value = "SELECT * FROM dms_target_setting where org_id=:orgId and branch=:branchId  and department=:departmentId and designation=:designation and salary_range is null and experience is null", nativeQuery = true)
	List<TargetEntity> getTargetmappingDataWithOutExpSal(@Param(value = "orgId") String orgId,
			@Param(value = "branchId") String branchId,
			@Param(value = "departmentId") String department,
			@Param(value = "designation") String designation);
	
	
	@Query(value = "SELECT * FROM dms_target_setting where org_id=:orgId and department=:department and designation=:designation and branch=:branch and is_active='Y'", nativeQuery = true)
	List<TargetEntity> getTargetmappingDataWithOutExpSalV2(@Param(value = "orgId") String orgId,
			@Param(value="department") String deptId,
			@Param(value="designation") String designation,
			@Param(value="branch") String branch
			);
	
	
	@Query(value = "SELECT * FROM dms_target_setting where org_id=:orgId and branch=:branchId and department=:departmentId and experience=:experience and salary_range =:salary and designation=:designation", nativeQuery = true)
	List<TargetEntity> getTargetmappingData(@Param(value = "orgId") String orgId,
			@Param(value = "branchId") String branchId,
			@Param(value = "departmentId") String department, @Param(value = "experience") String experience,
			@Param(value = "salary") String salary,
			@Param(value = "designation") String designation);

}
