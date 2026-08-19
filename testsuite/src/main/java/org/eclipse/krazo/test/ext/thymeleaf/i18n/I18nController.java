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

import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

/**
 * Controller for testing Thymeleaf internationalization features.
 *
 * @author Satoshi Seto
 */
@Controller
@Path("messages")
public class I18nController {

    @Inject
    private Models models;

    @GET
    public String index(@QueryParam("name") String name) {
        if (name == null || name.isEmpty()) {
            name = "World";
        }
        models.put("user", new User(name));
        return "i18n.html";
    }

    @GET
    @Path("custom")
    public String custom() {
        return "custom.html";
    }

    @GET
    @Path("multiple-resolvers")
    public String multipleResolvers(@QueryParam("name") String name) {
        if (name == null || name.isEmpty()) {
            name = "Test";
        }
        models.put("user", new User(name));
        return "multiple-resolvers.html";
    }
}
