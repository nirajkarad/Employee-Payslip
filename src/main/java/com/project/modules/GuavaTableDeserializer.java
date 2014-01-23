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
package com.project.modules;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import java.io.IOException;
import java.util.Map;
import org.joda.time.YearMonth;

public class GuavaTableDeserializer extends JsonDeserializer<Table<YearMonth, String, Integer>>
{

    @Override
    public Table<YearMonth, String, Integer> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException
    {
        final ImmutableTable.Builder<YearMonth, String, Integer> tableBuilder = ImmutableTable.builder();

        final Map<String, Map<String, String>> rowMap = jp.readValueAs(Map.class);

        for (final Map.Entry<String, Map<String, String>> rowEntry : rowMap.entrySet()) {

            final YearMonth rowKey = YearMonth.parse(rowEntry.getKey());

            for (final Map.Entry<String, String> cellEntry : rowEntry.getValue().entrySet()) {
                final String colKey = cellEntry.getKey();
                final Integer val = Integer.parseInt(cellEntry.getValue());
                tableBuilder.put(rowKey, colKey, val);
            }
        }
        return tableBuilder.build();
    }
}
