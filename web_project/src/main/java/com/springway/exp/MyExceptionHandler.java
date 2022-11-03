package com.springway.exp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;

public class MyExceptionHandler extends HandelExceptionResolver{
    @Override
    protected void handleException(Exception e, HttpServletRequest req, HttpServletResponse response) {
        if(e instanceof InvocationTargetException){
            Throwable targetException = ((InvocationTargetException) e).getTargetException();
            System.out.println(targetException);
        }
    }
}
