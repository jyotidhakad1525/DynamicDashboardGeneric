package com.automate.df.model.oh;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmployeeDTORoot {
    public DmsEntityEmp dmsEntity;
    public String errorMessage;
    public boolean success;
    public boolean error;
}
