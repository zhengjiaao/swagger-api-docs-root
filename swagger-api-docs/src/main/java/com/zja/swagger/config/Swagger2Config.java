/**
 * @Company: 上海***技术有限公司
 * @Department: 数据中心
 * @Author: 宇宙小神特别萌
 * @Email: ***
 * @Date: 2021-11-22 15:57
 * @Since:
 */
package com.zja.swagger.config;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * swagger2 api docs：http://localhost:port/swagger-ui.html
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket adminApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .forCodeGeneration(true)
                .pathMapping("/")
                .select()
                .paths(paths())
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .securityContexts(securityContexts());
    }

    private List<SecurityContext> securityContexts() {
        return Lists.newArrayList(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .build()
        );
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(new SecurityReference("Authorization", authorizationScopes));
    }

    private Predicate<String> paths() {
        return PathSelectors.regex("^/(?!error).*$");
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("Zhengja", "https://www.zhengjiaao.cn", "***");
        return new ApiInfoBuilder()
                .title("Swagger2 API To Word 系统")
                .description(
                        "swagger官方示例：" + "\n"
                                + "swagger2 ：https://petstore.swagger.io" + "\n"
                                + "swagger2 api ：https://petstore.swagger.io/v2/swagger.json" + "\n"
                                + "swagger3 ：https://generator3.swagger.io" + "\n"
                                + "swagger3 api ：https://generator3.swagger.io/openapi.json" + "\n"
                )
                .contact(contact)
                .version("2.0")
                .build();
    }
}
