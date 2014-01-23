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

import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.resetToNice;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Table;
import com.project.employee.Employee;
import com.project.organization.Organization;
import com.project.persist.Storage;
import com.project.resource.Finance;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.joda.time.YearMonth;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestFinanceResource
{

    private Storage str;
    private Finance fin;

    @BeforeClass
    public void setup()
    {
        str = createNiceMock(Storage.class);
        fin = new Finance(str);
    }

    @BeforeMethod
    public void resetStorageMock()
    {
        resetToNice(str);
    }

    // ----------------Organization-----------------
    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddOrganizationInvalidOrgId()
    {
        Organization org = new Organization(-1, "Wipro");
        fin.addOrganization(org);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddOrganizationNullOrgName()
    {
        Organization org = new Organization(1, null);
        fin.addOrganization(org);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddOrganizationBlankOrgName()
    {
        Organization org = new Organization(1, "    \t  ");
        fin.addOrganization(org);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddOrganizationEmptyOrgName()
    {
        Organization org = new Organization(1, "");
        fin.addOrganization(org);
    }

    @Test
    public void testAddOrganization()
    {
        expect(str.getDetail(1)).andReturn(null);
        replay(str);
        Organization org = new Organization(1, "Wipro");
        Response createOrg = fin.addOrganization(org);
        Assert.assertNotNull(createOrg);
        Assert.assertEquals(createOrg.getStatus(), Status.CREATED.getStatusCode());
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddExistingOrganization()
    {
        expect(str.getDetail(1)).andReturn(new Organization(1, "Wipro"));
        replay(str);
        Organization org = new Organization(1, "Wipro");
        fin.addOrganization(org);
    }

    // ----------------Employee------------------
    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddEmployeeInvalidOrgId()
    {
        Employee emp1 = new Employee(304, "Niraj", "Karad", "nirajk@glam.com");
        fin.addEmployee(-1, emp1);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddEmployeeInvalidEmpId()
    {
        Employee emp1 = new Employee(304, "Niraj", "Karad", "nirajk@glam.com");
        fin.addEmployee(2, emp1);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddEmployeeNullEmpFname()
    {
        Employee emp1 = new Employee(304, null, "Karad", "nirajk@glam.com");
        fin.addEmployee(2, emp1);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddEmployeeEmptyEmpFname()
    {
        Employee emp1 = new Employee(304, "", "Karad", "nirajk@glam.com");
        fin.addEmployee(2, emp1);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddEmployeeBlankEmpFname()
    {
        Employee emp1 = new Employee(304, " \t  ", "Karad", "nirajk@glam.com");
        fin.addEmployee(2, emp1);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddEmployeeNullEmpLname()
    {
        Employee emp1 = new Employee(304, "Niraj", null, "nirajk@glam.com");
        fin.addEmployee(2, emp1);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddEmployeeEmptyEmpLname()
    {
        Employee emp1 = new Employee(304, "Niraj", "", "nirajk@glam.com");
        fin.addEmployee(2, emp1);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddEmployeeBlankEmpLname()
    {
        Employee emp1 = new Employee(304, "Niraj", "    \t  ", "nirajk@glam.com");
        fin.addEmployee(2, emp1);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddEmployeeNullEmpEmail()
    {
        Employee emp1 = new Employee(304, "Niraj", "Karad", null);
        fin.addEmployee(2, emp1);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddEmployeeEmptyEmpEmail()
    {
        Employee emp1 = new Employee(304, "Niraj", "karad", "");
        fin.addEmployee(2, emp1);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddEmployeeBlankEmpEmail()
    {
        Employee emp1 = new Employee(304, "Niraj", "karad", "   \t  ");
        fin.addEmployee(2, emp1);
    }

    @Test
    public void testAddEmployee()
    {
        Organization org = new Organization(1, "Wipro");
        expect(str.getDetail(1)).andReturn(org);
        replay(str);

        Employee emp = new Employee(300, "Niraj", "karad", "nirajk@glam.com");
        Response createEmployee = fin.addEmployee(org.getId(), emp);
        Assert.assertNotNull(createEmployee);
        Assert.assertEquals(createEmployee.getStatus(), Status.CREATED.getStatusCode());
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddSameEmployee()
    {
        Organization org = new Organization(1, "Wipro");
        Employee emp = new Employee(300, "Niraj", "karad", "nirajk@glam.com");
        org.addEmployeeToOrganization(emp);
        expect(str.getDetail(1)).andReturn(org);
        replay(str);
        emp = new Employee(300, "Niraj", "karad", "nirajk@glam.com");
        fin.addEmployee(org.getId(), emp);
    }

    // -----------------Employee Payslip----------

    private Map<String, Integer> salBreakup()
    {
        Map<String, Integer> breakup = new HashMap<String, Integer>();
        breakup.put("Basic", 10000);
        breakup.put("HRA", 4000);
        breakup.put("Conveyence", 2000);
        breakup.put("Special Allowance", 8000);
        return ImmutableMap.<String, Integer> builder().putAll(breakup).build();
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddPayslipInvalidOrgID()
    {
        fin.addPayslip(-1, 1, salBreakup(), 2014, 1);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddPayslipInvalidEmpID()
    {
        fin.addPayslip(1, -1, salBreakup(), 2014, 1);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddPayslipNullSalBreakupID()
    {
        fin.addPayslip(1, 1, null, 2014, 1);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddPayslipInvalidYear()
    {
        fin.addPayslip(1, 1, salBreakup(), 2015, 1);
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddPayslipInvalidMonth()
    {
        fin.addPayslip(1, 1, salBreakup(), 2014, 13);
    }

    @Test
    public void testAddPayslipSuccess()
    {
        Organization org = new Organization(1, "Wipro");
        Employee emp = new Employee(300, "Niraj", "karad", "nirajk@glam.com");
        org.addEmployeeToOrganization(emp);
        expect(str.getDetail(1)).andReturn(org);
        replay(str);
        Response slipAdded = fin.addPayslip(org.getId(), emp.getId(), salBreakup(), 2012, 2);
        Assert.assertNotNull(slipAdded);
        Assert.assertEquals(slipAdded.getStatus(), Status.CREATED.getStatusCode());
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddPayslipForSameYearAndMonth()
    {
        Organization org = new Organization(1, "Wipro");
        Employee emp = new Employee(300, "Niraj", "karad", "nirajk@glam.com");
        emp.addBreakupYearMonth(salBreakup(), 2012, 2);
        org.addEmployeeToOrganization(emp);
        expect(str.getDetail(1)).andReturn(org);
        replay(str);
        fin.addPayslip(org.getId(), emp.getId(), salBreakup(), 2012, 2);
    }

    // --------------------Payslip using table---------
    private Table<YearMonth, String, Integer> tableSalBreakup()
    {
        Table<YearMonth, String, Integer> table = HashBasedTable.create();
        YearMonth ym = new YearMonth();
        table.put(ym, "HRA", 4000);
        table.put(ym, "Conveyence", 2000);
        table.put(ym, "Special Allowance", 8000);
        return table;
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddTablePayslipInvalidOrgID()
    {
        fin.addPayslipUsingTable(-1, 1, tableSalBreakup());
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddTablePayslipInvalidEmpID()
    {
        fin.addPayslipUsingTable(1, -1, tableSalBreakup());
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddTablePayslipInvalidSalbreakup()
    {
        fin.addPayslipUsingTable(1, 1, null);
    }

    @Test
    public void testAddPayslipInTableSuccess()
    {
        Organization org = new Organization(1, "Wipro");
        Employee emp = new Employee(300, "Niraj", "karad", "nirajk@glam.com");
        org.addEmployeeToOrganization(emp);
        expect(str.getDetail(1)).andReturn(org);
        replay(str);
        Response slipAdded = fin.addPayslipUsingTable(org.getId(), emp.getId(), tableSalBreakup());
        Assert.assertNotNull(slipAdded);
        Assert.assertEquals(slipAdded.getStatus(), Status.CREATED.getStatusCode());
    }

    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddPayslipInTableForSameYearMonth()
    {
        Organization org = new Organization(1, "Wipro");
        Employee emp = new Employee(300, "Niraj", "karad", "nirajk@glam.com");
        emp.addBreakupYearMonthInTable(tableSalBreakup());
        org.addEmployeeToOrganization(emp);
        expect(str.getDetail(1)).andReturn(org);
        replay(str);
        fin.addPayslipUsingTable(org.getId(), emp.getId(), tableSalBreakup());
    }

}
