package ru.vyukov.portto.examplespringboot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import ru.vyukov.portto.springboot.PortTo;
import ru.vyukov.portto.springboot.annotations.PortToRemoteAddress;
import ru.vyukov.portto.springboot.annotations.PortToRemotePort;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@PortTo
public class ManualExampleTestPortListening {

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
        RestOperations likeBrowserInSeleniumGrid = new RestTemplate();
//TODO likeBrowserInSeleniumGrid.getRequest()

    }

}
