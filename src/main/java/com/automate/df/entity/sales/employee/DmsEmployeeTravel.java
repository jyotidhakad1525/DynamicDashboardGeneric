package com.automate.df.entity.sales.employee;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name = "dms_employee_travel")
@NamedQuery(name = "DmsEmployeeTravel.findAll", query = "SELECT d FROM DmsEmployeeTravel d")
public class DmsEmployeeTravel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dms_employee_travel_id")
    private int dmsEmployeeTravelId;

    @Column(name = "created_by")
    private String createdBy;

    @Temporal(TemporalType.DATE)
    @Column(name = "created_time")
    private Date createdTime;

    @Temporal(TemporalType.DATE)
    @Column(name = "expiry_date")
    private Date expiryDate;

    @Column(name = "issued_at")
    private String issuedAt;

    @Temporal(TemporalType.DATE)
    @Column(name = "issued_date")
    private Date issuedDate;

    @Column(name = "passport_no")
    private String passportNo;

    @Column(name = "pp_remarks")
    private String ppRemarks;

    private String profession;

    @Column(name = "updated_by")
    private String updatedBy;

    @Temporal(TemporalType.DATE)
    @Column(name = "updated_time")
    private Date updatedTime;

    @Temporal(TemporalType.DATE)
    @Column(name = "visa_expiry_date")
    private Date visaExpiryDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "visa_issued_date")
    private Date visaIssuedDate;

    @Column(name = "visa_no")
    private String visaNo;

    @Column(name = "visa_type")
    private String visaType;

    //bi-directional many-to-one association to DmsEmployee
    @OneToMany(mappedBy = "dmsEmployeeTravel")
    private List<com.automate.df.entity.sales.employee.DMSEmployee> DMSEmployees;

    public com.automate.df.entity.sales.employee.DMSEmployee addDmsEmployee(com.automate.df.entity.sales.employee.DMSEmployee dmsEmployee) {
        getDMSEmployees().add(dmsEmployee);
        dmsEmployee.setDmsEmployeeTravel(this);

        return dmsEmployee;
    }

    public com.automate.df.entity.sales.employee.DMSEmployee removeDmsEmployee(com.automate.df.entity.sales.employee.DMSEmployee dmsEmployee) {
        getDMSEmployees().remove(dmsEmployee);
        dmsEmployee.setDmsEmployeeTravel(null);

        return dmsEmployee;
    }

}