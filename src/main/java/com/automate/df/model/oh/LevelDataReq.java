package com.automate.df.model.oh;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LevelDataReq {
	
	Integer orgId;
	Integer empId;
	List<LevelDropDownData> data;
	Integer parentId;
	boolean isRootLevel;

}
