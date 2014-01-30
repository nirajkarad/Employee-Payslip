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
package com.project.persist.map;

import com.project.organization.Organization;

import com.project.persist.OrganizationPersistance;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OrganizationPersistanceMapImpl implements OrganizationPersistance
{

    Map<Integer, Organization> map = new ConcurrentHashMap<Integer, Organization>();

    public void putDetail(int id, Organization o)
    {
        map.put(id, o);
    }

    public Organization getDetail(int id)
    {
        return map.get(id);
    }

}
