package com.automate.df.util;


public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 8642870094668737414L;
    public String[] customMessage;

    public String status;

    public ErrorMessages errorMessages;

    public BaseException() {
        super();
    }

    public BaseException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public BaseException(ErrorMessages errorMessages) {
        super();
        this.errorMessages = errorMessages;
    }

    public BaseException(ErrorMessages errorMessages, String status) {
        super();
        this.errorMessages = errorMessages;
        this.status = status;
    }

    public BaseException(ErrorMessages errorMessages, String status, String... customMessage) {
        super();
        this.errorMessages = errorMessages;
        this.customMessage = customMessage;
        this.status = status;
    }
}
