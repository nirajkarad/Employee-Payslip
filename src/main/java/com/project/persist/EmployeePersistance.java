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

import com.project.employee.Employee;
import java.util.List;

public interface EmployeePersistance
{
    public static final int NONE = 0;
    public static final int IN_MEMORY = 1;
    public static final int DATABASE = 2;
    
    public static final boolean TRUE = true;
    public static final boolean FALSE = false;
    
    void putDetail(int id , Employee employee) throws PersistanceException;
    
    Employee getDetail(int id);
    
    List<Employee> findAllEmployees() throws PersistanceException;
    
    int getType();
    
    public boolean addBinding();
   
}
