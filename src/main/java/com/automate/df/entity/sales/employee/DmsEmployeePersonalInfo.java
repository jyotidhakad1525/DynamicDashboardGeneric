package com.automate.df.entity.sales.employee;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name = "dms_employee_personal_info")
@NamedQuery(name = "DmsEmployeePersonalInfo.findAll", query = "SELECT d FROM DmsEmployeePersonalInfo d")
public class DmsEmployeePersonalInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dms_employee_personal_info_id")
    private int dmsEmployeePersonalInfoId;

    @Column(name = "academic_qualifications")
    private String academicQualifications;

    @Column(name = "blood_group")
    private String bloodGroup;

    private String caste;

    @Column(name = "exp_in_years1")
    private String expInYears1;

    @Column(name = "exp_in_years2")
    private String expInYears2;

    @Column(name = "father_name")
    private String fatherName;

    @Column(name = "is_physically_challenged")
    private Boolean isPhysicallyChallenged;

    @Column(name = "is_permanent_rented_house")
    private String isPermanentRentedHouse;

    @Column(name = "is_present_rented_house")
    private String isPresentRentedHouse;

    @Column(name = "permanent_address")
    private int permanentAddress;

    @Column(name = "present_address")
    private int presentAddress;

    @Column(name = "sub_caste")
    private String subCaste;

    @Column(name = "technical_qualifications")
    private String technicalQualifications;

    //bi-directional many-to-one association to DmsEmployee
    @OneToMany(mappedBy = "dmsEmployeePersonalInfo")
    private List<DMSEmployee> DMSEmployees;

    public DMSEmployee addDmsEmployee(DMSEmployee dmsEmployee) {
        getDMSEmployees().add(dmsEmployee);
        dmsEmployee.setDmsEmployeePersonalInfo(this);

        return dmsEmployee;
    }

    public DMSEmployee removeDmsEmployee(DMSEmployee dmsEmployee) {
        getDMSEmployees().remove(dmsEmployee);
        dmsEmployee.setDmsEmployeePersonalInfo(null);

        return dmsEmployee;
    }

}