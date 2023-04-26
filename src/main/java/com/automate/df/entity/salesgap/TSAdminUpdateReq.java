package com.automate.df.entity.salesgap;

import java.util.List;

import com.automate.df.model.salesgap.Target;
import com.automate.df.model.salesgap.TargetSettingReq;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class TSAdminUpdateReq {

	int id;
	String orgId;
	String branch;
	String location;
	String department;
	String designation;
	String experience;
	String salrayRange;
	
	List<Target> targets;
}
