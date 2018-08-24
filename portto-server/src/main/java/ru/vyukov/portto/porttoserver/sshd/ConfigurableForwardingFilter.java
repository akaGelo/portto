package ru.vyukov.portto.porttoserver.sshd;

import org.apache.sshd.common.forward.DefaultForwardingFilter;
import org.apache.sshd.common.session.ConnectionService;
import org.apache.sshd.common.util.net.SshdSocketAddress;
import ru.vyukov.portto.porttoserver.config.ServerConfig;

import java.io.IOException;

/**
 * @author Oleg Vyukov
 */
public class ConfigurableForwardingFilter extends DefaultForwardingFilter {

    private final ServerConfig.ForwardingConfig forwardingConfig;

    public ConfigurableForwardingFilter(ConnectionService service, ServerConfig.ForwardingConfig forwardingConfig) {
        super(service);
        this.forwardingConfig = forwardingConfig;

    }


    @Override
    public synchronized SshdSocketAddress localPortForwardingRequested(SshdSocketAddress local) throws IOException {
        return super.localPortForwardingRequested(local);
    }

    @Override
    public synchronized void localPortForwardingCancelled(SshdSocketAddress local) throws IOException {
        super.localPortForwardingCancelled(local);
    }
}
