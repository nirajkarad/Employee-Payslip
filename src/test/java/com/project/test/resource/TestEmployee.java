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
import com.project.employee.Employee;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestEmployee
{
    private Employee emp;
    private Map<String, Integer> breakup;
    
    @BeforeClass
    public void setup()
    {
        emp = new Employee(1, "fname", "lname", "email");
        breakup = ImmutableMap.of("Basic", 5000 , "HRA", 2000, "Tax", 756);
    }
    
    @Test
    public void TestaddBreakupYearMonth()
    {
        emp.addBreakupYearMonth(breakup, 2014, 1);
    }
    
    @Test(dependsOnMethods="TestaddBreakupYearMonth")
    public void TestGetPaySlip()
    {
        Map<String, Integer> sal =emp.getPayslipForSpecificMonth(2014, 1);
        Assert.assertNotNull(sal);
        Assert.assertEquals(Integer.valueOf(5000), sal.get("Basic"));
        Assert.assertEquals(Integer.valueOf(2000), sal.get("HRA"));
        Assert.assertEquals(Integer.valueOf(756), sal.get("Tax"));
    }
    
    @Test
    public void testSameEmployee(){
        Employee emp1 = new Employee(1, "first", "lastname", "first@glam.com");
        Employee emp2 = new Employee(1, "first", "lastname", "first@glam.com");
        Employee emp3 = new Employee(2, "second", "secondname", "second@glam.com");

        Assert.assertEquals(emp1, emp2);
        Assert.assertEquals(emp2, emp1);
        Assert.assertNotEquals(emp1, emp3);
        Assert.assertNotEquals(emp2, emp3);
    }
}
