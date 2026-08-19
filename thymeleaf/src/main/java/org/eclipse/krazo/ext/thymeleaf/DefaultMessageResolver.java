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

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.messageresolver.AbstractMessageResolver;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Message resolver that loads messages from resource bundles using the standard
 * Java {@link ResourceBundle} mechanism. This allows Thymeleaf templates to use
 * internationalized messages via the #{...} syntax.
 *
 * <p>Message files should be placed in the classpath with the naming pattern:
 * <ul>
 *   <li>messages.properties (default)</li>
 *   <li>messages_ja.properties (Japanese)</li>
 *   <li>messages_en.properties (English)</li>
 *   <li>etc.</li>
 * </ul>
 * </p>
 *
 * <p>This resolver is configured to run just before Thymeleaf's StandardMessageResolver,
 * allowing it to provide messages from resource bundles before falling back to the
 * standard Thymeleaf resolution mechanism.</p>
 *
 * @author Satoshi Seto
 */
public class DefaultMessageResolver extends AbstractMessageResolver {

    /**
     * Default basename for resource bundle message files (e.g., messages.properties, messages_ja.properties).
     */
    public static final String DEFAULT_BASENAME = "messages";
    private static final Object[] EMPTY_MESSAGE_PARAMETERS = new Object[0];

    private final String basename;

    /**
     * Creates a new message resolver with the default basename "messages".
     */
    public DefaultMessageResolver() {
        this(DEFAULT_BASENAME);
    }

    /**
     * Creates a new message resolver with a custom basename.
     *
     * @param basename the base name of the resource bundle
     */
    public DefaultMessageResolver(String basename) {
        this.basename = basename;
        // Set order to run just before StandardMessageResolver (Integer.MAX_VALUE)
        setOrder(Integer.MAX_VALUE - 1);
    }

    @Override
    public String resolveMessage(
            ITemplateContext context,
            Class<?> origin,
            String key,
            Object[] messageParameters) {

        Locale locale = context.getLocale();

        try {
            ResourceBundle bundle = ResourceBundle.getBundle(basename, locale);
            String message = bundle.getString(key);

            if (messageParameters != null && messageParameters.length > 0) {
                return formatMessage(locale, message, messageParameters);
            }

            return message;

        } catch (MissingResourceException e) {
            // Message not found, return null to allow other resolvers to try
            return null;
        }
    }

    @Override
    public String createAbsentMessageRepresentation(
            ITemplateContext context,
            Class<?> origin,
            String key,
            Object[] messageParameters) {
        // Use default behavior from AbstractMessageResolver
        return null;
    }

    private String formatMessage(Locale locale, String message, Object[] params) {
        if (message == null) {
            return null;
        }
        final MessageFormat messageFormat = new MessageFormat(message, locale);
        return messageFormat.format((params != null ? params : EMPTY_MESSAGE_PARAMETERS));
    }

    public String getBasename() {
        return basename;
    }
}
