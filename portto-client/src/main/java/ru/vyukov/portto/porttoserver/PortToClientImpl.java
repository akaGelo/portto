package ru.vyukov.portto.porttoserver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Oleg Vyukov
 */
@Slf4j
@RequiredArgsConstructor
public class PortToClientImpl implements PortToClient {

    private final PortToClientConfig portToClientConfig;
    private boolean isStarted = true;


    @Override
    public void startForwarding(int port) {
        log.warn("Port forwarding");
    }

    @Override
    public void stopForwardingAll() {
        log.warn("Port forwarding stopped");
    }

    @Override
    public int getRemotePort() {
        isStartedCheck();
        return 22;
    }


    @Override
    public String getRemoteHost() {
        isStartedCheck();
        return "localhost";
    }

    @Override
    public String getRemoteAddress() {
        return getRemoteHost() + ":" + getRemotePort();
    }

    private void isStartedCheck() {
        if (!isStarted) {
            throw new IllegalStateException("PortTo Client not started. WebServerInitializedEvent");
        }
    }

}
