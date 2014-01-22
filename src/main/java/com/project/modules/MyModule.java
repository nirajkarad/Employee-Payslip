/*

 * Copyright 2010-2013 Ning, Inc.

 *

 * Ning licenses this file to you under the Apache License, version 2.0

 * (the "License"); you may not use this file except in compliance with the

 * License.  You may obtain a copy of the License at:

 *

 *    http://www.apache.org/licenses/LICENSE-2.0

 *

 * Unless required by applicable law or agreed to in writing, software

 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT

 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the

 * License for the specific language governing permissions and limitations

 * under the License.

 */
package com.project.modules;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.project.persist.Storage;
import com.project.persist.map.MapOrgStorageImpl;
import com.project.resource.Finance;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import java.util.HashMap;
import java.util.Map;

public class MyModule extends JerseyServletModule
{

    private static final String JERSEY_API_JSON_POJO_MAPPING_FEATURE = "com.sun.jersey.api.json.POJOMappingFeature";
    private static final String JERSEY_CONFIG_PROPERTY_PACKAGES = "com.sun.jersey.config.property.packages";
    private static final String CUSTOMER_RESOURCES_PACKAGE = "com.project.resource";

    @Override
    protected void configureServlets()
    {
        bind(Finance.class);
        bind(Storage.class).to(MapOrgStorageImpl.class).asEagerSingleton();

        // Configure Jackson for generating JSON

        bind(JacksonJsonProvider.class).toProvider(JacksonJsonProviderWrapper.class).asEagerSingleton();

        // Configures Jackson object mapper to convert JodaTime YearMonth.

        bind(ObjectMapper.class).toProvider(new MyCustomObjectMapperProvider()).asEagerSingleton();

        final Map<String, String> params = new HashMap<String, String>();

        params.put(JERSEY_CONFIG_PROPERTY_PACKAGES, CUSTOMER_RESOURCES_PACKAGE);
        params.put(JERSEY_API_JSON_POJO_MAPPING_FEATURE, "true");

        serve("/*").with(GuiceContainer.class, params);
    }
}
