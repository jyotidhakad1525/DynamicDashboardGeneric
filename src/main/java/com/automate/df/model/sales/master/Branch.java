package com.automate.df.model.sales.master;

import com.automate.df.model.sales.lead.DmsAddressDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Branch {

    private int branchId;

    private String branchType;

    private String email;

    private int mobile;

    private String name;

    private int phone;

    private String status;

    private String website;

    private DmsAddressDto dmsAddressDto;
}
