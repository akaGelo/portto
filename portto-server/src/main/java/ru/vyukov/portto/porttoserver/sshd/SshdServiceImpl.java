package ru.vyukov.portto.porttoserver.sshd;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.common.io.IoServiceFactoryFactory;
import org.apache.sshd.common.keyprovider.ClassLoadableResourceKeyPairProvider;
import org.apache.sshd.server.ServerBuilder;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.config.keys.AuthorizedKeysAuthenticator;
import org.apache.sshd.server.forward.AcceptAllForwardingFilter;
import org.apache.sshd.server.session.SessionFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.vyukov.portto.porttoserver.ServerConfig;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author Oleg Vyukov
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SshdServiceImpl implements SshdService {

    private final ServerConfig config;
    private final IoServiceFactoryFactory ioServiceFactory;


    private SshServer sshd;


    @PostConstruct
    public void init() {
        log.info(">>>> SSHd listen {}:{}", config.getListenInterface(), config.getListenPort());
        log.info(">>>> Forwarding range {}-{}", config.getForwarding().getStartPort(), config.getForwarding().getEndPort());
    }


    @EventListener(ApplicationReadyEvent.class)
    public void startSshd() throws IOException {
        sshd = ServerBuilder.builder().build();
        sshd.setPort(config.getListenPort());
        sshd.setHost(config.getListenInterface());


        sshd.setIoServiceFactoryFactory(ioServiceFactory);

        sshd.setSessionFactory(new SessionFactory(sshd));

        sshd.setForwardingFilter(AcceptAllForwardingFilter.INSTANCE);
        sshd.setKeyPairProvider(new ClassLoadableResourceKeyPairProvider("server_key_pair.pem"));


        if (config.isAllowAnyPassword()) {
            sshd.setPublickeyAuthenticator((username, key, session) -> true);
            sshd.setPasswordAuthenticator((username, password, session) -> true);
            log.info(">>>> SSHd allow anonymous connections");
        } else {
            sshd.setPublickeyAuthenticator(new AuthorizedKeysAuthenticator(config.getAuthorizedKeysPath()));
            log.info(">>>> SSHd AuthorizedKeys authorization");
        }

        sshd.start();
    }


}
