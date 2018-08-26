package ru.vyukov.portto.porttoserver;

import com.jcraft.jsch.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Oleg Vyukov
 */
@Slf4j
@RequiredArgsConstructor
public class ConfiguredUserInfo implements UserInfo {

    private final PortToClientConfig config;

    public String getPassphrase() {
        return null;
    }

    public String getPassword() {
        return config.getPassword();
    }

    public boolean promptPassword(String message) {
        return true;
    }

    public boolean promptPassphrase(String message) {
        return false;
    }

    public boolean promptYesNo(String message) {
        return true;
    }

    public void showMessage(String message) {
        log.info(message);
    }
}
