package com.springway.servlet;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.springway.anno.EnableGlobalException;
import com.springway.constant.CommonType;
import com.springway.constant.MethodContainer;
import com.springway.constant.ResponseCode;
import com.springway.exp.FrameException;
import com.springway.exp.HandelExceptionResolver;
import com.springway.model.MethodDefinition;
import com.springway.model.Model;
import com.springway.model.ModelAndView;
import com.springway.model.ParameterDefinition;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DispatcherServlet extends HttpServlet {
    private static Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private static HandelExceptionResolver handler;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        String appConfig = servletContext.getInitParameter("config");

        Class<?> aClass = null;
        try {
            aClass = Class.forName(appConfig);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        EnableGlobalException annotation = aClass.getAnnotation(EnableGlobalException.class);
        if (annotation != null) {
            String userExpPath = annotation.value();
            try {
                handler = (HandelExceptionResolver) Class.forName(userExpPath).getDeclaredConstructor().newInstance();
//                System.err.println(handler);
            } catch (InstantiationException e) {
                throw new FrameException(ResponseCode.NO_SUCH_METHOD);
            } catch (IllegalAccessException e) {
                throw new FrameException(ResponseCode.NO_SUCH_METHOD);
            } catch (InvocationTargetException e) {
                throw new FrameException(ResponseCode.NO_SUCH_METHOD);
            } catch (NoSuchMethodException e) {
                throw new FrameException(ResponseCode.NO_SUCH_METHOD);
            } catch (ClassNotFoundException e) {
                throw new FrameException(ResponseCode.BAD_EXCEPTION_HANDLER_CLASS_PATH_EXCEPTION);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String requestURI = req.getRequestURI();
        logger.info("????????????????????????----> {}", requestURI);
        Model model = new Model();  //todo
        ConcurrentHashMap<String, MethodDefinition> mappings = MethodContainer.getMappings();
        if (!mappings.containsKey(requestURI)) {
            throw new FrameException(ResponseCode.NO_MAPPING_EXCEPTION);
        }

        MethodDefinition methodDefinition = mappings.get(requestURI);
        Method method = methodDefinition.getMethod();
        Object obj = methodDefinition.getBeanDefinition().getInstance();

        try {
            Object[] params = handleParameters(methodDefinition, req, resp, model);
            logger.info("????????????????????????----> {}", Arrays.toString(params));
            Object result = method.invoke(obj, params);
            // ?????????????????????
            // ??????????????????ResponseBody???????????????Json?????????
            if (methodDefinition.isResponseBody()) {
                resp.setContentType("text/html; charset=utf-8");
                resp.setCharacterEncoding("UTF-8");
                String jsonString = JSON.toJSONString(result);
                PrintWriter writer = resp.getWriter();
                writer.write(jsonString);
                writer.flush();
                writer.close();
            } else if (result instanceof ModelAndView) { // ???????????????ModelAndView??????
                ModelAndView mv = (ModelAndView) result;
                ConcurrentHashMap<String, Object> attributes = mv.getModel().getAttributes();
                for (String key :
                        attributes.keySet()) {
                    req.setAttribute(key, attributes.get(key));
                }
                req.getRequestDispatcher(mv.getView()).forward(req, resp);
            } else { // ????????????????????????
                if (!(result instanceof String)) { // ???????????????????????????????????????ResponseBody?????? ???????????????
                    throw new FrameException(ResponseCode.BAD_RETURNTYPE_EXCEPTION);
                }
                ConcurrentHashMap<String, Object> attributes = model.getAttributes();
                for (String key :
                        attributes.keySet()) {
                    req.setAttribute(key, attributes.get(key));
                }
                req.getRequestDispatcher(result.toString()).forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (handler != null) {
                handler.handle(e, req, resp);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

    /**
     * ?????????????????????????????????
     *
     * @param methodDefinition
     * @param req
     * @param resp
     */
    private Object[] handleParameters(MethodDefinition methodDefinition, HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException {
        List<ParameterDefinition> parameterDefinitionList = methodDefinition.getParameterDefinitionList();
        Object[] result = new Object[parameterDefinitionList.size()];
        Map<String, Object> jsonMap = null;
        for (int i = 0; i < result.length; i++) {
            // ????????? MethodDefinition ???????????? ParameterDefinition ??????
            ParameterDefinition parameterDefinition = parameterDefinitionList.get(i);
            Class parameterType = parameterDefinition.getParameterType();
            String typeName = parameterType.getName();
            Object obj = null;
            if (!parameterDefinition.isRequestBody()) {
                // ???????????????????????????????????????????????????
                if (CommonType.isCommonType(typeName)) {
                    // ??????????????????????????????
                    obj = handleCommon(typeName, req, parameterDefinition.getParameterName());
                } else if ("javax.servlet.http.HttpServletRequest".equals(typeName)) {
                    // ?????? HttpServletRequest ??????
                    obj = req;
                } else if ("javax.servlet.http.HttpServletResponse".equals(typeName)) {
                    // ?????? HttpServletResponse ??????
                    obj = resp;
                } else if ("com.springway.model.Model".equals(typeName)) {
                    // ???????????????model??????
                    obj = model;
                } else {
                    // ??????????????????
                    obj = handleObject(req, parameterType);
                }
            } else {
                // ??????Json????????????
                // ??????IO?????????json?????????
                String jsonString = null;
                if (jsonMap == null) { // ??????IO????????????????????? Map ?????????
                    byte[] bytes = req.getInputStream().readAllBytes();
                    jsonString = new String(bytes, "UTF-8").trim();
                    logger.info("????????????json????????? {}", jsonString);
                    jsonMap = JSON.parseObject(jsonString);
                }
                obj = handleJson(methodDefinition, parameterType, parameterDefinition.getParameterName(), i, jsonMap);
            }
            result[i] = obj;
        }
        return result;
    }

    private Object handleJson(MethodDefinition methodDefinition, Class parameterType, String parameterName, int index, Map<String, Object> jsonMap) {
        Object result = null;
        // ??????List????????????
        if ("java.util.List".equals(parameterType.getName())) {
            // ????????????????????????????????????????????????????????????????????????????????????
            Type gpt = methodDefinition.getMethod().getGenericParameterTypes()[index];
            // ???????????????????????????
            ParameterizedType pt = (ParameterizedType) gpt;
            // ?????????????????????
            Type at = pt.getActualTypeArguments()[0];
            Class<?> aClass = null;
            try {
                // ???????????????????????????Class??????
                aClass = Class.forName(at.getTypeName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            logger.info("????????????Json????????? {}", jsonMap);
            logger.info("????????? {}", parameterName);
            logger.info("???????????????Object {}", jsonMap.get(parameterName));
            JSONArray jsonArray = (JSONArray) jsonMap.get(parameterName);
            result = jsonArray.toList(aClass);
        } else {  // ????????????????????????
            logger.info("????????????Json????????? {}", jsonMap);
            logger.info("????????? {}", parameterName);
            logger.info("???????????????Object {}", jsonMap.get(parameterName));
            JSONObject jsonObject = (JSONObject) jsonMap.get(parameterName);
            try {
                result = jsonObject.to(parameterType);
            } catch (Exception e) {
                throw new FrameException(ResponseCode.NO_SUCH_METHOD);
            }
        }
        logger.info("?????????????????? {}", result);
        return result;
    }

    /**
     * ????????????????????????
     *
     * @param req
     * @param parameterType
     * @return
     */
    private Object handleObject(HttpServletRequest req, Class parameterType) {
        Object result = null;
        try {
            result = parameterType.getDeclaredConstructor().newInstance();
            BeanUtils.populate(result, req.getParameterMap());
        } catch (Exception e) {
            // ???????????????????????????????????????
            throw new FrameException(ResponseCode.NO_SUCH_METHOD);
        }
        return result;
    }

    /**
     * ????????????????????????
     *
     * @param typeName
     * @param req
     * @param parameterName
     * @return
     */
    private Object handleCommon(String typeName, HttpServletRequest req, String parameterName) {
        Object result = null;
        String parameter = req.getParameter(parameterName);
        switch (typeName) {
            case "java.lang.String":
                result = parameter;
                break;
            case "char":
            case "java.lang.Character":
                result = parameter.charAt(0);
                break;
            case "double":
            case "java.lang.Double":
                result = Double.parseDouble(parameter);
                break;
            case "float":
            case "java.lang.float":
                result = Float.parseFloat(parameter);
                break;
            case "int":
            case "java.lang.Integer":
                result = Integer.parseInt(parameter);
                break;
            case "byte":
            case "java.lang.Byte":
                result = Byte.parseByte(parameter);
                break;
            case "short":
            case "java.lang.Short":
                result = Short.parseShort(parameter);
                break;
            case "long":
            case "java.lang.Long":
                result = Long.parseLong(parameter);
                break;
            case "boolean":
            case "java.lang.Boolean":
                result = Boolean.parseBoolean(parameter);
                break;
        }
        return result;
    }
}
