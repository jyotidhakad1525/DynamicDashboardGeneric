package com.automate.df.dao.salesgap;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.automate.df.entity.salesgap.TargetEntity;
import com.automate.df.entity.salesgap.TargetEntityUser;


public interface TargetUserRepo extends JpaRepository<TargetEntityUser, Integer> {
	
	@Query(value="SELECT * FROM dms_target_setting_user where emp_id=:empId",nativeQuery = true)
	public Optional<TargetEntityUser> findByEmpId(@Param(value="empId") String empId);
	
	@Query(value="SELECT * FROM dms_target_setting_user where emp_id=:empId and is_active='Y'",nativeQuery = true)
	public List<TargetEntityUser> findAllEmpIds(@Param(value="empId") String empId);
	
	
	@Query(value = "SELECT * FROM dms_target_setting_user where org_id=:orgId and department=:department and designation=:designation and branch=:branch and is_active='Y'", nativeQuery = true)
	List<TargetEntityUser> getUserTargetData(@Param(value = "orgId") String orgId,
			@Param(value="department") String deptId,
			@Param(value="designation") String designation,
			@Param(value="branch") String branch
			);
	
	
	@Query(value="SELECT * FROM dms_target_setting_user where emp_id=:empId and start_date=:startDate and end_date=:endDate",nativeQuery = true)
	public Optional<TargetEntityUser> findByEmpIdWithDate(@Param(value="empId") String empId,
			@Param(value="startDate") String startDate,
			@Param(value="endDate") String endDate);

	@Query(value="SELECT * FROM dms_target_setting_user where emp_id=:empId and type='default'",nativeQuery = true)
	public Optional<TargetEntityUser> checkDefaultDataInTargetUser(@Param(value="empId") String empId);

	@Query(value="SELECT * FROM dms_target_setting_user where emp_id=:empId and id=:recordId",nativeQuery = true)
	public Optional<TargetEntityUser> findByEmpIdWithRecordId(@Param(value="recordId")  String recordId, @Param(value="empId")  String empId);

	@Query(value="SELECT * FROM dms_target_setting_user where emp_id = :empId and start_date>=:startDate and end_date<=:endDate",nativeQuery = true)
	public List<TargetEntityUser> findAllQ1(@Param(value="empId") String empId, @Param(value="startDate")  String startDate,  @Param(value="endDate") String endDate);

	@Query(value="SELECT * FROM dms_target_setting_user where emp_id = :empId and start_date>=:startDate",nativeQuery = true)
	public List<TargetEntityUser> findAllQ2(@Param(value="empId") String empId, @Param(value="startDate")  String startDate);

	
	@Query(value="SELECT * FROM dms_target_setting_user where emp_id = :empId ",nativeQuery = true)
	public List<TargetEntityUser> findAllQ3(@Param(value="empId") String empId);
	


}
