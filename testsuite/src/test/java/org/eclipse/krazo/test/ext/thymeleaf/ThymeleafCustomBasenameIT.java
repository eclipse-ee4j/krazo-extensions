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
package org.eclipse.krazo.test.ext.thymeleaf;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.eclipse.krazo.test.ext.util.WebArchiveBuilder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URL;
import java.nio.file.Paths;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Integration test for custom basename configuration.
 *
 * @author Satoshi Seto
 */
@RunWith(Arquillian.class)
public class ThymeleafCustomBasenameIT {

    private static final String WEB_INF_SRC = "src/main/resources/thymeleaf/custombasename/";

    @ArquillianResource
    private URL baseURL;

    private WebClient webClient;

    @Before
    public void setUp() {
        webClient = new WebClient();
        webClient.getOptions()
            .setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions()
            .setRedirectEnabled(true);
    }

    @Deployment(testable = false, name = "thymeleaf-custom-basename")
    public static WebArchive createDeployment() {
        return new WebArchiveBuilder()
            .addPackage("org.eclipse.krazo.test.ext.thymeleaf.custombasename")
            .addView(Paths.get(WEB_INF_SRC).resolve("views/custom-basename.html").toFile(), "custom-basename.html")
            // Add custom message properties files
            .addResource(Paths.get("src/main/resources").resolve("custom-messages.properties").toFile(), "custom-messages.properties")
            .addResource(Paths.get("src/main/resources").resolve("custom-messages_en.properties").toFile(), "custom-messages_en.properties")
            .addResource(Paths.get("src/main/resources").resolve("custom-messages_ja.properties").toFile(), "custom-messages_ja.properties")
            .addBeansXml()
            .addDependency("org.eclipse.krazo.ext:krazo-thymeleaf")
            .build();
    }

    @Test
    @RunAsClient
    public void testCustomBasenameEnglish() throws Exception {
        final HtmlPage page = webClient.getPage(baseURL + "thymeleaf/custom-basename/messages?name=John&lang=en");

        final DomElement welcome = page.getElementById("custom-welcome");
        final DomElement greeting = page.getElementById("custom-greeting");
        final DomElement saveButton = page.getElementById("custom-save");

        assertNotNull("Welcome element should exist", welcome);
        assertNotNull("Greeting element should exist", greeting);
        assertNotNull("Save button should exist", saveButton);

        assertTrue("Should show custom welcome message from custom-messages.properties",
            welcome.getTextContent().contains("Custom Welcome Message"));
        assertTrue("Should show custom greeting with parameter",
            greeting.getTextContent().contains("Custom Hello, John!"));
        assertTrue("Should show custom save button label",
            saveButton.getTextContent().contains("Save Custom"));
    }

    @Test
    @RunAsClient
    public void testCustomBasenameJapanese() throws Exception {
        final HtmlPage page = webClient.getPage(baseURL + "thymeleaf/custom-basename/messages?name=太郎&lang=ja");

        final DomElement welcome = page.getElementById("custom-welcome");
        final DomElement greeting = page.getElementById("custom-greeting");
        final DomElement saveButton = page.getElementById("custom-save");

        assertNotNull("Welcome element should exist", welcome);
        assertNotNull("Greeting element should exist", greeting);
        assertNotNull("Save button should exist", saveButton);

        assertTrue("Should show Japanese custom welcome message",
            welcome.getTextContent().contains("カスタムメッセージファイルからのウェルカムメッセージ"));
        assertTrue("Should show Japanese custom greeting with parameter",
            greeting.getTextContent().contains("カスタムこんにちは、太郎さん"));
        assertTrue("Should show Japanese custom save button label",
            saveButton.getTextContent().contains("カスタム保存"));
    }

    @Test
    @RunAsClient
    public void testCustomBasenameDefault() throws Exception {
        final HtmlPage page = webClient.getPage(baseURL + "thymeleaf/custom-basename/messages?name=Test");

        final DomElement welcome = page.getElementById("custom-welcome");

        assertNotNull("Welcome element should exist", welcome);

        // Default should be English
        assertTrue("Should show default (English) custom welcome message",
            welcome.getTextContent().contains("Custom Welcome Message"));
    }
}
