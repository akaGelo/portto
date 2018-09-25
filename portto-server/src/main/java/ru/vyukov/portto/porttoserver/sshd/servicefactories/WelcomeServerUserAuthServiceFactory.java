package ru.vyukov.portto.porttoserver.sshd.servicefactories;

import lombok.RequiredArgsConstructor;
import org.apache.sshd.common.Service;
import org.apache.sshd.common.session.Session;
import org.apache.sshd.server.session.ServerUserAuthServiceFactory;
import org.springframework.stereotype.Component;
import ru.vyukov.portto.porttoserver.ServerConfig;
import ru.vyukov.portto.porttoserver.ports.PortsRegistry;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class WelcomeServerUserAuthServiceFactory extends ServerUserAuthServiceFactory {

    private final ServerConfig serverConfig;

    private final PortsRegistry portRegistry;

    @Override
    public Service create(Session session) throws IOException {
        return new WelcomeBannerServerUserAuthService(session, portRegistry, serverConfig);
    }
}
