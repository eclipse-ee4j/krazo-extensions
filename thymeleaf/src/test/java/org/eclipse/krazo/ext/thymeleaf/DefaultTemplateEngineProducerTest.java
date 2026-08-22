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

import jakarta.enterprise.inject.Instance;
import jakarta.mvc.MvcContext;
import jakarta.ws.rs.core.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.thymeleaf.messageresolver.IMessageResolver;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link DefaultTemplateEngineProducer}.
 *
 * @author Satoshi Seto
 */
public class DefaultTemplateEngineProducerTest {

    private DefaultTemplateEngineProducer producer;
    private MvcContext mvcContext;
    private Configuration configuration;

    @Before
    public void setUp() {
        JakartaServletWebApplicationWrapper applicationWrapper = createMock(JakartaServletWebApplicationWrapper.class);
        mvcContext = createMock(MvcContext.class);
        @SuppressWarnings("unchecked")
        Instance<IMessageResolver> messageResolvers = createMock(Instance.class);
        configuration = createMock(Configuration.class);

        producer = new DefaultTemplateEngineProducer(applicationWrapper, mvcContext, messageResolvers);
    }

    @Test
    public void shouldReturnTrueWhenCachePropertyIsNotSet() {
        // Given: Cache property is not set
        expect(mvcContext.getConfig()).andReturn(configuration);
        expect(configuration.getProperty(ThymeleafProperties.CACHE)).andReturn(null);
        replay(mvcContext, configuration);

        // When: Call usingCache()
        boolean result = producer.usingCache();

        // Then: Default value true is returned
        assertTrue("Should return true when cache property is not set", result);
        verify(mvcContext, configuration);
    }

    @Test
    public void shouldReturnTrueWhenCachePropertyIsTrue() {
        // Given: Cache property is set to true
        expect(mvcContext.getConfig()).andReturn(configuration);
        expect(configuration.getProperty(ThymeleafProperties.CACHE)).andReturn(true);
        replay(mvcContext, configuration);

        // When: Call usingCache()
        boolean result = producer.usingCache();

        // Then: Returns true
        assertTrue("Should return true when cache property is true", result);
        verify(mvcContext, configuration);
    }

    @Test
    public void shouldReturnFalseWhenCachePropertyIsFalse() {
        // Given: Cache property is set to false
        expect(mvcContext.getConfig()).andReturn(configuration);
        expect(configuration.getProperty(ThymeleafProperties.CACHE)).andReturn(false);
        replay(mvcContext, configuration);

        // When: Call usingCache()
        boolean result = producer.usingCache();

        // Then: Returns false
        assertFalse("Should return false when cache property is false", result);
        verify(mvcContext, configuration);
    }
}
