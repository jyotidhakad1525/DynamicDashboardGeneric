package com.automate.df.enums;

public enum WorkflowTaskStatus {

    ASSIGN("ASSIGN", "ASSIGNED"),
    IN_PROGRESS("IN_PROGRESS", "IN_PROGRESS"),
    CLOSE("CLOSE", "CLOSED"),
    SYSTEM_ERROR("SYSTEM_ERROR", "SYSTEM_ERROR"),
    RESCHEDULE("RESCHEDULE", "RESCHEDULED"),
    CANCEL("CANCEL", "CANCELLED"),
    APPROVAL("SEND_FOR_APPROVAL", "SENT_FOR_APPROVAL"),
    APPROVE("APPROVED", "APPROVED");


    private final String key;
    private final String value;

    WorkflowTaskStatus(String key, String value) {
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
