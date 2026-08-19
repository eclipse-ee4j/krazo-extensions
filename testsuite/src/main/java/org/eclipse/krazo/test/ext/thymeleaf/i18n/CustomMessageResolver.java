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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Example custom message resolver that resolves messages from an in-memory map.
 * This demonstrates how applications can provide their own message resolution logic
 * (e.g., from database, cache, external service, etc.)
 *
 * @author Satoshi Seto
 */
public class CustomMessageResolver extends AbstractMessageResolver {

    private final Map<String, Map<Locale, String>> messages = new HashMap<>();

    public CustomMessageResolver() {
        // Set highest priority (lowest order value)
        setOrder(0);
        initializeMessages();
    }

    private void initializeMessages() {
        // English messages
        Map<Locale, String> customGreeting = new HashMap<>();
        customGreeting.put(Locale.ENGLISH, "Custom Hello from CDI!");
        customGreeting.put(Locale.JAPANESE, "CDIからのカスタムこんにちは！");
        messages.put("custom.greeting", customGreeting);

        Map<Locale, String> customTitle = new HashMap<>();
        customTitle.put(Locale.ENGLISH, "Custom Application");
        customTitle.put(Locale.JAPANESE, "カスタムアプリケーション");
        messages.put("custom.title", customTitle);
    }

    @Override
    public String resolveMessage(
            ITemplateContext context,
            Class<?> origin,
            String key,
            Object[] messageParameters) {

        Map<Locale, String> localeMessages = messages.get(key);
        if (localeMessages == null) {
            return null;
        }

        Locale locale = context.getLocale();
        String message = localeMessages.get(locale);

        if (message == null) {
            // Fallback to English
            message = localeMessages.get(Locale.ENGLISH);
        }

        return message;
    }

    @Override
    public String createAbsentMessageRepresentation(
            ITemplateContext context,
            Class<?> origin,
            String key,
            Object[] messageParameters) {
        return null; // Delegate to next resolver
    }
}
