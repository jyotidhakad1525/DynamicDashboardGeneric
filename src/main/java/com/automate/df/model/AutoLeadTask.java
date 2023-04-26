package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AutoLeadTask {

    public DmsEntity dmsEntity;
    public String errorMessage;
    public boolean success;
    public boolean error;
}
