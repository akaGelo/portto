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
import org.apache.sshd.common.io.mina.MinaConnector;
import ru.vyukov.portto.porttoserver.ServerConfig;

import java.util.concurrent.ExecutorService;


public class RandomForwardingPortMinaServiceFactory extends AbstractIoServiceFactory {

    private final ServerConfig serverConfig;
    private final IoProcessor<NioSession> ioProcessor;

    public RandomForwardingPortMinaServiceFactory(ServerConfig serverConfig, FactoryManager factoryManager, ExecutorService service) {
        super(factoryManager, service, true);
        this.serverConfig = serverConfig;
        ioProcessor = new SimpleIoProcessorPool<>(NioProcessor.class, service, getNioWorkers(factoryManager), null);
    }

    @Override
    public IoConnector createConnector(IoHandler handler) {
        return new MinaConnector(getFactoryManager(), handler, ioProcessor);
    }

    @Override
    public IoAcceptor createAcceptor(IoHandler handler) {
        return new ConfigurableRandomPortBindMinaAcceptor(serverConfig, getFactoryManager(), handler, ioProcessor);
    }
}