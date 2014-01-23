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

import javax.ws.rs.QueryParam;

import com.google.common.base.Strings;
import com.google.common.collect.Table;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.project.employee.Employee;
import com.project.organization.Organization;
import com.project.persist.Storage;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.joda.time.YearMonth;

@Path("employee")
@Singleton
public class Finance
{

    private Storage org_store;

    @Inject
    public Finance(Storage str)
    {
        this.org_store = str;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String employeeFinanace()
    {
        return "Test : Welcome to Employee Finance";
    }

    private void checkAddOrgDetails(Organization org)
    {
        if (org.getOrg_id() <= 0)
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid Organization id " + org.getOrg_id()).build());
        else if (Strings.isNullOrEmpty(org.getOrg_name()) || org.getOrg_name().trim().isEmpty())
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid Organization Name : " + org.getOrg_name()).build());

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addOrganization(Organization org)
    {
        checkAddOrgDetails(org);
        if (org_store.getDetail(org.getOrg_id()) != null) {
            throw new WebApplicationException(Response.status(Status.CONFLICT).entity("Organization already exists").build());
        }
        org_store.putDetail(org.getOrg_id(), org);
        return Response.status(Status.CREATED).entity(org.getOrg_name() + " Organization created with id - " + org.getOrg_id()).build();
    }

    private void checkAddEmployeeDetails(int org_id, Employee emp)
    {
        if (org_id <= 0)
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid Organization Id " + org_id).build());
        else if (emp.getEmp_id() <= 0)
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid Employee Id " + emp.getEmp_id()).build());
        else if (Strings.isNullOrEmpty(emp.getEmp_fname()) || emp.getEmp_fname().trim().isEmpty())
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid Employee first name " + emp.getEmp_fname()).build());
        else if (Strings.isNullOrEmpty(emp.getEmp_lname()) || emp.getEmp_lname().trim().isEmpty())
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid Employee last name  " + emp.getEmp_lname()).build());
        else if (Strings.isNullOrEmpty(emp.getEmp_email()) || emp.getEmp_email().trim().isEmpty())
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid Employee email-id " + emp.getEmp_email()).build());
    }

    @POST
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addEmployee(final @PathParam("id") int org_id, Employee emp)
    {
        checkAddEmployeeDetails(org_id, emp);
        Organization org = org_store.getDetail(org_id);

        if (org != null) {
            if (org.getEmp(emp.getEmp_id()) != null) {
                throw new WebApplicationException(Response.status(Status.CONFLICT).entity("Employee already exists..").build());
            }
            org.addEmployeeToOrganization(emp);
            org_store.putDetail(org_id, org);
            return Response.status(Status.CREATED).entity("Employee with Id " + emp.getEmp_id() + " created successfully.").build();
        }
        else
            throw new WebApplicationException(Response.status(Status.NOT_FOUND).entity("Organization with id " + org_id + " not found.").build());
    }

    private void checkAddPaySlip(int org_id, int emp_id, Map<String, Integer> breakup, int year, int month)
    {
        if (org_id <= 0)
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid Organization Id " + org_id).build());
        else if (emp_id <= 0)
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid Employee Id " + emp_id).build());
        else if (breakup == null)
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid salary breakup details").build());
        else if (year < 2000 || year > Calendar.getInstance().get(Calendar.YEAR))
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid year " + year).build());
        else if (month <= 0 || month > 12)
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid month " + year).build());
    }

    @PUT
    @Path("/{o_id}/{e_id}/{year}/{month}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPayslip(final @PathParam("o_id") int org_id, final @PathParam("e_id") int emp_id, Map<String, Integer> breakup, final @PathParam("year") int year, final @PathParam("month") int month)
    {
        checkAddPaySlip(org_id, emp_id, breakup, year, month);
        Organization o = org_store.getDetail(org_id);
        if (o != null) {
            Employee emp = o.getEmp(emp_id);
            if (emp != null) {
                emp.addBreakupYearMonth(breakup, year, month);
                return Response.status(Status.CREATED).entity("Payslip added for employee-" + emp_id + " of organization id-" + org_id).build();
            }
            throw new WebApplicationException(Response.status(Status.NOT_FOUND).entity("Employee Id-" + emp_id + " does not exists in the organization " + org_id).build());
        }
        else
            throw new WebApplicationException(Response.status(Status.CONFLICT).entity("Organization with Id-" + org_id + " does not exist").build());
    }
    
    private void checkAddPaySlipTable(int org_id, int emp_id, Table<YearMonth, String, Integer> breakup)
    {
        if (org_id <= 0)
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid Organization Id " + org_id).build());
        else if (emp_id <= 0)
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid Employee Id " + emp_id).build());
        else if (breakup == null)
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid salary breakup details").build());
    }
    
    @PUT
    @Path("/{o_id}/{e_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPayslipUsingTable(final @PathParam("o_id") int org_id, 
        final @PathParam("e_id") int emp_id, 
        Table<YearMonth, String, Integer> breakup)
    {       
        checkAddPaySlipTable(org_id, emp_id, breakup);
        
        Organization o = org_store.getDetail(org_id);
        if (o != null) {
            Employee emp = o.getEmp(emp_id);
            if (emp != null) {
                emp.addBreakupYearMonthInTable(breakup);
                return Response.status(Status.CREATED).entity("Table : Payslip added for employee-" + emp_id + " of organization id-" + org_id).build();
            }
            throw new WebApplicationException(Response.status(Status.NOT_FOUND).entity("Table : Employee Id-" + emp_id + " does not exists in the organization " + org_id).build());
        }
        else
            throw new WebApplicationException(Response.status(Status.CONFLICT).entity("Table : Organization with Id-" + org_id + " does not exist").build());
    }
    

    private void checkValidOrgId(int org_id)
    {
        if (org_id <= 0)
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid Organization Id " + org_id).build());
    }

    @GET
    @Path("/{o_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Employee> showAllEmployeesOfOrganization(final @PathParam("o_id") int org_id)
    {
        checkValidOrgId(org_id);
        Organization o = org_store.getDetail(org_id);
        if (o != null)
            return o.getEmp_collection();
        return null;
    }

    @GET
    @Path("/{o_id}/{e_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Employee showEmployeeDetails(final @PathParam("o_id") int org_id, final @PathParam("e_id") int emp_id)
    {
        Organization org = org_store.getDetail(org_id);
        return org.getEmp(emp_id);
    }

    @GET
    @Path("/{o_id}/{e_id}/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Integer> showEmployeePaySlipDetails(final @PathParam("o_id") int org_id, final @PathParam("e_id") int emp_id, final @PathParam("year") int year, final @PathParam("month") int month)
    {
        Organization org = org_store.getDetail(org_id);
        Employee emp = org.getEmp(emp_id);

        Map<YearMonth, Map<String, Integer>> map = emp.getPay_slip();
        return map.get(new YearMonth(year, month));
    }
    
    @GET
    @Path("/{o_id}/{e_id}/salary")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Integer> showEmployeePayslipDetailsFromTable(
        final @PathParam("o_id") int org_id, final @PathParam("e_id") int emp_id, @QueryParam("yearmonth") String ym)
    {
        Organization org = org_store.getDetail(org_id);
        Employee emp = org.getEmp(emp_id);
        return emp.getPaySlipFromTable(YearMonth.parse(ym));
    }
}
