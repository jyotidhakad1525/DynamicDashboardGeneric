package com.automate.df.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

/*@Table(name="dms_delivery")
@Entity*/
@Data
@NoArgsConstructor
public class DmsDelivery_1 {

	/*
	 * @Id
	 * 
	 * @GeneratedValue(strategy = GenerationType.IDENTITY)
	 */	String id;
	String lead_id;
	String booking_id;
	String invoice_id;
	String challan_no;
	String challan_date;
	String hypothicated_to;
	String insurance_company;
	String insurance_policy_no;
	String insurance_date;
	String insurence_exp_date;
	String rc_no;
	String etd_warranty_no;
	String fasttag_no;
	String original_key;
	String duplicate_key;
	String user_manual;
	String service_booklet;
	String tool_kit;
	String jack;
	String jack_rod;
	String spare_wheel;
	String insurence_copy;
	String tr_copy;
	String created_datetime;


}
