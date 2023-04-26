package com.automate.df.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

/*@Table(name="dms_invoice")
@Entity*/
@Data
@NoArgsConstructor
public class DmsInvoice_1 {

	//@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	String id;
	String lead_id;
	String booking_id;
	String invoice_type;
	String invoice_date;
	String financer_name;
	String branch;
	String corporate_code;
	String corporate_name;
	String state_type;
	String gst;
	String gst_rate;
	String cess_percentage;
	String total_tax;
	String engine_cc;
	String basic_price;
	String cess_amount;
	String total_amount;
	String created_datetime;
	;

}
