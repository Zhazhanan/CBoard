package org.cboard.cboardservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ServletComponentScan
//@Import(CacheConfig.class)
public class CboardServiceApplication {
    public static ConfigurableApplicationContext run;

    public static void main(String[] args) {
        run = SpringApplication.run(CboardServiceApplication.class, args);
    }

}
