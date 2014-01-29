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

import java.io.ByteArrayInputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.collect.Table;
import com.project.employee.Employee;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import org.joda.time.YearMonth;

public class SmileConversion
{
    private static ObjectMapper objectMapper;
    
    static
    {
        objectMapper = new ObjectMapper(new SmileFactory());
        SimpleModule sm = new SimpleModule();
        sm.addSerializer(Table.class, new GuavaTableSerializer());
        sm.addDeserializer(Table.class, new GuavaTableDeserializer());
        objectMapper.registerModules(new GuavaModule() , sm, 
        new JodaModule().addKeyDeserializer(YearMonth.class, new YearMonthKeyDeserializer()));
    }
    

    public static Blob smileCompressionSerialize(Employee emp) throws IOException, SerialException, SQLException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        GZIPOutputStream zos = new GZIPOutputStream(bos);
        objectMapper.writeValue(zos, emp);
        zos.close();
        return new SerialBlob(bos.toByteArray());
    }
    
    public static Employee smileDecompressionDeSerializeS(Blob blob) throws IOException, SerialException, SQLException, ClassNotFoundException
    {
        Employee emp = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(blob.getBytes(1, (int)blob.length()));
        GZIPInputStream zis = new GZIPInputStream(bis);
        emp = objectMapper.readValue(zis, Employee.class);
        zis.close();
        return emp;
    }

}
