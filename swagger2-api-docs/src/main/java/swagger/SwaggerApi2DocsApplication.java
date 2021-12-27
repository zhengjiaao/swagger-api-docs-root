package swagger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * swagger: http://127.0.0.1:8088/swagger-ui.html
 *
 * 参考来源：
 *      https://github.com/JMCuixy/swagger2word
 *      https://github.com/Sayi/poi-tl
 * TODO 基于开源项目的二次开发
 */
@SpringBootApplication
public class SwaggerApi2DocsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SwaggerApi2DocsApplication.class, args);
    }
}
