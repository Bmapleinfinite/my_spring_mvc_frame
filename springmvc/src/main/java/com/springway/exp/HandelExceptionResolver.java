package com.springway.exp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class HandelExceptionResolver{

    protected abstract void handleException(Exception e, HttpServletRequest req, HttpServletResponse response);

    public void handle(Exception e, HttpServletRequest req, HttpServletResponse res){
        handleException(e, req, res);
    }
}
