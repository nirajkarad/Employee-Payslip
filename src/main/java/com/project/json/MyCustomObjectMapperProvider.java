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
package com.project.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.collect.Table;
import com.google.inject.Provider;
import org.joda.time.YearMonth;

public class MyCustomObjectMapperProvider implements Provider<ObjectMapper>
{

    @Override
    public ObjectMapper get()
    {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule sm = new SimpleModule();
        sm.addSerializer(Table.class, new GuavaTableSerializer());
        sm.addDeserializer(Table.class, new GuavaTableDeserializer());
        mapper.registerModules(new GuavaModule() , sm, 
        new JodaModule().addKeyDeserializer(YearMonth.class, new YearMonthKeyDeserializer()));
        return mapper;
    }
}
