package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DmsAddress {
	
	public String addressType;
    public String houseNo;
    public String street;
    public String city;
    public String district;
    public String pincode;
    public String state;
    public String village;
    public String county;
    public boolean rural;
    public boolean urban;
    public int id;

}
