package com.automate.df.service;

import com.automate.df.dao.DepartmentRepository;
import com.automate.df.dao.DmsOrganizationDao;
import com.automate.df.dao.oh.DmsBranchDao;
import com.automate.df.entity.DepartmentBulkUploadReq;
import com.automate.df.entity.DepartmentData;
import com.automate.df.entity.DepartmentRequest;
import com.automate.df.entity.DepartmentResponse;
import com.automate.df.entity.oh.DmsBranch;
import com.automate.df.entity.sales.DmsOrganization;
import com.automate.df.entity.sales.employee.DmsDepartment;
import com.automate.df.exception.DynamicFormsServiceException;
import com.automate.df.model.BaseResponse;
import com.automate.df.util.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DmsBranchDao dmsBranchDao;

    @Autowired
    private DmsOrganizationDao dmsOrganizationDao;

    @Transactional
    public BaseResponse saveDepartment(DepartmentRequest departmentRequest) throws DynamicFormsServiceException {

        List<DmsDepartment> dmsDepartmentList = departmentRepository.findAllByOrg(departmentRequest.getDepartmentName(),departmentRequest.getOrgId());
        if(dmsDepartmentList!=null && !dmsDepartmentList.isEmpty()){
            throw new DynamicFormsServiceException("Duplicate branch name not allowed",
                    HttpStatus.BAD_REQUEST);
        }

        if (departmentRequest.getDepartmentSubData() == null || departmentRequest.getDepartmentSubData().isEmpty()) {
            throw new DynamicFormsServiceException("Minimum one branch selection required",
                    HttpStatus.BAD_REQUEST);
        }

        for (int i = 0; i <departmentRequest.getDepartmentSubData().size() ; i++) {
            Optional<DmsBranch> dmsBranch = dmsBranchDao.findById(departmentRequest.getDepartmentSubData().get(i).getBranchId());
            Optional<DmsOrganization> dmsOrganization = dmsOrganizationDao.findById(departmentRequest.getOrgId());
            if(dmsBranch.isEmpty()){
                throw new DynamicFormsServiceException("Something went wrong in branch selection",
                        HttpStatus.BAD_REQUEST);
            }else if(dmsOrganization.isEmpty()){
                throw new DynamicFormsServiceException("Something went wrong in org",
                        HttpStatus.BAD_REQUEST);
            }

            DmsDepartment dmsDepartment = new DmsDepartment();
            dmsDepartment.setHrmsDepartmentId(departmentRequest.getDepartmentName());
            dmsDepartment.setIsActive(true);

            try{
                dmsDepartment.setLocation(departmentRepository.locationName(dmsBranch.get().getBranchId()));
            }catch (Exception e){
                throw new DynamicFormsServiceException("Location not found for selected branch : " +  dmsBranch.get().getBranchId(),
                        HttpStatus.BAD_REQUEST);
            }

            dmsBranch.ifPresent(dmsDepartment::setDmsBranch);
            dmsOrganization.ifPresent(dmsDepartment::setDmsOrganization);
            dmsDepartment.setDepartmentName(departmentRequest.getDepartmentName());
            departmentRepository.save(dmsDepartment);
        }

        BaseResponse baseResponse = new BaseResponse();
        ErrorMessages success = ErrorMessages.SUCCESS;
        baseResponse.setStatusCode(success.code());
        baseResponse.setStatusDescription("Record Created Successfully");
        baseResponse.setStatus("SUCCESS");
        return baseResponse;
    }

    @Transactional
    public DepartmentResponse fetchAllDepartment(DepartmentRequest departmentRequest) {
        List<String> departmentName = new ArrayList<>();
        if(departmentRequest.getDepartmentName()!=null && !departmentRequest.getDepartmentName().equalsIgnoreCase("")){
            List<DmsDepartment> dmsDepartmentList = departmentRepository.findAllByOrg(departmentRequest.getDepartmentName(),departmentRequest.getOrgId());
            if(dmsDepartmentList!=null && !dmsDepartmentList.isEmpty()){
                departmentName.add(departmentRequest.getDepartmentName());
            }else{
                return new DepartmentResponse();
            }
        }else{
            departmentName = departmentRepository.departmentNameByOrg(departmentRequest.getOrgId());
        }

        if(departmentName.isEmpty()){
            return new DepartmentResponse();
        }


        DepartmentResponse departmentResponse = new DepartmentResponse();
        List<DepartmentData> departmentDataList = new ArrayList<>();
        for (int i = 0; i <departmentName.size() ; i++) {
            List<DmsDepartment> dmsDepartmentList = departmentRepository.findAllByOrg(departmentName.get(i),departmentRequest.getOrgId());
            DepartmentData departmentData = new DepartmentData();
            List<DepartmentData.DepartmentSubData> departmentSubDataList = new ArrayList<>();
            departmentData.setDepartmentName(departmentName.get(i));

            Set<String> locationList = new HashSet<>();
            Set<String> branchList = new HashSet<>();
            for (int j = 0; j <dmsDepartmentList.size() ; j++) {
                DepartmentData.DepartmentSubData departmentSubData = new DepartmentData.DepartmentSubData();
                departmentSubData.setId(dmsDepartmentList.get(j).getDmsDepartmentId());
                departmentSubData.setStatus(dmsDepartmentList.get(j).getIsActive());
                departmentSubData.setBranchId(dmsDepartmentList.get(j).getDmsBranch().getBranchId());
                departmentSubData.setBranchName(dmsDepartmentList.get(j).getDmsBranch().getName());
                departmentSubData.setLocation(dmsDepartmentList.get(j).getLocation());
                departmentSubDataList.add(departmentSubData);
                locationList.add(dmsDepartmentList.get(j).getLocation());
                branchList.add(dmsDepartmentList.get(j).getDmsBranch().getName());
            }
            departmentData.setDepartmentSubData(departmentSubDataList);
            departmentData.setBranchList(branchList);
            departmentData.setLocationList(locationList);
            departmentDataList.add(departmentData);
        }
        departmentResponse.setDepartmentData(departmentDataList);
        return departmentResponse;
    }

    @Transactional
    public BaseResponse updateDepartment(DepartmentRequest departmentRequest) throws DynamicFormsServiceException {


        if (departmentRequest.getDepartmentSubData() == null || departmentRequest.getDepartmentSubData().isEmpty()) {
            throw new DynamicFormsServiceException("Minimum one branch selection required",
                    HttpStatus.BAD_REQUEST);
        }


        List<DmsDepartment> dmsDepartmentList = departmentRepository.findAllByOrg(departmentRequest.getDepartmentName(),departmentRequest.getOrgId());

        for (int i = 0; i <dmsDepartmentList.size() ; i++) {
            DmsDepartment dmsDepartment = dmsDepartmentList.get(i);
            dmsDepartment.setIsActive(false);
            departmentRepository.save(dmsDepartment);
        }



        for (int i = 0; i <departmentRequest.getDepartmentSubData().size() ; i++) {
            List<DmsDepartment> recordExists = departmentRepository.findAllByBranchAndOrg(departmentRequest.getDepartmentName(),departmentRequest.getOrgId(),departmentRequest.getDepartmentSubData().get(i).getBranchId());

            if(recordExists!=null && !recordExists.isEmpty()){
                for (int j = 0; j <recordExists.size() ; j++) {
                    DmsDepartment dmsDepartment = recordExists.get(i);
                    dmsDepartment.setIsActive(true);
                    departmentRepository.save(dmsDepartment);
                }
            }else{
                Optional<DmsBranch> dmsBranch = dmsBranchDao.findById(departmentRequest.getDepartmentSubData().get(i).getBranchId());
                Optional<DmsOrganization> dmsOrganization = dmsOrganizationDao.findById(departmentRequest.getOrgId());
                if(dmsBranch.isEmpty()){
                    throw new DynamicFormsServiceException("Something went wrong in branch selection",
                            HttpStatus.BAD_REQUEST);
                }else if(dmsOrganization.isEmpty()){
                    throw new DynamicFormsServiceException("Something went wrong in org",
                            HttpStatus.BAD_REQUEST);
                }

                DmsDepartment dmsDepartment = new DmsDepartment();
                dmsDepartment.setHrmsDepartmentId(departmentRequest.getDepartmentName());
                dmsDepartment.setIsActive(true);

                try{
                    dmsDepartment.setLocation(departmentRepository.locationName(dmsBranch.get().getBranchId()));
                }catch (Exception e){
                    throw new DynamicFormsServiceException("Location not found for selected branch : " +  dmsBranch.get().getBranchId(),
                            HttpStatus.BAD_REQUEST);
                }

                dmsBranch.ifPresent(dmsDepartment::setDmsBranch);
                dmsOrganization.ifPresent(dmsDepartment::setDmsOrganization);
                dmsDepartment.setDepartmentName(departmentRequest.getDepartmentName());
                departmentRepository.save(dmsDepartment);
            }
        }

        BaseResponse baseResponse = new BaseResponse();
        ErrorMessages success = ErrorMessages.SUCCESS;
        baseResponse.setStatusCode(success.code());
        baseResponse.setStatusDescription("Record Updated Successfully");
        baseResponse.setStatus("SUCCESS");
        return baseResponse;
    }

    @Transactional
    public List<String> allDepartment(DepartmentRequest departmentRequest) {
        return departmentRepository.departmentNameByOrg(departmentRequest.getOrgId());
    }


    @Transactional
    public BaseResponse addSingleDepartment(DepartmentBulkUploadReq departmentRequest) throws DynamicFormsServiceException {
        Optional<DmsBranch> dmsBranch = dmsBranchDao.getBranchByDealerCode(departmentRequest.getBranchName(),departmentRequest.getOrgId());
        if (dmsBranch.isEmpty()) {
            throw new DynamicFormsServiceException("Something went wrong in branch selection",
                    HttpStatus.BAD_REQUEST);
        }

        List<DmsDepartment> dmsDepartmentList = departmentRepository.findAllByBranchAndOrg(departmentRequest.getDepartmentName(), departmentRequest.getOrgId(),dmsBranch.get().getBranchId());


        if (dmsDepartmentList != null && !dmsDepartmentList.isEmpty()) {
            throw new DynamicFormsServiceException("Record already exists",
                    HttpStatus.BAD_REQUEST);
        }

        Optional<DmsOrganization> dmsOrganization = dmsOrganizationDao.findById(departmentRequest.getOrgId());
        DmsDepartment dmsDepartment = new DmsDepartment();
        dmsDepartment.setHrmsDepartmentId(departmentRequest.getDepartmentName());
        dmsDepartment.setIsActive(departmentRequest.getStatus());

        try {
            dmsDepartment.setLocation(departmentRepository.locationName(dmsBranch.get().getBranchId()));
        } catch (Exception e) {
            throw new DynamicFormsServiceException("Location not found for selected branch : " + dmsBranch.get().getBranchId(),
                    HttpStatus.BAD_REQUEST);
        }

        dmsBranch.ifPresent(dmsDepartment::setDmsBranch);
        dmsOrganization.ifPresent(dmsDepartment::setDmsOrganization);
        dmsDepartment.setDepartmentName(departmentRequest.getDepartmentName());
        departmentRepository.save(dmsDepartment);

        BaseResponse baseResponse = new BaseResponse();
        ErrorMessages success = ErrorMessages.SUCCESS;
        baseResponse.setStatusCode(success.code());
        baseResponse.setStatusDescription("Record Created Successfully");
        baseResponse.setStatus("SUCCESS");
        return baseResponse;
    }
}
