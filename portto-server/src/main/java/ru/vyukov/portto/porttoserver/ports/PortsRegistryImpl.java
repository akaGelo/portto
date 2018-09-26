package ru.vyukov.portto.porttoserver.ports;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.vyukov.portto.porttoserver.ServerConfig;
import ru.vyukov.portto.porttoserver.ServerConfig.ForwardingConfig;

import javax.net.ServerSocketFactory;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Oleg Vyukov
 */

@Slf4j
@Service
public class PortsRegistryImpl implements PortsRegistry {

    private final ServerConfig serverConfig;

    private final BlockingQueue<Integer> ports;
    private final int totalPorts;

    public PortsRegistryImpl(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
        this.ports = new LinkedBlockingQueue<>(findAllowedPorts());
        this.totalPorts = ports.size();
    }

    private List<Integer> findAllowedPorts() {
        ForwardingConfig forwarding = serverConfig.getForwarding();
        int minPort = forwarding.getMinPort();
        int maxPort = forwarding.getMaxPort();
        int endPortExclusive = maxPort + 1;

        List<Integer> allowedPorts = IntStream.range(minPort, endPortExclusive).filter(PortsRegistryImpl::isPortAvailable).boxed().collect(Collectors.toList());

        allowedPorts.remove((Integer) serverConfig.getListenPort());

        Collections.shuffle(allowedPorts);

        return allowedPorts;
    }


    @Scheduled(fixedDelayString = "${sshd.print-stats-interval}", initialDelay = 10_000)
    public void printStats() {
        log.info("Free ports: {} of {}", getFreePorts(), getTotalPorts());
    }


    @Override
    public int pullRandomPort() throws NoPortException {
        try {
            return ports.remove();
        } catch (NoSuchElementException e) {
            throw new NoPortException(e);
        }
    }

    @Override
    public void retrievePort(int port) {
        ports.add(port);
    }

    @Override
    public int getFreePorts() {
        return ports.size();
    }

    @Override
    public int getTotalPorts() {
        return totalPorts;
    }

    private static boolean isPortAvailable(int port) {
        try {
            ServerSocket serverSocket = ServerSocketFactory.getDefault().createServerSocket(
                    port, 1, InetAddress.getByName("localhost"));
            serverSocket.close();
            return true;
        } catch (Exception ex) {
            log.warn("Port {} not available", port);
            return false;
        }
    }

}
