package ru.vyukov.portto.porttoserver.sshd;

import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.common.FactoryManager;
import org.apache.sshd.common.io.*;
import org.apache.sshd.common.io.mina.MinaServiceFactory;
import org.apache.sshd.common.io.mina.MinaServiceFactoryFactory;
import org.apache.sshd.common.util.GenericUtils;
import org.apache.sshd.common.util.threads.ExecutorServiceConfigurer;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.concurrent.ExecutorService;

@Slf4j
public class CustomMinaDefaultIoServiceFactoryFactory extends AbstractIoServiceFactoryFactory {


    private IoServiceFactoryFactory factory;

    @Deprecated
    protected CustomMinaDefaultIoServiceFactoryFactory() {
        this(null, true);
    }

    protected CustomMinaDefaultIoServiceFactoryFactory(ExecutorService executors, boolean shutdownOnExit) {
        super(executors, shutdownOnExit);
    }

    @Override
    public IoServiceFactory create(FactoryManager manager) {
        IoServiceFactoryFactory factoryInstance = getIoServiceProvider();
        return factoryInstance.create(manager);
    }

    /**
     * @return The actual {@link IoServiceFactoryFactory} being delegated
     */
    public IoServiceFactoryFactory getIoServiceProvider() {
        synchronized (this) {
            if (factory == null) {
                factory = newInstance(IoServiceFactoryFactory.class);
                if (factory instanceof ExecutorServiceConfigurer) {
                    ExecutorServiceConfigurer configurer = (ExecutorServiceConfigurer) factory;
                    configurer.setExecutorService(getExecutorService());
                    configurer.setShutdownOnExit(isShutdownOnExit());
                }
            }
        }
        return factory;
    }


    public static <T extends IoServiceFactoryFactory> MinaServiceFactoryFactory newInstance(Class<T> clazz) {
        return new MinaServiceFactoryFactory() {
            @Override
            public IoServiceFactory create(FactoryManager manager) {
                return new ru.vyukov.portto.porttoserver.sshd.MinaServiceFactory(manager, getExecutorService(), isShutdownOnExit());
            }
        };
    }


}
