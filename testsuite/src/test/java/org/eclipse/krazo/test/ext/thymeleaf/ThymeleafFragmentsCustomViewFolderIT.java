/*
 * Copyright (c) 2018, 2022 Eclipse Krazo committers and contributors
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
 * This test verifies that another view folder can be used to get Thymeleaf fragments.
 * This test uses the same HTML files than
 */
@RunWith(Arquillian.class)
public class ThymeleafFragmentsCustomViewFolderIT {

	private static final String WEB_INF_SRC = "src/main/resources/thymeleaf/viewfolder/";

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

	@Deployment(testable = false, name = "thymeleaf-fragments-custom")
	public static WebArchive createDeployment() {
		return new WebArchiveBuilder()
			.addPackage("org.eclipse.krazo.test.ext.thymeleaf.customviewfolder")
			.addView(Paths.get(WEB_INF_SRC).resolve("views/index.html").toFile(), "custom","index.html")
			.addView(Paths.get(WEB_INF_SRC).resolve("views/fragment.html").toFile(), "custom","fragment.html")
			.addBeansXml()
			.addDependency("org.eclipse.krazo.ext:krazo-thymeleaf")
			.build();
	}

	@Test
	public void shouldResolveFragment() throws Exception {
		final HtmlPage page = webClient.getPage(baseURL + "resources/thymeleaf-fragments-custom");
		final DomElement div = page.getElementById("from-fragment");
		assertNotNull(div);
		assertTrue(div.getTextContent().contains("I'm from the fragment"));
	}
}
