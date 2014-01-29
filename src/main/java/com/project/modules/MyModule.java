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

import com.project.resource.EmployeeResource;

import com.project.persist.map.MapOrgStorageImpl;
import com.project.persist.EmployeePersistance;
import com.project.config.ConfigProvider;
import com.project.config.DBConfig;
import com.project.json.MyCustomObjectMapperProvider;
import com.project.json.JacksonJsonProviderWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.project.persist.Storage;
import com.project.persist.db.DatabaseStorageImpl;
import com.project.resource.Finance;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class MyModule extends JerseyServletModule
{

    @Override
    protected void configureServlets()
    {
        bind(Finance.class);
        bind(EmployeeResource.class);
        bind(Storage.class).to(MapOrgStorageImpl.class).asEagerSingleton();
        bind(DBConfig.class).toProvider(ConfigProvider.class).asEagerSingleton();
        bind(EmployeePersistance.class).to(DatabaseStorageImpl.class).asEagerSingleton();
        // Configure Jackson for generating JSON
        bind(JacksonJsonProvider.class).toProvider(JacksonJsonProviderWrapper.class).asEagerSingleton();

        // Configures Jackson object mapper to convert JodaTime YearMonth.
        bind(ObjectMapper.class).toProvider(new MyCustomObjectMapperProvider()).asEagerSingleton();

        serve("/*").with(GuiceContainer.class);
    }
}
