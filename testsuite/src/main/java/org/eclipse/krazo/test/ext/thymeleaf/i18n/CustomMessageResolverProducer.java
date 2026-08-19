/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.eclipse.krazo.test.ext.thymeleaf.i18n;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import org.eclipse.krazo.engine.ViewEngineConfig;
import org.thymeleaf.messageresolver.IMessageResolver;

/**
 * Example producer that demonstrates how to add custom message resolvers.
 * Multiple resolvers can be produced and will be automatically registered
 * with the TemplateEngine in order of their priority.
 *
 * @author Satoshi Seto
 */
public class CustomMessageResolverProducer {

    /**
     * Produces a custom in-memory message resolver.
     * You can set the order to control priority.
     *
     * @return a custom message resolver
     */
    @Produces
    @ViewEngineConfig
    @Named("customMessageResolver")
    public IMessageResolver createCustomMessageResolver() {
        CustomMessageResolver resolver = new CustomMessageResolver();
        resolver.setOrder(0); // Set explicit priority (0 = highest)
        return resolver;
    }

    // You can add more producers for additional message resolvers:
    //
    // @Produces
    // @ViewEngineConfig
    // @Named("databaseMessageResolver")
    // public IMessageResolver createDatabaseMessageResolver() {
    //     DatabaseMessageResolver resolver = new DatabaseMessageResolver();
    //     resolver.setOrder(1); // Set priority as needed
    //     return resolver;
    // }
}
