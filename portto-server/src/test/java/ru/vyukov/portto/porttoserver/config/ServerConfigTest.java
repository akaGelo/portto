package ru.vyukov.portto.porttoserver.config;

import org.junit.Test;
import ru.vyukov.portto.porttoserver.ServerConfig;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Oleg Vyukov
 */
public class ServerConfigTest {

    @Test
    public void isAllowAnyPassword() {
        ServerConfig serverConfig = new ServerConfig();
        assertTrue(serverConfig.isAllowAnyPassword());

        serverConfig.setAuthorizedKeysPath("/tmp/path");
        assertFalse(serverConfig.isAllowAnyPassword());
    }


    @Test(expected = IllegalStateException.class)
    public void keysConfigConstraintTest() {
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setAuthorizedKeysPath("/tmp/path");
        serverConfig.setAuthorizedKeys("keys body");
    }

    @Test
    public void getAuthorizedKeysPath() throws IOException {
        final String keyBody = "key body";

        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setAuthorizedKeys(keyBody);

        Path authorizedKeysPathOne = serverConfig.getAuthorizedKeysPath();
        Path authorizedKeysPathTwo = serverConfig.getAuthorizedKeysPath();

        assertEquals(authorizedKeysPathOne, authorizedKeysPathTwo);

        List<String> strings = Files.readAllLines(authorizedKeysPathOne, Charset.defaultCharset());
        assertEquals(keyBody, strings.get(0));

    }
}