package ru.vyukov.portto.porttoserver.portregistry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.SocketUtils;
import ru.vyukov.portto.porttoserver.config.ServerConfig;

import javax.annotation.PostConstruct;

/**
 * @author Oleg Vyukov
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PortRegistryImpl implements PortRegistry {

    private final ServerConfig config;

    @PostConstruct
    public void init(){
        log.info(">>> Port forwarding range " + config.getForwarding());
    }
}
