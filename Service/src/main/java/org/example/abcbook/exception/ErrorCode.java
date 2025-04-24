package org.example.abcbook.exception;


public enum ErrorCode {
    EMAIL_EMPTY(1001, "create.user.email.empty"),
    EMAIL_EXISTED(1002,"create.user.email.existed");

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
