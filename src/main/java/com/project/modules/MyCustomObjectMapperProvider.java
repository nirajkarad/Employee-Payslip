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
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.inject.Provider;

public class MyCustomObjectMapperProvider implements Provider<ObjectMapper>
{
    
    @Override
    public ObjectMapper get()
    {
      /*  @SuppressWarnings("deprecation")
        SimpleModule simpleModule = new SimpleModule("SimpleModule",new Version(1,0,0,null));
        simpleModule.addSerializer(new YearMonthSerializer());*/
        ObjectMapper mapper = new ObjectMapper();
        //mapper.registerModules(new JodaModule() , simpleModule);
        mapper.registerModule(new JodaModule());
        return mapper;
    }
    
}
