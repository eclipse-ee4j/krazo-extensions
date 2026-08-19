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
import org.eclipse.krazo.engine.ViewEngineConfig;
import org.thymeleaf.messageresolver.IMessageResolver;

/**
 * Producer for CustomMessageResolver2.
 * This demonstrates that multiple custom message resolvers can be registered.
 *
 * @author Satoshi Seto
 */
public class CustomMessageResolver2Producer {

    @Produces
    @ViewEngineConfig
    public IMessageResolver createCustomMessageResolver2() {
        return new CustomMessageResolver2();
    }
}
