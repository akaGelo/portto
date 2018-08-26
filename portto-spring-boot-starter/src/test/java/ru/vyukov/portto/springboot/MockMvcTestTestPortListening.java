package ru.vyukov.portto.springboot;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vyukov.portto.springboot.annotations.PortToRemoteAddress;
import ru.vyukov.portto.springboot.annotations.PortToRemotePort;

@Ignore
@PortTo
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class MockMvcTestTestPortListening {

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

    @PortToRemoteAddress
    private String remoteAddress;


    @Test
    public void openInBrowser() {

        String url = "http://" + remoteAddress;

//TODO likeBrowserInSeleniumGrid.getRequest()

    }

}
