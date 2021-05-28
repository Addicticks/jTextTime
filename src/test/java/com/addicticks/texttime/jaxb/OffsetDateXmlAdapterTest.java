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
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OffsetDateXmlAdapterTest {

    @Test
    public void testUnmarshal() {
        OffsetDateXmlAdapter instance = new OffsetDateXmlAdapter() {
            @Override
            public ZoneOffset getZoneOffsetForDate(LocalDate date) {
                return ZoneOffset.ofTotalSeconds(OFFSET_SECONDS_TEST_VALUE);  // +03:45:49
            }
        };
        
        OffsetDateTime result;
        try {
            result = instance.unmarshal("2018-03-14T23:30:28.123456789012345678901234567890Z");
        } catch (Exception ex) {
            assertTrue(ex instanceof DateTimeParseException);
        }
        
        
        result = instance.unmarshal("2018-03-14Z");
        assertEquals(2018, result.getYear());
        assertEquals(3, result.getMonthValue());
        assertEquals(14, result.getDayOfMonth());
        assertEquals(0, result.getHour());
        assertEquals(0, result.getMinute());
        assertEquals(0, result.getSecond());
        assertEquals(0, result.getNano());
        assertEquals(0, result.getOffset().getTotalSeconds());
        
        result = instance.unmarshal("2018-03-14-07:30");
        assertEquals(2018, result.getYear());
        assertEquals(3, result.getMonthValue());
        assertEquals(14, result.getDayOfMonth());
        assertEquals(0, result.getHour());
        assertEquals(0, result.getMinute());
        assertEquals(0, result.getSecond());
        assertEquals(0, result.getNano());
        assertEquals(-((7*60*60) + (30*60)), result.getOffset().getTotalSeconds());
        
        result = instance.unmarshal("2018-03-14");
        assertEquals(2018, result.getYear());
        assertEquals(3, result.getMonthValue());
        assertEquals(14, result.getDayOfMonth());
        assertEquals(0, result.getHour());
        assertEquals(0, result.getMinute());
        assertEquals(0, result.getSecond());
        assertEquals(0, result.getNano());
        assertEquals(OFFSET_SECONDS_TEST_VALUE, result.getOffset().getTotalSeconds());
        
    }

    @Test
    public void testMarshal() {
        OffsetDateXmlAdapter instance = new OffsetDateXmlAdapter();
        OffsetDateTime t;
        
        t = OffsetDateTime.of(2018, 3, 14, 0, 0, 0, 0, ZoneOffset.UTC);
        assertEquals("2018-03-14Z", instance.marshal(t));        
        
        t = OffsetDateTime.of(2018, 3, 14, 0, 0, 0, 0, ZoneOffset.ofHoursMinutes(2, 0));
        assertEquals("2018-03-14+02:00", instance.marshal(t));
    }
    
    /**
     * Test of marshal method, of class OffsetDateXmlAdapter with null value. (Ex. optional attributes)
     */
    @Test
    public void testMarshalNullValue() {
        OffsetDateXmlAdapter instance = new OffsetDateXmlAdapter();
        OffsetDateTime t = null;
        
        
        assertNull( instance.marshal(t));        
    }
    
}
