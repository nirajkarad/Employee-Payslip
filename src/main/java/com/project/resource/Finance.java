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
import com.project.organization.Organization;
import com.project.persist.OrganizationPersistance;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.joda.time.YearMonth;
import org.joda.time.format.DateTimeFormat;

@Path("employee")
@Singleton
public class Finance
{

    private OrganizationPersistance orgStore;

    @Inject
    public Finance(OrganizationPersistance str)
    {
        this.orgStore = str;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String employeeFinanace()
    {
        return "Welcome to Employee Finance";
    }

    private void checkOrganizationID(int orgId)
    {
        if (orgId <= 0)
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid Organization id : " + orgId).build());
    }

    private void checkEmployeeID(int empId)
    {
        if (empId <= 0)
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid Employee id : " + empId).build());
    }

    private void checkOrganizationDetails(Organization org)
    {
        checkOrganizationID(org.getId());
        if (Strings.isNullOrEmpty(org.getName()) || org.getName().trim().isEmpty())
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid Organization Name : " + org.getName()).build());

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addOrganization(Organization org)
    {
        checkOrganizationDetails(org);
        if (orgStore.getDetail(org.getId()) != null) {
            throw new WebApplicationException(Response.status(Status.CONFLICT).entity("Organization already exists...").build());
        }
        orgStore.putDetail(org.getId(), org);
        return Response.status(Status.CREATED).entity(org.getName() + " Organization created with id : " + org.getId()).build();
    }

    private void checkAddEmployeeDetails(int orgId, Employee emp)
    {
        checkOrganizationID(orgId);
        checkEmployeeID(emp.getId());
        if (Strings.isNullOrEmpty(emp.getFirstName()) || emp.getFirstName().trim().isEmpty())
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid Employee first name : " + emp.getFirstName()).build());
        else if (Strings.isNullOrEmpty(emp.getLastName()) || emp.getLastName().trim().isEmpty())
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid Employee last name : " + emp.getLastName()).build());
        else if (Strings.isNullOrEmpty(emp.getEmail()) || emp.getEmail().trim().isEmpty())
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid Employee email-id : " + emp.getEmail()).build());
    }

    private Organization getOrganization(int orgId)
    {
        Organization org = orgStore.getDetail(orgId);
        if (org == null)
            throw new WebApplicationException(Response.status(Status.NOT_FOUND).entity("Organization : " + orgId + " not found.").build());
        return org;
    }

    private Employee getEmployee(int orgId, int empId)
    {
        Organization org = getOrganization(orgId);
        Employee emp = org.getEmployee(empId);
        if (emp == null)
            throw new WebApplicationException(Response.status(Status.NOT_FOUND).entity("Employee : " + empId + " not found.").build());
        return emp;
    }

    @POST
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addEmployee(final @PathParam("id") int orgId, Employee employee)
    {
        checkAddEmployeeDetails(orgId, employee);
        Organization org = getOrganization(orgId);

        if (org.getEmployee(employee.getId()) != null) {
            throw new WebApplicationException(Response.status(Status.CONFLICT).entity("Employee already exists..").build());
        }
        org.addEmployeeToOrganization(employee);

        orgStore.putDetail(orgId, org);

        return Response.status(Status.CREATED).entity("Employee with Id : " + employee.getId() + " created successfully.").build();
    }

    private void checkPaySlipDetails(int orgId, int empId, int year, int month)
    {
        checkOrganizationID(orgId);
        checkEmployeeID(empId);
        if (year < 2000 || year > Calendar.getInstance().get(Calendar.YEAR))
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid year : " + year).build());
        else if (month <= 0 || month > 12)
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid month : " + year).build());

    }

    private void checkSalBreakup(Object breakup)
    {
        if (breakup == null)
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid salary breakup details.. ").build());
    }

    @PUT
    @Path("/{orgId}/{empId}/{year}/{month}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPayslip(final @PathParam("orgId") int orgId, final @PathParam("empId") int empId, Map<String, Integer> breakup, final @PathParam("year") int year, final @PathParam("month") int month)
    {
        checkPaySlipDetails(orgId, empId, year, month);
        checkSalBreakup(breakup);
        Employee emp = getEmployee(orgId, empId);
        if (emp.getPayslipForSpecificMonth(year, month) == null) {
            emp.addBreakupYearMonth(breakup, year, month);
        }
        else {
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Payslip already exists for give year(" + year + ") & month(" + month + ")").build());
        }
        return Response.status(Status.CREATED).entity("Payslip added for employee : " + empId).build();
    }

    @PUT
    @Path("/{orgId}/{empId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPayslipUsingTable(final @PathParam("orgId") int orgId, final @PathParam("empId") int empId, Table<YearMonth, String, Integer> breakup)
    {
        checkOrganizationID(orgId);
        checkEmployeeID(empId);
        checkSalBreakup(breakup);

        Employee emp = getEmployee(orgId, empId);
        if (emp.getPaySlipFromTable(breakup.rowKeySet().iterator().next()).isEmpty()) {
            emp.addBreakupYearMonthInTable(breakup);
            return Response.status(Status.CREATED).entity("Table : Payslip added for employee : " + empId).build();
        }
        else
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Payslip already exists for give Year/Month").build());

    }

    @GET
    @Path("/{orgId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<Employee> showAllEmployeesOfOrganization(final @PathParam("orgId") int orgId)
    {
        checkOrganizationID(orgId);
        return getOrganization(orgId).getEmployeeList();
    }

    @GET
    @Path("/{orgId}/{empId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Employee showEmployeeDetails(final @PathParam("orgId") int orgId, final @PathParam("empId") int empId)
    {
        checkOrganizationID(orgId);
        checkEmployeeID(empId);
        return getEmployee(orgId, empId);
    }

    @GET
    @Path("/{orgId}/{empId}/{year}/{month}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Integer> showEmployeePaySlipDetails(final @PathParam("orgId") int orgId, final @PathParam("empId") int empId, final @PathParam("year") int year, final @PathParam("month") int month)
    {
        checkPaySlipDetails(orgId, empId, year, month);
        Employee emp = getEmployee(orgId, empId);
        Map<YearMonth, Map<String, Integer>> paySlip = emp.getPaySlip();
        if (paySlip == null)
            throw new WebApplicationException(Response.status(Status.NOT_FOUND).entity("Payslip Does not exists for (Year/Month) : (" + year + "/" + month + ")").build());
        else
            return paySlip.get(new YearMonth(year, month));
    }

    @GET
    @Path("/{orgId}/{empId}/salary")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Integer> showEmployeePayslipDetailsFromTable(final @PathParam("orgId") int orgId, final @PathParam("empId") int empId, @QueryParam("yearmonth") String yearMonth)
    {
        checkOrganizationID(orgId);
        checkEmployeeID(empId);
        Employee emp = getEmployee(orgId, empId);
        YearMonth ym = YearMonth.parse(yearMonth, DateTimeFormat.forPattern("YYYY-MM"));
        if (ym == null)
            throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).entity("Invalid (Year/Month) : " + yearMonth).build());
        Map<String, Integer> salBreakup = emp.getPaySlipFromTable(ym);
        if (salBreakup == null)
            throw new WebApplicationException(Response.status(Status.NOT_FOUND).entity("Payslip Does not exists for (Year/Month) : " + yearMonth).build());
        return salBreakup;
    }
}
