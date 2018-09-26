package ru.vyukov.portto.porttoserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PorttoServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PorttoServerApplication.class, args);
    }
}
