package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EnqSource {
	   public int id;
	    public String name;
	    public String value;
	    public String description;
	    public boolean active;
}
