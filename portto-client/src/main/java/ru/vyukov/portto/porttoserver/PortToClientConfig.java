package ru.vyukov.portto.porttoserver;

import lombok.Data;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

/**
 * @author Oleg Vyukov
 */
@Data
public class PortToClientConfig {

    private List<InetSocketAddress> serversList = Arrays.asList(new InetSocketAddress("portto.vyukov.ru", 3222));

    private String userName = "portto";

    private String password = "qwerty";




}
