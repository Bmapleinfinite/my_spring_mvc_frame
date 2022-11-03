package com.springway.app;

import com.springway.anno.Configuration;
import com.springway.anno.EnableGlobalException;

@Configuration("com.springway.web")
@EnableGlobalException("com.springway.exp.MyExceptionHandler")
public class AppConfig {
}
