package com.automate.df.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ResponseJson {

    private String status;

    private int statusCode;

    private String statusMessage;

    private String showMessage;

    private Object result;
}
