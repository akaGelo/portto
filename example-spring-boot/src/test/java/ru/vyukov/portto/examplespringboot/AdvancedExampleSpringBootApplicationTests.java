package ru.vyukov.portto.examplespringboot;

import org.junit.Test;
import ru.vyukov.portto.springboot.PortTo;

import static com.codeborne.selenide.Selenide.open;

@PortTo
public class AdvancedExampleSpringBootApplicationTests extends AbstractSelenideTest {


    @Test
    public void contextLoads() {
        open("/");
    }

}
