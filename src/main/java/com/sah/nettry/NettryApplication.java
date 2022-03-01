package com.sah.nettry;

import com.sah.nettry.service.NettryService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class NettryApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context= SpringApplication.run(NettryApplication.class, args);
        context.getBean(NettryService.class).start();
    }

}
