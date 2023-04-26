package com.automate.df.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class Rootparam {
	public String fieldName;
	public int id;
	public String value;
	public List<Child1L1> child1_l1;
	public List<Child2L1> child2_l1;
	public List<Child3L1> child3_l1;
}
