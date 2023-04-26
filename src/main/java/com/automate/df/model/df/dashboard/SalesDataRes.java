package com.automate.df.model.df.dashboard;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SalesDataRes {
	
	String todaySales;
	String todayVisitors;
	Long totalEarnings;
	String pendingOrders;
	Long totalRevenue;
	Long dropRevenue;
	Long liveBookings;
	Long complaints;
	Long deliveries;
}
