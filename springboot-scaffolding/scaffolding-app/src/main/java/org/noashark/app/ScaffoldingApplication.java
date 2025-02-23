package org.noashark.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 程序应用类
 * @author zhangxt
 * @date 2025/02/23 18:27
 **/
@SpringBootApplication(scanBasePackages = {"org.noashark"})
@MapperScan("org.noashark.**.mapper")
public class ScaffoldingApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScaffoldingApplication.class, args);
    }
}
