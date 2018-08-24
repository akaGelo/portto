package ru.vyukov.portto.porttoserver.sshd;

import org.apache.mina.core.service.IoProcessor;
import org.apache.mina.core.service.SimpleIoProcessorPool;
import org.apache.mina.transport.socket.nio.NioProcessor;
import org.apache.mina.transport.socket.nio.NioSession;
import org.apache.sshd.common.FactoryManager;
import org.apache.sshd.common.io.AbstractIoServiceFactory;
import org.apache.sshd.common.io.IoAcceptor;
import org.apache.sshd.common.io.IoConnector;
import org.apache.sshd.common.io.IoHandler;
import org.apache.sshd.common.io.mina.MinaAcceptor;
import org.apache.sshd.common.io.mina.MinaConnector;
import org.apache.sshd.common.util.threads.ThreadUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;


//@Component
public class MinaServiceFactory extends AbstractIoServiceFactory {

    private final IoProcessor<NioSession> ioProcessor;

    public MinaServiceFactory(FactoryManager factoryManager, ExecutorService service, boolean shutdownOnExit) {
        super(factoryManager,
                (service == null) ? ThreadUtils.newCachedThreadPool(factoryManager.toString() + "-mina") : service,
                (service == null) || shutdownOnExit);
        ioProcessor = new SimpleIoProcessorPool<>(NioProcessor.class, getExecutorService(), getNioWorkers(factoryManager), null);
    }

    @Override
    public IoConnector createConnector(IoHandler handler) {
        return new MinaConnector(getFactoryManager(), handler, ioProcessor);
    }

    @Override
    public IoAcceptor createAcceptor(IoHandler handler) {
        return new MinaAcceptor(getFactoryManager(), handler, ioProcessor){
            @Override
            public void bind(SocketAddress address) throws IOException {
                InetSocketAddress inetSocketAddress = (InetSocketAddress) address;
                int port = inetSocketAddress.getPort();
                if (0==port){
                    port = 2223;
                    inetSocketAddress = new InetSocketAddress(inetSocketAddress.getAddress(),port);
                }
                super.bind(inetSocketAddress);
            }
        };
    }
}