package com.springway.model;

import java.lang.reflect.Method;
import java.util.List;

public class MethodDefinition {
    private String methodName;                                  // 方法名
    private Method method;                                      // 方法对象
    private String requestMapping;                              // 方法对应的映射路径
    private List<ParameterDefinition> parameterDefinitionList;  // 方法参数列表
    private BeanDefinition beanDefinition;                      // 方法所在类的信息
    private Object returnType;                                  // 返回值
    private boolean responseBody;                               // 是否返回json

    public MethodDefinition() {
    }

    public MethodDefinition(String methodName, Method method, String requestMapping, List<ParameterDefinition> parameterDefinitionList, BeanDefinition beanDefinition, Object returnType, boolean responseBody) {
        this.methodName = methodName;
        this.method = method;
        this.requestMapping = requestMapping;
        this.parameterDefinitionList = parameterDefinitionList;
        this.beanDefinition = beanDefinition;
        this.returnType = returnType;
        this.responseBody = responseBody;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getRequestMapping() {
        return requestMapping;
    }

    public void setRequestMapping(String requestMapping) {
        this.requestMapping = requestMapping;
    }

    public List<ParameterDefinition> getParameterDefinitionList() {
        return parameterDefinitionList;
    }

    public void setParameterDefinitionList(List<ParameterDefinition> parameterDefinitionList) {
        this.parameterDefinitionList = parameterDefinitionList;
    }

    public BeanDefinition getBeanDefinition() {
        return beanDefinition;
    }

    public void setBeanDefinition(BeanDefinition beanDefinition) {
        this.beanDefinition = beanDefinition;
    }

    public Object getReturnType() {
        return returnType;
    }

    public void setReturnType(Object returnType) {
        this.returnType = returnType;
    }

    public boolean isResponseBody() {
        return responseBody;
    }

    public void setResponseBody(boolean responseBody) {
        this.responseBody = responseBody;
    }

    @Override
    public String toString() {
        return "MethodDefinition{" +
                "methodName='" + methodName + '\'' +
                ", method=" + method +
                ", requestMapping='" + requestMapping + '\'' +
                ", parameterDefinitionList=" + parameterDefinitionList +
                ", beanDefinition=" + beanDefinition +
                ", returnType=" + returnType +
                ", responseBody=" + responseBody +
                '}';
    }
}
