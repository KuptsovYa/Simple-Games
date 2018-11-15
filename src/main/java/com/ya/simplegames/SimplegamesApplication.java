package com.ya.simplegames;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

//@SpringBootApplication
//public class SimplegamesApplication {
//
//    public static void main(String[] args) {
//        SpringApplication.run(SimplegamesApplication.class, args);
//    }
//}


@SpringBootApplication
public class SimplegamesApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SimplegamesApplication.class);
    }

    public static void main(String[] args){
        SpringApplication.run(SimplegamesApplication.class, args);
    }

}
