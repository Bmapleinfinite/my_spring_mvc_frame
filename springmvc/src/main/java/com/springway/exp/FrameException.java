package com.springway.exp;

import com.springway.constant.ResponseCode;

public class FrameException extends RuntimeException{
    private int id;
    private String message;

    public FrameException(ResponseCode configException) {
        this.id = configException.getId();
        this.message = configException.getMessage();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "code: " + id + " message:" + message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
