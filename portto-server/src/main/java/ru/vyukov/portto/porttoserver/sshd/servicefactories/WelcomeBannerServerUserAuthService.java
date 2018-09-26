package ru.vyukov.portto.porttoserver.sshd.servicefactories;

import org.apache.sshd.common.session.Session;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.session.ServerUserAuthService;
import ru.vyukov.portto.porttoserver.ServerConfig;
import ru.vyukov.portto.porttoserver.ports.PortsRegistry;

import java.io.IOException;

public class WelcomeBannerServerUserAuthService extends ServerUserAuthService {
    private final PortsRegistry portsRegistry;

    private final ServerConfig config;

    public WelcomeBannerServerUserAuthService(Session s, PortsRegistry portsRegistry, ServerConfig config) throws IOException {
        super(s);
        this.portsRegistry = portsRegistry;
        this.config = config;
    }

    @Override
    protected String resolveWelcomeBanner(ServerSession session) {
        String authorization = config.isAllowAnyPassword() ? "anonymous" : "ssh keys and passwords";
        int maxConnections = portsRegistry.getTotalPorts();
        int availableConnections = portsRegistry.getFreePorts();

        StringBuilder builder = new StringBuilder("\n\nWelcome to portto server!\n");
        builder.append("\nSupport authorization: ").append(authorization);
        builder.append("\nMaximum connections: ").append(maxConnections);
        builder.append("\nAvailable connections: ").append(availableConnections);

        builder.append("\n\n");

        return builder.toString();

    }

}
