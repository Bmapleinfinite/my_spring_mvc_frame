package com.springway.model;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 存放需要放到作用域里的数据
 * model的作用域是request
 */
public class Model {
    private ConcurrentHashMap<String, Object> modelMap = new ConcurrentHashMap<>();

    public ConcurrentHashMap<String, Object> getAttributes() {
        return modelMap;
    }

    public void setAttribute(String key, Object value) {
        modelMap.put(key, value);
    }
}
