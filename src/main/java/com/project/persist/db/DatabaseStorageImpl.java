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
import com.project.config.DBConfig;
import com.project.employee.Employee;
import com.project.json.SmileConversion;
import com.project.persist.EmployeePersistance;
import com.project.persist.StorageException;
import com.project.persist.db.jdbi.FinanceDAO;
import java.util.ArrayList;
import java.util.List;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.ResultIterator;

public class DatabaseStorageImpl implements EmployeePersistance
{
    private final DBConfig db;
    private FinanceDAO dao;

    @Inject
    public DatabaseStorageImpl(DBConfig config)
    {
        this.db = config;
        DBI dbAccess = new DBI(db.getUrl(), db.getUser(), db.getPass());
        this.dao = dbAccess.open(FinanceDAO.class);
        try {
            dao.checkEmployeeCount();
        }
        catch (Exception e) {
            dao.createEmployee();
        }
    }

    @Override
    public void putDetail(int id, Employee employee) throws StorageException
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
            throw new StorageException(e);
        }

    }

    @Override
    public Employee getDetail(int id)
    {
        return dao.findEmployeeById(id);
    }

    @Override
    public List<Employee> findAllEmployees() throws StorageException
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
                    throw new StorageException(e);
                }
            }// end while
            return lst;
        }
    }
}
