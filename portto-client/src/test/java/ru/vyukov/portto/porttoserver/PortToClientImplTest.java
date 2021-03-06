package ru.vyukov.portto.porttoserver;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import org.apache.sshd.common.util.io.IoUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.SocketUtils;

import java.io.IOException;
import java.net.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Oleg Vyukov
 */
@RunWith(Parameterized.class)
public class PortToClientImplTest {

    private static final int WEB_MOCK_PORT = SocketUtils.findAvailableTcpPort();

    @Parameterized.Parameters
    public static Collection<String> data() {
        return HostNameUtil.getHostAddresses();
    }


    @ClassRule
    public static WireMockClassRule wireMockRule = new WireMockClassRule(WEB_MOCK_PORT);


    private static ConfigurableApplicationContext serverApp;
    private static int serverSshPort;

    private PortToClientImpl portToClient;

    private String testingInterface;

    public PortToClientImplTest(String testingInterface) {
        this.testingInterface = testingInterface;
    }

    @BeforeClass
    public static void setUpSuite() throws Exception {
        serverApp = SpringApplication.run(PorttoServerApplication.class);
        ServerConfig serverConfig = serverApp.getBean(ServerConfig.class);
        serverSshPort = serverConfig.getListenPort();
    }

    @Before
    public void setUpTest() throws Exception {
        PortToClientConfig portToClientConfig = new PortToClientConfig();
        portToClientConfig.setServersList(Collections.singletonList(new InetSocketAddress(testingInterface, serverSshPort)));

        portToClient = new PortToClientImpl(portToClientConfig);

        stubFor(get(urlEqualTo("/"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody("mock index page")));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        if (null != serverApp) {
            serverApp.close();
        }
    }

    @Test
    public void startForwarding() throws PortToClientException, IOException {
        portToClient.startForwarding(WEB_MOCK_PORT);

        String remoteUrl = "http://" + portToClient.getRemoteAddress();
        List<String> strings = IoUtils.readAllLines(new URL(remoteUrl));
        assertEquals("mock index page", strings.get(0));

        portToClient.stopForwardingAll();
    }


    @Test(expected = IllegalStateException.class)
    public void startForwardingAllowOnlyOne() throws PortToClientException {
        portToClient.startForwarding(WEB_MOCK_PORT);
        portToClient.startForwarding(WEB_MOCK_PORT + 1);
    }
}