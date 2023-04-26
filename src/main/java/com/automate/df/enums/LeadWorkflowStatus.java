package com.automate.df.enums;

public enum LeadWorkflowStatus {

    PREENQUIRYFOLLOWUP("TESTDRIVE", "PREENQUIRY_COMPLETED"),
    ENQUIRYFOLLOWUP("ENQUIRYFOLLOWUP", "ENQUIRYCOMPLETED");

    private final String key;
    private final String value;

    LeadWorkflowStatus(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

}
