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
package com.project.organization;

import com.fasterxml.jackson.annotation.JsonCreator;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Sets;
import com.project.employee.Employee;
import java.util.Iterator;
import java.util.Set;

public class Organization
{

    private int org_id;

    private String org_name;

    private Set<Employee> emp_collection;

    @JsonCreator
    public Organization(@JsonProperty("org_id") int id, @JsonProperty("org_name") String name)
    {
        this.org_id = id;
        this.org_name = name;
        this.emp_collection = Sets.newConcurrentHashSet();
    }

    public String getOrg_name()
    {
        return org_name;
    }

    public Set<Employee> getEmp_collection()
    {
        return emp_collection;
    }

    public int getOrg_id()
    {
        return org_id;
    }

    public Employee getEmp(int empId)
    {
        Iterator<Employee> itr = emp_collection.iterator();
        while (itr.hasNext()) {
            Employee emp = itr.next();
            if (emp.getEmp_id() == empId)
                return emp;
        }
        return null;
    }

    public void addEmployeeToOrganization(Employee emp)
    {
        emp_collection.add(emp);
    }
}
