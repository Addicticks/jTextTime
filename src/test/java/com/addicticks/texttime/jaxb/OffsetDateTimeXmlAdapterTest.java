/*
 * Copyright 2018 Addicticks.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.addicticks.texttime.jaxb;

import static com.addicticks.texttime.jaxb.TestBase.OFFSET_SECONDS_TEST_VALUE;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OffsetDateTimeXmlAdapterTest {
    
    @Test
    public void testUnmarshal() {
        System.out.println("unmarshal");
        OffsetDateTimeXmlAdapter instance = new OffsetDateTimeXmlAdapter() {
            @Override
            public ZoneOffset getZoneOffsetForDateTime(LocalDateTime dateTime) {
                return ZoneOffset.ofTotalSeconds(OFFSET_SECONDS_TEST_VALUE);  // +03:45:49
            }
        };
        
        OffsetDateTime result;
        

        // More than 9 decimals on the seconds value should be 
        // gracefully handled using truncation.
        assertEquals( 
                instance.unmarshal("2018-03-14T23:30:28.123456789012345678901234567890Z"),
                instance.unmarshal("2018-03-14T23:30:28.123456789Z")
        );
        
        assertEquals( 
                instance.unmarshal("2018-03-14T23:30:28.123456789012345678901234567890"),
                instance.unmarshal("2018-03-14T23:30:28.123456789")
        );
        
        assertEquals( 
                instance.unmarshal("2018-03-14T23:30:28.123456789012345678901234567890+08:00"),
                instance.unmarshal("2018-03-14T23:30:28.123456789+08:00")
        );
        assertEquals( 
                instance.unmarshal("2018-03-14T23:30:28.123456789012345678901234567890-06:00"),
                instance.unmarshal("2018-03-14T23:30:28.123456789-06:00")
        );
        
        assertEquals(
                instance.unmarshal("2018-03-14T23:30:28.123z"),
                instance.unmarshal("2018-03-14T23:30:28.123Z")
        );

        assertEquals(
                instance.unmarshal("2018-03-14t23:30:28.123Z"),
                instance.unmarshal("2018-03-14T23:30:28.123Z")
        );


        
        
        assertNotEquals( 
                instance.unmarshal("2018-03-14T23:30:28.123456789012345678901234567890Z"),
                instance.unmarshal("2018-03-14T23:30:28.123456790Z")
        );
        
        result = instance.unmarshal("2018-03-14T23:30:28.123456789Z");
        assertEquals(2018, result.getYear());
        assertEquals(3, result.getMonthValue());
        assertEquals(14, result.getDayOfMonth());
        assertEquals(23, result.getHour());
        assertEquals(30, result.getMinute());
        assertEquals(28, result.getSecond());
        assertEquals(123456789, result.getNano());
        assertEquals(0, result.getOffset().getTotalSeconds());
        
        result = instance.unmarshal("2018-03-14T23:30:28.123456789-07:30");
        assertEquals(2018, result.getYear());
        assertEquals(3, result.getMonthValue());
        assertEquals(14, result.getDayOfMonth());
        assertEquals(23, result.getHour());
        assertEquals(30, result.getMinute());
        assertEquals(28, result.getSecond());
        assertEquals(123456789, result.getNano());
        assertEquals(-((7*60*60) + (30*60)), result.getOffset().getTotalSeconds());
        
        result = instance.unmarshal("2018-03-14T23:30:28.123456789");
        assertEquals(2018, result.getYear());
        assertEquals(3, result.getMonthValue());
        assertEquals(14, result.getDayOfMonth());
        assertEquals(23, result.getHour());
        assertEquals(30, result.getMinute());
        assertEquals(28, result.getSecond());
        assertEquals(123456789, result.getNano());
        assertEquals(OFFSET_SECONDS_TEST_VALUE, result.getOffset().getTotalSeconds());
        
        
        // Test where ' ' is used as delimiter between date and time value.
        // Not compliant with XML Schema definition but we allow it anyway.
        result = instance.unmarshal("2018-03-14 23:30:28.123456789");
        assertEquals(2018, result.getYear());
        assertEquals(3, result.getMonthValue());
        assertEquals(14, result.getDayOfMonth());
        assertEquals(23, result.getHour());
        assertEquals(30, result.getMinute());
        assertEquals(28, result.getSecond());
        assertEquals(123456789, result.getNano());
        assertEquals(OFFSET_SECONDS_TEST_VALUE, result.getOffset().getTotalSeconds());
    }

    
    @Test(expected = DateTimeParseException.class)
    public void testUnmarshalEx1() {
        OffsetDateTimeXmlAdapter instance = new OffsetDateTimeXmlAdapter();
        instance.unmarshal("2018-03-14T23:30:28.123456x89");
    }
    
    @Test(expected = DateTimeParseException.class)
    public void testUnmarshalEx2() {
        OffsetDateTimeXmlAdapter instance = new OffsetDateTimeXmlAdapter();
        instance.unmarshal("2018-03-14T23:30:28.1234567890123X");
    }
    
    
    
    
    @Test
    public void testMarshal() {
        System.out.println("marshal");
        OffsetDateTimeXmlAdapter instance = new OffsetDateTimeXmlAdapter();
        OffsetDateTime t;
        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 28, 123456789, ZoneOffset.UTC);
        assertEquals("2018-03-14T23:30:28.123456789Z", instance.marshal(t));
        
        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 28, 0, ZoneOffset.UTC);
        assertEquals("2018-03-14T23:30:28Z", instance.marshal(t));
        
        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 0, 0, ZoneOffset.ofHoursMinutes(2, 0));
        assertEquals("2018-03-14T23:30:00+02:00", instance.marshal(t));
    }
    
    /**
     * Test of marshal method, of class OffsetDateTimeXmlAdapter with null value. (Ex. optional attributes)
     */
    @Test
    public void testMarshalNullValue() {
        
        OffsetDateTimeXmlAdapter instance = new OffsetDateTimeXmlAdapter();
        OffsetDateTime t = null;
        
        assertNull(instance.marshal(t));
    }
    
}
