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
package com.project.persist;

import com.google.inject.Inject;
import com.project.employee.Employee;
import java.util.Set;

public class PersistanceAPI
{

    Set<EmployeePersistance> set;

    @Inject
    public PersistanceAPI(Set<EmployeePersistance> set)
    {
        this.set = set;
    }

    public void putDetail(int id, Employee employee) throws PersistanceException
    {
        for (EmployeePersistance persistance : set) {
            persistance.putDetail(id, employee);
        }

    }

    public Employee getDetail(int id)
    {
        for (EmployeePersistance persistance : set) {
            if (persistance.getType() == EmployeePersistance.DATABASE) {
                return persistance.getDetail(id);
            }
        }
        return null;
    }

}
