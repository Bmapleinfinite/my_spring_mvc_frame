package com.springway.model;

import java.lang.annotation.Annotation;
import java.util.List;

public class BeanDefinition {
    private String ClassName;                   // 类名
    private List<Annotation> annotationList;    // 注解列表
    private Class aClass;                       // class对象
    private Object instance;                    // 实例化对象
    private String requestMapping;              // 映射路径

    public BeanDefinition() {
    }

    public BeanDefinition(String className, List<Annotation> annotationList, Class aClass, Object instance, String requestMapping) {
        ClassName = className;
        this.annotationList = annotationList;
        this.aClass = aClass;
        this.instance = instance;
        this.requestMapping = requestMapping;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public List<Annotation> getAnnotationList() {
        return annotationList;
    }

    public void setAnnotationList(List<Annotation> annotationList) {
        this.annotationList = annotationList;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }

    public String getRequestMapping() {
        return requestMapping;
    }

    public void setRequestMapping(String requestMapping) {
        this.requestMapping = requestMapping;
    }

    @Override
    public String toString() {
        return "BeanDefinition{" +
                "ClassName='" + ClassName + '\'' +
                ", annotationList=" + annotationList +
                ", aClass=" + aClass +
                ", instance=" + instance +
                ", requestMapping='" + requestMapping + '\'' +
                '}';
    }
}
