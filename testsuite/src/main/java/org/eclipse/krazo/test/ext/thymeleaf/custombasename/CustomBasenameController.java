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

import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

import java.util.Locale;

/**
 * Controller for testing custom basename configuration.
 *
 * @author Satoshi Seto
 */
@Controller
@Path("messages")
public class CustomBasenameController {

    @Inject
    private Models models;

    @GET
    @View("custom-basename.html")
    public void showMessages(@QueryParam("name") String name, @QueryParam("lang") String lang) {
        if (name != null) {
            models.put("userName", name);
        }

        if (lang != null) {
            models.put("locale", new Locale(lang));
        }
    }
}
