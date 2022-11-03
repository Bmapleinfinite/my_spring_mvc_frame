package com.springway.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

//动态代理处理敏感词过滤器
public class CharacterFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding("utf-8");

        ServletRequest request = (ServletRequest) Proxy.newProxyInstance(
                servletRequest.getClass().getClassLoader(),
                servletRequest.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if(method.getName().equals("getParameter")){
                            String invoke = (String) method.invoke(servletRequest, args);
                            return wordProxy(invoke);
                        }else{
                            return method.invoke(servletRequest, args);
                        }
                    }
                }
        );

        filterChain.doFilter(request,servletResponse);
    }

    private String wordProxy(String str) {
        return str.replace("fuck", "****");
    }

    @Override
    public void destroy() {

    }
}
