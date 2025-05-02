package org.noahsark.gw.ws;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author: zhangxt
 * @version:
 * @date: 2024/03/14
 */
@SpringBootApplication()
@MapperScan("org.noahsark.**.mapper")
@ComponentScan(
        basePackages = {"org.noahsark"}
        /* excludeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "org.noahsark.web.*"),
                @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = RestController.class)
        }*/
        )
@EnableScheduling
public class WsGwServerApp {
    public static void main(String[] args) {

        SpringApplication application = new SpringApplication(WsGwServerApp.class);
        application.setWebApplicationType(WebApplicationType.NONE); // 以非web方式启动应用
        application.run(args);

    }
}
