package com.rahul.learning.ems.serviceregistry.ems_service_registration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EmsServiceRegistryApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmsServiceRegistryApplication.class, args);
    }
}