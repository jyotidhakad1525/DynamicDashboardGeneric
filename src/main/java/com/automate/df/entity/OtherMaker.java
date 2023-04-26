package com.automate.df.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "dms_othermaker")
public class OtherMaker  implements Serializable {
    private static final long serialVersionUID = 4895908114629386216L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;	
    @Column(name = "other_maker")
	private String otherMaker;
    @Column(name = "vehicle_segment")
	private String vehicleSegment;
	private String status;
	String orgId;
	private String bulkUploadId;
	private String createdBy;
	private String updatedBy;
	private String createdAt;
	private String updatedAt;
	@OneToMany(targetEntity = OtherModel.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "othermaker_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private Set<OtherModel> othermodels;

}
