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

import com.google.common.collect.ImmutableMap;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.project.employee.Employee;
import com.project.modules.MyModule;
import com.project.organization.Organization;
import com.project.persist.Storage;
import com.project.resource.Finance;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import junit.framework.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestFinance
{

    private Finance fin;
    private Storage str;

    @BeforeClass
    private void setup()
    {
        Injector inj = Guice.createInjector(new MyModule());
        this.str = inj.getInstance(Storage.class);
        this.fin = new Finance(str);
    }

    @Test
    public void testAddOrganization()
    {
        Organization org = new Organization(1, "Wipro");
        Response createOrg = fin.addOrganization(org);
        Assert.assertEquals(createOrg.getStatus(), Status.CREATED.getStatusCode());
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddOrganizationInvalidOrgId()
    {
        Organization org = new Organization(-1, "Wipro");
        fin.addOrganization(org);

    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddOrganizationInvalidOrgName()
    {
        Organization org = new Organization(1, "");
        fin.addOrganization(org);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddEmployeeAbsentOrgId()
    {
        Employee emp1 = new Employee(304, "Niraj", "Karad", "nirajk@glam.com");
        fin.addEmployee(2, emp1);
    }

    @Test(dependsOnMethods = "testAddOrganization")
    public void testAddEmployee()
    {
        Employee emp1 = new Employee(304, "Niraj", "Karad", "nirajk@glam.com");
        Response firstEmployee = fin.addEmployee(1, emp1);
        Assert.assertEquals(firstEmployee.getStatus(), Status.CREATED.getStatusCode());

        // Next Employee

        Employee emp2 = new Employee(305, "Rahul", "p", "rahulp@glam.com");
        Response secondEmployee = fin.addEmployee(1, emp2);
        Assert.assertEquals(secondEmployee.getStatus(), Status.CREATED.getStatusCode());
    }

    @Test(dependsOnMethods = "testAddEmployee", expectedExceptions = WebApplicationException.class)
    public void testAddPayslipInvalidOrgID()
    {
        Map<String, Integer> breakup = new HashMap<String, Integer>();
        breakup.put("Basic", 10000);
        breakup.put("HRA", 4000);
        breakup.put("Conveyence", 2000);
        breakup.put("Special Allowance", 8000);
        final ImmutableMap<String, Integer> salary_breakup = ImmutableMap.<String, Integer> builder().putAll(breakup).build();

        fin.addPayslip(2, 305, salary_breakup, 2014, 1);
    }

    @Test(dependsOnMethods = "testAddEmployee", expectedExceptions = WebApplicationException.class)
    public void testAddPayslipInvalidEmpID()
    {
        Map<String, Integer> breakup = new HashMap<String, Integer>();
        breakup.put("Basic", 10000);
        breakup.put("HRA", 4000);
        breakup.put("Conveyence", 2000);
        breakup.put("Special Allowance", 8000);
        final ImmutableMap<String, Integer> salary_breakup = ImmutableMap.<String, Integer> builder().putAll(breakup).build();

        fin.addPayslip(1, 365, salary_breakup, 2014, 1);
    }

    @Test(dependsOnMethods = "testAddEmployee", expectedExceptions = WebApplicationException.class)
    public void testAddPayslipNoBreakup()
    {
        fin.addPayslip(1, 305, null, 2014, 1);
    }

    @Test(dependsOnMethods = "testAddEmployee", expectedExceptions = WebApplicationException.class)
    public void testAddPayslipInvalidYear()
    {
        Map<String, Integer> breakup = new HashMap<String, Integer>();
        breakup.put("Basic", 10000);
        breakup.put("HRA", 4000);
        breakup.put("Conveyence", 2000);
        breakup.put("Special Allowance", 8000);
        final ImmutableMap<String, Integer> salary_breakup = ImmutableMap.<String, Integer> builder().putAll(breakup).build();

        fin.addPayslip(1, 305, salary_breakup, -1, 1);
    }

    @Test(dependsOnMethods = "testAddEmployee", expectedExceptions = WebApplicationException.class)
    public void testAddPayslipInvalidMonth()
    {
        Map<String, Integer> breakup = new HashMap<String, Integer>();
        breakup.put("Basic", 10000);
        breakup.put("HRA", 4000);
        breakup.put("Conveyence", 2000);
        breakup.put("Special Allowance", 8000);
        final ImmutableMap<String, Integer> salary_breakup = ImmutableMap.<String, Integer> builder().putAll(breakup).build();

        fin.addPayslip(1, 305, salary_breakup, 2014, 14);
    }

    @Test(dependsOnMethods = { "testAddEmployee", "testAddPayslipInvalidOrgID", "testAddPayslipInvalidEmpID", "testAddPayslipNoBreakup", "testAddPayslipInvalidYear", "testAddPayslipInvalidMonth" })
    public void testAddPayslipSuccess()
    {
        Map<String, Integer> breakup = new HashMap<String, Integer>();
        breakup.put("Basic", 10000);
        breakup.put("HRA", 4000);
        breakup.put("Conveyence", 2000);
        breakup.put("Special Allowance", 8000);
        final ImmutableMap<String, Integer> salary_breakup = ImmutableMap.<String, Integer> builder().putAll(breakup).build();

        Response addPaySlip = fin.addPayslip(1, 305, salary_breakup, 2014, 1);
        Assert.assertEquals(addPaySlip.getStatus(), Status.CREATED.getStatusCode());
    }
}
