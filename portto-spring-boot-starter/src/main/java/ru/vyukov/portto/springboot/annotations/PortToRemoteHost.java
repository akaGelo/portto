/*
 * Copyright 2012-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.vyukov.portto.springboot.annotations;

import org.springframework.beans.factory.annotation.Value;

import java.lang.annotation.*;

import static ru.vyukov.portto.springboot.annotations.PortToRemoteHost.PORTTO_REMOTE_HOST_PROPERTY;

/**
 * @author Oleg Vyukov
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
        ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Value("${" + PORTTO_REMOTE_HOST_PROPERTY + "}")
public @interface PortToRemoteHost {
    String PORTTO_REMOTE_HOST_PROPERTY = "portto.remote.host";
}
