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

import static org.easymock.EasyMock.*;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import com.project.employee.Employee;
import javax.ws.rs.core.Response.Status;
import org.testng.Assert;
import javax.ws.rs.core.Response;
import com.project.organization.Organization;
import com.project.persist.Storage;
import com.project.resource.Finance;
import javax.ws.rs.WebApplicationException;
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
    //----------------Organization-----------------
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
    //----------------Employee------------------
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
        Response createEmployee = fin.addEmployee(org.getOrg_id(), emp);
        Assert.assertNotNull(createEmployee);
        Assert.assertEquals(createEmployee.getStatus(), Status.CREATED.getStatusCode());
    }
    
    @Test
    public void testAddSameEmployee()
    {
        //TODO
    }
    
    //-----------------Employee Payslip----------
    
    private Map<String, Integer> salBreakup()
    {
    Map<String, Integer> breakup = new HashMap<String, Integer>();
    breakup.put("Basic", 10000);
    breakup.put("HRA", 4000);
    breakup.put("Conveyence", 2000);
    breakup.put("Special Allowance", 8000);
    return  ImmutableMap.<String, Integer> builder().putAll(breakup).build();
    }
    
    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddPayslipInvalidOrgID()
    {
        fin.addPayslip(-1, 1, salBreakup() , 2014, 1);
    }
    
    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddPayslipInvalidEmpID()
    {
        fin.addPayslip(1, -1, salBreakup() , 2014, 1);
    }
    
    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddPayslipNullSalBreakupID()
    {
        fin.addPayslip(1, 1, null , 2014, 1);
    }
    
    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddPayslipInvalidYear()
    {
        fin.addPayslip(1, 1, salBreakup() , 2015, 1);
    }
    
    @Test(expectedExceptions = WebApplicationException.class)
    public void testAddPayslipInvalidMonth()
    {
        fin.addPayslip(1, 1, salBreakup() , 2014, 13);
    }
    
    @Test
    public void testAddPayslipSuccess()
    {
        //TODO
    }

}
