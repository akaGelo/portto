package ru.vyukov.portto.examplespringboot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vyukov.portto.springboot.PortTo;
import ru.vyukov.portto.springboot.annotations.PortToRemoteAddress;
import ru.vyukov.portto.springboot.annotations.PortToRemotePort;

import static org.assertj.core.api.Java6Assertions.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@PortTo
@ActiveProfiles("manual-test")
public class ManualWebDriverExampleTestPortListening {

    /**
     * eq @Value("${portto.remote.address}")
     */
    @PortToRemoteAddress
    private String portToExternalAddress;

    /**
     * eq @Value("${portto.remote.port}")
     */
    @PortToRemotePort
    private int portoToExternalPort;


    @Test
    public void contextLoads() {
        fail("");
    }

}
