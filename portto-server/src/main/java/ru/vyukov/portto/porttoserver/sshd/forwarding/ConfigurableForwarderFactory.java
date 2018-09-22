package ru.vyukov.portto.porttoserver.sshd.forwarding;

import lombok.RequiredArgsConstructor;
import org.apache.sshd.common.forward.DefaultForwarderFactory;
import org.apache.sshd.common.forward.ForwardingFilter;
import org.apache.sshd.common.session.ConnectionService;
import org.springframework.stereotype.Service;
import ru.vyukov.portto.porttoserver.ports.PortsRegistry;

/**
 * @author Oleg Vyukov
 */
@Service
@RequiredArgsConstructor
public class ConfigurableForwarderFactory extends DefaultForwarderFactory {

    private final PortsRegistry portsRegistry;

    @Override
    public ForwardingFilter create(ConnectionService service) {
        ForwardingFilter forwarder = new ConfigurableForwardingFilter(service, portsRegistry);
        forwarder.addPortForwardingEventListenerManager(this);
        return forwarder;
    }
}
