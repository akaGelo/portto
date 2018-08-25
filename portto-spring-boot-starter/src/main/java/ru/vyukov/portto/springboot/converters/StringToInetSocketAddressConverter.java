package ru.vyukov.portto.springboot.converters;

import org.springframework.core.convert.converter.Converter;

import java.net.InetSocketAddress;

/**
 * @author Oleg Vyukov
 */
public class StringToInetSocketAddressConverter implements Converter<String, InetSocketAddress> {
    @Override
    public InetSocketAddress convert(String source) {
        String[] parts = source.split(":");
        if (2 != parts.length) {
            throw new IllegalArgumentException("wrong host format [" + source + "]");
        }
        String host = parts[0];
        int port = Integer.parseInt(parts[1]);
        return new InetSocketAddress(host, port);
    }
}
