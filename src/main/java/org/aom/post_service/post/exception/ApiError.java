package org.aom.post_service.post.exception;

import java.time.LocalDateTime;

public class ApiError {
    private LocalDateTime errorDateTime;
    private String errorMsg;
    private int errCode;

    public ApiError() {

    }

    public ApiError(LocalDateTime errorDateTime, String errorMsg, int errCode){
        this.errorDateTime = errorDateTime;
        this.errorMsg = errorMsg;
        this.errCode = errCode;
    }

    public LocalDateTime getErrorDateTime() {
        return errorDateTime;
    }

    public void setErrorDateTime(LocalDateTime errorDateTime) {
        this.errorDateTime = errorDateTime;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }
}

