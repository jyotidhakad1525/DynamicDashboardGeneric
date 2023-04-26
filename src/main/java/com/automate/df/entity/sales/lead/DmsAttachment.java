package com.automate.df.entity.sales.lead;

import com.automate.df.entity.dashboard.DmsLead;
import com.automate.df.entity.oh.DmsBranch;
import com.automate.df.entity.sales.employee.DMSEmployee;
import com.automate.df.entity.sales.DmsOrganization;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Setter
@Getter
@Entity
@Table(name = "dms_attachment")
@NamedQuery(name = "DmsAttachment.findAll", query = "SELECT d FROM DmsAttachment d")
public class DmsAttachment implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "content_size")
    private int contentSize;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_by")
    private Date createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;

    private String description;

    @Column(name = "document_number")
    private String documentNumber;

    @Column(name = "document_path")
    private String documentPath;

    @Column(name = "document_type")
    private String documentType;

    @Column(name = "document_version")
    private int documentVersion;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "gst_number")
    private String gstNumber;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_private")
    private Boolean isPrivate;

    @Column(name = "key_name")
    private String keyName;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "parent_id")
    private String parentId;

    @Column(name = "tin_number")
    private String tinNumber;

    //bi-directional many-to-one association to DmsLead
    @ManyToOne
    @JoinColumn(name = "dms_lead_id")
    private DmsLead dmsLead;

    //bi-directional many-to-one association to DmsAccount
    @ManyToOne
    @JoinColumn(name = "dms_account_id")
    private com.automate.df.entity.sales.lead.DmsAccount dmsAccount;

    //bi-directional many-to-one association to DmsContact
    @ManyToOne
    @JoinColumn(name = "dms_contact_id")
    private com.automate.df.entity.sales.lead.DmsContact dmsContact;

    //bi-directional many-to-one association to DmsOrganization
    @ManyToOne
    @JoinColumn(name = "org_id")
    private DmsOrganization dmsOrganization;

    //bi-directional many-to-one association to DmsBranch
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private DmsBranch dmsBranch;

    //bi-directional many-to-one association to DmsEmployee
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private DMSEmployee dmsEmployee;

}