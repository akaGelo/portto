package ru.vyukov.portto.porttoserver.ports;

import org.junit.Test;
import ru.vyukov.portto.porttoserver.ServerConfig;

import static org.junit.Assert.*;

/**
 * @author Oleg Vyukov
 */
public class PortsRegistryImplTest {

    @Test(expected = NoPortException.class)
    public void noPortTest() throws NoPortException {
        PortsRegistryImpl portsRegistry = new PortsRegistryImpl(testForwardingConfig(80, 90));
        portsRegistry.pullRandomPort();
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
        assertEquals(freePortsBeforeTest,freePortsAfterTest);

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