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
package com.project.persist.db.jdbi;

import org.skife.jdbi.v2.sqlobject.customizers.FetchSize;

import com.project.employee.Employee;
import java.sql.Blob;
import org.skife.jdbi.v2.ResultIterator;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

public interface FinanceDAO
{    
    @SqlUpdate("create table employee (id int primary key,value blob)")
    void createEmployee();
    
    @SqlQuery("select count(*) from employee")
    int checkEmployeeCount();

    @SqlUpdate("insert into employee (id, value) values (:id, :value)")
    int insertEmployee(@Bind("id") int id, @Bind("value") Blob value);
    
    @SqlUpdate("update employee set value=:value where id = :id")
    int updateEmployee(@Bind("id") int id, @Bind("value") Blob value);
    
    @Mapper(EmployeeMapper.class)
    @SqlQuery("select value from employee where id = :id")
    Employee findEmployeeById(@Bind("id") int id);
    
    @SqlQuery("select * from employee")
    @Mapper(EmployeeMapper.class)
    @FetchSize(1000)
    ResultIterator<Employee> findAll();
    
}
