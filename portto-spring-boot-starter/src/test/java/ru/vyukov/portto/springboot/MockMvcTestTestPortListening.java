package ru.vyukov.portto.springboot;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vyukov.portto.springboot.annotations.PortToRemoteAddress;
import ru.vyukov.portto.springboot.annotations.PortToRemotePort;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@Ignore
@PortTo
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MockMvcTestTestPortListening {

    /**
     * eq @Value("${portto.remote.address}")
     */
    @PortToRemoteAddress
    private String portToExternalAddress;

    /**
     * eq @Value("${portto.remote.port}")
     */
    @PortToRemotePort
    private int portoToExternalPort;

    @PortToRemoteAddress
    private String remoteAddress;


    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void openInBrowser() {
        assertNotNull(portoToExternalPort);
        assertNotEquals(0, portoToExternalPort);
        assertNotNull(remoteAddress);
    }

}
