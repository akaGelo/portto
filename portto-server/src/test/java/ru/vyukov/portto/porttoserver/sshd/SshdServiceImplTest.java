package ru.vyukov.portto.porttoserver.sshd;

import org.apache.sshd.common.forward.ForwardingFilterFactory;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator;
import org.apache.sshd.server.command.CommandFactory;
import org.apache.sshd.server.config.keys.AuthorizedKeysAuthenticator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import ru.vyukov.portto.porttoserver.ServerConfig;
import ru.vyukov.portto.porttoserver.ports.PortsRegistry;

import java.io.IOException;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * @author Oleg Vyukov
 */
@RunWith(MockitoJUnitRunner.class)
public class SshdServiceImplTest {


    @Spy
    private ServerConfig serverConfig = new ServerConfig();

    @Mock
    private PortsRegistry portsRegistry;
    @Mock
    private CommandFactory commandFactory;
    @Mock
    private ForwardingFilterFactory forwardingFilterFactory;

    @Mock
    private SshServer sshServer;

    @Captor
    private ArgumentCaptor<PublickeyAuthenticator> publickeyAuthenticatorArgumentCaptor;

    @Captor
    private ArgumentCaptor<PasswordAuthenticator> passwordAuthenticatorArgumentCaptor;

    @InjectMocks
    private SshdServiceImpl underTest;

    @Before
    public void setUp() throws Exception {
        serverConfig.setForwarding(new ServerConfig.ForwardingConfig());
    }

    @Test
    public void authKeysConfigTest() throws IOException {
        serverConfig.setAuthorizedKeys("ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCoHJSqzwtMcYSqKF99dabrsDeTIdgZx8b3ohd5fYHASNN4yDpqip5WJbBnTMi8hf5FRQ/pOxUoq+Dfd45wXhEEtz3dp7BPG0Y+1Mhyh3EySC0+K83pO7A6WQ0wnJKJ49/gUXwQkS4uE6Ji3e3iFYbkROm6+0JBTklJX6ktRgiFN3ytNTYiYFLGyHAYU3oh9ODzXrUYoyoVTiFail5iaUK9B6UfoQ2KZHus0Ba6tbT1 gelo@gelo-laptop");
        underTest.setSshd(sshServer);

        underTest.startSshd();

        verify(sshServer).setPublickeyAuthenticator(any(AuthorizedKeysAuthenticator.class));

        verify(sshServer, never()).setPasswordAuthenticator(any());
    }

    @Test
    public void authAnyPasswordsTest() throws IOException {

        underTest.setSshd(sshServer);

        underTest.startSshd();

        verify(sshServer).setPublickeyAuthenticator(publickeyAuthenticatorArgumentCaptor.capture());
        verify(sshServer).setPasswordAuthenticator(passwordAuthenticatorArgumentCaptor.capture());

        PublickeyAuthenticator publickeyAuthenticator = publickeyAuthenticatorArgumentCaptor.getValue();
        assertTrue(publickeyAuthenticator.authenticate("any", null, null));

        PasswordAuthenticator passwordAuthenticator = passwordAuthenticatorArgumentCaptor.getValue();
        assertTrue(passwordAuthenticator.authenticate("any", "any", null));

    }


}