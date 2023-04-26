package com.automate.df.model.df.dashboard;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TodaysRes {

	int sNo;
	String empName;
	Long call;
	Long td;
	Long v;
	Long pb;
	Long d;
	Long pending;
}
