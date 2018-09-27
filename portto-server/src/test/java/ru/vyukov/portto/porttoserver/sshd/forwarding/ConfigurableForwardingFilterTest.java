package ru.vyukov.portto.porttoserver.sshd.forwarding;

import org.apache.sshd.common.FactoryManager;
import org.apache.sshd.common.session.ConnectionService;
import org.apache.sshd.common.session.Session;
import org.apache.sshd.common.util.net.SshdSocketAddress;
import org.apache.sshd.server.forward.TcpForwardingFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.vyukov.portto.porttoserver.ports.PortsRegistry;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurableForwardingFilterTest {


    @Mock
    private ConnectionService connectionService;

    @Mock
    private PortsRegistry portsRegistry;

    @Mock
    private Session session;

    @Captor
    private ArgumentCaptor<SshdSocketAddress> canListen;


    private ConfigurableForwardingFilter underTest;

    @Before
    public void setUp() throws Exception {
        when(connectionService.getSession()).thenReturn(session);

        underTest = new ConfigurableForwardingFilter(connectionService, portsRegistry);

        when(portsRegistry.pullRandomPort()).thenReturn(33333);
    }

    @Test
    public void localPortForwardingRequested() throws IOException {
        FactoryManager factoryManager = mock(FactoryManager.class);
        when(session.getFactoryManager()).thenReturn(factoryManager);
        TcpForwardingFilter filter = mock(TcpForwardingFilter.class);
        when(factoryManager.getTcpForwardingFilter()).thenReturn(filter);

        SshdSocketAddress local = new SshdSocketAddress("localhost", 8080);
        underTest.localPortForwardingRequested(local);

        verify(filter).canListen(canListen.capture(), any(Session.class));
        SshdSocketAddress canListenAddrss = canListen.getValue();
        assertEquals("0.0.0.0", canListenAddrss.getHostName());
        assertEquals(33333, canListenAddrss.getPort());

    }

    @Test
    public void signalEstablishedExplicitTunnel() {
    }
}