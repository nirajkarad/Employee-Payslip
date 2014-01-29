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
package com.project.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * RIP 2009-06-25. Returns a JacksonJsonProvider with a custom ObjectMapper to
 * allow mapping of custom types.
 */
@Singleton
public class JacksonJsonProviderWrapper implements Provider<JacksonJsonProvider>
{
    private final ObjectMapper objectMapper;

    @Inject
    public JacksonJsonProviderWrapper(final ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    public JacksonJsonProvider get()
    {
        return new JacksonJsonProvider(objectMapper);
    }
}
