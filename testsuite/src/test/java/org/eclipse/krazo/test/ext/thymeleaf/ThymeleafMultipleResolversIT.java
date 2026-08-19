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
 * Integration test for multiple message resolvers.
 * Tests that CustomMessageResolver (order=0), CustomMessageResolver2 (order=50),
 * and DefaultMessageResolver (order=Integer.MAX_VALUE-1) work correctly together.
 *
 * @author Satoshi Seto
 */
@RunWith(Arquillian.class)
public class ThymeleafMultipleResolversIT {

    private static final String WEB_INF_SRC = "src/main/resources/thymeleaf/i18n/";

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

    @Deployment(testable = false, name = "thymeleaf-multiple-resolvers")
    public static WebArchive createDeployment() {
        return new WebArchiveBuilder()
            .addPackage("org.eclipse.krazo.test.ext.thymeleaf.i18n")
            .addView(Paths.get(WEB_INF_SRC).resolve("views/multiple-resolvers.html").toFile(), "multiple-resolvers.html")
            // Add global messages for DefaultMessageResolver
            .addResource(Paths.get("src/main/resources").resolve("messages.properties").toFile(), "messages.properties")
            .addResource(Paths.get("src/main/resources").resolve("messages_en.properties").toFile(), "messages_en.properties")
            .addResource(Paths.get("src/main/resources").resolve("messages_ja.properties").toFile(), "messages_ja.properties")
            .addBeansXml()
            .addDependency("org.eclipse.krazo.ext:krazo-thymeleaf")
            .build();
    }

    @Test
    @RunAsClient
    public void testMultipleResolversEnglish() throws Exception {
        final HtmlPage page = webClient.getPage(baseURL + "thymeleaf/i18n/messages/multiple-resolvers?name=Alice&lang=en");

        // Test CustomMessageResolver (order=0 - highest priority)
        final DomElement customGreeting = page.getElementById("custom-greeting");
        final DomElement customTitle = page.getElementById("custom-title");

        assertNotNull("Custom greeting should exist", customGreeting);
        assertNotNull("Custom title should exist", customTitle);

        assertTrue("Should resolve from CustomMessageResolver",
            customGreeting.getTextContent().contains("Custom Hello from CDI!"));
        assertTrue("Should resolve custom title",
            customTitle.getTextContent().contains("Custom Application"));

        // Test CustomMessageResolver2 (order=50)
        final DomElement resolver2Message = page.getElementById("resolver2-message");
        final DomElement resolver2Greeting = page.getElementById("resolver2-greeting");

        assertNotNull("Resolver2 message should exist", resolver2Message);
        assertNotNull("Resolver2 greeting should exist", resolver2Greeting);

        assertTrue("Should resolve from CustomMessageResolver2",
            resolver2Message.getTextContent().contains("This is from CustomMessageResolver2"));
        assertTrue("Should resolve resolver2 greeting with parameter",
            resolver2Greeting.getTextContent().contains("Hello from Resolver2, Alice!"));

        // Test DefaultMessageResolver (order=Integer.MAX_VALUE-1)
        final DomElement defaultWelcome = page.getElementById("default-welcome");

        assertNotNull("Default welcome should exist", defaultWelcome);

        assertTrue("Should resolve from DefaultMessageResolver",
            defaultWelcome.getTextContent().contains("Welcome to Krazo Thymeleaf Extension"));

        // Test priority: custom.greeting exists in both CustomMessageResolver and CustomMessageResolver2
        // CustomMessageResolver should win because it has higher priority (lower order value)
        final DomElement priorityTest = page.getElementById("priority-test");

        assertNotNull("Priority test element should exist", priorityTest);

        assertTrue("Should resolve from higher priority CustomMessageResolver, not Resolver2",
            priorityTest.getTextContent().contains("Custom Hello from CDI!"));
    }

    @Test
    @RunAsClient
    public void testMultipleResolversJapanese() throws Exception {
        final HtmlPage page = webClient.getPage(baseURL + "thymeleaf/i18n/messages/multiple-resolvers?name=太郎&lang=ja");

        // Test CustomMessageResolver (Japanese)
        final DomElement customGreeting = page.getElementById("custom-greeting");

        assertNotNull("Custom greeting should exist", customGreeting);

        assertTrue("Should resolve Japanese from CustomMessageResolver",
            customGreeting.getTextContent().contains("CDIからのカスタムこんにちは"));

        // Test CustomMessageResolver2 (Japanese)
        final DomElement resolver2Message = page.getElementById("resolver2-message");
        final DomElement resolver2Greeting = page.getElementById("resolver2-greeting");

        assertNotNull("Resolver2 message should exist", resolver2Message);
        assertNotNull("Resolver2 greeting should exist", resolver2Greeting);

        assertTrue("Should resolve Japanese from CustomMessageResolver2",
            resolver2Message.getTextContent().contains("これはCustomMessageResolver2からです"));
        assertTrue("Should resolve Japanese resolver2 greeting with parameter",
            resolver2Greeting.getTextContent().contains("Resolver2からこんにちは、太郎さん"));

        // Test DefaultMessageResolver (Japanese)
        final DomElement defaultWelcome = page.getElementById("default-welcome");

        assertNotNull("Default welcome should exist", defaultWelcome);

        assertTrue("Should resolve Japanese from DefaultMessageResolver",
            defaultWelcome.getTextContent().contains("Krazo Thymeleaf拡張機能へようこそ"));
    }

    @Test
    @RunAsClient
    public void testResolverPriority() throws Exception {
        final HtmlPage page = webClient.getPage(baseURL + "thymeleaf/i18n/messages/multiple-resolvers?name=Test&lang=en");

        final DomElement priorityTest = page.getElementById("priority-test");

        assertNotNull("Priority test element should exist", priorityTest);

        // The key "custom.greeting" exists in:
        // - CustomMessageResolver (order=0)
        // - CustomMessageResolver2 (order=50)
        //
        // Since lower order = higher priority, CustomMessageResolver should win
        assertTrue("Priority test: CustomMessageResolver (order=0) should override CustomMessageResolver2 (order=50)",
            priorityTest.getTextContent().contains("Custom Hello from CDI!"));
    }
}
