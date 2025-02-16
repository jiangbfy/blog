package com.blog;

import com.blog.common.RedisUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableScheduling
@SpringBootApplication
public class MainApplication extends SpringBootServletInitializer implements CommandLineRunner {
    public static void main(String[] args) {
        ConfigurableApplicationContext application = SpringApplication.run(MainApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MainApplication.class);
    }
    @Override
    public void run(String... args) throws Exception {
    }
}
