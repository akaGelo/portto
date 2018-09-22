package ru.vyukov.portto.porttoserver;

import com.jcraft.jsch.JSchException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import ru.vyukov.portto.porttoserver.ports.PortsRegistry;

import static org.junit.Assert.*;
import static ru.vyukov.portto.porttoserver.PorttoServerApplicationTests.createSession;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties =
        {
                "sshd.listen-port=32227",
                "sshd.forwarding.min-port=80",
                "sshd.forwarding.max-port=80"
        })
public class MaxConnectionsTest {


    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private PortsRegistry portsRegistry;

    private RestTemplate restTemplate = new RestTemplate();


    private String mockPageUrl;

    @Before
    public void setUp() throws Exception {
        assertTrue(serverConfig.isAllowAnyPassword());
        assertEquals(0, portsRegistry.getFreePorts());
    }


    /**
     * @throws JSchException        com.jcraft.jsch.JSchException: SSH_MSG_DISCONNECT: 12 Too many concurrent connections (0) - max. allowed: 0
     * @throws InterruptedException
     */
    @Test(expected = JSchException.class)
    public void testRemotePortForwarding() throws JSchException, InterruptedException {
        createSession(serverConfig);
    }


}
