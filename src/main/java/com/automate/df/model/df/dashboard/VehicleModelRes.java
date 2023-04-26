package com.automate.df.model.df.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleModelRes {
	
	String model;
	Long E;
	Long T;
	Long V;
	Long B;
	Long R;
	Long L;
	String bookingPercentage;


}
