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

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.messageresolver.AbstractMessageResolver;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Second custom message resolver for testing multiple resolvers.
 * This resolver has lower priority (order=50) than CustomMessageResolver (order=0).
 *
 * @author Satoshi Seto
 */
public class CustomMessageResolver2 extends AbstractMessageResolver {

    private static final Map<String, String> MESSAGES_EN = new HashMap<>();
    private static final Map<String, String> MESSAGES_JA = new HashMap<>();
    private static final Object[] EMPTY_MESSAGE_PARAMETERS = new Object[0];

    static {
        // English messages (only resolved if CustomMessageResolver doesn't handle them)
        MESSAGES_EN.put("resolver2.message", "This is from CustomMessageResolver2");
        MESSAGES_EN.put("resolver2.greeting", "Hello from Resolver2, {0}!");
        MESSAGES_EN.put("custom.greeting", "This should NOT be used (Resolver2 has lower priority)");

        // Japanese messages
        MESSAGES_JA.put("resolver2.message", "これはCustomMessageResolver2からです");
        MESSAGES_JA.put("resolver2.greeting", "Resolver2からこんにちは、{0}さん！");
        MESSAGES_JA.put("custom.greeting", "これは使われないはず（Resolver2は優先度が低い）");
    }

    public CustomMessageResolver2() {
        // Lower priority than CustomMessageResolver (which has order=0)
        setOrder(50);
    }

    @Override
    public String resolveMessage(
            ITemplateContext context,
            Class<?> origin,
            String key,
            Object[] messageParameters) {

        Locale locale = context.getLocale();
        Map<String, String> messages = locale != null && locale.getLanguage().equals("ja")
                ? MESSAGES_JA
                : MESSAGES_EN;

        String message = messages.get(key);
        if (message == null) {
            return null;
        }

        return formatMessage(locale, message, messageParameters);
    }

    @Override
    public String createAbsentMessageRepresentation(
            ITemplateContext context,
            Class<?> origin,
            String key,
            Object[] messageParameters) {
        return null;
    }

    private String formatMessage(Locale locale, String message, Object[] params) {
        if (message == null) {
            return null;
        }
        final MessageFormat messageFormat = new MessageFormat(message, locale);
        return messageFormat.format((params != null ? params : EMPTY_MESSAGE_PARAMETERS));
    }
}
