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
 * Integration test for Thymeleaf internationalization (i18n) support.
 *
 * @author Satoshi Seto
 */
@RunWith(Arquillian.class)
public class ThymeleafI18nIT {

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

    @Deployment(testable = false, name = "thymeleaf-i18n")
    public static WebArchive createDeployment() {
        return new WebArchiveBuilder()
            .addPackage("org.eclipse.krazo.test.ext.thymeleaf.i18n")
            .addView(Paths.get(WEB_INF_SRC).resolve("views/i18n.html").toFile(), "i18n.html")
            // Add template-specific properties for StandardMessageResolver
            .addView(Paths.get(WEB_INF_SRC).resolve("views/i18n.properties").toFile(), "i18n.properties")
            .addView(Paths.get(WEB_INF_SRC).resolve("views/i18n_en.properties").toFile(), "i18n_en.properties")
            .addView(Paths.get(WEB_INF_SRC).resolve("views/i18n_ja.properties").toFile(), "i18n_ja.properties")
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
    public void testDefaultLocale() throws Exception {
        final HtmlPage page = webClient.getPage(baseURL + "thymeleaf/i18n/messages?name=World");
        final DomElement h1 = page.getElementsByTagName("h1").get(0);
        assertNotNull(h1);
        assertTrue("Should contain English welcome message",
            h1.getTextContent().contains("Welcome to Krazo Thymeleaf Extension"));
    }

    @Test
    @RunAsClient
    public void testEnglishLocale() throws Exception {
        final HtmlPage page = webClient.getPage(baseURL + "thymeleaf/i18n/messages?name=Alice&lang=en");
        final DomElement h1 = page.getElementsByTagName("h1").get(0);
        final DomElement greeting = page.getElementsByTagName("p").get(0);

        assertNotNull(h1);
        assertNotNull(greeting);
        assertTrue("Should contain English welcome message",
            h1.getTextContent().contains("Welcome to Krazo Thymeleaf Extension"));
        assertTrue("Should contain English greeting with parameter",
            greeting.getTextContent().contains("Hello, Alice!"));
    }

    @Test
    @RunAsClient
    public void testJapaneseLocale() throws Exception {
        final HtmlPage page = webClient.getPage(baseURL + "thymeleaf/i18n/messages?name=太郎&lang=ja");
        final DomElement h1 = page.getElementsByTagName("h1").get(0);
        final DomElement greeting = page.getElementsByTagName("p").get(0);

        assertNotNull(h1);
        assertNotNull(greeting);
        assertTrue("Should contain Japanese welcome message",
            h1.getTextContent().contains("ようこそ"));
        assertTrue("Should contain Japanese greeting with parameter",
            greeting.getTextContent().contains("こんにちは、太郎さん"));
    }

    @Test
    @RunAsClient
    public void testButtonLabels() throws Exception {
        final HtmlPage page = webClient.getPage(baseURL + "thymeleaf/i18n/messages?lang=ja");
        final DomElement submitButton = page.getElementsByTagName("button").get(0);
        final DomElement cancelButton = page.getElementsByTagName("button").get(1);

        assertNotNull(submitButton);
        assertNotNull(cancelButton);
        assertTrue("Submit button should be in Japanese",
            submitButton.getTextContent().contains("送信"));
        assertTrue("Cancel button should be in Japanese",
            cancelButton.getTextContent().contains("キャンセル"));
    }

    @Test
    @RunAsClient
    public void testStandardMessageResolverSuccess() throws Exception {
        final HtmlPage page = webClient.getPage(baseURL + "thymeleaf/i18n/messages?name=Test&lang=en");
        final DomElement standardResolverElement = page.getElementById("standard-resolver-test");
        final DomElement templateSpecificElement = page.getElementById("template-specific-message");

        assertNotNull("Standard resolver test element should exist", standardResolverElement);
        assertNotNull("Template specific message element should exist", templateSpecificElement);

        // StandardMessageResolver should resolve messages from template-specific properties
        final String standardResolverText = standardResolverElement.getTextContent();
        final String templateSpecificText = templateSpecificElement.getTextContent();

        assertTrue("StandardMessageResolver should resolve message from i18n_en.properties",
            standardResolverText.contains("StandardMessageResolver successfully resolved this message"));
        assertTrue("StandardMessageResolver should resolve template-specific message",
            templateSpecificText.contains("This message is from template-specific properties file"));
    }

    @Test
    @RunAsClient
    public void testStandardMessageResolverSuccessJapanese() throws Exception {
        final HtmlPage page = webClient.getPage(baseURL + "thymeleaf/i18n/messages?name=Test&lang=ja");
        final DomElement standardResolverElement = page.getElementById("standard-resolver-test");
        final DomElement templateSpecificElement = page.getElementById("template-specific-message");

        assertNotNull("Standard resolver test element should exist", standardResolverElement);
        assertNotNull("Template specific message element should exist", templateSpecificElement);

        // StandardMessageResolver should resolve messages from template-specific properties (Japanese)
        final String standardResolverText = standardResolverElement.getTextContent();
        final String templateSpecificText = templateSpecificElement.getTextContent();

        assertTrue("StandardMessageResolver should resolve message from i18n_ja.properties",
            standardResolverText.contains("StandardMessageResolverがこのメッセージを正常に解決しました"));
        assertTrue("StandardMessageResolver should resolve template-specific message in Japanese",
            templateSpecificText.contains("このメッセージはテンプレート固有のプロパティファイルから来ています"));
    }

    @Test
    @RunAsClient
    public void testStandardMessageResolverFallback() throws Exception {
        final HtmlPage page = webClient.getPage(baseURL + "thymeleaf/i18n/messages?name=Test&lang=en");
        final DomElement fallbackElement = page.getElementById("fallback-test");

        assertNotNull("Fallback test element should exist", fallbackElement);

        // StandardMessageResolver returns ??key_locale?? format for missing keys
        final String fallbackText = fallbackElement.getTextContent();
        assertTrue("StandardMessageResolver should return ??key_locale?? format for missing keys",
            fallbackText.contains("??nonexistent.message.key") ||
            fallbackText.matches(".*\\?\\?nonexistent\\.message\\.key.*"));
    }

    @Test
    @RunAsClient
    public void testStandardMessageResolverFallbackJapanese() throws Exception {
        final HtmlPage page = webClient.getPage(baseURL + "thymeleaf/i18n/messages?name=Test&lang=ja");
        final DomElement fallbackElement = page.getElementById("fallback-test");

        assertNotNull("Fallback test element should exist", fallbackElement);

        // StandardMessageResolver should include locale in the representation
        final String fallbackText = fallbackElement.getTextContent();
        assertTrue("StandardMessageResolver should include locale (ja) in fallback representation",
            fallbackText.contains("??nonexistent.message.key") &&
            (fallbackText.contains("_ja") || fallbackText.contains("ja")));
    }
}
