package com.automate.df.dao;


import com.automate.df.entity.sales.employee.DmsDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<DmsDepartment, Integer> {

    @Query(value = "select name from location_node_data where id = (select lnd.parent_id from location_node_data as lnd where lnd.id in(select db.org_map_id from dms_branch as db where db.branch_id =:branch_id))", nativeQuery = true)
    String locationName(@Param("branch_id") Integer branch_id);


    @Query(value = "select * from dms_department where branch_id in (select branch_id from dms_branch where org_map_id IS NOT NULL and org_map_id!=0)", nativeQuery = true)
    List<DmsDepartment> findAll();


    @Query(value = "select * from dms_department where branch_id in (select branch_id from dms_branch where org_map_id IS NOT NULL and org_map_id!=0) and department_name =:department_name and org_id =:org_id and is_active = 1", nativeQuery = true)
    List<DmsDepartment> findAllByOrg(@Param("department_name") String department_name,@Param("org_id") Integer org_id);

    @Query(value = "select DISTINCT  department_name from dms_department where org_id =:org_id and is_active = 1" , nativeQuery = true)
    List<String> departmentNameByOrg(@Param("org_id") Integer org_id);

    @Query(value = "select * from dms_department where department_name =:department_name and org_id =:org_id and branch_id =:branch_id", nativeQuery = true)
    List<DmsDepartment> findAllByBranchAndOrg(@Param("department_name") String department_name,@Param("org_id") Integer org_id,@Param("branch_id") Integer branch_id);

}
