package ru.vyukov.portto.springboot.converters;

import org.springframework.core.convert.converter.Converter;

import java.net.InetSocketAddress;

/**
 * @author Oleg Vyukov
 */
public class InetSocketAddressToStringConverter implements Converter<InetSocketAddress, String> {
    @Override
    public String convert(InetSocketAddress source) {
        return source.toString();
    }
}
