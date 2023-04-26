package com.automate.df.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(Include.NON_NULL)
public class BaseResponse {
    private String confirmationId;
    private String status;
    private String statusDescription;
    private String statusCode;
    private int count;
    private int totalCount;

}
