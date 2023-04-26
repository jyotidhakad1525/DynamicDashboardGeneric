package com.automate.df.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Setter
@Getter
public class DmsAttachmentDto {

    private int id;
    private String branchId;
    private int contentSize;
    private Date createdBy;
    private String description;
    private String documentNumber;
    private String documentPath;
    private String documentType;
    private int documentVersion;
    private String fileName;
    private String gstNumber;
    private Boolean isActive;
    private Boolean isPrivate;
    private String keyName;
    private String modifiedBy;
    private String orgId;
    private String ownerId;
    private String ownerName;
    private String parentId;
    private String tinNumber;

}
