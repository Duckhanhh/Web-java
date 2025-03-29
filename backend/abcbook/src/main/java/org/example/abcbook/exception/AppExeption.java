package org.example.abcbook.exception;

public class AppExeption extends RuntimeException {
    private ErrorCode errorCode;

    public AppExeption(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
