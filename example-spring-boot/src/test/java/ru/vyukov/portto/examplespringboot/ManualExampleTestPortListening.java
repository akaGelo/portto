package ru.vyukov.portto.examplespringboot;

import com.codeborne.selenide.Configuration;
import lombok.extern.slf4j.Slf4j;
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

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

@PortTo
@Slf4j
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
    public void openInBrowserExampleOne() {
        String url = "http://" + remoteAddress;

        log.info("Request: " + url);

        RestOperations likeBrowserInSeleniumGrid = new RestTemplate();
        String response = likeBrowserInSeleniumGrid.getForObject(url, String.class);

        assertThat(response, containsString("From Russia with Love"));

    }

    @Test
    public void openInBrowserExampleTwo() {
        String url = "http://" + remoteAddress;

        log.info("Request: " + url);

        Configuration.baseUrl = url;
        Configuration.reportsFolder = "target/selenoid/";
        Configuration.browser = "phantomjs";

        open("/");
        $("body").shouldHave(text("From Russia with Love"));
    }


}
