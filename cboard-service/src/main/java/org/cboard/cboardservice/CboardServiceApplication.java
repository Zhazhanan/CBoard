package org.cboard.cboardservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@ServletComponentScan
public class CboardServiceApplication {
    public static ConfigurableApplicationContext run;

    public static void main(String[] args) {
        run = SpringApplication.run(CboardServiceApplication.class, args);
    }

}
