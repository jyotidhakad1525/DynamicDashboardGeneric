package com.automate.df.entity.sales.lead;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Setter
@Getter
@Entity
@Table(name = "dms_helpdesk")
@NamedQuery(name = "DmsHelpDesk.findAll", query = "SELECT d FROM DmsHelpDesk d")
public class DmsHelpDesk implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "branch_id")
    private int branchId;
    
    @Column(name = "org_id")
    private int orgId;
    
    @Column(name = "emp_id")
    private int empId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "description")
    private String description;
    
    @Column(name = "status")
    private String status;


    @Column(name = "path")
    private String documentPath;


    @Column(name = "file_name")
    private String fileName;
    
    @Column(name = "category")
    private String category;
    
    @Column(name = "stage")
    private String stage;
    @Column(name = "universalid")
    private String universalid;

    

}