package org.bpm.abcbook.exception;

public class AppException extends RuntimeException {
    private final String errorCode;
    private final String errorMessage;

    public AppException(String code, String message) {
        super(message);
        this.errorCode = code;
        this.errorMessage = message;
    }

    public String getMessage() {
        return this.errorMessage;
    }

    public String getCode() {
        return this.errorCode;
    }
}
