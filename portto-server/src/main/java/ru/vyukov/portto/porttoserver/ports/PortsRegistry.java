package ru.vyukov.portto.porttoserver.ports;

/**
 * @author Oleg Vyukov
 */
public interface PortsRegistry {

    int pullRandomPort() throws NoPortException;

    public void retrievePort(int port);

    int getFreePorts();
}
