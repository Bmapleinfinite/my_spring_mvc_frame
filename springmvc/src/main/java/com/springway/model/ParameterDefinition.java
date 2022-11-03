package com.springway.model;

public class ParameterDefinition {
    private Class parameterType;    //参数类型
    private String parameterName;   //参数名字
    private int parameterIndex;     //参数位置
    private boolean requestBody;    //参数是否是json格式

    public ParameterDefinition() {
    }

    public ParameterDefinition(Class parameterType, String parameterName, int parameterIndex, boolean requestBody) {
        this.parameterType = parameterType;
        this.parameterName = parameterName;
        this.parameterIndex = parameterIndex;
        this.requestBody = requestBody;
    }

    public Class getParameterType() {
        return parameterType;
    }

    public void setParameterType(Class parameterType) {
        this.parameterType = parameterType;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public int getParameterIndex() {
        return parameterIndex;
    }

    public void setParameterIndex(int parameterIndex) {
        this.parameterIndex = parameterIndex;
    }

    public boolean isRequestBody() {
        return requestBody;
    }

    public void setRequestBody(boolean requestBody) {
        this.requestBody = requestBody;
    }

    @Override
    public String toString() {
        return "ParameterDefinition{" +
                "parameterType=" + parameterType +
                ", parameterName='" + parameterName + '\'' +
                ", parameterIndex=" + parameterIndex +
                ", requestBody=" + requestBody +
                '}';
    }
}
