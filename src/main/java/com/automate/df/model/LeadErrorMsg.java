package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class LeadErrorMsg {
    public String message;
    public String httpStatus;
    public Integer statusCode;
}
