package com.automate.df.service;

import java.util.List;
import java.util.Map;

import com.automate.df.model.df.dashboard.DashBoardReq;
import com.automate.df.model.df.dashboard.EventDataRes;
import com.automate.df.model.df.dashboard.LeadSourceRes;
import com.automate.df.model.df.dashboard.TargetAchivement;
import com.automate.df.model.df.dashboard.TodaysRes;
import com.automate.df.model.df.dashboard.VehicleModelRes;

public interface DashBoardService {

	List<TargetAchivement> getTargetAchivementParams(DashBoardReq req);

	List<VehicleModelRes> getVehicleModelData(DashBoardReq req);

	List<VehicleModelRes> getVehicleModelDataByBranch(DashBoardReq req);

	List<LeadSourceRes> getLeadSourceData(DashBoardReq req);

	List<LeadSourceRes> getLeadSourceDataByBranch(DashBoardReq req);

	List<EventDataRes> getEventSourceData(DashBoardReq req);

	List<EventDataRes> getEventSourceDataByBranch(DashBoardReq req);

	Map<String, Object> getLostDropData(DashBoardReq req);
	
	 Map<String, Object> getLostDropDataByBranch(DashBoardReq req);

	Map<String, Object> getTodaysPendingUpcomingData(DashBoardReq req);

}
