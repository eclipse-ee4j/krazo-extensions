/*
 * Copyright (c) 2014-2015 Oracle and/or its affiliates. All rights reserved.
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
package org.eclipse.krazo.ext.thymeleaf;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.mvc.MvcContext;
import jakarta.mvc.engine.ViewEngine;
import org.eclipse.krazo.engine.ViewEngineConfig;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.IServletWebApplication;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * Producer for the TemplateEngine used by ThymeleafViewEngine.
 *
 * @author Christian Kaltepoth
 * @author Eddú Meléndez
 */
public class DefaultTemplateEngineProducer {

	public static final String FILE_SUFFIX = ".html";

	private static final int VIEW_RESOLVER_ORDER = 1;
	private static final int FRAGMENT_RESOLVER_ORDER = 2;

	@Inject
	private JakartaServletWebApplicationWrapper applicationWrapper;

	@Inject
	private MvcContext mvcContext;

	@Produces
	@ViewEngineConfig
	public TemplateEngine getTemplateEngine() {

		IServletWebApplication servletApplication = applicationWrapper.get();

		final WebApplicationTemplateResolver viewResolver = configureViewResolver(servletApplication);
		final WebApplicationTemplateResolver fragmentResolver = configureFragmentResolver(servletApplication);

		TemplateEngine engine = new TemplateEngine();
		engine.addTemplateResolver(viewResolver);
		engine.addTemplateResolver(fragmentResolver);

		return engine;

	}

	/**
	 * Krazo adds its {@link ViewEngine#VIEW_FOLDER} automatically at views which are returned by a controller instance.
	 * Therefore a {@link WebApplicationTemplateResolver} needs to be configured which is executed first and allows
	 * following resolvers to be executed.
	 *
	 * @param servletApplication the currently used {@link IServletWebApplication} for Thymeleaf
	 * @return a {@link WebApplicationTemplateResolver} for resolving views returned by controllers which contain already
	 * the {@link ViewEngine#VIEW_FOLDER}
	 */
	private WebApplicationTemplateResolver configureViewResolver(IServletWebApplication servletApplication) {
		final WebApplicationTemplateResolver viewResolver = new WebApplicationTemplateResolver(servletApplication);
		viewResolver.setOrder(VIEW_RESOLVER_ORDER);
		viewResolver.setCheckExistence(true);
		return viewResolver;
	}

	/**
	 * For using fragments inside the default view directory of Jakarta MVC applications without having the need to prefix and suffix
	 * everything with the views directory or file type, we need to use an additional resolver chained behind the default resolver.
	 *
	 * @param servletApplication the currently used {@link IServletWebApplication} for Thymeleaf
	 * @return a {@link WebApplicationTemplateResolver} providing the additional information necessary to resolve fragments
	 */
	private WebApplicationTemplateResolver configureFragmentResolver(final IServletWebApplication servletApplication) {
		final String mvcViewPath = Optional.ofNullable(mvcContext.getConfig().getProperty(ViewEngine.VIEW_FOLDER))
			.map(String::valueOf)
			.orElse(ViewEngine.DEFAULT_VIEW_FOLDER);

		final WebApplicationTemplateResolver fragmentResolver = new WebApplicationTemplateResolver(servletApplication);
		fragmentResolver.setPrefix(mvcViewPath);
		fragmentResolver.setSuffix(FILE_SUFFIX);
		fragmentResolver.setTemplateMode(TemplateMode.HTML);
		fragmentResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
		fragmentResolver.setOrder(FRAGMENT_RESOLVER_ORDER);

		return fragmentResolver;
	}

}
