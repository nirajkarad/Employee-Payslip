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
package com.project.test.resource;

import org.testng.Assert;

import org.testng.annotations.Test;
import com.project.employee.Employee;
import org.testng.annotations.BeforeClass;
import com.project.organization.Organization;

public class TestOrganization
{

    private Organization org;
    private Employee emp;
    
    @BeforeClass
    public void setup()
    {
        org = new Organization(1, "TestOrg");
        emp = new Employee(1, "fname", "lname", "email");
    }
    
    @Test
    public void testAddEmployeeToOrganization()
    {
        org.addEmployeeToOrganization(emp);
    }
    
    
    @Test(dependsOnMethods="testAddEmployeeToOrganization")
    public void testgetEmpId()
    {
        Employee e = org.getEmployee(1);
        Assert.assertNotNull(e);
        Assert.assertEquals(1, emp.getId());
        Assert.assertEquals("fname", emp.getFirstName());
        Assert.assertEquals("lname", emp.getLastName());
        Assert.assertEquals("email", emp.getEmail());
    }
    
    @Test(dependsOnMethods="testAddEmployeeToOrganization")
    public void testgetAbsentEmpId()
    {
        Employee e = org.getEmployee(3);
        Assert.assertNull(e);
    }
    
    @Test
    public void testEmployeeCollection(){
        Employee emp1 = new Employee(1, "first", "lastname", "first@glam.com");
        Employee emp2 = new Employee(1, "first", "lastname", "first@glam.com");
        Employee emp3 = new Employee(2, "second", "secondname", "second@glam.com");

        org.addEmployeeToOrganization(emp1);
        org.addEmployeeToOrganization(emp2);
        org.addEmployeeToOrganization(emp3);
        
        Assert.assertEquals( org.getEmployeeList().size(), 2);
    }
    
}
