package com.automate.df.model.oh;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class LevelMapping {

	Integer empId;
	String levelCode;
	List<Integer> nodesIds;
}
