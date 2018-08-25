package ru.vyukov.portto.examplespringboot;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.junit.ScreenShooter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.vyukov.portto.springboot.PortTo;
import ru.vyukov.portto.springboot.annotations.PortToRemoteAddress;

/**
 * @author Oleg Vyukov
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ExampleSpringBootApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractSelenideTest {


    @Rule
    public ScreenShooter makeScreenshotOnFailure = ScreenShooter.failedTests().succeededTests();

    @PortTo
    @TestConfiguration
    public static class Config {

        public Config(@PortToRemoteAddress String remoteAddress, @Value("${tests.seleniumGrid:#{null}}") String seleniumGridAddress) {
            Configuration.baseUrl = String.format("http://%s", remoteAddress);
            //for remote debug
            Configuration.reportsFolder = "target/reports/";
            //spring property tests.seleniumGrid override selenide   -Dremote = http://localhost:5678/wd/hub
            if (null != seleniumGridAddress) {
                Configuration.remote = seleniumGridAddress;
            }


            log.info("SELENIDE: baseUrl = " + Configuration.baseUrl);
            log.info("SELENIDE: browser = " + Configuration.browser);
            log.info("SELENIDE: remote = " + Configuration.remote);
        }

    }
}
