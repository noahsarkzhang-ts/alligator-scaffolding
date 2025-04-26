package org.noashark.app.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


@Configuration
public class Swagger3Config implements WebMvcConfigurer {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30)
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.regex("/api/.*"))
                .build().apiInfo(new ApiInfoBuilder()
                        //设置文档标题
                        .description("Java后端接口文档")
                        //设置联系人信息
                        .contact(new Contact("allen", "", "allen@qq.com"))
                        //设置版本号
                        .version("1.0.1")
                        //设置文档抬头
                        .title("Java后端接口文档")
                        //设置授权
                        .license("License By swagger")
                        //设置授权访问网址
                        .licenseUrl("https://swagger.io")
                        .build());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**").addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .resourceChain(false);
    }

}
