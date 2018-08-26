package ru.vyukov.portto.porttoserver;

import com.jcraft.jsch.JSchException;

/**
 * @author Oleg Vyukov
 */
public class PortToClientException extends Exception {
    public PortToClientException(String message) {
        super(message);
    }

    public PortToClientException(String message, JSchException e) {
        super(message, e);
    }
}
