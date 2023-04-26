package com.automate.df.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Param{
	public String fieldName;
    public int id;
    public Object value;
    public List<Child1L2> child1_l2;
    public List<Child2L2> child2_l2;
    public List<Child3L2> child3_l2;
  /*  public List<Child4L2> child4_l2;
    public List<Child5L2> child5_l2;*/
}

