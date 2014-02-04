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
package com.project.persist.db;

import com.google.inject.Inject;

import com.project.employee.Employee;
import com.project.json.SmileConversion;
import com.project.persist.EmployeePersistance;
import com.project.persist.PersistanceException;
import com.project.persist.db.jdbi.EmployeeDAO;
import java.util.ArrayList;
import java.util.List;
import org.skife.jdbi.v2.ResultIterator;

public class EmployeeDatabaseStorageImpl implements EmployeePersistance
{
    private EmployeeDAO dao;
    
    @Inject
    public EmployeeDatabaseStorageImpl(EmployeeDAO dao)
    {
        this.dao = dao;
    }

    @Override
    public void putDetail(int id, Employee employee) throws PersistanceException
    {
        try {
            if (getDetail(id) == null) {
                dao.insertEmployee(id, SmileConversion.smileCompressionSerialize(employee));
            }
            else {
                dao.updateEmployee(id, SmileConversion.smileCompressionSerialize(employee));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new PersistanceException(e);
        }

    }

    @Override
    public Employee getDetail(int id)
    {
        return dao.findEmployeeById(id);
    }

    @Override
    public List<Employee> findAllEmployees() throws PersistanceException
    {
        List<Employee> lst = new ArrayList<Employee>();
        ResultIterator<Employee> rs = dao.findAll();
        
        if (rs == null)
            return null;
        else {
            while (rs.hasNext()) {
                try {
                    Employee emp = rs.next();
                    lst.add(emp);
                }
                catch (Exception e) {
                    throw new PersistanceException(e);
                }
            }// end while
            return lst;
        }
    }

    @Override
    public int getType()
    {
        return EmployeePersistance.DATABASE;
    }

    @Override
    public boolean addBinding()
    {
        return EmployeePersistance.FALSE;
    }
}
