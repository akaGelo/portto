package ru.vyukov.portto.porttoserver.sshd;

import lombok.RequiredArgsConstructor;
import org.apache.sshd.common.forward.DefaultForwarderFactory;
import org.apache.sshd.common.forward.DefaultForwardingFilter;
import org.apache.sshd.common.forward.ForwardingFilter;
import org.apache.sshd.common.session.ConnectionService;
import org.springframework.stereotype.Service;
import ru.vyukov.portto.porttoserver.config.ServerConfig;

/**
 * @author Oleg Vyukov
 */
@Service
@RequiredArgsConstructor
public class ConfigurableForwarderFactory extends DefaultForwarderFactory {

    private final ServerConfig serverConfig;


    @Override
    public ForwardingFilter create(ConnectionService service) {
        ForwardingFilter forwarder = new ConfigurableForwardingFilter(service, serverConfig.getForwarding());
        forwarder.addPortForwardingEventListenerManager(this);
        return forwarder;
    }


}
