package com.springway.dynamicproxy;

import org.junit.Test;

public class Test01 {

    @Test
    public void test01(){
        DynamicProxyFactory dynamicProxyFactory = new DynamicProxyFactory(new ShopService());
        IShopService proxy = (IShopService) dynamicProxyFactory.getProxy();
        System.out.println(proxy.delete());
    }
}
