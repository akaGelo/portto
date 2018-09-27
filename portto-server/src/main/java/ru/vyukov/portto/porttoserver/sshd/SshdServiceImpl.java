package ru.vyukov.portto.porttoserver.sshd;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.common.forward.ForwardingFilterFactory;
import org.apache.sshd.common.keyprovider.ClassLoadableResourceKeyPairProvider;
import org.apache.sshd.common.session.helpers.AbstractSession;
import org.apache.sshd.server.ServerAuthenticationManager;
import org.apache.sshd.server.ServerBuilder;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.command.CommandFactory;
import org.apache.sshd.server.config.keys.AuthorizedKeysAuthenticator;
import org.apache.sshd.server.forward.AcceptAllForwardingFilter;
import org.apache.sshd.server.session.ServerConnectionServiceFactory;
import org.apache.sshd.server.session.ServerUserAuthServiceFactory;
import org.apache.sshd.server.session.SessionFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.vyukov.portto.porttoserver.ServerConfig;
import ru.vyukov.portto.porttoserver.ports.PortsRegistry;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static java.util.Arrays.asList;
import static lombok.AccessLevel.PROTECTED;

/**
 * @author Oleg Vyukov
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SshdServiceImpl implements SshdService {

    private final ServerConfig config;
    private final PortsRegistry portsRegistry;
    private final CommandFactory commandFactory;
    private final ForwardingFilterFactory forwarderFactory;

    private final ServerUserAuthServiceFactory serverUserAuthServiceFactory;
    private final ServerConnectionServiceFactory serverConnectionServiceFactory;


    @Setter(PROTECTED)
    private SshServer sshd;


    @PostConstruct
    public void init(){
        sshd = ServerBuilder.builder().build();
    }

    @PreDestroy
    public void destroy() throws IOException {
        if (null != sshd) {
            for (AbstractSession session : sshd.getActiveSessions()) {
                session.close();
            }
            sshd.stop(true);
        }
    }


    @EventListener(ApplicationReadyEvent.class)
    public void startSshd() throws IOException {
        sshd.setPort(config.getListenPort());
        sshd.setHost(config.getListenInterface());

        setProperties(sshd);

        sshd.setCommandFactory(commandFactory);

        sshd.setForwarderFactory(forwarderFactory);

        sshd.setSessionFactory(new SessionFactory(sshd));

        sshd.setForwardingFilter(AcceptAllForwardingFilter.INSTANCE);


        sshd.setKeyPairProvider(new ClassLoadableResourceKeyPairProvider("server_key_pair.pem"));


        sshd.setServiceFactories(asList(serverUserAuthServiceFactory, serverConnectionServiceFactory));

        authConfig();

        sshd.start();

        log.info(">>>> SSHd listen {}:{}", config.getListenInterface(), config.getListenPort());
        log.info(">>>> Forwarding range {}-{}", config.getForwarding().getMinPort(), config.getForwarding().getMaxPort());
    }


    private void setProperties(SshServer sshd) {
        this.sshd.getProperties().put(SshServer.IDLE_TIMEOUT, config.getIdleTimeoutMs());
        this.sshd.getProperties().put(SshServer.MAX_CONCURRENT_SESSIONS, portsRegistry.getTotalPorts());
    }


    private void authConfig() {
        if (config.isAllowAnyPassword()) {
            sshd.setPublickeyAuthenticator((username, key, session) -> true);
            sshd.setPasswordAuthenticator((username, password, session) -> true);
            log.info(">>>> SSHd allow anonymous connections");
        } else {
            sshd.setPublickeyAuthenticator(new AuthorizedKeysAuthenticator(config.getAuthorizedKeysPath()));
            log.info(">>>> SSHd AuthorizedKeys authorization");
        }
    }

}
