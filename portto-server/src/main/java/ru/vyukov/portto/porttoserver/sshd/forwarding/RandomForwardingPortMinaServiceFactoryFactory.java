package ru.vyukov.portto.porttoserver.sshd;

import org.apache.sshd.common.FactoryManager;
import org.apache.sshd.common.io.IoServiceFactory;
import org.apache.sshd.common.io.mina.MinaServiceFactoryFactory;
import org.springframework.stereotype.Component;
import ru.vyukov.portto.porttoserver.ServerConfig;

import java.util.concurrent.ExecutorService;

/**
 * @author Oleg Vyukov
 */

@Component
public class RandomForwardingPortMinaServiceFactoryFactory extends MinaServiceFactoryFactory {


    private final ServerConfig serverConfig;

    public RandomForwardingPortMinaServiceFactoryFactory(ServerConfig serverConfig, ExecutorService executors) {
        super(executors, true);
        this.serverConfig = serverConfig;
    }

    @Override
    public IoServiceFactory create(FactoryManager manager) {
        return new RandomForwardingPortMinaServiceFactory(serverConfig, manager, getExecutorService());
    }
}