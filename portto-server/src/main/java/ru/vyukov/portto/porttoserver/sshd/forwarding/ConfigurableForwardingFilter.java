package ru.vyukov.portto.porttoserver.sshd.forwarding;

import org.apache.sshd.common.forward.DefaultForwardingFilter;
import org.apache.sshd.common.session.ConnectionService;
import org.apache.sshd.common.session.Session;
import org.apache.sshd.common.session.SessionListener;
import org.apache.sshd.common.util.net.SshdSocketAddress;
import ru.vyukov.portto.porttoserver.ports.NoPortException;
import ru.vyukov.portto.porttoserver.ports.PortsRegistry;

import java.io.IOException;

/**
 * @author Oleg Vyukov
 */
public class ConfigurableForwardingFilter extends DefaultForwardingFilter {


    private final PortsRegistry portsRegistry;


    public ConfigurableForwardingFilter(ConnectionService service, PortsRegistry portsRegistry) {
        super(service);
        this.portsRegistry = portsRegistry;
    }

    @Override
    public synchronized SshdSocketAddress getForwardedPort(int remotePort) {
        return super.getForwardedPort(remotePort);
    }


    @Override
    public synchronized SshdSocketAddress localPortForwardingRequested(SshdSocketAddress local) throws IOException, NoPortException {
        int port = portsRegistry.pullRandomPort();
        SshdSocketAddress address = new SshdSocketAddress(local.getHostName(), port);
        return super.localPortForwardingRequested(address);
    }

    @Override
    protected void signalEstablishedExplicitTunnel(SshdSocketAddress local, SshdSocketAddress remote, boolean localForwarding, SshdSocketAddress boundAddress, Throwable reason) throws IOException {
        Session session = getSession();
        session.addSessionListener(new SessionListener() {
            @Override
            public void sessionClosed(Session session) {
                int port = boundAddress.getPort();
                portsRegistry.retrievePort(port);
            }
        });
        super.signalEstablishedExplicitTunnel(local, remote, localForwarding, boundAddress, reason);
    }


}
