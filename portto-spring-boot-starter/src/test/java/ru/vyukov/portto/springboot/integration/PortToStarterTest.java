package ru.vyukov.portto.springboot.integration;

import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import ru.vyukov.portto.porttoserver.PortToClientConfig;
import ru.vyukov.portto.porttoserver.PorttoServerApplication;
import ru.vyukov.portto.porttoserver.ServerConfig;
import ru.vyukov.portto.springboot.PortTo;
import ru.vyukov.portto.springboot.annotations.PortToRemoteAddress;
import ru.vyukov.portto.springboot.annotations.PortToRemotePort;

import java.net.InetSocketAddress;

import static org.junit.Assert.*;

@PortTo
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestWebApp.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class PortToStarterTest {

    @Autowired
    private PortToClientConfig portToClientConfig;

    /**
     * eq @Value("${portto.remote.port}")
     */
    @PortToRemotePort
    private int portoToExternalPort;

    @PortToRemoteAddress
    private String remoteAddress;


    private static ConfigurableApplicationContext serverApp;


    @BeforeClass
    public static void setUpTestServer() throws Exception {
        serverApp = SpringApplication.run(PorttoServerApplication.class);
        ServerConfig serverConfig = serverApp.getBean(ServerConfig.class);
        assertEquals(32222, serverConfig.getListenPort());
    }

    @Before
    public void setUpTest() throws Exception {
        InetSocketAddress localServer = new InetSocketAddress("localhost", 32222);

        assertEquals(localServer, portToClientConfig.getServersList().get(0));

        assertNotNull(portoToExternalPort);
        assertNotEquals(0, portoToExternalPort);
        assertNotNull(remoteAddress);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        if (null != serverApp) {
            serverApp.close();
        }
    }


    @Test
    public void openInBrowser() {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject("http://" + remoteAddress, String.class);
        assertEquals("hello", response);
    }

}
