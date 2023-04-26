package com.automate.df.util;
public class TrailPlan {
	
}

//
//
//
//
//	import java.sql.Connection;
//	import java.sql.PreparedStatement;
//	import java.sql.ResultSet;
//	import java.sql.SQLException;
//	import java.sql.Statement;
//	import java.text.DateFormat;
//	import java.text.SimpleDateFormat;
//	import java.util.ArrayList;
//	import java.util.HashSet;
//	import java.util.List;
//	import java.util.Set;
//
//	import org.apache.commons.lang.math.NumberUtils;
//	import org.apache.commons.lang3.StringUtils;
//	import org.springframework.beans.factory.annotation.Autowired;
//	import org.springframework.core.env.Environment;
//	import org.springframework.dao.EmptyResultDataAccessException;
//	import org.springframework.jdbc.core.BatchPreparedStatementSetter;
//	import org.springframework.jdbc.core.JdbcTemplate;
//	import org.springframework.jdbc.core.PreparedStatementCreator;
//	import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
//	import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//	import org.springframework.jdbc.support.GeneratedKeyHolder;
//	import org.springframework.jdbc.support.KeyHolder;
//	import org.springframework.stereotype.Component;
//	import org.springframework.util.CollectionUtils;
//	import org.springframework.util.ObjectUtils;
//
//	import com.bayer.bos.CompanyDO;
//	import com.bayer.bos.CountryDO;
//	import com.bayer.bos.CropDO;
//	import com.bayer.bos.DistrictDO;
//	import com.bayer.bos.EntryDO;
//	import com.bayer.bos.EntryObservationDataDO;
//	import com.bayer.bos.FarmerDO;
//	import com.bayer.bos.FunctionDO;
//	import com.bayer.bos.RegionDO;
//	import com.bayer.bos.SeasonDO;
//	import com.bayer.bos.SegmentDO;
//	import com.bayer.bos.StartedByPlannedTrialsCountDO;
//	import com.bayer.bos.StateDO;
//	import com.bayer.bos.TerritoryDO;
//	import com.bayer.bos.TrailAgronomyDataDO;
//	import com.bayer.bos.TrailPlanDO;
//	import com.bayer.bos.TrailPlanListDO;
//	import com.bayer.bos.TrialEntryPlanningDO;
//	import com.bayer.bos.TrialPlanningDO;
//	import com.bayer.bos.UserEntryMappingDO;
//	import com.bayer.bos.WorkFlowStatusRequestDO;
//	import com.bayer.bos.YearDO;
//	import com.bayer.bos.ZoneDO;
//	import com.bayer.constants.BayerConstants;
//	import com.bayer.constants.ErrorCode;
//	import com.bayer.constants.StatusConstants;
//	import com.bayer.constants.UserConstants;
//	import com.bayer.constants.UserRoles;
//	import com.bayer.dao.TrailDao;
//	import com.bayer.error.Error;
//	import com.bayer.error.TrialPlanningError;
//	import com.bayer.exception.BayerException;
//	import com.bayer.mobile.services.dao.StageObservationFormDao;
//	import com.bayer.request.TrialEntryKV;
//	import com.bayer.response.EntriesMasterData;
//	import com.bayer.response.TrailStageRemarksList;
//
//	@Component
//	public class TrailPlan { {
//
//		private static final String SAVE_TRIAL_DATA_FAILED = "Save trial data failed";
//
//		private static final String UPDATE_TREATMENT_PLAN_FAILED = "Update TreatmentPlan failed";
//		
//		private static final String SAVE_TREATMENT_PLAN_FAILED = "Save TreatmentPlan failed";
//
//		private static final String UPDATE_PLANNED_TRIAL_COUNT_FAILED = "Update PlannedTrial Count failed";
//
//		@Autowired
//		private JdbcTemplate jdbcTemplate;
//
//		@Autowired
//		private Environment envProps;
//
//		@Autowired
//		private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
//		
//		@Autowired
//		private StageObservationFormDao stageObservationFormDao;
//		
//		@Override
//		public int updateTrailPlans(TrailPlanDO updateTrailList) {
//			return jdbcTemplate.update(envProps.getProperty("query.updateTrailPlans"), new Object[] { updateTrailList.getPlannedCount(), updateTrailList.getTrailPlanningId() });
//		}
//		
//		@Override
//		public int updateLandId(TrailPlanDO updateLandId) {
//			return jdbcTemplate.update(envProps.getProperty("query.updateLandId"), new Object[] { updateLandId.getUpdateLandId(), updateLandId.getTrailPlanningId(), updateLandId.gettFAId(), updateLandId.getLandId() });
//		}
//
//		@Override
//		public List<TrailPlanDO> getTrailPlans(Integer yearId, Integer cropId, Integer seasonId, Integer userId, Integer countryId, Integer companyId, Integer functionId) {
//			String roleName = null;
//			List<TrailPlanDO> trailPlans = new ArrayList<TrailPlanDO>();
//			if (userId != null) {
//				roleName = jdbcTemplate.queryForObject(envProps.getProperty("query.getUserRoleName"),
//						new Object[] { userId }, (result, rowNum) -> {
//							return new String(result.getString("user_role_name"));
//						});
//			}
//			if (roleName.equals(UserRoles.AGRONOMIST.getValue())) {
//				System.out.println("Agronomist User:" + roleName);
//				return jdbcTemplate.query(envProps.getProperty("query.getTrailPlansForAgronomist"),
//						new Object[] { yearId, cropId, seasonId, userId, countryId, companyId, functionId }, (result, rowNum) -> {
//							TrailPlanDO trailPlan = constructTrailPlan(result);
//							List<EntryDO> listOfEntries = new ArrayList<>();
//							trailPlan.setListOfEntries(listOfEntries);
//							getEntries(trailPlan.getTrailPlanningId(), listOfEntries);
//							trailPlan.setPlannedStartedCount(getStartedCountByTrialId(trailPlan.getTrailPlanningId()));
//							return trailPlan;
//						});
//			}
//			if (roleName.equals(UserRoles.ZONAL_MANAGER.getValue())) {
//				System.out.println("ZONAL_MANAGER User:" + roleName);
//				return jdbcTemplate.query(envProps.getProperty("query.getTrailPlansForZonalManager"),
//						new Object[] { yearId, cropId, seasonId, userId, countryId, companyId, functionId }, (result, rowNum) -> {
//							TrailPlanDO trailPlan = constructTrailPlan(result);
//							List<EntryDO> listOfEntries = new ArrayList<>();
//							trailPlan.setListOfEntries(listOfEntries);
//							getEntries(trailPlan.getTrailPlanningId(), listOfEntries);
//							trailPlan.setPlannedStartedCount(getStartedCountByTrialId(trailPlan.getTrailPlanningId()));
//							return trailPlan;
//						});
//
//			}
//			if (roleName.equals(UserRoles.CROP_HEAD.getValue())) {
//				System.out.println("CROP_HEAD User:" + roleName);
//				return jdbcTemplate.query(envProps.getProperty("query.getTrailPlans"),
//						new Object[] { yearId, cropId, seasonId,userId, countryId, companyId, functionId  }, (result, rowNum) -> {
//							TrailPlanDO trailPlan = constructTrailPlan(result);
//							List<EntryDO> listOfEntries = new ArrayList<>();
//							trailPlan.setListOfEntries(listOfEntries);
//							getEntries(trailPlan.getTrailPlanningId(), listOfEntries);
//							trailPlan.setPlannedStartedCount(getStartedCountByTrialId(trailPlan.getTrailPlanningId()));
//							return trailPlan;
//						});
//			}
//			return trailPlans;
//		}
//
//		@Override
//		public List<TrailPlanDO> getAllTrails(Integer yearId, Integer cropId, Integer seasonId, Integer userId, Integer countryId ,Integer companyId,Integer functionId) {
//			String roleName = null;
//			List<TrailPlanDO> trailPlans = new ArrayList<TrailPlanDO>();
//			if (userId != null) {
//				roleName = jdbcTemplate.queryForObject(envProps.getProperty("query.getUserRoleName"),
//						new Object[] { userId }, (result, rowNum) -> {
//							return new String(result.getString("user_role_name"));
//						});
//			}
//			if (roleName.equals(UserRoles.AGRONOMIST.getValue())) {
//				System.out.println("Agronomist User:" + roleName);
//				return jdbcTemplate.query(envProps.getProperty("query.getAllTrailsForAgronomist"),
//						new Object[] { yearId, cropId, seasonId, userId, countryId , companyId, functionId  }, (result, rowNum) -> {
//							TrailPlanDO trailPlan = constructTrailPlan(result);
//							List<EntryDO> listOfEntries = new ArrayList<>();
//							trailPlan.setListOfEntries(listOfEntries);
//							getEntries(trailPlan.getTrailPlanningId(), listOfEntries);
//							return trailPlan;
//						});
//			}
//			if (roleName.equals(UserRoles.ZONAL_MANAGER.getValue())) {
//				System.out.println("ZONAL_MANAGER User:" + roleName);
//				return jdbcTemplate.query(envProps.getProperty("query.getAllTrailsForZonalManager"),
//						new Object[] { yearId, cropId, seasonId, userId, countryId , companyId, functionId  }, (result, rowNum) -> {
//							TrailPlanDO trailPlan = constructTrailPlan(result);
//							List<EntryDO> listOfEntries = new ArrayList<>();
//							trailPlan.setListOfEntries(listOfEntries);
//							getEntries(trailPlan.getTrailPlanningId(), listOfEntries);
//							return trailPlan;
//						});
//
//			}
//			if (roleName.equals(UserRoles.CROP_HEAD.getValue())) {
//				System.out.println("CROP_HEAD User:" + roleName);
//				return jdbcTemplate.query(envProps.getProperty("query.getAllTrails"),
//						new Object[] { yearId, cropId, seasonId, countryId , companyId, functionId  }, (result, rowNum) -> {
//							TrailPlanDO trailPlan = constructTrailPlan(result);
//							List<EntryDO> listOfEntries = new ArrayList<>();
//							trailPlan.setListOfEntries(listOfEntries);
//							getEntries(trailPlan.getTrailPlanningId(), listOfEntries);
//							return trailPlan;
//						});
//			}
//			return trailPlans;
//		}
//		
//		@Override
//		public Integer getStartedCountByTrialId(Integer trialId) throws SQLException {
//			try {
//				return jdbcTemplate.queryForObject(envProps.getProperty("query.getStartedCountByTrialId"),
//						new Object[] { trialId }, (result, rowNum) -> {
//							return result.getInt("start_count");
//						});
//			} catch (EmptyResultDataAccessException e) {
//				return 0;
//			}
//		}
//
//		private TrailPlanDO constructTrailPlan(ResultSet result) throws SQLException {
//			TrailPlanDO trailPlan = new TrailPlanDO();
//			trailPlan.setTrailPlanningId(result.getInt("trial_planning_id"));
//			trailPlan.setTrailCategoryName(result.getString("trial_category_name"));
//			trailPlan.setTrailSubcategoryName(result.getString("trial_sub_category_name"));
//			trailPlan.setTrailObjectiveId(result.getInt("trial_objective_id"));
//			trailPlan.setTrailSubcategoryId(result.getInt("trial_sub_category_id"));
//			trailPlan.setTrailObjective(result.getString("trial_objective"));
//			CropDO crop = new CropDO();
//			crop.setCropId(result.getInt("crop_id"));
//			crop.setCropName(result.getString("crop_name"));
//			trailPlan.setCrop(crop);
//			FunctionDO function = new FunctionDO();
//			function.setFunctionId(result.getInt("function_id"));
//			function.setFunctionName(result.getString("function_name"));
//			trailPlan.setFunction(function);
//			CompanyDO company = new CompanyDO(result.getInt("company_id"), result.getString("company_name"));
//			trailPlan.setCompany(company);
//			YearDO year = new YearDO();
//			year.setYearId(result.getInt("year_id"));
//			year.setYearName(result.getInt("year_name"));
//			trailPlan.setYear(year);
//			TerritoryDO territory=new TerritoryDO();
//			territory.setTerritoryId(result.getInt("territory_id"));
//			territory.setTerritoryName(result.getString("territory_name"));
//			trailPlan.setTerritory(territory);
//			RegionDO region = new RegionDO();
//			region.setRegionId(result.getInt("region_id"));
//			region.setRegionName(result.getString("region_name"));
//			trailPlan.setRegion(region);
//			StateDO state = new StateDO();
//			state.setStateId(result.getInt("state_id"));
//			state.setStateName(result.getString("state_name"));
//			trailPlan.setState(state);
//			SeasonDO season = new SeasonDO();
//			season.setSeasonId(result.getInt("season_id"));
//			season.setSeasonName(result.getString("season_name"));
//			trailPlan.setSeason(season);
//			SegmentDO segment = new SegmentDO();
//			segment.setSegmentId(result.getInt("crop_segment_id"));
//			segment.setSegmentName(result.getString("crop_segment_name"));
//			trailPlan.setSegment(segment);
//			trailPlan.setStatus(result.getString("trial_status"));
//			trailPlan.setPlannedCount(result.getInt("planned_trials_count"));
//			DistrictDO district = new DistrictDO();
//			district.setDistrictId(result.getInt("district_id"));
//			district.setDistrictName(result.getString("district_name"));
//			trailPlan.setDistrict(district);
//			CountryDO country = new CountryDO();
//			country.setCountryId(result.getInt("country_id"));
//			country.setCountryName(result.getString("country_name"));
//			trailPlan.setCountry(country);
//			ZoneDO zone = new ZoneDO();
//			zone.setZoneId(result.getInt("zone_id"));
//			zone.setZoneName(result.getString("zone_name"));
//			trailPlan.setZone(zone);
//			trailPlan.setNumberOfReplications(result.getInt("number_of_replications"));
//			trailPlan.setNumberOfTreatments(result.getInt("number_of_treatments"));
//			return trailPlan;
//		}
//		
//		private TrailPlanDO constructStartedTrailPlan(ResultSet result) throws SQLException {
//			TrailPlanDO trailPlan = new TrailPlanDO();
//			trailPlan.setTrailPlanningId(result.getInt("trial_planning_id"));
//			trailPlan.setTrailCategoryName(result.getString("trial_category_name"));
//			trailPlan.setTrailSubcategoryName(result.getString("trial_sub_category_name"));
//			trailPlan.setTrailObjectiveId(result.getInt("trial_objective_id"));
//			trailPlan.setTrailSubcategoryId(result.getInt("trial_sub_category_id"));
//			trailPlan.setTrailObjective(result.getString("trial_objective"));
//			trailPlan.setWorkFlowStatus(result.getString("work_flow_status"));
//			trailPlan.setDiscardStatus(result.getString("status") != null ? result.getString("status") : "");
//			CropDO crop = new CropDO();
//			crop.setCropId(result.getInt("crop_id"));
//			crop.setCropName(result.getString("crop_name"));
//			trailPlan.setCrop(crop);
//			FunctionDO function = new FunctionDO();
//			function.setFunctionId(result.getInt("function_id"));
//			function.setFunctionName(result.getString("function_name"));
//			trailPlan.setFunction(function);
//			CompanyDO company = new CompanyDO(result.getInt("company_id"), result.getString("company_name"));
//			trailPlan.setCompany(company);
//			YearDO year = new YearDO();
//			year.setYearId(result.getInt("year_id"));
//			year.setYearName(result.getInt("year_name"));
//			trailPlan.setYear(year);
//			TerritoryDO territory=new TerritoryDO();
//			territory.setTerritoryId(result.getInt("territory_id"));
//			territory.setTerritoryName(result.getString("territory_name"));
//			trailPlan.setTerritory(territory);
//			RegionDO region = new RegionDO();
//			region.setRegionId(result.getInt("region_id"));
//			region.setRegionName(result.getString("region_name"));
//			trailPlan.setRegion(region);
//			StateDO state = new StateDO();
//			state.setStateId(result.getInt("state_id"));
//			state.setStateName(result.getString("state_name"));
//			trailPlan.setState(state);
//			SeasonDO season = new SeasonDO();
//			season.setSeasonId(result.getInt("season_id"));
//			season.setSeasonName(result.getString("season_name"));
//			trailPlan.setSeason(season);
//			SegmentDO segment = new SegmentDO();
//			segment.setSegmentId(result.getInt("crop_segment_id"));
//			segment.setSegmentName(result.getString("crop_segment_name"));
//			trailPlan.setSegment(segment);
//			trailPlan.setStatus(result.getString("trial_status"));
//			trailPlan.setPlannedCount(result.getInt("planned_trials_count"));
//			DistrictDO district = new DistrictDO();
//			district.setDistrictId(result.getInt("district_id"));
//			district.setDistrictName(result.getString("district_name"));
//			trailPlan.setDistrict(district);
//			CountryDO country = new CountryDO();
//			country.setCountryId(result.getInt("country_id"));
//			country.setCountryName(result.getString("country_name"));
//			trailPlan.setCountry(country);
//			ZoneDO zone = new ZoneDO();
//			zone.setZoneId(result.getInt("zone_id"));
//			zone.setZoneName(result.getString("zone_name"));
//			trailPlan.setZone(zone);
//			trailPlan.setNumberOfReplications(result.getInt("number_of_replications"));
//			trailPlan.setNumberOfTreatments(result.getInt("number_of_treatments"));
//			return trailPlan;
//		}
//
//		@Override
//		public void assignTrails(Integer landId, Integer userId, Integer trailPlanningId) throws BayerException {
//
//			List<Integer> entryUserMappingIds = jdbcTemplate.query(envProps.getProperty("query.checkDuplicateLands"),
//					new Object[] { landId, trailPlanningId }, (result, rowNum) -> {
//						return new Integer(result.getInt("entry_user_mapping_id"));
//					});
//			if (entryUserMappingIds.isEmpty()) {
//				List<Integer> entryInfoIds = jdbcTemplate.query(envProps.getProperty("query.getEntryInfoIds"),
//						new Object[] { trailPlanningId }, (result, rowNum) -> {
//							return new Integer(result.getInt("trial_entry_info_id"));
//						});
//				if(entryInfoIds.isEmpty()) {
//					List<Error> errors = new ArrayList<Error>();
//					errors.add(new Error(ErrorCode.ENTRIES_NOT_CREATED_FOR_THIS_TRIAL,
//							UserConstants.ENTRIES_NOT_CREATED_FOR_THIS_TRIAL));
//					throw new BayerException(errors);
//				}
//
//				for (int entryId : entryInfoIds) {
//					jdbcTemplate.update(envProps.getProperty("query.assignTrails"),
//							new Object[] { userId, landId, entryId });
//				}
//				
//				
//			} else {
//				List<Error> errors = new ArrayList<Error>();
//				errors.add(new Error(ErrorCode.DUPLICATE_LAND_CANNOT_BE_ASSIGNED_TO_SAME_TRIAL,
//						UserConstants.DUPLICATE_LAND_CANNOT_BE_ASSIGNED_TO_SAME_TRIAL));
//				throw new BayerException(errors);
//
//			}
//		}
//
//		@Override
//		public void discardFiller(Integer landId, Integer userId, Integer trailPlanningId) {
//			List<EntryDO> listOfEntries = new ArrayList<>();
//			getEntries(trailPlanningId, listOfEntries);
//			for (EntryDO entryDo : listOfEntries) {
//				if(StringUtils.isNoneBlank(entryDo.getRemarks()) && entryDo.getRemarks().equalsIgnoreCase("filler")) {
//					 jdbcTemplate.update(envProps.getProperty("query.updateDiscardEntryStatus"),
//							new Object[] { "filler",userId, landId, entryDo.getEntryInfoId() });
//					 stageObservationFormDao.updateEntryUserMapWorkFlowStatus("submitted",userId, landId, entryDo.getEntryInfoId());
//				}
//			}
//		}
//
//		@Override
//		public List<TrialPlanningError> planTrials(List<TrialPlanningDO> trialPlanningList,int userId, String userRole, Integer countryId, Integer companyId, Integer functionId) {
//
//			List<TrialPlanningError> errors = new ArrayList<>();
//			TrialPlanningError error = null;
//			
//			List<TrialPlanningDO> trialsList = new ArrayList<TrialPlanningDO>();
//			
//			//Fix: unassigned district trials plans are uploading for user
//			List<String> districtNames = new ArrayList<>();
//			List<DistrictDO> districts = getDistrictsByUserId(userId);
//			for(DistrictDO district: districts) {
//				districtNames.add(district.getDistrictName());
//			}
//
//			for (TrialPlanningDO trialPlanningDO : trialPlanningList) {
//				try {
//					TrialPlanningError trialPlanningError =null;
//					//Mandatory data validation
//					trialPlanningError=validateTrialPlanForMandatoryData(trialPlanningDO.getSno(), trialPlanningDO.getCountry(), trialPlanningDO.getCompany(),
//							trialPlanningDO.getFunction(), trialPlanningDO.getCrop(),trialPlanningDO.getSegment(),trialPlanningDO.getTrialCategory(),
//							trialPlanningDO.getTrialSubCategory(),trialPlanningDO.getTrialObjective(),trialPlanningDO.getYear(),trialPlanningDO.getZone(),
//							trialPlanningDO.getState(), trialPlanningDO.getRegion(),trialPlanningDO.getTerritory(),trialPlanningDO.getDistrict(),trialPlanningDO.getSeason());
//					List<String> invalidFields=new ArrayList<>();
//					
//					if(StringUtils.isEmpty(trialPlanningDO.getNumberOfTrials()) || !NumberUtils.isNumber(trialPlanningDO.getNumberOfTrials())){
//						invalidFields.add("numberOfTrials");
//						trialPlanningError = addErrorFields(trialPlanningDO, trialPlanningError, invalidFields);
//					}
//					if(NumberUtils.isNumber(trialPlanningDO.getNumberOfTrials().trim()) && Integer.parseInt(trialPlanningDO.getNumberOfTrials().trim()) <= 0 ) {
//						invalidFields.add("numberOfTrials");
//						trialPlanningError = addErrorFields(trialPlanningDO, trialPlanningError, invalidFields);
//					}
//					if (trialPlanningError != null) {
//						errors.add(trialPlanningError);
//					}
//					
//					 trialPlanningError =  validateTrialPlanFields(trialPlanningDO.getSno(), trialPlanningDO.getCountry(), trialPlanningDO.getCompany(),
//							trialPlanningDO.getFunction(), trialPlanningDO.getCrop(),trialPlanningDO.getSegment(),trialPlanningDO.getTrialCategory(),
//							trialPlanningDO.getTrialSubCategory(),trialPlanningDO.getTrialObjective(),Integer.parseInt(trialPlanningDO.getYear()),trialPlanningDO.getZone(),
//							trialPlanningDO.getState(), trialPlanningDO.getRegion(),trialPlanningDO.getTerritory(),trialPlanningDO.getDistrict(),trialPlanningDO.getSeason());
//					if (trialPlanningError != null) {
//						errors.add(trialPlanningError);
//						continue;
//					}
//					//Rolebased validation
//						if(!isCropAndUserAssociated(userId,trialPlanningDO.getCrop(), countryId, companyId, functionId)){
//							error = new TrialPlanningError();
//							error.setErrorCode(ErrorCode.USER_AND_CROP_NOT_ASSOCIATED);
//							error.setErrorMessage(String.format("User is not permitted to create the trial for given crop :%s",trialPlanningDO.getCrop()));
//							error.setRecordLineNumber(trialPlanningDO.getSno());
//							errors.add(error);
//						}
//					//region validation for regional manager
//					if(UserRoles.REGIONAL_MANGAER.getValue().toString().equalsIgnoreCase(userRole)){
//						error = new TrialPlanningError();
//						error.setErrorCode(ErrorCode.USER_AND_CROP_NOT_ASSOCIATED);
//						error.setErrorMessage(String.format("User is not permitted to create the trial for given region :%s",trialPlanningDO.getRegion()));
//						error.setRecordLineNumber(trialPlanningDO.getSno());
//						errors.add(error);
//						
//					}
//					//Fix: unassigned district trials plans are uploading for user
//					if(!districtNames.contains(trialPlanningDO.getDistrict())) {
//						error = new TrialPlanningError();
//						error.setErrorCode(ErrorCode.USER_AND_DISTRICT_NOT_ASSOCIATED);
//						error.setErrorMessage(String.format("User is not permitted to create the trial for given district :%s",trialPlanningDO.getDistrict()));
//						error.setRecordLineNumber(trialPlanningDO.getSno());
//						errors.add(error);
//					}
//					
//					String treatMent1 = trialPlanningDO.getTreatment1Value();
//					String treatMent2 = trialPlanningDO.getTreatment2Value();
//					String treatMent3 = trialPlanningDO.getTreatment3Value();
//					String treatMent4 = trialPlanningDO.getTreatment4Value();
//					String treatMent5 = trialPlanningDO.getTreatment5Value();
//					String treatMent6 = trialPlanningDO.getTreatment6Value();
//					
//					int noOfTreatMents=0;
//					int noOfReplications=0;
//					
//					if(null == trialPlanningDO.getNumberOfTreatments())
//						trialPlanningDO.setNumberOfTreatments("");
//					if(null == trialPlanningDO.getNumberOfReplications())
//						trialPlanningDO.setNumberOfReplications("");
//					if(null == trialPlanningDO.getTreatmentIdentifier())
//						trialPlanningDO.setTreatmentIdentifier("");
//					
//					if(null == treatMent1)
//						treatMent1 = "";
//					if(null == treatMent2)
//						treatMent2 = "";
//					if(null == treatMent3)
//						treatMent3 = "";
//					if(null == treatMent4)
//						treatMent4 = "";
//					if(null == treatMent5)
//						treatMent5 = "";
//					if(null == treatMent6)
//						treatMent6 = "";
//					
//					try {
//						noOfTreatMents = Integer.parseInt(trialPlanningDO.getNumberOfTreatments().trim()); 
//						noOfReplications = Integer.parseInt(trialPlanningDO.getNumberOfReplications().trim());
//					} catch(NumberFormatException ex){
//						setTechnicalError(errors, trialPlanningDO, "Number of treatments/replications should not be empty and allows only numbers");
//					} catch (NullPointerException exe) {
//						setTechnicalError(errors, trialPlanningDO, "Number of treatments/replications should not be empty and allows only numbers");
//					}
//					
//					if(noOfTreatMents > 6 ){
//						setTechnicalError(errors, trialPlanningDO, "noOfTreatments should not be greater than 6.");
//						continue;
//					}
//					
//					if(noOfTreatMents < 0 || noOfReplications < 0){
//						setTechnicalError(errors, trialPlanningDO, "noOfTreatments/Replications doesn't allow nagative values");
//						continue;
//					}
//
//					if(StringUtils.isEmpty(trialPlanningDO.getTreatmentIdentifier().trim())) {
//						setTechnicalError(errors, trialPlanningDO, "TreatmentIdentifier should not be empty ");
//					}
//					
//					if(NumberUtils.isNumber(trialPlanningDO.getNumberOfTreatments().trim()) && noOfTreatMents == 0 && !trialPlanningDO.getTreatmentIdentifier().trim().equals("NA")) {
//						setTechnicalError(errors, trialPlanningDO, "TreatmentIdentifier should be 'NA' when noOfTreatments value is zero");
//					}
//					
//						boolean treatmentValidationFailed = false;
//						if(noOfTreatMents == 0 && trialPlanningDO.getTreatmentIdentifier().trim().equals("NA")) {
//
//	                		if (StringUtils.isNotEmpty(treatMent1.trim())|| StringUtils.isNotEmpty(treatMent2.trim())
//											|| StringUtils.isNotEmpty(treatMent3.trim())|| StringUtils.isNotEmpty(treatMent4.trim()) 
//											|| StringUtils.isNotEmpty(treatMent5.trim())|| StringUtils.isNotEmpty(treatMent6.trim())) {
//								treatmentValidationFailed = true;
//							}
//						} else if ( (null != trialPlanningDO.getNumberOfTreatments() && NumberUtils.isNumber(trialPlanningDO.getNumberOfTreatments().trim()) && noOfTreatMents >= 0) 
//								&& (null != trialPlanningDO.getNumberOfReplications() && NumberUtils.isNumber(trialPlanningDO.getNumberOfReplications().trim()) &&  noOfReplications >= 0)) {
//	                    	
//	                    	if(Integer.parseInt(trialPlanningDO.getNumberOfTreatments().trim()) > 0) {
//	                    		
//	                    		if (noOfTreatMents == 1
//	    								&& (StringUtils.isEmpty(treatMent1.trim())|| StringUtils.isNotEmpty(treatMent2.trim())
//	    										|| StringUtils.isNotEmpty(treatMent3.trim())|| StringUtils.isNotEmpty(treatMent4.trim()) 
//	    										|| StringUtils.isNotEmpty(treatMent5.trim())|| StringUtils.isNotEmpty(treatMent6.trim()))) {
//	    							treatmentValidationFailed = true;
//	    						}
//	    						if (noOfTreatMents == 2
//	    								&& (StringUtils.isEmpty(treatMent1.trim())|| StringUtils.isEmpty(treatMent2.trim())
//	    										|| StringUtils.isNotEmpty(treatMent3.trim())|| StringUtils.isNotEmpty(treatMent4.trim()) 
//	    										|| StringUtils.isNotEmpty(treatMent5.trim())|| StringUtils.isNotEmpty(treatMent6.trim()))) {
//	    							treatmentValidationFailed = true;
//	    						}
//	    						if (noOfTreatMents == 3
//	    								&& (StringUtils.isEmpty(treatMent1.trim())|| StringUtils.isEmpty(treatMent2.trim())
//	    										|| StringUtils.isEmpty(treatMent3.trim())|| StringUtils.isNotEmpty(treatMent4.trim()) 
//	    										|| StringUtils.isNotEmpty(treatMent5.trim())|| StringUtils.isNotEmpty(treatMent6.trim()))) {
//	    							treatmentValidationFailed = true;
//	    						}
//
//	    						if (noOfTreatMents == 4
//	    								&& (StringUtils.isEmpty(treatMent1.trim())|| StringUtils.isEmpty(treatMent2.trim())
//	    										|| StringUtils.isEmpty(treatMent3.trim())|| StringUtils.isEmpty(treatMent4.trim()) 
//	    										|| StringUtils.isNotEmpty(treatMent5.trim())|| StringUtils.isNotEmpty(treatMent6.trim()))) {
//	    							treatmentValidationFailed = true;
//	    						}
//	    						if (noOfTreatMents == 5
//	    								&& (StringUtils.isEmpty(treatMent1.trim())|| StringUtils.isEmpty(treatMent2.trim())
//	    										|| StringUtils.isEmpty(treatMent3.trim())|| StringUtils.isEmpty(treatMent4.trim()) 
//	    										|| StringUtils.isEmpty(treatMent5.trim())|| StringUtils.isNotEmpty(treatMent6.trim()))) {
//	    							treatmentValidationFailed = true;
//	    						}
//	    						if (noOfTreatMents == 6
//	    								&& (StringUtils.isEmpty(treatMent1.trim())|| StringUtils.isEmpty(treatMent2.trim())
//	    										|| StringUtils.isEmpty(treatMent3.trim())|| StringUtils.isEmpty(treatMent4.trim()) 
//	    										|| StringUtils.isEmpty(treatMent5.trim())|| StringUtils.isEmpty(treatMent6.trim()))) {
//	    							treatmentValidationFailed = true;
//	    						}
//	                    	}
//						} 
//						
//						if (treatmentValidationFailed) {
//							error = new TrialPlanningError();
//							error.setErrorCode(ErrorCode.INVALID_TREATMENT_DATA);
//							error.setErrorMessage("Mismatch in number of treatments and treatment values");
//							error.setRecordLineNumber(trialPlanningDO.getSno());
//							errors.add(error);
//							continue;
//						}
//	                    trialsList.add(trialPlanningDO);
//
//				} catch (Exception e) {
//					setTechnicalError(errors, trialPlanningDO, e.getMessage());
//				}
//
//			}
//			
//			if(!ObjectUtils.isEmpty(errors))
//				return errors;
//			
//			for (TrialPlanningDO trialPlanningDO : trialsList) {
//
//				int tialPlanningId = getTrialPlanningId(trialPlanningDO, errors);
//				if (!ObjectUtils.isEmpty(errors))
//					return errors;
//
//				if (tialPlanningId > 0) {
//					updatePlannedTrialCount(trialPlanningDO, tialPlanningId, errors);
//					updateTreatmentPlan(trialPlanningDO, tialPlanningId, errors);
//				} else {
//					Long trialPlannindId = saveTrial(trialPlanningDO, errors);
//					if(trialPlannindId != 0) {
//						saveTreatmentPlan(trialPlannindId, trialPlanningDO, errors);
//					}
//				}
//				
//				if (!ObjectUtils.isEmpty(errors))
//					return errors;
//			}
//			
//			return errors;
//		}
//
//		private TrialPlanningError addErrorFields(TrialPlanningDO trialPlanningDO, TrialPlanningError trialPlanningError,
//				List<String> invalidFields) {
//			if (trialPlanningError != null) {
//				trialPlanningError.getErrorFields().addAll(invalidFields);
//			}
//			else{
//				trialPlanningError = new TrialPlanningError();
//				trialPlanningError.setErrorCode(ErrorCode.INVALID_TRIAL_DATA);
//				trialPlanningError.setRecordLineNumber(trialPlanningDO.getSno());
//				trialPlanningError.setErrorMessage("Field values are empty or invalid data !");
//				trialPlanningError.setErrorFields(invalidFields);
//			}
//			return trialPlanningError;
//		}
//
//		private TrialPlanningError validateTrialPlanForMandatoryData(String sno, String country, String company,
//				String function, String crop, String segment, String trialCategory, String trialSubCategory,
//				String trialObjective, String year, String zone, String state, String region, String territory,
//				String district, String season) {
//			
//			    List<String> invalidFields = new ArrayList<>();
//				TrialPlanningError trialPlanningError = null;
//				
//				if(StringUtils.isEmpty(sno) || !NumberUtils.isNumber(sno)){
//					invalidFields.add("sno");
//				}
//				if(StringUtils.isEmpty(country)){
//					invalidFields.add("country");
//				}
//				if(StringUtils.isEmpty(company)){
//					invalidFields.add("company");
//				}
//				if(StringUtils.isEmpty(function)){
//					invalidFields.add("function");
//				}
//				
//				if(StringUtils.isEmpty(crop)){
//					invalidFields.add("crop");
//				}
//				
//				if(StringUtils.isEmpty(segment)){
//					invalidFields.add("segment");
//				}
//				
//				if(StringUtils.isEmpty(trialCategory)){
//					invalidFields.add("trialCategory");
//				}
//				
//				if(StringUtils.isEmpty(trialSubCategory)){
//					invalidFields.add("trialSubCategory");
//				}
//				
//				if(StringUtils.isEmpty(trialObjective)){
//					invalidFields.add("trialObjective");
//				}
//				
//				if(StringUtils.isEmpty(year) || !NumberUtils.isNumber(year)){
//					invalidFields.add("year");
//				}
//		
//				if(StringUtils.isEmpty(zone)){
//					invalidFields.add("zone");
//				}
//				if(StringUtils.isEmpty(state)){
//					invalidFields.add("state");
//				}
//				if(StringUtils.isEmpty(region)){
//					invalidFields.add("region");
//				}
//				if(StringUtils.isEmpty(territory)){
//					invalidFields.add("territory");
//				}
//				if(StringUtils.isEmpty(district)){
//					invalidFields.add("district");
//				}
//				if(StringUtils.isEmpty(season)){
//					invalidFields.add("season");
//				}
//
//			if (!invalidFields.isEmpty()) {
//					trialPlanningError = new TrialPlanningError();
//					trialPlanningError.setErrorCode(ErrorCode.INVALID_TRIAL_DATA);
//					trialPlanningError.setRecordLineNumber(sno);
//					trialPlanningError.setErrorMessage("Field values are empty or invalid data type !");
//					trialPlanningError.setErrorFields(invalidFields);
//				}
//			return trialPlanningError;
//			
//		}
//
//		private boolean isCropAndUserAssociated(int userId, String crop, Integer countryId, Integer companyId, Integer functionId) {
//
//			int count = jdbcTemplate.queryForObject(envProps.getProperty("query.selectUserCropCount"),
//					new Object[] { userId,crop, countryId, companyId, functionId }, Integer.class);
//
//			if (count > 0) {
//				return true;
//			}
//
//			return false;
//		
//		}
//		
//		@SuppressWarnings("unused")
//		private boolean isUserAndRegionAssociated(int userId, String region) {
//
//			int count = jdbcTemplate.queryForObject(envProps.getProperty("query.selectUserRegionCount"),
//					new Object[] { userId,region }, Integer.class);
//
//			if (count > 0) {
//				return true;
//			}
//
//			return false;
//		
//		}
//		
//		private void setTechnicalError(List<TrialPlanningError> errors, TrialPlanningDO trialPlanningDO, String errorMsg) {
//			TrialPlanningError error;
//			error = new TrialPlanningError();
//			error.setErrorCode(ErrorCode.TECHNICAL_ERROR);
//			error.setErrorMessage(errorMsg);
//			error.setRecordLineNumber(trialPlanningDO.getSno());
//			errors.add(error);
//		}
//
//		private TrialPlanningError validateTrialPlanFields(String sno, String country, String company, String function, String crop,
//				String segment, String trialCategory, String trialSubCategory, String trialObjective, int year, String zone,
//				String state, String region, String territory, String district, String season) {
//
//			List<String> invalidFields = new ArrayList<>();
//			TrialPlanningError trialPlanningError = null;
//
//			if (!isValidCountry(country)) {
//				invalidFields.add("country");
//			}
//			if (!isValidCompany(company)) {
//				invalidFields.add("company");
//			}
//
//			if (!isValidFunction(function)) {
//				invalidFields.add("function");
//			}
//			if (!isValidCrop(crop, function, company, country)) {
//				invalidFields.add("crop");
//			}
//			if (!isValidSegment(segment)) {
//				invalidFields.add("segment");
//			}
//			if (!isValidTrialCategory(trialCategory)) {
//				invalidFields.add("trialCategory");
//			}
//			if (!isValidTrialSubCategory(trialCategory, trialSubCategory)) {
//				invalidFields.add("trialSubCategory");
//			}
//			if (!isValidTrialObjective(trialObjective)) {
//				invalidFields.add("trialObjective");
//			}
//			if (!isValidYear(year)) {
//				invalidFields.add("year");
//			}
//			if (!isValidZone(zone, country)) {
//				invalidFields.add("zone");
//			}
//			if (!isValidState(state, zone, country)) {
//				invalidFields.add("state");
//			}
//			if (!isValidRegion(region, state, zone, country)) {
//				invalidFields.add("region");
//			}
//			if (!isValidTerritory(territory, region, state, zone, country)) {
//				invalidFields.add("territory");
//			}
//			if (!isValidDistrict(district, territory, region, state, zone, country)) {
//				invalidFields.add("district");
//			}
//			if (!isValidSeason(season)) {
//				invalidFields.add("season");
//			}
//
//			if (!invalidFields.isEmpty()) {
//				trialPlanningError = new TrialPlanningError();
//				trialPlanningError.setErrorCode(ErrorCode.INVALID_TRIAL_DATA_FIELDS);
//				trialPlanningError.setRecordLineNumber(sno);
//				trialPlanningError.setErrorMessage("Please provide the correct fields !");
//				trialPlanningError.setErrorFields(invalidFields);
//			}
//
//			return trialPlanningError;
//		}
//
//		private TrialPlanningError validateTrialEntryPlanFields(TrialEntryPlanningDO trialEntryPlanningDO) {
//
//			TrialPlanningError trialPlanningError = validateTrialPlanFields(trialEntryPlanningDO.getSno(), trialEntryPlanningDO.getCountry(), trialEntryPlanningDO.getCompany(),
//					trialEntryPlanningDO.getFunction(), trialEntryPlanningDO.getCrop(),trialEntryPlanningDO.getSegment(),trialEntryPlanningDO.getTrialCategory(),
//					trialEntryPlanningDO.getTrialSubCategory(),trialEntryPlanningDO.getTrialObjective(),Integer.parseInt(trialEntryPlanningDO.getYear()),trialEntryPlanningDO.getZone(),
//					trialEntryPlanningDO.getState(), trialEntryPlanningDO.getRegion(),trialEntryPlanningDO.getTerritory(),trialEntryPlanningDO.getDistrict(),trialEntryPlanningDO.getSeason());
//
//			List<String> invalidFields = new ArrayList<>();
//			
//			if (!isValidGrainType(trialEntryPlanningDO.getGrainType())) {
//				invalidFields.add("grainType");
//			}
//			if (!isValidMaterial(trialEntryPlanningDO.getMaterial())) {
//				invalidFields.add("material");
//			}
//			if (!invalidFields.isEmpty()) {
//				if (trialPlanningError == null) {
//					trialPlanningError = new TrialPlanningError();
//					trialPlanningError.setErrorFields(invalidFields);
//				} else {
//					trialPlanningError.getErrorFields().addAll(invalidFields);
//				}
//				trialPlanningError.setErrorCode(ErrorCode.INVALID_TRIAL_ENTRY_FIELDS);
//				trialPlanningError.setRecordLineNumber(trialEntryPlanningDO.getSno());
//				trialPlanningError.setErrorMessage("Please provide the correct fields !");
//			}
//
//			return trialPlanningError;
//		}
//
//		private boolean isValidSeason(String season) {
//			int id = 0;
//			try {
//				id = jdbcTemplate.queryForObject(envProps.getProperty("query.selectSeasonId"), new Object[] { season },
//						(result, rowNum) -> {
//							return new Integer(result.getInt("season_id"));
//						});
//			} catch (Exception e) {
//			}
//			if (id == 0) {
//				return false;
//			} else {
//				return true;
//			}
//
//		}
//
//		private boolean isValidDistrict(String district,String territory,String region,String state,String zone,String country) {
//			int id = 0;
//			try {
//				id = jdbcTemplate.queryForObject(envProps.getProperty("query.selectDistrictId"),
//						new Object[] { district,territory,region,
//								state, zone, country },
//						(result, rowNum) -> {
//							return new Integer(result.getInt("district_id"));
//						});
//			} catch (Exception e) {
//			}
//			if (id == 0) {
//				return false;
//			} else {
//				return true;
//			}
//
//		}
//
//		private boolean isValidTerritory(String territory,String region,String state,String zone,String country) {
//			int id = 0;
//			try {
//				id = jdbcTemplate.queryForObject(envProps.getProperty("query.selectTerritoryId"),
//						new Object[] { territory,region,
//								state, zone, country},
//						(result, rowNum) -> {
//							return new Integer(result.getInt("territory_id"));
//						});
//			} catch (Exception e) {
//			}
//			if (id == 0) {
//				return false;
//			} else {
//				return true;
//			}
//
//		}
//
//		private boolean isValidRegion(String region,String state,String zone,String country) {
//			int id = 0;
//			try {
//				id = jdbcTemplate.queryForObject(
//						envProps.getProperty("query.regionId"), new Object[] { region,
//								state, zone, country},
//						(result, rowNum) -> {
//							return new Integer(result.getInt("region_id"));
//						});
//			} catch (Exception e) {
//			}
//			if (id == 0) {
//				return false;
//			} else {
//				return true;
//			}
//
//		}
//
//		private boolean isValidState(String state,String zone,String country) {
//			int id = 0;
//			try {
//				id = jdbcTemplate.queryForObject(envProps.getProperty("query.selectStateId"), new Object[] {
//						state, zone, country },
//						(result, rowNum) -> {
//							return new Integer(result.getInt("state_id"));
//						});
//			} catch (Exception e) {
//			}
//			if (id == 0) {
//				return false;
//			} else {
//				return true;
//			}
//
//		}
//
//		private boolean isValidZone(String zone,String country) {
//			int id = 0;
//			try {
//				id = jdbcTemplate.queryForObject(envProps.getProperty("query.selectZoneId"),
//						new Object[] { zone, country }, (result, rowNum) -> {
//							return new Integer(result.getInt("zone_id"));
//						});
//			} catch (Exception e) {
//			}
//			if (id == 0) {
//				return false;
//			} else {
//				return true;
//			}
//
//		}
//
//		private boolean isValidTrialObjective(String trialObjective) {
//			int id = 0;
//			try {
//				id = jdbcTemplate.queryForObject(envProps.getProperty("query.selectTrialObjectiveId"),
//						new Object[] { trialObjective }, (result, rowNum) -> {
//							return new Integer(result.getInt("trial_objective_id"));
//						});
//			} catch (Exception e) {
//			}
//			if (id == 0) {
//				return false;
//			} else {
//				return true;
//			}
//
//		}
//
//		private boolean isValidTrialSubCategory(String trialCategory, String trialSubCategory) {
//			int id = 0;
//			try {
//				id = jdbcTemplate.queryForObject(envProps.getProperty("query.selectTrialSubCategoryId"),
//						new Object[] { trialSubCategory, trialCategory }, (result, rowNum) -> {
//							return new Integer(result.getInt("trial_sub_category_id"));
//						});
//			} catch (Exception e) {
//			}
//			if (id == 0) {
//				return false;
//			} else {
//				return true;
//			}
//
//		}
//
//		private boolean isValidTrialCategory(String trialCategory) {
//			int id = 0;
//			try {
//				id = jdbcTemplate.queryForObject(envProps.getProperty("query.selectTrialCategory"),
//						new Object[] { trialCategory }, (result, rowNum) -> {
//							return new Integer(result.getInt("trial_category_id"));
//						});
//			} catch (Exception e) {
//			}
//			if (id == 0) {
//				return false;
//			} else {
//				return true;
//			}
//
//		}
//
//		private boolean isValidSegment(String segment) {
//			int id = 0;
//			try {
//				id = jdbcTemplate.queryForObject(envProps.getProperty("query.selectCropSegmentId"),
//						new Object[] { segment }, (result, rowNum) -> {
//							return new Integer(result.getInt("crop_segment_id"));
//						});
//			} catch (Exception e) {
//			}
//			if (id == 0) {
//				return false;
//			} else {
//				return true;
//			}
//
//		}
//
//		private boolean isValidCrop(String crop,String function,String company,String country) {
//			int id = 0;
//			try {
//				id = jdbcTemplate.queryForObject(
//						envProps.getProperty("query.selectCropId"), new Object[] { crop,
//								function, company, country },
//						(result, rowNum) -> {
//							return new Integer(result.getInt("crop_id"));
//						});
//			} catch (Exception e) {
//			}
//			if (id == 0) {
//				return false;
//			} else {
//				return true;
//			}
//
//		}
//
//		private boolean isValidFunction(String function) {
//			int id = 0;
//			try {
//				id = jdbcTemplate.queryForObject(envProps.getProperty("query.selectFunctionId"), new Object[] { function },
//						(result, rowNum) -> {
//							return new Integer(result.getInt("function_id"));
//						});
//			} catch (Exception e) {
//			}
//			if (id == 0) {
//				return false;
//			} else {
//				return true;
//			}
//
//		}
//
//		private boolean isValidCompany(String company) {
//			int id = 0;
//			try {
//				id = jdbcTemplate.queryForObject(envProps.getProperty("query.selectCompanyId"), new Object[] { company },
//						(result, rowNum) -> {
//							return new Integer(result.getInt("company_id"));
//						});
//			} catch (Exception e) {
//			}
//			if (id == 0) {
//				return false;
//			} else {
//				return true;
//			}
//
//		}
//
//		private boolean isValidCountry(String country) {
//			int id = 0;
//			try {
//				id = jdbcTemplate.queryForObject(envProps.getProperty("query.selectCountryId"), new Object[] { country },
//						(result, rowNum) -> {
//							return new Integer(result.getInt("country_id"));
//						});
//			} catch (Exception e) {
//			}
//			if (id == 0) {
//				return false;
//			} else {
//				return true;
//			}
//
//		}
//
//		private boolean isValidYear(int year) {
//			int yearId = 0;
//			try {
//				yearId = jdbcTemplate.queryForObject(envProps.getProperty("query.selectYearId"), new Object[] { year },
//						(result, rowNum) -> {
//							return new Integer(result.getInt("year_id"));
//						});
//			} catch (EmptyResultDataAccessException e) {
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			if (yearId == 0) {
//				return false;
//			} else {
//				return true;
//			}
//
//		}
//		
//		private boolean isValidGrainType(String grainType) {
//			int id = 0;
//			try {
//				id = jdbcTemplate.queryForObject(envProps.getProperty("query.selectGrainTypeId"), new Object[] { grainType },
//						(result, rowNum) -> {
//							return new Integer(result.getInt("grain_type_id"));
//						});
//			} catch (Exception e) {
//			}
//			if (id == 0) {
//				return false;
//			} else {
//				return true;
//			}
//
//		}
//		
//		private boolean isValidMaterial(String material) {
//			int id = 0;
//			try {
//				id = jdbcTemplate.queryForObject(envProps.getProperty("query.selectMaterialId"), new Object[] { material },
//						(result, rowNum) -> {
//							return new Integer(result.getInt("material_id"));
//						});
//			} catch (Exception e) {
//			}
//			if (id == 0) {
//				return false;
//			} else {
//				return true;
//			}
//
//		}
//
//
//		@Override
//		public List<TrialPlanningError> deleteTrialPlans(List<TrialPlanningDO> trialPlanningList,int userId, String userRole, Integer countryId, Integer companyId, Integer functionId) {
//			List<TrialPlanningError> errors = new ArrayList<>();
//
//			for (TrialPlanningDO trialPlanningDO : trialPlanningList) {
//				try {
//					TrialPlanningError trialPlanningError=null;
//					
//					//Mandatory data validation
//					trialPlanningError=validateTrialPlanForMandatoryData(trialPlanningDO.getSno(), trialPlanningDO.getCountry(), trialPlanningDO.getCompany(),
//							trialPlanningDO.getFunction(), trialPlanningDO.getCrop(),trialPlanningDO.getSegment(),trialPlanningDO.getTrialCategory(),
//							trialPlanningDO.getTrialSubCategory(),trialPlanningDO.getTrialObjective(),trialPlanningDO.getYear(),trialPlanningDO.getZone(),
//							trialPlanningDO.getState(), trialPlanningDO.getRegion(),trialPlanningDO.getTerritory(),trialPlanningDO.getDistrict(),trialPlanningDO.getSeason());
//					if(StringUtils.isEmpty(trialPlanningDO.getNumberOfTrials()) || !NumberUtils.isNumber(trialPlanningDO.getNumberOfTrials())){
//						if (trialPlanningError != null) {
//							trialPlanningError.getErrorFields().add("numberOfTrials");
//						}
//						else{
//							trialPlanningError = new TrialPlanningError();
//							trialPlanningError.setErrorCode(ErrorCode.INVALID_TRIAL_DATA);
//							trialPlanningError.setRecordLineNumber(trialPlanningDO.getSno());
//							trialPlanningError.setErrorMessage("Field values are empty or invalid data type !");
//							List<String> invalidFields=new ArrayList<>();
//							invalidFields.add("numberOfTrials");
//							trialPlanningError.setErrorFields(invalidFields);
//						}
//					}
//					if (trialPlanningError != null) {
//						errors.add(trialPlanningError);
//						continue;
//					}
//					
//					 trialPlanningError = validateTrialPlanFields(trialPlanningDO.getSno(), trialPlanningDO.getCountry(), trialPlanningDO.getCompany(),
//							trialPlanningDO.getFunction(), trialPlanningDO.getCrop(),trialPlanningDO.getSegment(),trialPlanningDO.getTrialCategory(),
//							trialPlanningDO.getTrialSubCategory(),trialPlanningDO.getTrialObjective(),Integer.parseInt(trialPlanningDO.getYear()),trialPlanningDO.getZone(),
//							trialPlanningDO.getState(), trialPlanningDO.getRegion(),trialPlanningDO.getTerritory(),trialPlanningDO.getDistrict(),trialPlanningDO.getSeason());
//					if (trialPlanningError != null) {
//						errors.add(trialPlanningError);
//						continue;
//					}
//
//					//Rolebased validation
//					if(!isCropAndUserAssociated(userId,trialPlanningDO.getCrop(), countryId, companyId, functionId)){
//						TrialPlanningError error;
//						error = new TrialPlanningError();
//						error.setErrorCode(ErrorCode.USER_AND_CROP_NOT_ASSOCIATED);
//						error.setErrorMessage(String.format("User is not permitted to delete the trial for given crop :%s",trialPlanningDO.getCrop()));
//						error.setRecordLineNumber(trialPlanningDO.getSno());
//						errors.add(error);
//						continue;
//					}
//				//region validation for regional manager
//				if(UserRoles.REGIONAL_MANGAER.getValue().toString().equalsIgnoreCase(userRole)){
//					TrialPlanningError error;
//					error = new TrialPlanningError();
//					error.setErrorCode(ErrorCode.USER_AND_CROP_NOT_ASSOCIATED);
//					error.setErrorMessage(String.format("User is not permitted to delete the trial for given region :%s",trialPlanningDO.getRegion()));
//					error.setRecordLineNumber(trialPlanningDO.getSno());
//					errors.add(error);
//					continue;
//					
//				}
//					int trialPlanningId = getTrialPlanningId(trialPlanningDO, errors);
//					if (trialPlanningId == 0) {
//						TrialPlanningError error;
//						error = new TrialPlanningError();
//						error.setErrorCode(ErrorCode.NO_TRIAL_FOUND);
//						error.setErrorMessage(
//								"Given Trial Plan is not available to delete, correct the data and retry again !");
//						error.setRecordLineNumber(trialPlanningDO.getSno());
//						errors.add(error);
//						continue;
//					}
//
//					// check if the trial is already assigned
//					if (isTrialHaveAssociatedEntries(trialPlanningId)) {
//						TrialPlanningError error;
//						error = new TrialPlanningError();
//						error.setErrorCode(ErrorCode.TRIAL_ASSOCIATED_WITH_ENTRIES);
//						error.setErrorMessage("Please delete the associated trial entries for given trial and try again !");
//						error.setRecordLineNumber(trialPlanningDO.getSno());
//						errors.add(error);
//						continue;
//					}
//
//					deleteTreatmentPlan(trialPlanningId);
//					deleteTrialPlan(trialPlanningId);
//					
//
//				} catch (Exception e) {
//					setTechnicalError(errors, trialPlanningDO, e.getMessage());
//				}
//
//			}
//			return errors;
//		}
//
//		private boolean isTrialHaveAssociatedEntries(int trialPlanningId) {
//
//			int count = jdbcTemplate.queryForObject(envProps.getProperty("query.selectEntryInfoIdCount"),
//					new Object[] { trialPlanningId }, Integer.class);
//
//			if (count > 0) {
//				return true;
//			}
//
//			return false;
//		}
//		
//		private boolean isTrialAlreadyAssigned(int trialPlanningId) {
//
//			int count = jdbcTemplate.queryForObject(envProps.getProperty("query.selectEntryUserMappingCount"),
//					new Object[] { trialPlanningId }, Integer.class);
//
//			if (count > 0) {
//				return true;
//			}
//
//			return false;
//		}
//
//		private int updateTreatmentPlan(TrialPlanningDO trialPlanningDO, int tialPlanningId, List<TrialPlanningError> errors) {
//			int trialUpdateCount = 0;
//			try {
//				// update trial count
//				int updateCount = updatePlannedTrialCount(trialPlanningDO, tialPlanningId, errors);
//				// update treatment data
//				if (updateCount > 0) {
//					trialUpdateCount = jdbcTemplate.update(envProps.getProperty("query.updateTreatmentPlan"),
//							new Object[] { trialPlanningDO.getNumberOfReplications(),
//									trialPlanningDO.getNumberOfTreatments(), trialPlanningDO.getTreatmentIdentifier(),
//									trialPlanningDO.getTreatment1Value(), trialPlanningDO.getTreatment2Value(),
//									trialPlanningDO.getTreatment3Value(), trialPlanningDO.getTreatment4Value(),
//									trialPlanningDO.getTreatment5Value(), trialPlanningDO.getTreatment6Value(),
//									tialPlanningId });
//				}
//			} catch (Exception e) {
//				setTechnicalError(errors, trialPlanningDO, UPDATE_TREATMENT_PLAN_FAILED);
//			}
//			return trialUpdateCount;
//
//		}
//
//		private void saveTreatmentPlan(Long trialPlannindId, TrialPlanningDO trialPlanningDO,
//				List<TrialPlanningError> errors) {
//			try {
//				jdbcTemplate.update(envProps.getProperty("query.insertTreatmentPlan"),
//						new Object[] { trialPlannindId, trialPlanningDO.getNumberOfReplications(),
//								trialPlanningDO.getNumberOfTreatments(), trialPlanningDO.getTreatmentIdentifier(),
//								trialPlanningDO.getTreatment1Value(), trialPlanningDO.getTreatment2Value(),
//								trialPlanningDO.getTreatment3Value(), trialPlanningDO.getTreatment4Value(),
//								trialPlanningDO.getTreatment5Value(), trialPlanningDO.getTreatment6Value() });
//			} catch (Exception ex) {
//				setTechnicalError(errors, trialPlanningDO, SAVE_TREATMENT_PLAN_FAILED);
//				jdbcTemplate.update(envProps.getProperty("query.deleteTrialPlan"), new Object[]{ trialPlannindId });
//			}
//		}
//
//		private Long saveTrial(TrialPlanningDO trialPlanningDO, List<TrialPlanningError> errors) {
//
//			KeyHolder keyHolder = new GeneratedKeyHolder();
//			
//			try {
//				jdbcTemplate.update(new PreparedStatementCreator() {
//
//					@Override
//					public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
//						PreparedStatement ps = connection.prepareStatement(envProps.getProperty("query.insertPlannedTrial"),
//								Statement.RETURN_GENERATED_KEYS);
//						ps.setString(1, trialPlanningDO.getFunction());
//						ps.setString(2, trialPlanningDO.getCompany());
//						ps.setString(3, trialPlanningDO.getCountry());
//						ps.setInt(4, Integer.parseInt(trialPlanningDO.getYear()));
//						ps.setString(5, trialPlanningDO.getTrialSubCategory());
//						ps.setString(6, trialPlanningDO.getTrialCategory());
//						ps.setString(7, trialPlanningDO.getTrialObjective());
//						ps.setString(8, trialPlanningDO.getSeason());
//						ps.setString(9, trialPlanningDO.getCrop());
//						ps.setString(10, trialPlanningDO.getFunction());
//						ps.setString(11, trialPlanningDO.getCompany());
//						ps.setString(12, trialPlanningDO.getCountry());
//						ps.setString(13, trialPlanningDO.getSegment());
//						ps.setString(14, trialPlanningDO.getZone());
//						ps.setString(15, trialPlanningDO.getState());
//						ps.setString(16, trialPlanningDO.getRegion());
//						ps.setString(17, trialPlanningDO.getTerritory());
//						ps.setString(18, trialPlanningDO.getDistrict());
//						ps.setString(19, trialPlanningDO.getTrialCategory());
//						ps.setString(20, trialPlanningDO.getNumberOfTrials());
//						return ps;
//					}
//				}, keyHolder);
//			} catch (Exception e) {
//				setTechnicalError(errors, trialPlanningDO, SAVE_TRIAL_DATA_FAILED);
//				return 0l;
//			}
//			
//			return keyHolder.getKey().longValue();
//
//		}
//
//		private void saveEntry(TrialEntryPlanningDO trialEntryPlanningDO, int trialPlanningId) {
//
//			jdbcTemplate.update(envProps.getProperty("query.insertEntries"),
//					new Object[] { trialEntryPlanningDO.getMaterial(), trialEntryPlanningDO.getGrainType(), trialPlanningId,
//							trialEntryPlanningDO.getEntryName(), trialEntryPlanningDO.getEntryDescription(),
//							StatusConstants.PLANNED, trialEntryPlanningDO.getRangeRow(),
//							trialEntryPlanningDO.getEntryType(), trialEntryPlanningDO.getPlotId(),trialEntryPlanningDO.getRecId(),trialEntryPlanningDO.getBookId(),trialEntryPlanningDO.getRemarks(),trialEntryPlanningDO.getEntryNo(),trialEntryPlanningDO.getRepNo() });
//
//		}
//		
//		private int[] saveEntryBatch(List<TrialEntryPlanningDO> trialEntryPlanningList, int trialPlanningId) {
//			System.out.print("Breeding query.insertEntries");
//			return jdbcTemplate.batchUpdate(envProps.getProperty("query.insertEntries"),
//					new BatchPreparedStatementSetter() {
//
//						@Override
//						public void setValues(PreparedStatement ps, int i) throws SQLException {
//							TrialEntryPlanningDO data = trialEntryPlanningList.get(i);
//
//							ps.setString(1, data.getMaterial());
//							ps.setString(2, data.getGrainType());
//							ps.setInt(3, trialPlanningId);
//							ps.setString(4, data.getEntryName());
//							ps.setString(5, data.getEntryDescription());
//							ps.setString(6, StatusConstants.PLANNED);
//							ps.setString(7, data.getRangeRow());
//							ps.setString(8, data.getEntryType());
//							ps.setString(9, data.getPlotId());
//							ps.setString(10, data.getRecId());
//							ps.setString(11, data.getBookId());
//							ps.setString(12, data.getRemarks());
//							ps.setString(13, data.getEntryNo());
//							ps.setString(14, data.getRepNo());
//							
//						}
//
//						@Override
//						public int getBatchSize() {
//							return trialEntryPlanningList.size();
//						}
//
//					});
//
//			/*jdbcTemplate.update(envProps.getProperty("query.insertEntries"),
//					new Object[] { trialEntryPlanningDO.getMaterial(), trialEntryPlanningDO.getGrainType(), trialPlanningId,
//							trialEntryPlanningDO.getEntryName(), trialEntryPlanningDO.getEntryDescription(),
//							StatusConstants.PLANNED, trialEntryPlanningDO.getRangeRow(),
//							trialEntryPlanningDO.getEntryType(), trialEntryPlanningDO.getPlotId(),trialEntryPlanningDO.getRecId(),trialEntryPlanningDO.getBookId(),trialEntryPlanningDO.getRemarks(),trialEntryPlanningDO.getEntryNo(),trialEntryPlanningDO.getRepNo() });*/
//
//		}
//		
//		
//
//		private int getTrialPlanningId(TrialPlanningDO trialPlanningDO, List<TrialPlanningError> errors) {
//			int trialPlanningId = 0;
//			try {
//				trialPlanningId = jdbcTemplate.queryForObject(envProps.getProperty("query.selectTrialPlanningId"),
//						new Object[] { trialPlanningDO.getFunction(), trialPlanningDO.getCompany(),
//								trialPlanningDO.getCountry(), trialPlanningDO.getYear(),
//								trialPlanningDO.getTrialSubCategory(), trialPlanningDO.getTrialCategory(),trialPlanningDO.getTrialObjective(),
//								trialPlanningDO.getSeason(), trialPlanningDO.getCrop(), trialPlanningDO.getFunction(),
//								trialPlanningDO.getCompany(), trialPlanningDO.getCountry(), trialPlanningDO.getSegment(),
//								trialPlanningDO.getZone(), trialPlanningDO.getState(), trialPlanningDO.getRegion(),
//								trialPlanningDO.getTerritory(), trialPlanningDO.getDistrict() },
//						(result, rowNum) -> {
//							return new Integer(result.getInt("trial_planning_id"));
//						});
//			} catch (Exception e) {
//			}
//
//			return trialPlanningId;
//		}
//
//		private int getTrialPlanningId(TrialEntryPlanningDO trialEntryPlanningDO) {
//			int trialPlanningId = 0;
//			try {
//				trialPlanningId = jdbcTemplate.queryForObject(envProps.getProperty("query.selectTrialPlanningId"),
//						new Object[] { trialEntryPlanningDO.getFunction(), trialEntryPlanningDO.getCompany(),
//								trialEntryPlanningDO.getCountry(), trialEntryPlanningDO.getYear(),
//								trialEntryPlanningDO.getTrialSubCategory(), trialEntryPlanningDO.getTrialCategory(), trialEntryPlanningDO.getTrialObjective(),
//								trialEntryPlanningDO.getSeason(), trialEntryPlanningDO.getCrop(),
//								trialEntryPlanningDO.getFunction(), trialEntryPlanningDO.getCompany(),
//								trialEntryPlanningDO.getCountry(), trialEntryPlanningDO.getSegment(),
//								trialEntryPlanningDO.getZone(), trialEntryPlanningDO.getState(),
//								trialEntryPlanningDO.getRegion(), trialEntryPlanningDO.getTerritory(),
//								trialEntryPlanningDO.getDistrict() },
//						(result, rowNum) -> {
//							return new Integer(result.getInt("trial_planning_id"));
//						});
//			} catch (Exception e) {
//			}
//			return trialPlanningId;
//		}
//
//		@SuppressWarnings("unused")
//		private String getTrialStatus(int tialPlanningId) {
//			String trialStatus = null;
//			try {
//				trialStatus = jdbcTemplate.queryForObject(envProps.getProperty("query.selectTrialStatus"),
//						new Object[] { tialPlanningId }, (result, rowNum) -> {
//							return new String(result.getString("trial_status"));
//						});
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			return trialStatus;
//		}
//
//		@SuppressWarnings("unused")
//		private String getEntryStatus(int tialPlanningId) {
//			String trialStatus = null;
//			try {
//				trialStatus = jdbcTemplate.queryForObject(envProps.getProperty("query.selectTrialStatus"),
//						new Object[] { tialPlanningId }, (result, rowNum) -> {
//							return new String(result.getString("trial_status"));
//						});
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			return trialStatus;
//		}
//
//		private int updatePlannedTrialCount(TrialPlanningDO trialPlanningDO, int toBeUpdatedtrialPlanningId, List<TrialPlanningError> errors) {
//			int updateCount = 0;
//			int trialCount = Integer.parseInt(trialPlanningDO.getNumberOfTrials());
//			try {
//				updateCount = jdbcTemplate.update(envProps.getProperty("query.updatePlannedTrialCount"),
//					new Object[] { trialCount, toBeUpdatedtrialPlanningId });
//				
//			} catch(Exception ex) {
//				setTechnicalError(errors, trialPlanningDO, UPDATE_PLANNED_TRIAL_COUNT_FAILED);
//			}
//			return updateCount;
//			
//		}
//
//		private int deleteTrialPlan(int trialPlanningId) {
//			return jdbcTemplate.update(envProps.getProperty("query.deleteTrialPlan"), new Object[] { trialPlanningId });
//		}
//
//		private int deleteTreatmentPlan(int trialPlanningId) {
//			return jdbcTemplate.update(envProps.getProperty("query.deleteTreatmentPlan"), new Object[] { trialPlanningId });
//		}
//
//		@Override
//		public TrailPlanDO getTrailPlanById(Integer trailPlanningId) {
//
//			return jdbcTemplate.queryForObject(envProps.getProperty("query.getTrailPlanById"),
//					new Object[] { trailPlanningId }, (result, rowNum) -> {
//						TrailPlanDO trailPlan = constructTrailPlan(result);
//						List<EntryDO> listOfEntries = new ArrayList<>();
//						trailPlan.setListOfEntries(listOfEntries);
//						getEntries(trailPlanningId, listOfEntries);
//						boolean approve = true;
//						List<String> entryIds = new ArrayList<String>();
//						for (EntryDO entryDo : listOfEntries) {
//							entryIds.add(entryDo.getEntryInfoId().toString());
//						}
//						List<UserEntryMappingDO> userLandIds = getUserIdsLandIdsFromEntry(entryIds);
//
//						List<UserEntryMappingDO> workFlowStatuses = new ArrayList<UserEntryMappingDO>();
//						for (UserEntryMappingDO userEntryMappingDO : userLandIds) {
//							List<UserEntryMappingDO> workFlowStatusFromDB = getWorkFlowStatusFromEntry(
//									userEntryMappingDO.getUserId(), userEntryMappingDO.getLandId(), entryIds);
//							workFlowStatuses.addAll(workFlowStatusFromDB);
//						}
//						for (UserEntryMappingDO userEntryMappingDO : workFlowStatuses) {
//							if (userEntryMappingDO.getWorkFlowStatus() == null
//									|| !(userEntryMappingDO.getWorkFlowStatus().equals("submitted"))) {
//								approve = false;
//								break;
//							}
//						}
//						trailPlan.setEnableApprove(approve);
//						return trailPlan;
//					});
//		}
//
//		private void getEntries(Integer trailPlanningId, List<EntryDO> listOfEntries) {
//			jdbcTemplate.query(envProps.getProperty("query.getTrailEntries"), new Object[] { trailPlanningId },
//					(result1, rowNum1) -> {
//						EntryDO entry = new EntryDO();
//						entry.setEntryInfoId(result1.getInt("trial_entry_info_id"));
//						entry.setEntryName(result1.getString("entry_name"));
//						entry.setEntryStatus(result1.getString("entry_status"));
//						entry.setGrainType(result1.getString("grain_name"));
//						entry.setEntryType(result1.getString("entry_type"));
//						entry.setMaterial(result1.getString("material_name"));
//						entry.setPlotId(result1.getInt("plot_id"));
//						entry.setRangeRow(result1.getInt("range_row"));
//						entry.setEntryDescription(result1.getString("entry_description"));
//						entry.setRemarks(result1.getString("remarks"));
//						listOfEntries.add(entry);
//						return entry;
//					});
//		}
//
//		@Override
//		public List<TrialPlanningError> planTrialEntries(List<TrialEntryPlanningDO> trialEntryPlanningList,int userId, String userRole, Integer countryId, Integer companyId, Integer functionId) {
//
//			List<TrialPlanningError> errors = new ArrayList<>();
//
//			for (TrialEntryPlanningDO trialEntryPlanningDO : trialEntryPlanningList) {
//				try {
//					
//					TrialPlanningError trialPlanningError =null;
//					//Mandatory data validation
//					trialPlanningError=validateTrialPlanForMandatoryData(trialEntryPlanningDO.getSno(), trialEntryPlanningDO.getCountry(), trialEntryPlanningDO.getCompany(),
//							trialEntryPlanningDO.getFunction(), trialEntryPlanningDO.getCrop(),trialEntryPlanningDO.getSegment(),trialEntryPlanningDO.getTrialCategory(),
//							trialEntryPlanningDO.getTrialSubCategory(),trialEntryPlanningDO.getTrialObjective(),trialEntryPlanningDO.getYear(),trialEntryPlanningDO.getZone(),
//							trialEntryPlanningDO.getState(), trialEntryPlanningDO.getRegion(),trialEntryPlanningDO.getTerritory(),trialEntryPlanningDO.getDistrict(),trialEntryPlanningDO.getSeason());
//					
//					List<String> invalidFields=new ArrayList<>();
//					/*if(StringUtils.isEmpty(trialEntryPlanningDO.getRangeRow()) || !NumberUtils.isNumber(trialEntryPlanningDO.getRangeRow())){
//						invalidFields.add("rangeRow");
//						
//					}
//					if(StringUtils.isEmpty(trialEntryPlanningDO.getPlotId()) || !NumberUtils.isNumber(trialEntryPlanningDO.getPlotId())){
//						invalidFields.add("plotId");
//						
//					}*/
//					if(StringUtils.isEmpty(trialEntryPlanningDO.getEntryName())){
//						invalidFields.add("entryName");
//						
//					}
//					if(StringUtils.isEmpty(trialEntryPlanningDO.getEntryDescription())){
//						invalidFields.add("entryDescription");
//						
//					}
//					if(StringUtils.isEmpty(trialEntryPlanningDO.getEntryType())){
//						invalidFields.add("entryType");
//						
//					}
//					
//					if(invalidFields!=null && !invalidFields.isEmpty()){
//						if (trialPlanningError != null) {
//							trialPlanningError.getErrorFields().addAll(invalidFields);
//						}
//						else{
//							trialPlanningError = new TrialPlanningError();
//							trialPlanningError.setErrorCode(ErrorCode.INVALID_TRIAL_DATA);
//							trialPlanningError.setRecordLineNumber(trialEntryPlanningDO.getSno());
//							trialPlanningError.setErrorMessage("Field values are empty or invalid data type !");
//							trialPlanningError.setErrorFields(invalidFields);
//						}
//					}
//					if (trialPlanningError != null) {
//						errors.add(trialPlanningError);
//						continue;
//					}
//					
//					//Field validation
//					trialPlanningError= validateTrialEntryPlanFields(trialEntryPlanningDO);
//					if (trialPlanningError != null) {
//						errors.add(trialPlanningError);
//						continue;
//					}
//					
//					//Rolebased validation
//					if(!isCropAndUserAssociated(userId,trialEntryPlanningDO.getCrop(), countryId, companyId, functionId)){
//						TrialPlanningError error = new TrialPlanningError();
//						error.setErrorCode(ErrorCode.USER_AND_CROP_NOT_ASSOCIATED);
//						error.setErrorMessage(String.format("User is not permitted to create the trial entry for given crop :%s",trialEntryPlanningDO.getCrop()));
//						error.setRecordLineNumber(trialEntryPlanningDO.getSno());
//						errors.add(error);
//						continue;
//					}
//				//region validation for regional manager
//				if(UserRoles.REGIONAL_MANGAER.getValue().toString().equalsIgnoreCase(userRole)){
//					TrialPlanningError error = new TrialPlanningError();
//					error.setErrorCode(ErrorCode.USER_AND_CROP_NOT_ASSOCIATED);
//					error.setErrorMessage(String.format("User is not permitted to create the trial entry for given region :%s",trialEntryPlanningDO.getRegion()));
//					error.setRecordLineNumber(trialEntryPlanningDO.getSno());
//					errors.add(error);
//					continue;
//					
//				}
//					
//					int trialPlanningId = getTrialPlanningId(trialEntryPlanningDO);
//					if (trialPlanningId == 0) {
//						TrialPlanningError error;
//						error = new TrialPlanningError();
//						error.setErrorCode(ErrorCode.NO_TRIAL_FOUND);
//						error.setErrorMessage("Associated trial not found, entry cann't be created !");
//						error.setRecordLineNumber(trialEntryPlanningDO.getSno());
//						errors.add(error);
//						continue;
//					}
//
//					// is the trial already assigned
//					// check if the trial is already assigned
//					if (isTrialAlreadyAssigned(trialPlanningId)) {
//						TrialPlanningError error;
//						error = new TrialPlanningError();
//						error.setErrorCode(ErrorCode.TRIAL_ALREADY_ASSIGNED);
//						error.setErrorMessage("New entry can not be created for already assigned trial plan !");
//						error.setRecordLineNumber(trialEntryPlanningDO.getSno());
//						errors.add(error);
//						continue;
//					}
//
//				} catch (Exception e) {
//					TrialPlanningError error;
//					error = new TrialPlanningError();
//					error.setErrorCode(ErrorCode.TECHNICAL_ERROR);
//					error.setErrorMessage(
//							"There is some technical error with given entry data, please check the data and retry !");
//					error.setRecordLineNumber(trialEntryPlanningDO.getSno());
//					errors.add(error);
//				}
//
//			}
//			
//			if (!ObjectUtils.isEmpty(errors))
//				return errors;
//
//			if(!CollectionUtils.isEmpty(trialEntryPlanningList)) {
//				/*if(trialEntryPlanningList.get(0).getFunction().equalsIgnoreCase("Breeding")) {
//					int trialId = getTrialPlanningId(trialEntryPlanningList.get(0));
//					if (getTrialEntryPlanningId(trialEntryPlanningList.get(0), trialId) == 0) {
//						saveEntryBatch(trialEntryPlanningList, trialId);
//					}
//				}
//				else {*/
//			for (TrialEntryPlanningDO trialEntryPlanningDO : trialEntryPlanningList) {
//				int trialId = getTrialPlanningId(trialEntryPlanningDO);
//				if (getTrialEntryPlanningId(trialEntryPlanningDO, trialId) == 0) {
//					saveEntry(trialEntryPlanningDO, trialId);
//				} else {
//					TrialPlanningError error;
//					error = new TrialPlanningError();
//					error.setErrorCode(ErrorCode.ENTRY_ALREADY_EXISTED);
//					error.setErrorMessage(ErrorCode.ENTRY_ALREADY_EXISTED.getValue());
//					error.setRecordLineNumber(trialEntryPlanningDO.getSno());
//					errors.add(error);
//				}
//			}
//			/*	}*/
//			}
//
//			return errors;
//		}
//
//		@Override
//		public List<TrialPlanningError> deleteTrialEntries(List<TrialEntryPlanningDO> trialEntryPlanningList,int userId, String userRole, Integer countryId, Integer companyId, Integer functionId) {
//
//			List<TrialPlanningError> errors = new ArrayList<>();
//
//			for (TrialEntryPlanningDO trialEntryPlanningDO : trialEntryPlanningList) {
//				try {
//
//					// Field validation
//					TrialPlanningError trialPlanningError = validateTrialEntryPlanFields(trialEntryPlanningDO);
//					if (trialPlanningError != null) {
//						errors.add(trialPlanningError);
//						continue;
//					}
//
//					// Rolebased validation
//					if (!isCropAndUserAssociated(userId, trialEntryPlanningDO.getCrop(), countryId, companyId, functionId)) {
//						TrialPlanningError error = new TrialPlanningError();
//						error.setErrorCode(ErrorCode.USER_AND_CROP_NOT_ASSOCIATED);
//						error.setErrorMessage(
//								String.format("User is not permitted to delete the trial entry for given crop :%s",
//										trialEntryPlanningDO.getCrop()));
//						error.setRecordLineNumber(trialEntryPlanningDO.getSno());
//						errors.add(error);
//						continue;
//					}
//					// region validation for regional manager
//					if (UserRoles.REGIONAL_MANGAER.getValue().toString().equalsIgnoreCase(userRole)) {
//						TrialPlanningError error = new TrialPlanningError();
//						error.setErrorCode(ErrorCode.USER_AND_CROP_NOT_ASSOCIATED);
//						error.setErrorMessage(
//								String.format("User is not permitted to delete the trial entry for given region :%s",
//										trialEntryPlanningDO.getRegion()));
//						error.setRecordLineNumber(trialEntryPlanningDO.getSno());
//						errors.add(error);
//						continue;
//
//					}
//					int trialPlanningId = getTrialPlanningId(trialEntryPlanningDO);
//					if (trialPlanningId == 0) {
//						TrialPlanningError error;
//						error = new TrialPlanningError();
//						error.setErrorCode(ErrorCode.NO_TRIAL_FOUND);
//						error.setErrorMessage("Associated trial not found, entry cann't be created !");
//						error.setRecordLineNumber(trialEntryPlanningDO.getSno());
//						errors.add(error);
//						continue;
//					}
//					if (isTrialAlreadyAssigned(trialPlanningId)) {
//						TrialPlanningError error;
//						error = new TrialPlanningError();
//						error.setErrorCode(ErrorCode.TRIAL_ALREADY_ASSIGNED);
//						error.setErrorMessage("New entry can not be created for already assigned trial plan !");
//						error.setRecordLineNumber(trialEntryPlanningDO.getSno());
//						errors.add(error);
//						continue;
//					}
//					int trialEntryPlanningId = getTrialEntryPlanningId(trialEntryPlanningDO, trialPlanningId);
//
//					if (trialEntryPlanningId > 0) {
//						deleteTrialEntry(trialEntryPlanningId);
//					} else {
//						TrialPlanningError error;
//						error = new TrialPlanningError();
//						error.setErrorCode(ErrorCode.NO_TRIAL_ENTRY_FOUND);
//						error.setErrorMessage("Given Entry does not exists to delete !");
//						error.setRecordLineNumber(trialEntryPlanningDO.getSno());
//						errors.add(error);
//					}
//
//				} catch (Exception e) {
//					TrialPlanningError error;
//					error = new TrialPlanningError();
//					error.setErrorCode(ErrorCode.TECHNICAL_ERROR);
//					error.setErrorMessage(
//							"There is some technical error with given entry data, please check the data and retry !");
//					error.setRecordLineNumber(trialEntryPlanningDO.getSno());
//					errors.add(error);
//				}
//
//			}
//			return errors;
//		}
//
//		@Override
//		public TrailPlanListDO getStartedTrails(Integer yearId, Integer cropId, Integer seasonId,
//				Integer userId,Integer trailPlanningId, Integer countryId, Integer companyId, Integer functionId) {
//			List<TrailPlanDO> trailplans = new ArrayList<>();
//			TrailPlanListDO trailPlanListDO = new TrailPlanListDO();
//			
//			if (null != trailPlanningId) {
//				jdbcTemplate.query(envProps.getProperty("query.getStartedTrailsById"),
//						new Object[] { yearId, cropId, seasonId, trailPlanningId }, (result, rowNum) -> {
//							TrailPlanDO trailPlan = constructTrailPlan(result);
//								List<TrialEntryKV> list = getEntryInfo();
//								list.forEach((trialEntryId) -> {
//									if (trialEntryId.getTrialPlanningId().equals(trailPlan.getTrailPlanningId())) {
//										setFarmerInfo(trailplans, trailPlan, trialEntryId.getTrailEntryInfoId());
//									}
//								});
//							return trailPlan;
//						});
//			} else {
//				
//
//					/*if (roleName.equals(UserRoles.AGRONOMIST.getValue())) {
//						trailPlanListDO.setTotalCount(getTotalCountByRole(yearId, cropId, seasonId, userId, countryId, companyId, functionId, roleName));
//						*/
//						jdbcTemplate.query(envProps.getProperty("query.getStartedTrailsForAgronomist"),
//								new Object[] { yearId, cropId, seasonId, userId, countryId, companyId, functionId }, (result, rowNum) -> {
//									TrailPlanDO trailPlan = constructStartedTrailPlan(result);
//									/*List<TrialEntryKV> list = getEntryInfo();
//									list.forEach((trialEntryId) -> {
//										if (trialEntryId.getTrialPlanningId().equals(trailPlan.getTrailPlanningId())) {
//											setFarmerInfo(trailplans, trailPlan, trialEntryId.getTrailEntryInfoId());
//										}
//									});*/
//									
//									FarmerDO farmer = new FarmerDO();
//									farmer.setFarmerName(result.getString("farmer_name"));
//									farmer.setFatherName(result.getString("farmer_father_name"));
//									farmer.setMobile(result.getString("farmer_mobile"));
//									trailPlan.settFAName(result.getString("first_name")+" " + result.getString("last_name"));
//									trailPlan.setVillageName(result.getString("village_name"));
//									trailPlan.setLandId(result.getInt("land_id"));
//									trailPlan.setSownDate(result.getDate("sowing_date"));
//									DateFormat df = new SimpleDateFormat("MM-dd-yyyy");  
//									if (null != result.getDate("sowing_date")) {
//										trailPlan.setSowingDate(df.format(result.getDate("sowing_date")));
//									}
//									trailPlan.settFAId(result.getInt("user_id"));
//									trailPlan.setFarmer(farmer);
//									
//									trailplans.add(trailPlan);
//
//									return trailPlan;
//								});
//
//					/*}
//					if (roleName.equals(UserRoles.ZONAL_MANAGER.getValue())) {
//						
//						trailPlanListDO.setTotalCount(getTotalCountByRole(yearId, cropId, seasonId, userId, countryId, companyId, functionId, UserRoles.ZONAL_MANAGER.getValue()));
//						
//						jdbcTemplate.query(envProps.getProperty("query.getStartedTrailsForZonalManager"),
//								new Object[] { yearId, cropId, seasonId, userId, countryId, companyId, functionId,index,size }, (result, rowNum) -> {
//									TrailPlanDO trailPlan = constructTrailPlan(result);
//									List<TrialEntryKV> list = getEntryInfo();
//									list.forEach((trialEntryId) -> {
//										if (trialEntryId.getTrialPlanningId().equals(trailPlan.getTrailPlanningId())) {
//											setFarmerInfo(trailplans, trailPlan, trialEntryId.getTrailEntryInfoId());
//										}
//									});
//									FarmerDO farmer = new FarmerDO();
//									farmer.setFarmerName(result.getString("farmer_name"));
//									farmer.setFatherName(result.getString("farmer_father_name"));
//									farmer.setMobile(result.getString("farmer_mobile"));
//									trailPlan.settFAName(result.getString("first_name")+" " + result.getString("last_name"));
//									trailPlan.setVillageName(result.getString("village_name"));
//									trailPlan.setLandId(result.getInt("land_id"));
//									trailPlan.setSownDate(result.getDate("sowing_date"));
//									DateFormat df = new SimpleDateFormat("MM-dd-yyyy");  
//									if (null != result.getDate("sowing_date")) {
//										trailPlan.setSowingDate(df.format(result.getDate("sowing_date")));
//									}
//									trailPlan.settFAId(result.getInt("user_id"));
//									trailPlan.setFarmer(farmer);
//									
//									trailplans.add(trailPlan);
//									return trailPlan;
//								});
//					}
//					if (roleName.equals(UserRoles.CROP_HEAD.getValue())) {
//						
//						trailPlanListDO.setTotalCount(getTotalCountByRole(yearId, cropId, seasonId, userId, countryId, companyId, functionId, UserRoles.CROP_HEAD.getValue()));
//						
//						jdbcTemplate.query(envProps.getProperty("query.getStartedTrails"),
//								new Object[] { yearId, cropId, seasonId,userId, countryId, companyId, functionId,index,size }, (result, rowNum) -> {
//									TrailPlanDO trailPlan = constructTrailPlan(result);
//									List<TrialEntryKV> list = getEntryInfo();
//									list.forEach((trialEntryId) -> {
//										if (trialEntryId.getTrialPlanningId().equals(trailPlan.getTrailPlanningId())) {
//											setFarmerInfo(trailplans, trailPlan, trialEntryId.getTrailEntryInfoId());
//										}
//									});
//									
//									FarmerDO farmer = new FarmerDO();
//									farmer.setFarmerName(result.getString("farmer_name"));
//									farmer.setFatherName(result.getString("farmer_father_name"));
//									farmer.setMobile(result.getString("farmer_mobile"));
//									trailPlan.settFAName(result.getString("first_name")+" " + result.getString("last_name"));
//									trailPlan.setVillageName(result.getString("village_name"));
//									trailPlan.setLandId(result.getInt("land_id"));
//									trailPlan.setSownDate(result.getDate("sowing_date"));
//									DateFormat df = new SimpleDateFormat("MM-dd-yyyy");  
//									if (null != result.getDate("sowing_date")) {
//										trailPlan.setSowingDate(df.format(result.getDate("sowing_date")));
//									}
//									trailPlan.settFAId(result.getInt("user_id"));
//									trailPlan.setFarmer(farmer);
//									
//									trailplans.add(trailPlan);
//									
//									return trailPlan;
//								});
//					}*/
//				
//			}
//			trailPlanListDO.setAssignTrialList(trailplans);
//			return trailPlanListDO;
//		}
//		
//		private Integer getTotalCountByRole(Integer yearId, Integer cropId, Integer seasonId,
//				Integer userId, Integer countryId, Integer companyId, Integer functionId,String roleName){
//			try {
//				if (roleName.equals(UserRoles.AGRONOMIST.getValue()) || roleName.equals(UserRoles.ZONAL_MANAGER.getValue())) {
//				return jdbcTemplate.queryForObject(envProps.getProperty("query.getTotalCountStartedAgroZonal"),
//						new Object[] { yearId, cropId, seasonId, userId, countryId, companyId, functionId}, (result, rowNum) -> {
//							return result.getInt("total_count");
//						});
//				}
//				if (roleName.equals(UserRoles.CROP_HEAD.getValue())) {
//				return jdbcTemplate.queryForObject(envProps.getProperty("query.getTotalCountStartedCrpHd"),
//						new Object[] { yearId, cropId, seasonId, countryId, companyId, functionId}, (result, rowNum) -> {
//							return result.getInt("total_count");
//						});
//				}
//			} catch (EmptyResultDataAccessException e) {
//				return 0;
//			}
//			return 0;
//			
//		}
//		
//		
//
//		public List<TrialEntryKV> getEntryInfo() {
//			return jdbcTemplate.query(envProps.getProperty("query.getEntryInfo"), (result, rowNum) -> {
//				return new TrialEntryKV(result.getInt("trial_planning_id"), result.getInt("trial_entry_info_id"));
//			});
//		}
//
//		private void setFarmerInfo(List<TrailPlanDO> trailplans, TrailPlanDO trailPlan, Integer trailEntryInfoId) {
//			jdbcTemplate.query(envProps.getProperty("query.setFarmerInfo"), new Object[] { trailEntryInfoId },
//					(result, rowNum) -> {
//						TrailPlanDO trailPlanTemp = null;
//						try {
//							trailPlanTemp = (TrailPlanDO) trailPlan.clone();
//						} catch (CloneNotSupportedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						FarmerDO farmer = new FarmerDO();
//						farmer.setFarmerName(result.getString("farmer_name"));
//						farmer.setFatherName(result.getString("farmer_father_name"));
//						farmer.setMobile(result.getString("farmer_mobile"));
//						trailPlanTemp.settFAName(result.getString("user_name"));
//						trailPlanTemp.setVillageName(result.getString("village_name"));
//						trailPlanTemp.setLandId(result.getInt("land_id"));
//						trailPlanTemp.setSownDate(result.getDate("sowing_date"));
//						DateFormat df = new SimpleDateFormat("MM-dd-yyyy");  
//						if (null != result.getDate("sowing_date")) {
//							trailPlanTemp.setSowingDate(df.format(result.getDate("sowing_date")));
//						}
//						trailPlanTemp.settFAId(result.getInt("user_id"));
//						trailPlanTemp.setFarmer(farmer);
//						trailplans.add(trailPlanTemp);
//						return trailPlan;
//					});
//		}
//
//		private int deleteTrialEntry(int trialEntryPlanningId) {
//			return jdbcTemplate.update(envProps.getProperty("query.deleteTrialEntry"),
//					new Object[] { trialEntryPlanningId });
//		}
//
//		@Override
//		public void changeTrailStatus(Integer trailPlanningId, String status) {
//			jdbcTemplate.update(envProps.getProperty("query.changeTrialStatus"), new Object[] { status, trailPlanningId });
//		}
//
//		private int getTrialEntryPlanningId(TrialEntryPlanningDO trialEntryPlanningDO, int trialPlanningId) {
//
//			int trialEntryPlanningId = 0;
//			try {
//				trialEntryPlanningId = jdbcTemplate.queryForObject(envProps.getProperty("query.selectEntryId"),
//
//						new Object[] {
//
//								trialPlanningId, trialEntryPlanningDO.getEntryName(),
//								trialEntryPlanningDO.getEntryDescription(), trialEntryPlanningDO.getEntryType(),
//								trialEntryPlanningDO.getRangeRow(), trialEntryPlanningDO.getPlotId(),
//								trialEntryPlanningDO.getGrainType(), trialEntryPlanningDO.getMaterial()
//
//						}, (result, rowNum) -> {
//							return new Integer(result.getInt("trial_entry_info_id"));
//						});
//			} catch (EmptyResultDataAccessException e) {
//			}
//
//			return trialEntryPlanningId;
//		}
//
//		@Override
//		public List<UserEntryMappingDO> getUserIdsLandIdsFromEntry(List<String> entryIds) {
//			MapSqlParameterSource parameters = new MapSqlParameterSource();
//			parameters.addValue("ids", entryIds);
//
//			return namedParameterJdbcTemplate.query(envProps.getProperty("query.getUserIdsLandIdsFromEntry"), parameters,
//					(result, row) -> {
//						UserEntryMappingDO userEntryMappingDO = new UserEntryMappingDO();
//						userEntryMappingDO.setUserId(result.getInt("user_id"));
//						userEntryMappingDO.setLandId(result.getInt("land_id"));
//						return userEntryMappingDO;
//					});
//		}
//
//		@Override
//		public List<UserEntryMappingDO> getWorkFlowStatusFromEntry(Integer userId, Integer landId, List<String> entryIds) {
//			MapSqlParameterSource parameters = new MapSqlParameterSource();
//			parameters.addValue("user_id", userId);
//			parameters.addValue("land_id", landId);
//			parameters.addValue("ids", entryIds);
//
//			return namedParameterJdbcTemplate.query(envProps.getProperty("query.getworkFlowStatusFromEntry"), parameters,
//					(result, row) -> {
//						UserEntryMappingDO userEntryMappingDO = new UserEntryMappingDO();
//						userEntryMappingDO.setWorkFlowStatus(result.getString("work_flow_status"));
//						userEntryMappingDO.setStatus(result.getString("status"));
//						return userEntryMappingDO;
//					});
//		}
//		
//		@Override
//		public List<UserEntryMappingDO> isSeasonClosedEnable(Integer yearId, Integer cropId, Integer seasonId,
//				Integer userId, Integer countryId, Integer companyId, Integer functionId) {
//			
//			return jdbcTemplate.query(envProps.getProperty("query.getIsSeasonClosedEnable"), new Object[] { yearId, cropId, seasonId, userId, countryId, companyId, functionId  }, (result, rowNum) -> {
//						UserEntryMappingDO userEntryMappingDO = new UserEntryMappingDO();
//						userEntryMappingDO.setWorkFlowStatus(result.getString("work_flow_status") != null ? result.getString("work_flow_status") : "");
//						userEntryMappingDO.setStatus(result.getString("status") != null ? result.getString("status") : "");
//						return userEntryMappingDO;
//					});
//		}
//
//		@Override
//		public List<EntriesMasterData> getEntriesMasterDataWeb(Integer trailId, Integer landId, Integer userId) {
//			return jdbcTemplate.query(envProps.getProperty("query.getEntriesMasterDataWeb"),
//					new Object[] { trailId, landId, userId }, (result, rowNumber) -> {
//						EntriesMasterData trailEntriesMasterData = new EntriesMasterData();
//						trailEntriesMasterData.setTrailId(result.getInt("trial_planning_id"));
//						trailEntriesMasterData.setEntryId(result.getInt("trial_entry_info_id"));
//						// trailEntriesMasterData.setMaterialId(result.getInt("material_id"));
//						// trailEntriesMasterData.setMaterialName(result.getString("material_name"));
//						// trailEntriesMasterData.setGrainTypeId(result.getInt("grain_type_id"));
//						// trailEntriesMasterData.setGrainTypeName(result.getString("grain_name"));
//						trailEntriesMasterData.setEntryName(result.getString("entry_name"));
//						trailEntriesMasterData.setEntryDescription(result.getString("entry_description"));
//						trailEntriesMasterData.setEntryStatus(result.getString("entry_status"));
//						trailEntriesMasterData.setRangeRow(result.getString("range_row"));
//						// trailEntriesMasterData.setPlotId(result.getInt("plot_id"));
//						trailEntriesMasterData.setEntryType(result.getString("entry_type"));
//						trailEntriesMasterData.setLandId(result.getInt("land_id"));
//						trailEntriesMasterData.setUserId(result.getInt("user_id"));
//						trailEntriesMasterData.setEntryUserMappingId(result.getInt("entry_user_mapping_id"));
//						return trailEntriesMasterData;
//					});
//		}
//
//		@Override
//		public List<TrailStageRemarksList> getTrailStageRemarksWeb(int trailId, int userId, int landId) {
//			return jdbcTemplate.query(envProps.getProperty("query.getTrailStageRemarksDataWeb"),
//					new Object[] { trailId, userId, landId }, (result, rowNumber) -> {
//						TrailStageRemarksList trailStageRemarksList = new TrailStageRemarksList();
//						trailStageRemarksList.setTrailId(result.getInt("trial_planning_id"));
//						trailStageRemarksList.setUserId(result.getInt("user_id"));
//						trailStageRemarksList.setStageId(result.getInt("stage_id"));
//						trailStageRemarksList.setLandId(result.getInt("land_id"));
//						trailStageRemarksList.setRemarkText(result.getString("remark_text"));
//						trailStageRemarksList.setStageName(result.getString("stage_name"));
//						return trailStageRemarksList;
//					});
//		}
//
//		@Override
//		public List<EntryObservationDataDO> getTrailEntryStageObservFormListWeb(int userId, int landId, int trailId) {
//			return jdbcTemplate.query(envProps.getProperty("query.getTrailEntryLandStageFormsListWeb"),
//					new Object[] { trailId, landId, userId }, (result, rowNumber) -> {
//						EntryObservationDataDO stageFormData = new EntryObservationDataDO();
//						stageFormData.setEntryObserveDataId(result.getInt("entry_observation_data_id"));
//						stageFormData.setOdservFormStageMapId(result.getInt("observation_form_field_stage_mapping_id"));
//						stageFormData.setOdservFormFieldId(result.getInt("observation_form_field_id"));
//						stageFormData.setObservFormFieldValue(result.getString("observation_form_field_value"));
//						stageFormData.setStageId(result.getInt("stage_id"));
//						stageFormData.setObservFormDataTypeName(result.getString("form_field_data_type_name"));
//						stageFormData.setObservFormFieldName(result.getString("field_name"));
//						stageFormData.setObservFormHeaderName(result.getString("observation_header_field"));
//						stageFormData.setStageName(result.getString("stage_name"));
//						stageFormData.setEntryId(result.getInt("trial_entry_info_id"));
//						stageFormData.setLandId(result.getInt("land_id"));
//						stageFormData.setTrailId(result.getInt("trial_planning_id"));
//						stageFormData.setStatus(result.getString("status"));
//						stageFormData.setDateCreated(result.getString("date_created"));
//						stageFormData.setDateModified(result.getString("date_modified"));
//						return stageFormData;
//					});
//		}
//
//		@Override
//		public List<TrailAgronomyDataDO> getTrailAgronomyDataByUserIdWeb(Integer userId, Integer trialPlanningId,
//				Integer landId) {
//
//			return jdbcTemplate.query(envProps.getProperty("query.getTrailAgronomyDataByUserIdWeb"),
//					new Object[] { userId, trialPlanningId, landId }, (result, rowNumber) -> {
//						TrailAgronomyDataDO trailAgronomyData = new TrailAgronomyDataDO();
//						trailAgronomyData.setUserId(result.getInt("user_id"));
//						trailAgronomyData.setTrailAgronomyDataId(result.getInt("trial_agronomy_data_id"));
//						trailAgronomyData.setTrailPlanningId(result.getInt("trial_planning_id"));
//						trailAgronomyData.setLandId(result.getInt("land_id"));
//						trailAgronomyData.setAgronomyFormFieldId(result.getInt("agronomy_form_field_id"));
//						trailAgronomyData.setAgronomyFormFieldIdValue(result.getString("agronomy_form_field_id_value"));
//						trailAgronomyData.setTreatmentIndex(result.getInt("treatment_index"));
//						trailAgronomyData.setTreatmentDataCaptureIndex(result.getInt("treatment_data_capture_index"));
//						trailAgronomyData.setDateCreated(result.getString("date_created"));
//						trailAgronomyData.setDateModified(result.getString("date_modified"));
//						trailAgronomyData.setHeaderId(result.getInt("agronomy_form_field_header_id"));
//						trailAgronomyData.setAgronomyFormDataTypeName(result.getString("form_field_data_type_name"));
//						trailAgronomyData.setAgronomyFormFieldName(result.getString("field_name"));
//						trailAgronomyData.setHeaderName(result.getString("agronomy_header_field"));
//
//						return trailAgronomyData;
//					});
//		}
//
//		// update the WorkFlowstatus based on userId,landId and planningId
//		@Override
//		public int updateWorkFlowStatus(WorkFlowStatusRequestDO request) {
//			if(null != request.getStatus() && request.getStatus().equals(BayerConstants.REVIEWED)) {
//	            if(null != request.getDiscard() && request.getDiscard().equals(BayerConstants.TRUE_STRING)) {
//	            	jdbcTemplate.update(envProps.getProperty("query.updateTrialStageDataStatusToSaved"), new Object[] {
//	    					request.getUserId(), request.getLandId(), request.getTrailPlanningId() });
//	    			jdbcTemplate.update(envProps.getProperty("query.updateWorkflowstatusDiscard"), new Object[] {
//	    							request.getStatus(), request.getComments(), request.getTrailPlanningId(), request.getUserId(), request.getLandId() });
//				}else {
//				jdbcTemplate.update(envProps.getProperty("query.updateTrialStageDataStatusToCompleted"), new Object[] {
//						request.getUserId(), request.getLandId(), request.getTrailPlanningId() });
//				}
//				jdbcTemplate.update(envProps.getProperty("query.updateDateForObservData"), new Object[] {
//						 request.getTrailPlanningId(), request.getUserId(), request.getLandId()  });
//			}
//			int i = jdbcTemplate.update(envProps.getProperty("query.updateWorkflowstatus"), new Object[] {
//					request.getStatus(), request.getComments(), request.getTrailPlanningId(), request.getUserId(), request.getLandId() });
//			if(null != request.getStatus() && request.getStatus().equals(BayerConstants.REVIEWED)) {
//				jdbcTemplate.update(envProps.getProperty("query.updateWorkflowstatusDiscardEntry"), new Object[] {
//						request.getUserId(), request.getLandId(), request.getTrailPlanningId() });
//			}
//			return i;
//		}
//		
//		
//		@Override
//		public int updateDiscardWorkflowstatus(WorkFlowStatusRequestDO request) {
//			
//			return jdbcTemplate.update(envProps.getProperty("query.updateDiscardWorkflowstatus"), new Object[] {
//					request.getStatus(), request.getEntryId(), request.getUserId(), request.getLandId() });
//		}
//		
//		@Override
//		public List<TrialPlanningError> validateEntries(List<TrialEntryPlanningDO> trialEntryPlanningList) {
//			List<TrialPlanningError> errors=new ArrayList<>();
//			List<String> entryNameList=new ArrayList<>();
//	        Set<String> entryNotFoundList=new HashSet<>();
//	        for(TrialEntryPlanningDO trial:trialEntryPlanningList) {
//	        	entryNameList.add(trial.getEntryName());
//	        }
//	    	int chunkSize = 2000;
//	    	List<List<String>> lists = new ArrayList<>();
//	    	for (int i = 0; i < entryNameList.size(); i += chunkSize) {
//	    	    int end = Math.min(entryNameList.size(), i + chunkSize);
//	    	    lists.add(entryNameList.subList(i, end));
//	    	}
//	    	List<String> entryNameListDB = new ArrayList<>();
//	    	for(List<String> li : lists) {	    
//	        MapSqlParameterSource parameters = new MapSqlParameterSource();
//	        parameters.addValue("entryNameList", li);
//	         entryNameListDB=namedParameterJdbcTemplate.query(envProps.getProperty("query.validateEntries"),
//	        	     parameters, (result, rowNumber) -> {
//	 					return result.getString("entry_name").toLowerCase();
//	 				});
//	    	
//	        for(String entryName:li) {	
//	        	if(!entryNameListDB.contains(entryName.toLowerCase())) {
//	        		entryNotFoundList.add(entryName);
//	        	}
//	        }
//	        if(!entryNotFoundList.isEmpty()) {
//	        	TrialPlanningError error=new TrialPlanningError();
//	        	error.setErrorCode(ErrorCode.INVALID_ENTRIES_FOUND);
//				error.setErrorMessage("Invalid entries found in uploaded excel");
//				error.setErrorFields(new ArrayList<>(entryNotFoundList));
//				errors.add(error);
//	        }
//	    	}
//			return errors;
//		}
//
//
//		
//		
//
//	// Generic function to partition a list into sublists of size n each
//	// in Java using List.subList() (The final list might have less items)
//	public static<T> List<String> partition(List<T> list, int n)
//	{
//	    // get size of the list
//	    int size = list.size();
//	 
//	    // calculate number of partitions m of size n each
//	    int m = size / n;
//	    if (size % n != 0)
//	        m++;
//	 
//	    // create m empty lists and initialize it using List.subList()
//	    List<String> partition = new ArrayList<String>();
//	    for (int i = 0; i < m; i++)
//	    {
//	        int fromIndex = i*n;
//	        int toIndex = (i*n + n < size) ? (i*n + n) : size;
//	 
//	        partition = new ArrayList(list.subList(fromIndex, toIndex));
//	    }
//	 
//	    // return the lists
//	    return partition;
//	}
//		
//		@Override
//		public List<StartedByPlannedTrialsCountDO> getStartedByPlannedTrialsCountByUserID(Set<Integer> tfaIds, Integer yearId,
//				Integer cropId, Integer seasonId,  String sql, Integer countryId, Integer companyId, Integer functionId,Integer userId){
//			
//			MapSqlParameterSource parameters = new MapSqlParameterSource();
//			parameters.addValue("tfaids", tfaIds);
//			parameters.addValue("yearid", yearId);
//			parameters.addValue("cropid", cropId);
//			parameters.addValue("seasonid", seasonId);
//			/*parameters.addValue("locationIds", locationIds);*/
//			parameters.addValue("countryId", countryId);
//			parameters.addValue("companyId", companyId);
//			parameters.addValue("functionId", functionId);
//			parameters.addValue("userId", userId);
//			
//			return namedParameterJdbcTemplate.query(sql, parameters,
//					(result, row) -> {
//						StartedByPlannedTrialsCountDO trailDO = new StartedByPlannedTrialsCountDO();
//						trailDO.setLandId(result.getInt("land_id"));
//						trailDO.setPlannedTrailCount(result.getInt("planned_trials_count"));
//						trailDO.setTrailSubCategoryName(result.getString("trial_sub_category_name"));
//						trailDO.setTrailPlanningId(result.getInt("trial_planning_id"));
//						return trailDO;
//					});
//			
//		}
//		
//		@Override
//		public List<Integer> getTrialStageIds(int trialId, int userId,int landId) {
//			return jdbcTemplate.query(envProps.getProperty("query.getTrialStageIds"),
//					new Object[] { trialId, userId,landId }, (result, rowNum) -> {
//						return result.getInt("stage_id");
//					});
//		}
//		
//		private List<DistrictDO> getDistrictsByUserId(int userId) {
//			return jdbcTemplate.query(envProps.getProperty("query.getDistrictsByUserId"),
//					new Object[] { userId }, (result, rowNum) -> {
//						return new DistrictDO(result.getInt("district_id"), result.getString("district_name"));
//					});
//		}
//
//		
//	}
//
//}
