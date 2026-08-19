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

import org.eclipse.krazo.Properties;

/**
 * Application-level properties used to configure the Thymeleaf view engine extension.
 * Extends {@link org.eclipse.krazo.Properties} to inherit core Krazo properties.
 *
 * @author Satoshi Seto
 */
public interface ThymeleafProperties extends Properties {

    /**
     * String property that determines the base name of resource bundle message files to be used in
     * {@link DefaultMessageResolver}.
     */
    String MESSAGES_BASENAME = "org.eclipse.krazo.thymeleaf.messages.basename";
}
