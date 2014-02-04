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

import com.project.persist.EmployeePersistance;
import java.util.HashSet;
import java.util.Set;

public class AddConfigurations
{
    //Singleton Class
    private static AddConfigurations instance = new AddConfigurations();
    Set<Class<? extends EmployeePersistance>>  retVal = 
    new HashSet<Class<? extends EmployeePersistance>>();
    
    private AddConfigurations()
    {
    }
    
    public static AddConfigurations getInstance()
    {
        return instance;
    }
    
    public Set<Class<? extends EmployeePersistance>> add(Class<? extends EmployeePersistance> toBeIncluded)
    {
        retVal.add(toBeIncluded);
        return retVal;
    }

}
