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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Table;
import com.project.employee.Employee;
import com.project.json.GuavaTableDeserializer;
import com.project.json.GuavaTableSerializer;
import com.project.json.SmileConversion;
import com.project.json.YearMonthKeyDeserializer;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Map;
import javax.sql.rowset.serial.SerialException;
import org.joda.time.YearMonth;
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
        breakup = ImmutableMap.of("Basic", 5000, "HRA", 2000, "Tax", 756);
    }

    @Test
    public void TestaddBreakupYearMonth()
    {
        emp.addBreakupYearMonth(breakup, 2014, 1);
    }

    @Test(dependsOnMethods = "TestaddBreakupYearMonth")
    public void TestGetPaySlip()
    {
        Map<String, Integer> sal = emp.getPayslipForSpecificMonth(2014, 1);
        Assert.assertNotNull(sal);
        Assert.assertEquals(Integer.valueOf(5000), sal.get("Basic"));
        Assert.assertEquals(Integer.valueOf(2000), sal.get("HRA"));
        Assert.assertEquals(Integer.valueOf(756), sal.get("Tax"));
    }

    @Test
    public void testSameEmployee()
    {
        Employee emp1 = new Employee(1, "first", "lastname", "first@glam.com");
        Employee emp2 = new Employee(1, "first", "lastname", "first@glam.com");
        Employee emp3 = new Employee(2, "second", "secondname", "second@glam.com");

        Assert.assertEquals(emp1, emp2);
        Assert.assertEquals(emp2, emp1);
        Assert.assertNotEquals(emp1, emp3);
        Assert.assertNotEquals(emp2, emp3);
    }

/*    @Test
    public void testEmployeeSerialization() throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        Assert.assertEquals(mapper.writeValueAsString(new Employee(1, "fname", "lname", "email")), "{\"id\":1,\"firstName\":\"fname\",\"lastName\":\"lname\",\"email\":\"email\"}");
    }
*/
    @Test
    public void testEmployeeDeserialization() throws IOException, SerialException, SQLException, ClassNotFoundException
    {
        ObjectMapper objectMapper = new ObjectMapper(new SmileFactory());
        SimpleModule sm = new SimpleModule();
        sm.addSerializer(Table.class, new GuavaTableSerializer());
        sm.addDeserializer(Table.class, new GuavaTableDeserializer());
        objectMapper.registerModules(new GuavaModule() , sm, 
        new JodaModule().addKeyDeserializer(YearMonth.class, new YearMonthKeyDeserializer()));
        Employee emp = new Employee(1, "fname", "lname", "email");
        
        Table<YearMonth, String, Integer> table = HashBasedTable.create();
        YearMonth ym = new YearMonth();
        table.put(ym, "HRA", 4000);
        table.put(ym, "Conveyence", 2000);
        table.put(ym, "Special Allowance", 8000);
        
        emp.addBreakupYearMonthInTable(table);
        Blob b = SmileConversion.smileCompressionSerialize(emp);
        
        SmileConversion.smileDecompressionDeSerializeS(b);
    }
}
