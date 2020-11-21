package com.devteam.framework.config;

import java.util.TimeZone;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
  * Program annotation configuration
  *
  * @author ruoyi
  */
@Configuration
// Indicates that the proxy object is exposed through the aop framework, and AopContext can access
@EnableAspectJAutoProxy(exposeProxy = true)
// Specify the path of the package of the Mapper class to be scanned
@MapperScan("com.devteam.**.mapper")
public class ApplicationConfig
{
     /**
      * Time zone configuration
      */
     @Bean
     public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization()
     {
         return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.timeZone(TimeZone.getDefault());
     }
}
