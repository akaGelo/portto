package ru.vyukov.portto.porttoserver;

import com.jcraft.jsch.JSchException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vyukov.portto.porttoserver.ports.PortsRegistry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.vyukov.portto.porttoserver.PorttoServerApplicationTests.createSession;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties =
        {
                "sshd.listen-port=52229",
                "sshd.forwarding.min-port=52429",
                "sshd.forwarding.max-port=52429"
        })
@DirtiesContext
public class MaxConnectionsTest {


    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private PortsRegistry portsRegistry;

    @Before
    public void setUp() throws Exception {
        assertTrue(serverConfig.isAllowAnyPassword());
        assertEquals(1, portsRegistry.getTotalPorts());
    }


    /**
     * @throws JSchException        com.jcraft.jsch.JSchException: SSH_MSG_DISCONNECT: 12 Too many concurrent connections (0) - max. allowed: 0
     * @throws InterruptedException
     */
    @Test(expected = JSchException.class)
    public void twoSessionExceptionTest() throws JSchException, InterruptedException {
        createSession(serverConfig);

        createSession(serverConfig);
    }

    @Test
    public void oneSessionTest() throws JSchException, InterruptedException {
        createSession(serverConfig);
    }


}
