package ru.vyukov.portto.springboot.integration;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.SocketUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vyukov.portto.porttoserver.PortToClientConfig;
import ru.vyukov.portto.porttoserver.PortToClientImpl;
import ru.vyukov.portto.porttoserver.PorttoServerApplication;
import ru.vyukov.portto.porttoserver.ServerConfig;
import ru.vyukov.portto.springboot.annotations.PortToRemoteAddress;
import ru.vyukov.portto.springboot.annotations.PortToRemotePort;

import java.net.InetSocketAddress;
import java.util.Collections;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;

@RestController
@SpringBootApplication
public class TestWebApp {

    @GetMapping("/")
    public String helo() {
        return "hello";
    }
}
