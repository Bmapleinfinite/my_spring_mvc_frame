package com.springway.constant;

import com.springway.model.MethodDefinition;

import java.util.concurrent.ConcurrentHashMap;

public class MethodContainer {
    private static ConcurrentHashMap<String, MethodDefinition> mappings = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, MethodDefinition> getMappings() {
        return mappings;
    }
}
