package com.nomad.model;

public class GenericResponseModel<T> {

    private boolean status;
    private String message;
    private int userCheckInId;
    private ResponseError error;
    private T data;
    private String statusCode;

    public int getUserCheckInId() {
        return userCheckInId;
    }

    public void setUserCheckInId(int userCheckInId) {
        this.userCheckInId = userCheckInId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public ResponseError getError() {
        return error;
    }

    public void setError(ResponseError error) {
        this.error = error;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
