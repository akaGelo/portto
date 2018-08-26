package ru.vyukov.portto.porttoserver;

public interface PortToClient {

    void startForwarding(int port) throws PortToClientException;

    void stopForwardingAll() throws PortToClientException;

    int getRemotePort();

    String getRemoteHost();

    String getRemoteAddress();

}


