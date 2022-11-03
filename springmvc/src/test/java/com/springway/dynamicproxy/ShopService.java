package com.springway.dynamicproxy;

public class ShopService implements IShopService{
    @Override
    public int delete() {
        System.out.println("删除shop咯！");
        return 0;
    }
}
