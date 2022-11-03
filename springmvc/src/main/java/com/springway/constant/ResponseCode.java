package com.springway.constant;

public enum ResponseCode {
    CONFIG_EXCEPTION(1000, "config配置错误！"),
    NO_CONFIG_EXCEPTION(1001,"没有configuration注解！") ,
    NO_SUCH_METHOD(1002, "没有构造方法或实例化失败！") ,
    SAME_REQUESTMAPPING_EXCEPTION(1003, "两个方法的映射路径相同！"),
    EMPTY_MAPPING_EXCEPTION(1004, "方法的映射路径不能为空！"),
    NO_MAPPING_EXCEPTION(404, "找不到该方法！"),
    BAD_RETURNTYPE_EXCEPTION(1005, "方法的返回类型错误！"),
    BAD_EXCEPTION_HANDLER_CLASS_PATH_EXCEPTION(1006, "异常处理类路径错误！");
    private int id;
    private String message;

    ResponseCode(int id, String message) {
        this.id = id;
        this.message = message;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
