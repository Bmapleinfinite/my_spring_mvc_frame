package com.springway.listener;

import com.springway.anno.*;
import com.springway.constant.MethodContainer;
import com.springway.constant.ResponseCode;
import com.springway.exp.FrameException;
import com.springway.model.BeanDefinition;
import com.springway.model.MethodDefinition;
import com.springway.model.ParameterDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppListener implements ServletContextListener {

    private static List<File> fileList = new ArrayList<>();

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 获取 slf4j 的 logger对象
        Logger logger = LoggerFactory.getLogger(AppListener.class);
        // 通过 logger 的 info() 对象，打印日志
        logger.info("已经监听到客户端！");

        // 获取 ServletContext 对象
        ServletContext application = sce.getServletContext();
        // 获取 web.xml 设置的 Context-param 中 config 字段的值
        String appConfig = application.getInitParameter("config");
        logger.info("获取到的config路径----> {}", appConfig);

        // 通过 config 字段值获取对应的 class 对象
        Class<?> appClazz = null;
        try {
            appClazz = Class.forName(appConfig);
        } catch (ClassNotFoundException e) {
            // 自定义 FrameException异常
            throw new FrameException(ResponseCode.CONFIG_EXCEPTION);
        }

        // 获取 config 字段值对应的 class 对象上的 Configuration 注解
        Configuration configAnnotation = appClazz.getAnnotation(Configuration.class);
        // 若不存在Configuration注解，则抛出自定义异常
        if (configAnnotation == null) {
            throw new FrameException(ResponseCode.NO_CONFIG_EXCEPTION);
        }
        // 获取 configuration 的 value 值，根据该值确定项目 controller 包的相对路径
        String controllerPath = configAnnotation.value();
        logger.info("获取到的controller路径----> {}", controllerPath);

        // 通过 ServletContext 对象，获取 controller 包部署后的真实路径
        String packPath = getPackPath(application, controllerPath);
        logger.info("获取到的controller包的真实路径----> {}", packPath);

        // 获取 controller 包部署后的真实路径下的所有文件
        getAllController(packPath);
        logger.info("获取到的controller包下的所有文件路径----> {}", fileList);

        // 获取文件列表中所有文件的全类限定名
        String[] classNames = getClassName(application);
        logger.info("获取controller包下的全类限定名----> {}", Arrays.toString(classNames));

        // 通过全类限定名获取对应Controller包下的Class对象
        List<Class<?>> clazzList = getControllerClass(classNames);
        logger.info("获取Controller包下的Class对象----> {}", clazzList);

        handleClass(clazzList);
        logger.info("解析到的class对象映射----> {}", MethodContainer.getMappings());
    }

    private void handleClass(List<Class<?>> clazzList) {
        for (Class<?> aClass :
                clazzList) {
            // 获取 class 对象上的 Controller 注解
            Controller controllerAnno = aClass.getAnnotation(Controller.class);

            if (controllerAnno != null) {
                // 新建 BeanDefinition 对象 填充数据
                BeanDefinition beanDefinition = new BeanDefinition();
                beanDefinition.setaClass(aClass);
                beanDefinition.setClassName(aClass.getSimpleName());
                beanDefinition.setAnnotationList(Arrays.asList(aClass.getAnnotations()));
                // 获取 class 对象上的 RequestMapping注解
                RequestMapping requestMapping = aClass.getAnnotation(RequestMapping.class);
                beanDefinition.setRequestMapping(requestMapping != null ? requestMapping.value() : "");
                try {
                    beanDefinition.setInstance(aClass.getConstructor().newInstance());
                } catch (Exception e) {
                    // 抛出无构方法或实例化错误
                    throw new FrameException(ResponseCode.NO_SUCH_METHOD);
                }
                // 获取 class 对象下的所有方法
                Method[] methods = aClass.getMethods();

                for (Method method :
                        methods) {
                    // 获取 method 对象上的 RequestMapping注解
                    RequestMapping methodRequestMapping = method.getAnnotation(RequestMapping.class);
                    if(methodRequestMapping != null){
                        // 新建 MethodDefinition 对象 填充数据
                        if(methodRequestMapping.value() == ""){
                            // 抛出无映射路径错误
                            throw new FrameException(ResponseCode.EMPTY_MAPPING_EXCEPTION);
                        }
                        MethodDefinition methodDefinition = new MethodDefinition();
                        methodDefinition.setMethod(method);
                        methodDefinition.setBeanDefinition(beanDefinition);
                        methodDefinition.setMethodName(method.getName());
                        methodDefinition.setRequestMapping(methodRequestMapping.value());
                        methodDefinition.setReturnType(method.getReturnType());
                        // 获取 method 对象上的 ResponseBody 注解
                        ResponseBody methodResponseBody = method.getAnnotation(ResponseBody.class);
                        methodDefinition.setResponseBody(methodResponseBody != null ? true : false);

                        List<ParameterDefinition> parameterDefinitions = new ArrayList<>();
                        // 获取 method 下的所有参数
                        Parameter[] parameters = method.getParameters();
                        for (int index = 0; index < parameters.length; index++) {
                            Parameter param = parameters[index];
                            // 新建 ParameterDefinition 对象 填充数据
                            ParameterDefinition parameterDefinition = new ParameterDefinition();
                            parameterDefinition.setParameterName(param.getName());//todo
                            parameterDefinition.setParameterType(param.getType());
                            parameterDefinition.setParameterIndex(index);
                            RequsetBody requsetBodyAnno = param.getAnnotation(RequsetBody.class);
                            parameterDefinition.setRequestBody(requsetBodyAnno != null ? true : false);

                            parameterDefinitions.add(parameterDefinition);
                        }
                        methodDefinition.setParameterDefinitionList(parameterDefinitions);

                        // 存入 MethodContainer
                        String key = beanDefinition.getRequestMapping() + methodDefinition.getRequestMapping();
                        if (MethodContainer.getMappings().get(key) != null) {
                            // 抛出同一映射路径错误
                            throw new FrameException(ResponseCode.SAME_REQUESTMAPPING_EXCEPTION);
                        }
                        MethodContainer.getMappings().put(key, methodDefinition);
                    }
                }
            }
        }
    }

    /**
     * 通过全类限定名获取对应Controller包下的Class对象
     *
     * @param classNames
     * @return
     */
    private List<Class<?>> getControllerClass(String[] classNames) {
        List<Class<?>> result = new ArrayList<>();
        for (String className :
                classNames) {
            Class<?> aClass = null;
            try {
                aClass = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            result.add(aClass);
        }
        return result;
    }

    /**
     * 获取全类名
     *
     * @param application
     * @return
     */
    private String[] getClassName(ServletContext application) {
        String[] result = new String[fileList.size()];
        int count = 0;
        for (File file :
                fileList) {
            String absolutePath = file.getAbsolutePath();
            absolutePath = absolutePath.replace(application.getRealPath("/WEB-INF/classes/"), "");
            absolutePath = absolutePath.replace("\\", ".");
            absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("."));
            result[count++] = absolutePath;
        }
        return result;
    }

    /**
     * 通过递归获取controller路径下所有的文件
     *
     * @param packPath
     * @return
     */
    private void getAllController(String packPath) {
        File dir = new File(packPath);
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                fileList.add(file);
            } else {
                getAllController(file.getAbsolutePath());
            }
        }
    }

    /**
     * 获取项目部署后的controller包实际路径
     *
     * @param application
     * @param controllerPath
     * @return
     */
    private String getPackPath(ServletContext application, String controllerPath) {
        String realPath = application.getRealPath("/WEB-INF/classes/");
        return realPath + controllerPath.replace(".", "\\");
    }

}
