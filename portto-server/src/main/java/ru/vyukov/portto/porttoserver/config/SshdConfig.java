package ru.vyukov.portto.porttoserver.config;

import lombok.RequiredArgsConstructor;
import org.apache.sshd.common.util.threads.ThreadUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.vyukov.portto.porttoserver.ServerConfig;

import java.util.concurrent.ExecutorService;

/**
 * @author Oleg Vyukov
 */
@Configuration
@RequiredArgsConstructor
public class SshdConfig {


    final private ServerConfig serverConfig;

    @Bean
    ExecutorService executorService() {
        return ThreadUtils.newCachedThreadPool("portto");
    }


}
