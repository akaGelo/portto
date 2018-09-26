package ru.vyukov.portto.porttoserver.ports;

import org.junit.Test;
import ru.vyukov.portto.porttoserver.PorttoServerApplicationTests;
import ru.vyukov.portto.porttoserver.ServerConfig;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

import static org.junit.Assert.*;
import static ru.vyukov.portto.porttoserver.PorttoServerApplicationTests.bindPorts;

/**
 * @author Oleg Vyukov
 */
public class PortsRegistryImplTest {

    @Test(expected = NoPortException.class)
    public void noPortTest() throws IOException {
        ServerConfig serverConfig = testForwardingConfig(1024, 1054);
        ServerConfig.ForwardingConfig forwarding = serverConfig.getForwarding();

        List<ServerSocket> busyPorts = bindPorts(forwarding.getMinPort(), forwarding.getMaxPort());

        try {
            PortsRegistryImpl portsRegistry = new PortsRegistryImpl(serverConfig);
            portsRegistry.pullRandomPort();
        }finally {
            PorttoServerApplicationTests.closeServerSocets(busyPorts);
        }
    }


    @Test
    public void retrievePortTest() throws NoPortException {
        PortsRegistryImpl portsRegistry = new PortsRegistryImpl(testForwardingConfig(1024, 2048));
        int freePortsBeforeTest = portsRegistry.getFreePorts();

        final int port = portsRegistry.pullRandomPort();
        int freePortsAfterPull = portsRegistry.getFreePorts();

        assertEquals(freePortsBeforeTest, freePortsAfterPull + 1);

        portsRegistry.retrievePort(port);
        int freePortsAfterTest = portsRegistry.getFreePorts();
        assertEquals(freePortsBeforeTest, freePortsAfterTest);

    }

    private ServerConfig testForwardingConfig(int minPort, int maxPort) {
        ServerConfig.ForwardingConfig forwardingConfig = new ServerConfig.ForwardingConfig();
        forwardingConfig.setMinPort(minPort);
        forwardingConfig.setMaxPort(maxPort);

        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setForwarding(forwardingConfig);
        return serverConfig;
    }
}