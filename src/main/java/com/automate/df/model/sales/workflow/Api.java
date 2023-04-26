package com.automate.df.model.sales.workflow;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class Api {

    private int apiId;

    private String apiHeaders;

    private String apiMethod;

    private String apiRequestBody;

    private String apiUrl;

}
