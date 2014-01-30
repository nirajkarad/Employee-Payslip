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
package com.project.resource;

import com.google.common.base.Strings;
import com.google.common.collect.Table;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.project.employee.Employee;
import com.project.persist.PersistanceAPI;
import com.project.persist.PersistanceException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.joda.time.YearMonth;

@Path("emptest")
@Singleton
public class EmployeeResource
{
    
    private final PersistanceAPI empStore;
    
    @Inject
    public EmployeeResource(PersistanceAPI api)
    {
        this.empStore = api;
    }

    public void provideReuired(int inMem, int database)
    {
        
    }
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String displayTest()
    {
        return "Welcome to revised edition";    
    }

    @GET
    @Path("/all/{show}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Employee> showAllEmployeeDetails(final @PathParam("show") int show)
    {
        List<Employee> lst = new ArrayList<Employee>();
        for (int id = 1; id <= show; id++) {
            lst.add(empStore.getDetail(id));
        }
        return lst;
    }

    private void checkEmployeeID(int empId)
    {
        if (empId <= 0)
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid Employee id : " + empId).build());
    }

    private void checkAddEmployeeDetails(Employee emp)
    {
        if (Strings.isNullOrEmpty(emp.getFirstName()) || emp.getFirstName().trim().isEmpty())
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid Employee first name : " + emp.getFirstName()).build());
        else if (Strings.isNullOrEmpty(emp.getLastName()) || emp.getLastName().trim().isEmpty())
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid Employee last name : " + emp.getLastName()).build());
        else if (Strings.isNullOrEmpty(emp.getEmail()) || emp.getEmail().trim().isEmpty())
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid Employee email-id : " + emp.getEmail()).build());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addEmployee(Employee employee)
    {
        checkAddEmployeeDetails(employee);
        try {
            empStore.putDetail(employee.getId(), employee);
        }
        catch (PersistanceException e) {
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Error is storing employee : " + employee.getId()).build());
        }
        return Response.status(Status.CREATED).entity("Employee with Id : " + employee.getId() + " created successfully.").build();
    }

    private void checkSalBreakup(Object breakup)
    {
        if (breakup == null)
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid salary breakup details.. ").build());
    }

    @POST
    @Path("/{empId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPayslipUsingTable(final @PathParam("empId") int empId, Table<YearMonth, String, Integer> breakup)
    {
        checkEmployeeID(empId);
        checkSalBreakup(breakup);
        Employee emp = empStore.getDetail(empId);
        if (emp == null)
            throw new WebApplicationException(Response.status(Status.NOT_FOUND).entity("Employee Not present : " + empId).build());

        if (emp.getPaySlipFromTable(breakup.rowKeySet().iterator().next()).isEmpty()) {
            emp.addBreakupYearMonthInTable(breakup);
            try {
                empStore.putDetail(emp.getId(), emp);
            }
            catch (PersistanceException e) {
                throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Error is storing employee : " + empId + " while adding payslip").build());
            }
            return Response.status(Status.CREATED).entity("Table : Payslip added for employee : " + empId).build();
        }
        else
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Payslip already exists for give Year/Month").build());
    }

    @GET
    @Path("/{empId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Employee showEmployeeDetails(final @PathParam("empId") int empId)
    {
        checkEmployeeID(empId);
        Employee emp = empStore.getDetail(empId);
        if (emp == null)
            throw new WebApplicationException(Response.status(Status.NOT_FOUND).entity("Employee Not present : " + empId).build());
        return emp;
    }
}
