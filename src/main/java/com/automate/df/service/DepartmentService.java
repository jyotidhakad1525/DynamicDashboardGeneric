package com.automate.df.service;

import com.automate.df.entity.DepartmentBulkUploadReq;
import com.automate.df.entity.DepartmentRequest;
import com.automate.df.entity.DepartmentResponse;
import com.automate.df.exception.DynamicFormsServiceException;
import com.automate.df.model.BaseResponse;

import java.util.List;

public interface DepartmentService {

    public BaseResponse saveDepartment(DepartmentRequest departmentRequest) throws DynamicFormsServiceException;

    public BaseResponse updateDepartment(DepartmentRequest departmentRequest) throws DynamicFormsServiceException;

    public DepartmentResponse fetchAllDepartment(DepartmentRequest departmentRequest);

    public List<String> allDepartment(DepartmentRequest departmentRequest);

    public BaseResponse addSingleDepartment(DepartmentBulkUploadReq departmentRequest) throws DynamicFormsServiceException;


}
