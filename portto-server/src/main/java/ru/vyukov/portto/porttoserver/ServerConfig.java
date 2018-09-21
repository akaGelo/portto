package ru.vyukov.portto.porttoserver;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Oleg Vyukov
 */
@Data
@EqualsAndHashCode(doNotUseGetters = true)
@Component
@ConfigurationProperties("sshd")
@Validated
public class ServerConfig {

    @Min(1024)
    private int listenPort;

    @NotNull
    private String listenInterface;

    @NotNull
    private ForwardingConfig forwarding;


    private String authorizedKeysPath;

    /**
     * Content
     */
    private String authorizedKeys;


    public boolean isAllowAnyPassword() {
        return null == authorizedKeysPath && null == authorizedKeys;
    }


    public Path getAuthorizedKeysPath() {
        if (null != authorizedKeysPath) {
            return Paths.get(authorizedKeysPath);
        }

        if (Strings.isEmpty(authorizedKeys)) {
            throw new IllegalStateException("AuthorizedKeys not configured");
        }

        return writeAuthorizedKeys();
    }


    synchronized private Path writeAuthorizedKeys() {
        try {
            Path authorizedKeysTmp = Files.createTempFile("authorized_keys", "");
            Files.write(authorizedKeysTmp, authorizedKeys.getBytes());
            this.authorizedKeysPath = authorizedKeysTmp.toAbsolutePath().toString();
            return authorizedKeysTmp;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public void setAuthorizedKeys(String authorizedKeys) {
        if (null != this.authorizedKeysPath) {
            throw new IllegalStateException("authorizedKeysPath already configured. Only authorizedKeys or authorizedKeysPath");
        }
        this.authorizedKeys = authorizedKeys;
    }

    @Data
    public static class ForwardingConfig {

        private int startPort;

        private int endPort;
    }
}
