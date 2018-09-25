package ru.vyukov.portto.porttoserver;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vyukov.portto.porttoserver.ports.PortsRegistry;

import static org.junit.Assert.*;
import static ru.vyukov.portto.porttoserver.PorttoServerApplicationTests.createSession;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties =
        {
                "sshd.listen-port=62227",
                "sshd.forwarding.min-port=62228",
                "sshd.forwarding.max-port=62229"
        })
@DirtiesContext
public class NoPortTest {


    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private PortsRegistry portsRegistry;


    private Session session;



    @Before
    public void setUp() throws Exception {
        assertTrue(serverConfig.isAllowAnyPassword());
        assertEquals(2, portsRegistry.getTotalPorts());

        session = createSession(serverConfig);
    }


    @After
    public void tearDown() throws Exception {
        if (null != session) {
            session.disconnect();
        }
    }

    /**
     * @throws JSchException        com.jcraft.jsch.JSchException: SSH_MSG_DISCONNECT: 12 Too many concurrent connections (0) - max. allowed: 0
     * @throws InterruptedException
     */
    @Test(expected = JSchException.class)
    public void testNoPortRemotePortForwarding() throws JSchException, InterruptedException {
        final int twoPortsAvailable = 2;


        session.setPortForwardingR(0, "localhost", 80);
        assertEquals(1, session.getPortForwardingR().length);

        session.setPortForwardingR(0, "localhost", 81);
        assertEquals(twoPortsAvailable, session.getPortForwardingR().length);

        session.setPortForwardingR(0, "localhost", 5432);
        assertEquals(twoPortsAvailable, session.getPortForwardingR().length);
    }


    @Test
    public void testParallelSessions() throws JSchException, InterruptedException {
        Session secondSessions = createSession(serverConfig);
        assertTrue(secondSessions.isConnected());


        assertTrue(session.isConnected());
    }

}
