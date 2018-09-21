package ru.vyukov.portto.porttoserver.sshd;

import org.apache.mina.core.service.IoProcessor;
import org.apache.mina.transport.socket.nio.NioSession;
import org.apache.sshd.common.FactoryManager;
import org.apache.sshd.common.io.IoHandler;
import org.apache.sshd.common.io.mina.MinaAcceptor;
import org.springframework.util.SocketUtils;
import ru.vyukov.portto.porttoserver.ServerConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Force random port binding
 *
 * @author Oleg Vyukov
 */
public class ConfigurableRandomPortBindMinaAcceptor extends MinaAcceptor {

    private final ServerConfig serverConfig;

    public ConfigurableRandomPortBindMinaAcceptor(ServerConfig serverConfig, FactoryManager manager, IoHandler handler, IoProcessor<NioSession> ioProcessor) {
        super(manager, handler, ioProcessor);
        this.serverConfig = serverConfig;
    }

    @Override
    public void bind(SocketAddress address) throws IOException {
        InetSocketAddress inetSocketAddress = (InetSocketAddress) address;

        //not random bind only server port. all others force random
        if (isNotServerPort(inetSocketAddress.getPort())) {
            inetSocketAddress = new InetSocketAddress(inetSocketAddress.getAddress(), randomPort());
        }
        super.bind(inetSocketAddress);
    }

    private boolean isNotServerPort(int port) {
        return port != serverConfig.getListenPort();
    }

    /**
     * @return
     * @throws IllegalStateException if no available port could be found
     */
    private int randomPort() throws IllegalStateException {
        ServerConfig.ForwardingConfig forwarding = serverConfig.getForwarding();
        return SocketUtils.findAvailableTcpPort(forwarding.getStartPort(), serverConfig.getForwarding().getEndPort());
    }

}
