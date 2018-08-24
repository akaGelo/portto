package ru.vyukov.portto.porttoserver.sshd;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.common.forward.ForwardingFilterFactory;
import org.apache.sshd.common.io.IoServiceFactoryFactory;
import org.apache.sshd.server.ServerBuilder;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.forward.AcceptAllForwardingFilter;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.SessionFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.vyukov.portto.porttoserver.config.ServerConfig;

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
    private final ForwardingFilterFactory forwarderFactory;


    private SshServer sshd;


    @PostConstruct
    public void init() {
        log.info(">>>> SSHd listen {}:{}", config.getListenInterface(), config.getListenPort());
    }


    @EventListener(ApplicationReadyEvent.class)
    public void startSshd() throws IOException {
        sshd = ServerBuilder.builder()
                .build();
        sshd.setPort(config.getListenPort());
        sshd.setHost(config.getListenInterface());


        if (config.isAllowAnyPassword()) {
            sshd.setPasswordAuthenticator((username, password, session) -> true);
        }

        IoServiceFactoryFactory ioServiceFactory = new CustomMinaDefaultIoServiceFactoryFactory();

        sshd.setIoServiceFactoryFactory(ioServiceFactory);

        sshd.setSessionFactory(new SessionFactory(sshd));

        sshd.setForwarderFactory(forwarderFactory);

        sshd.setForwardingFilter(AcceptAllForwardingFilter.INSTANCE);
        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());

        sshd.setPublickeyAuthenticator((username, key, session) -> true);

        sshd.start();
    }


}
