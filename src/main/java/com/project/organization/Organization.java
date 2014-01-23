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

    private int id;

    private String name;

    private Set<Employee> employeeList;

    @JsonCreator
    public Organization(@JsonProperty("id") int id, @JsonProperty("name") String name)
    {
        this.id = id;
        this.name = name;
        this.employeeList = Sets.newConcurrentHashSet();
    }

    public String getName()
    {
        return name;
    }

    public Set<Employee> getEmployeeList()
    {
        return employeeList;
    }

    public int getId()
    {
        return id;
    }

    public Employee getEmployee(int empId)
    {
        Iterator<Employee> itr = employeeList.iterator();
        while (itr.hasNext()) {
            Employee emp = itr.next();
            if (emp.getId() == empId)
                return emp;
        }
        return null;
    }

    public void addEmployeeToOrganization(Employee emp)
    {
        employeeList.add(emp);
    }
}
