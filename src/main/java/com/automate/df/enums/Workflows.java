package com.automate.df.enums;


public enum Workflows {
    Lead("Lead Workflow");
    private final String workflow;

    Workflows(String workflow) {
        this.workflow = workflow;
    }

    public String getValue() {
        return this.workflow;
    }

    @Override
    public String toString() {
        return this.workflow;
    }
}