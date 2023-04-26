package com.automate.df.service;

import java.util.List;
import java.util.Map;

import com.automate.df.exception.DynamicFormsServiceException;
import com.automate.df.model.df.dashboard.DashBoardReq;
import com.automate.df.model.df.dashboard.DashBoardReqV2;
import com.automate.df.model.df.dashboard.EventDataRes;
import com.automate.df.model.df.dashboard.LeadSourceRes;
import com.automate.df.model.df.dashboard.SalesDataRes;
import com.automate.df.model.df.dashboard.TargetAchivement;
import com.automate.df.model.df.dashboard.VehicleModelRes;

public interface DashBoardServiceV2 {

	List<TargetAchivement> getTargetAchivementParams(DashBoardReqV2 req) throws DynamicFormsServiceException;

	List<VehicleModelRes> getVehicleModelData(DashBoardReqV2 req) throws DynamicFormsServiceException;

	List<VehicleModelRes> getVehicleModelDataByBranch(DashBoardReqV2 req) throws DynamicFormsServiceException;

	List<LeadSourceRes> getLeadSourceData(DashBoardReqV2 req) throws DynamicFormsServiceException;

	List<LeadSourceRes> getLeadSourceDataByBranch(DashBoardReqV2 req) throws DynamicFormsServiceException;

	List<EventDataRes> getEventSourceData(DashBoardReqV2 req) throws DynamicFormsServiceException;

	List<EventDataRes> getEventSourceDataByBranch(DashBoardReqV2 req) throws DynamicFormsServiceException;

	Map<String, Object> getLostDropData(DashBoardReqV2 req) throws DynamicFormsServiceException;
	
	 Map<String, Object> getLostDropDataByBranch(DashBoardReqV2 req) throws DynamicFormsServiceException;

	Map<String, Object> getTodaysPendingUpcomingData(DashBoardReqV2 req) throws DynamicFormsServiceException;

	SalesDataRes getSalesData(DashBoardReqV2 req) throws DynamicFormsServiceException;

	List<Map<String, Long>> getSalesComparsionData(DashBoardReqV2 req) throws DynamicFormsServiceException;

}
