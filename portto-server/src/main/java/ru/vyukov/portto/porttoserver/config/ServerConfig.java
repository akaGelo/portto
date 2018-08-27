package ru.vyukov.portto.porttoserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Oleg Vyukov
 */
@Data
@Component
@ConfigurationProperties("sshd")
public class ServerConfig {

    private int listenPort = 2222;

    private String listenInterface = "0.0.0.0";


    private boolean allowAnyPassword = true;


    private ForwardingConfig forwarding;

    @Data
    public static class ForwardingConfig {

        private int startPort;

        private int endPort;
    }
}
