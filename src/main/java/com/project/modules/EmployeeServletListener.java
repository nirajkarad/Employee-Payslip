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

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.project.persist.EmployeePersistance;
import com.project.persist.map.EmployeePersistanceMapImpl;
import java.util.Set;

public class EmployeeServletListener extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        //Default Map implementation
        AddConfigurations conf = AddConfigurations.getInstance();
        Set<Class<? extends EmployeePersistance>> addSet = conf.add(EmployeePersistanceMapImpl.class);
        
        MyModule mod = new MyModule();
        mod.addConfigToMultipleBinding(addSet);
        return Guice.createInjector(mod);
    }
}

