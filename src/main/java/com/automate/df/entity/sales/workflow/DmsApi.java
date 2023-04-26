package com.automate.df.entity.sales.workflow;

import com.automate.df.entity.oh.DmsBranch;
import com.automate.df.entity.sales.DmsOrganization;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


@Setter
@Getter
@Entity
@Table(name = "dms_api")
@NamedQuery(name = "DmsApi.findAll", query = "SELECT d FROM DmsApi d")
public class DmsApi implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "api_id")
    private int apiId;

    @Column(name = "api_headers")
    private String apiHeaders;

    @Column(name = "api_method")
    private String apiMethod;

    @Column(name = "api_request_body")
    private String apiRequestBody;

    @Column(name = "api_url")
    private String apiUrl;

    //bi-directional many-to-one association to DmsOrganization
    @ManyToOne
    @JoinColumn(name = "org_id")
    private DmsOrganization dmsOrganization;

    //bi-directional many-to-one association to DmsBranch
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private DmsBranch dmsBranch;

}