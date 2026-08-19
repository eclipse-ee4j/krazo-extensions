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
package org.eclipse.krazo.test.ext.thymeleaf.custombasename;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.krazo.ext.thymeleaf.ThymeleafProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Application with custom message basename configuration.
 *
 * @author Satoshi Seto
 */
@ApplicationPath("thymeleaf/custom-basename")
public class CustomBasenameApplication extends Application {

    @Override
    public Map<String, Object> getProperties() {
        final Map<String, Object> map = new HashMap<>();
        map.put(ThymeleafProperties.MESSAGES_BASENAME, "custom-messages");
        return map;
    }
}
