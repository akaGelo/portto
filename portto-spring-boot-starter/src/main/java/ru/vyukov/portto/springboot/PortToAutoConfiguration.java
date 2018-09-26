package ru.vyukov.portto.springboot;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.validation.annotation.Validated;
import ru.vyukov.portto.porttoserver.PortToClient;
import ru.vyukov.portto.porttoserver.PortToClientConfig;
import ru.vyukov.portto.porttoserver.PortToClientImpl;
import ru.vyukov.portto.springboot.converters.InetSocketAddressToStringConverter;
import ru.vyukov.portto.springboot.converters.StringToInetSocketAddressConverter;

/**
 * @author Oleg Vyukov
 */
@Configuration
@ConditionalOnClass({PortToClientConfig.class})
//TODO disable property for disable on local debug and enable on ci
//@AutoConfigureAfter(WebMvcAutoConfiguration.class)
//@ConditionalOnProperty(name = "server.port", matchIfMissing = false)
public class PortToAutoConfiguration {


    @Bean
    @Validated
    @ConfigurationProperties("portto")
    public PortToClientConfig portToClientConfig() {
        return new PortToClientConfig();
    }


    @Bean
    public PortToClient portToClient(PortToClientConfig portToClientConfig) {
        return new PortToClientImpl(portToClientConfig);
    }

    @Bean
    public PortToClientAutoStarterApplicationListener portToClientAutoStarterApplicationListener(PortToClient portToClient) {
        return new PortToClientAutoStarterApplicationListener(portToClient);
    }


    @Bean
    @Lazy(false)
    public PortToPropertySource portToPropertySource(PortToClient portToClient, ConfigurableEnvironment env) {
        PortToPropertySource portToPropertySource = new PortToPropertySource("portto", portToClient);
        MutablePropertySources sources = env.getPropertySources();
        sources.addFirst(portToPropertySource);
        return portToPropertySource;
    }

    @Bean
    @ConfigurationPropertiesBinding
    public StringToInetSocketAddressConverter stringToInetSocketAddressConverter() {
        return new StringToInetSocketAddressConverter();
    }

    @Bean
    @ConfigurationPropertiesBinding
    InetSocketAddressToStringConverter inetSocketAddressToStringConverter(){
        return new InetSocketAddressToStringConverter();
    }

}