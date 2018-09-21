package ru.vyukov.portto.porttoserver;

import com.jcraft.jsch.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWireMock(port = 0)
public class PorttoServerApplicationTests {


    @Value("${wiremock.server.port}")
    private int mockHttpPort;

    @Autowired
    private ServerConfig serverConfig;

    private RestTemplate restTemplate = new RestTemplate();


    private String mockPageUrl;
    private Session session;

    @Before
    public void setUp() throws Exception {
        assertTrue(serverConfig.isAllowAnyPassword());

        stubFor(get(urlEqualTo("/"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody("mock index page")));

        mockPageUrl = "http://localhost:" + mockHttpPort;

        session = createSession();
    }

    @After
    public void tearDown() throws Exception {
        session.disconnect();
    }

    @Test
    public void testRemotePortForwarding() throws JSchException, InterruptedException {


        session.setPortForwardingR(0, "localhost", mockHttpPort);


        String[] portForwardingR = session.getPortForwardingR();
        assertFalse(0 == portForwardingR.length);

        String responseBody = restTemplate.getForObject(mockPageUrl, String.class);
        assertEquals("mock index page", responseBody);


        for (String s : portForwardingR) {
            session.delPortForwardingR(parseFirsPort(s));
        }
    }

    /**
     * input string: "2223:localhost:8081"
     *
     * @param s
     * @return
     */
    private int parseFirsPort(String s) {
        String[] split = s.split(":");
        return Integer.parseInt(split[0]);
    }


    protected Session createSession() throws JSchException {
        JSch sch = new JSch();
        sch.setLogger(new Logger() {
            public boolean isEnabled(int i) {
                return true;
            }

            public void log(int i, String s) {
                System.out.println("Log(jsch," + i + "): " + s);
            }
        });
        Session session = sch.getSession("junit", "localhost", serverConfig.getListenPort());
        session.setUserInfo(new UserInfo() {
            public String getPassphrase() {
                return null;
            }

            public String getPassword() {
                return "sshd";
            }

            public boolean promptPassword(String message) {
                return true;
            }

            public boolean promptPassphrase(String message) {
                return false;
            }

            public boolean promptYesNo(String message) {
                return true;
            }

            public void showMessage(String message) {
            }
        });
        session.connect();
        return session;
    }

}
