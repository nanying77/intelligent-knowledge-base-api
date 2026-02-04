package com.bujian.aipersnonknowledge.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("知脉智能知识库 API 文档")
                        .version("1.0")
                        .description("知识库系统接口文档")
                        .contact(new Contact()
                                .name("WangYuhua")
                                .email("developer@example.com")));
    }
}