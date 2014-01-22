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


import com.project.modules.EmployeeServletListener;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import com.google.inject.servlet.GuiceFilter;

public class JettyLauncher {

    public static void main(String[] args) throws Exception {
         // Create the server.
          Server server = new Server(8081);
           
          // Create a servlet context and add the jersey servlet.
          ServletContextHandler sch = new ServletContextHandler(server, "/");
           
          // Add our Guice listener that includes our bindings
          sch.addEventListener(new EmployeeServletListener());
           
          // Then add GuiceFilter and configure the server to
          // reroute all requests through this filter.
          sch.addFilter(GuiceFilter.class, "/*", null);
           
          // Must add DefaultServlet for embedded Jetty.
          // Failing to do this will cause 404 errors.
          // This is not needed if web.xml is used instead.
          sch.addServlet(DefaultServlet.class, "/");
           
          // Start the server
          server.start();
          server.join();
    }

}

