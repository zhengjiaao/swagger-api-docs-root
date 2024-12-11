package com.zja.swagger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 启动类
 *
 * @swagger: <a href="http://127.0.0.1:8088/swagger-ui.html">...</a>
 * @author: zhengja
 * @since: 2024/12/11 10:19
 */
@SpringBootApplication
public class SwaggerAPIDocsApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SwaggerAPIDocsApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SwaggerAPIDocsApplication.class);
    }
}