package com.springway.staticproxy;

import org.junit.Test;

public class Test01 {

    @Test
    public void test01(){
        IUserService us = new UserServiceProxy();
        us.updateUser();
    }
}
