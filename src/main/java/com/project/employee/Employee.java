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
package com.project.employee;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import java.util.Map;
import org.joda.time.YearMonth;

public class Employee
{
    private int id;
    private String firstName;
    private String lastName;
    private String email;

    private Map<YearMonth, Map<String, Integer>> paySlip;
    
    private Table<YearMonth, String, Integer> table;

    @JsonCreator
    public Employee(@JsonProperty("id") int id, @JsonProperty("firstName") String firstName, @JsonProperty("lastName") String lastName, @JsonProperty("email") String email)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.paySlip = Maps.newConcurrentMap();
        this.table = HashBasedTable.create();
    }

    public Map<YearMonth, Map<String, Integer>> getPay_slip()
    {
        return paySlip;
    }

    public Map<String, Integer> getPayslipForSpecificMonth(int year, int month)
    {
        return paySlip.get(new YearMonth(year, month));
    }

    public int getId()
    {
        return id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getEmail()
    {
        return email;
    }

    @Override
    public int hashCode()
    {
        return id;
    }

    @Override
    public boolean equals(Object obj)
    {
        Employee emp = (Employee) obj;
        if (emp.id == this.id)
            return true;
        return false;

    }

    public void addBreakupYearMonth(Map<String, Integer> breakup, int year, int month)
    {
        paySlip.put(new YearMonth(year, month), ImmutableMap.<String, Integer> builder().putAll(breakup).build());
    }
    
    public Map<String, Integer> getPaySlipFromTable(YearMonth ym)
    {
        return table.row(ym);
    }
    
    public void addBreakupYearMonthInTable(Table<YearMonth, String, Integer> salBreakup)
    {
        table.putAll(salBreakup);
    }
}
