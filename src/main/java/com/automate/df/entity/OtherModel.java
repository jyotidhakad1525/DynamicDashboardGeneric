package com.automate.df.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "dms_othermodel")
public class OtherModel  implements Serializable {
    private static final long serialVersionUID = 4895908114629386216L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;	
    @Column(name = "other_maker")
	private String otherMaker;
    @Column(name = "other_model")
	private String otherModel;
    @Column(name = "othermaker_id")
	private String OthermakerId;
	private String status;
	private String orgId;
	private String bulkUploadId;
	private String createdBy;
	private String updatedBy;
	private String createdAt;
	private String updatedAt;

}
