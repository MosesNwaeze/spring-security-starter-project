
package com.example.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = {"com.example.securitystarter", "com.example"})
@ConfigurationPropertiesScan("com.example.securitystarter.config")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
