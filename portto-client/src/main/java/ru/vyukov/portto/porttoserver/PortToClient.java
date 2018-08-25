package ru.vyukov.portto.porttoserver;

public interface PortToClient {

    void startForwarding(int port);

    void stopForwardingAll();

    int getRemotePort();

    String getRemoteHost();

    String getRemoteAddress();

}
