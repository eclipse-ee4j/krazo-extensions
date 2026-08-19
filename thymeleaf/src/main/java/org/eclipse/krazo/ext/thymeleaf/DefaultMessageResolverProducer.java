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
package org.eclipse.krazo.ext.thymeleaf;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.mvc.MvcContext;
import org.eclipse.krazo.engine.ViewEngineConfig;
import org.thymeleaf.messageresolver.IMessageResolver;

import java.util.Optional;

/**
 * Producer for the default MessageResolver used by ThymeleafViewEngine.
 * Applications can override this by providing their own producer with higher priority
 * or by using {@link jakarta.enterprise.inject.Alternative}.
 *
 * @author Satoshi Seto
 */
public class DefaultMessageResolverProducer {

    @Inject
    private MvcContext mvcContext;

    /**
     * Produces the default DefaultMessageResolver.
     * The basename can be customized via the MVC configuration property
     * {@link ThymeleafProperties#MESSAGES_BASENAME}. Defaults to {@link DefaultMessageResolver#DEFAULT_BASENAME}.
     *
     * @return a {@link DefaultMessageResolver} configured with the appropriate basename
     */
    @Produces
    @ViewEngineConfig
    public IMessageResolver createMessageResolver() {
        final String basename = Optional.ofNullable(mvcContext.getConfig().getProperty(ThymeleafProperties.MESSAGES_BASENAME))
            .map(String::valueOf)
            .orElse(DefaultMessageResolver.DEFAULT_BASENAME);

        return new DefaultMessageResolver(basename);
    }
}
