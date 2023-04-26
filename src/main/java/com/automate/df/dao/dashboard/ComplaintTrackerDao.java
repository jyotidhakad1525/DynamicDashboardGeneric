package com.automate.df.dao.dashboard;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.automate.df.entity.dashboard.ComplaintsTracker;

public interface ComplaintTrackerDao extends JpaRepository<ComplaintsTracker, Integer>{

	@Query(value = "SELECT id FROM complaint_tracker where sales_consultant in(:empNamesList) ", nativeQuery = true)
	List<Integer> getLeadIdsByEmpNames(@Param(value = "empNamesList") List<String> empNamesList);

	@Query(value = "SELECT id FROM complaint_tracker where branch in(:updatedBranchnameList)", nativeQuery = true)
	List<ComplaintsTracker> findComplaintByBranchList(@Param(value = "updatedBranchnameList")  List<String> updatedBranchnameList);
	
	
}
