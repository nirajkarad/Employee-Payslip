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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import org.joda.time.YearMonth;

//@JsonDeserialize(using = EmpDeserializer.class)
public class Employee
{
    private int emp_id;
    private String emp_fname;
    private String emp_lname;
    private String emp_email;

    private Map<YearMonth, Map<String, Integer>> pay_slip;

    //@JsonCreator
    //public Employee(@JsonProperty("emp_id") int id, @JsonProperty("emp_fname") String fname, @JsonProperty("emp_lname") String lname, @JsonProperty("emp_email") String email)
    public Employee( int id, String fname, String lname, String email)
    {
        this.emp_id = id;
        this.emp_fname = fname;
        this.emp_lname = lname;
        this.emp_email = email;
        this.pay_slip = Maps.newConcurrentMap();
    }

    public Map<YearMonth, Map<String, Integer>> getPay_slip()
    {
        return pay_slip;
    }

    public int getEmp_id()
    {
        return emp_id;
    }

    public String getEmp_fname()
    {
        return emp_fname;
    }

    public String getEmp_lname()
    {
        return emp_lname;
    }

    public String getEmp_email()
    {
        return emp_email;
    }

    @Override
    public int hashCode()
    {
        return emp_id;
    }

    @Override
    public boolean equals(Object obj)
    {
        Employee emp = (Employee) obj;
        if (emp.emp_id == this.emp_id)
            return true;
        return false;

    }

    public void addBreakupYearMonth(Map<String, Integer> breakup, int year, int month)
    {
        pay_slip.put(new YearMonth(year, month), ImmutableMap.<String, Integer> builder().putAll(breakup).build());
    }
}
