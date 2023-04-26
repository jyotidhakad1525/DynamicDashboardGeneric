package com.automate.df.controller;


import com.automate.df.dao.DepartmentRepository;
import com.automate.df.entity.DepartmentBulkUploadReq;
import com.automate.df.entity.DepartmentRequest;
import com.automate.df.entity.sales.employee.DmsDepartment;
import com.automate.df.exception.DynamicFormsServiceException;
import com.automate.df.service.DepartmentService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
@RequestMapping(value = "/department")
@Api(value = "Dynamic Forms", tags = "Dynamic Forms", description = "Dynamic Forms")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    DepartmentRepository departmentRepository;

    @PostMapping("/add")
    public ResponseEntity<?> addNewDepartment(@RequestBody DepartmentRequest departmentRequest) throws DynamicFormsServiceException {
        return new ResponseEntity<>(departmentService.saveDepartment(departmentRequest), HttpStatus.OK);
    }

    @PostMapping("/add/single")
    public ResponseEntity<?> addNewSingleDepartment(@RequestBody DepartmentBulkUploadReq departmentRequest) throws DynamicFormsServiceException {
        return new ResponseEntity<>(departmentService.addSingleDepartment(departmentRequest), HttpStatus.OK);
    }

    @PostMapping("/fetch-department")
    public ResponseEntity<?> fetchAllDepartment(@RequestBody DepartmentRequest departmentRequest) {
        return new ResponseEntity<>(departmentService.fetchAllDepartment(departmentRequest), HttpStatus.OK);
    }

    @PostMapping("/all")
    public ResponseEntity<?> AllDepartment(@RequestBody DepartmentRequest departmentRequest) {
        return new ResponseEntity<>(departmentService.allDepartment(departmentRequest), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateDepartment(@RequestBody DepartmentRequest departmentRequest) throws DynamicFormsServiceException {
        return new ResponseEntity<>(departmentService.updateDepartment(departmentRequest), HttpStatus.OK);
    }


    @GetMapping("/addlocations")
    public ResponseEntity<?> addLocation() {

        List<DmsDepartment> dmsDepartment = departmentRepository.findAll();
        for (int i = 0; i < dmsDepartment.size(); i++) {
            dmsDepartment.get(i).setLocation(departmentRepository.locationName(dmsDepartment.get(i).getDmsBranch().getBranchId()));
            departmentRepository.save(dmsDepartment.get(i));
        }
        return new ResponseEntity<>("Done", HttpStatus.OK);
    }
}