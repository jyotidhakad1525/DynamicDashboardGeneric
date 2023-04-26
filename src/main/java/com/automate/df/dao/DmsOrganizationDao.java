package com.automate.df.dao;

import com.automate.df.entity.sales.DmsOrganization;
import com.automate.df.entity.sales.employee.DmsDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DmsOrganizationDao extends JpaRepository<DmsOrganization, Integer> {
}
