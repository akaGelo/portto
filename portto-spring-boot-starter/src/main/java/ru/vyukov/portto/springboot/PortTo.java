package ru.vyukov.portto.springboot;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;
/**
 * @author Oleg Vyukov
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
@Documented
@Import(PortToAutoConfiguration.class)
public @interface PortTo {

}