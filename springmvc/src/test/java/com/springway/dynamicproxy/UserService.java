package com.springway.dynamicproxy;

public class UserService implements IUserService {
    @Override
    public int updateUser() {
        System.out.println("修改用户方法");
        return 0;
    }
}
