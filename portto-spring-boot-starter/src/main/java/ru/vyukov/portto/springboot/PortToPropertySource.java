package ru.vyukov.portto.springboot;

import org.springframework.core.env.PropertySource;
import ru.vyukov.portto.porttoserver.PortToClient;

import static ru.vyukov.portto.springboot.annotations.PortToRemoteAddress.PORTTO_REMOTE_ADDRESS_PROPERTY;
import static ru.vyukov.portto.springboot.annotations.PortToRemoteHost.PORTTO_REMOTE_HOST_PROPERTY;
import static ru.vyukov.portto.springboot.annotations.PortToRemotePort.PORTTO_REMOTE_PORT_PROPERTY;

/**
 * @author Oleg Vyukov
 */
public class PortToPropertySource extends PropertySource<PortToClient> {

    public PortToPropertySource(String name, PortToClient portToClient) {
        super(name, portToClient);
    }

    @Override
    public Object getProperty(String name) {
        switch (name) {
            case PORTTO_REMOTE_PORT_PROPERTY:
                return source.getRemotePort();
            case PORTTO_REMOTE_HOST_PROPERTY:
                return source.getRemoteHost();
            case PORTTO_REMOTE_ADDRESS_PROPERTY:
                return source.getRemoteAddress();
        }
        return null;
    }


}
