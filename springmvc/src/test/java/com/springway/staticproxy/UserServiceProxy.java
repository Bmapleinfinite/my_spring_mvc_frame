package com.springway.staticproxy;

public class UserServiceProxy implements IUserService{

    UserService us = new UserService();

    @Override
    public int updateUser() {
        System.out.println("代理日志开始了！");
        int num = us.updateUser();
        System.out.println("代理日志结束了！");
        return num;
    }
}
