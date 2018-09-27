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

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

@PortTo
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
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
        String response = likeBrowserInSeleniumGrid.getForObject(url, String.class);
        assertThat(response, containsString("From Russia with Love"));

    }

}
