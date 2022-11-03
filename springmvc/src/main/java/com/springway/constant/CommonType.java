package com.springway.constant;

/**
 * 普通数据类型（8种+String）
 */
public class CommonType {
    private static String[] types = {
            "java.lang.String",
            "char", "java.lang.Character",
            "double", "java.lang.Double",
            "float", "java.lang.float",
            "int", "java.lang.Integer",
            "byte", "java.lang.Byte",
            "short", "java.lang.Short",
            "long", "java.lang.Long",
            "boolean", "java.lang.Boolean"
    };

    public static boolean isCommonType(String name) {
        for (String type :
                types) {
            if (type.equals(name)) {
                return true;
            }
        }
        return false;
    }
}
