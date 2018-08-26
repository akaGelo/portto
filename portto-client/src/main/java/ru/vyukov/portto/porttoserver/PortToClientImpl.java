package ru.vyukov.portto.porttoserver;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Oleg Vyukov
 */
@Slf4j
public class PortToClientImpl implements PortToClient {

    static {
        JSch.setLogger(new Slf4jBridge(log));
    }

    private final PortToClientConfig config;

    private final JSch sch;

    private Session session;

    private int remotePort;

    public PortToClientImpl(PortToClientConfig config) {
        this.config = config;
        this.sch = new JSch();
    }


    @Override
    public void startForwarding(int port) throws PortToClientException, IllegalStateException {
        if (!notStarted()) {
            throw new IllegalStateException("Already started");
        }
        connect(port);
        try {
            session.setPortForwardingR(0, "localhost", port);
            remotePort = extractRemotePort(session.getPortForwardingR());
        } catch (JSchException e) {
            throw new PortToClientException("Port forwarding problem", e);
        }
    }

    /**
     * @param portForwardingR a list of "rport:host:hostport"
     * @return
     */
    private int extractRemotePort(String[] portForwardingR) {
        if (1 != portForwardingR.length) {
            throw new IllegalArgumentException("Allow only one port forwarding");
        }
        String[] parts = portForwardingR[0].split(":");
        return Integer.parseInt(parts[0]);
    }

    private void connect(int port) throws PortToClientException {
        ArrayList<InetSocketAddress> serverListCopy = new ArrayList<>(config.getServersList());
        Collections.shuffle(serverListCopy);
        for (InetSocketAddress serverAddress : serverListCopy) {
            try {
                session = startForwarding(serverAddress, port);
                return;

            } catch (JSchException e) {
                log.warn("Server " + serverAddress + " problem: " + e.getMessage());
            } catch (Exception e) {
                //TODO stackTraces
                log.warn("Server " + serverAddress + " problem", e);
            }
        }
        throw new PortToClientException("No available server");
    }


    private Session startForwarding(InetSocketAddress serverAddress, int port) throws JSchException {
        Session session = sch.getSession(config.getUserName(), serverAddress.getHostString(), serverAddress.getPort());
        session.setUserInfo(new ConfiguredUserInfo(config));
        session.connect();
        return session;
    }

    @Override
    public void stopForwardingAll() throws PortToClientException {
        isStartedCheck();
        try {
            session.delPortForwardingR(remotePort);
        } catch (JSchException e) {
            throw new PortToClientException("Port forwarding not stoped", e);
        }
    }

    @Override
    public int getRemotePort() {
        isStartedCheck();
        return remotePort;
    }


    @Override
    public String getRemoteHost() {
        isStartedCheck();
        return session.getHost();
    }

    @Override
    public String getRemoteAddress() {
        return getRemoteHost() + ":" + getRemotePort();
    }

    private void isStartedCheck() {
        if (notStarted()) {
            throw new IllegalStateException("PortTo Client not started or closed");
        }
    }

    private boolean notStarted() {
        return null == session || !session.isConnected();
    }

}
