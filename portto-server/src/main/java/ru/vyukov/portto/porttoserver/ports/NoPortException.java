package ru.vyukov.portto.porttoserver.ports;

import java.io.IOException;

/**
 * @author Oleg Vyukov
 */
public class NoPortException extends IOException {
    public NoPortException(Exception e) {
        super(e);
    }
}
