package ru.vyukov.portto.springboot;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.vyukov.portto.porttoserver.PortToClient;

/**
 * @author Oleg Vyukov
 */
@Component
@RequiredArgsConstructor
class PortToClientAutoStarterApplicationListener {

    private final PortToClient portToClient;

    @EventListener(WebServerInitializedEvent.class)
    public void onWebServerInitializedEvent(WebServerInitializedEvent event) {
        int port = event.getWebServer().getPort();
        portToClient.startForwarding(port);
    }


    @EventListener(ContextClosedEvent.class)
    public void onContextClosedEvent(ContextClosedEvent event) {
        portToClient.stopForwardingAll();
    }

}