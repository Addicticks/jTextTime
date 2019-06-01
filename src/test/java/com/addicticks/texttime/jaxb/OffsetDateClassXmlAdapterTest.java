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

import com.addicticks.texttime.OffsetDate;
import static com.addicticks.texttime.jaxb.TestBase.OFFSET_SECONDS_TEST_VALUE;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OffsetDateClassXmlAdapterTest {

    @Test
    public void testUnmarshal() {
        OffsetDateClassXmlAdapter instance = new OffsetDateClassXmlAdapter() {
            @Override
            public ZoneOffset getZoneOffsetForDate(LocalDate localDate) {
                return ZoneOffset.ofTotalSeconds(OFFSET_SECONDS_TEST_VALUE);  // +03:45:49
            }
        };
        
        OffsetDate result;
        
        
        result = instance.unmarshal("2018-03-14Z");
        assertEquals(2018, result.getDate().getYear());
        assertEquals(3, result.getDate().getMonthValue());
        assertEquals(14, result.getDate().getDayOfMonth());
        assertEquals(0, result.getDate().getHour());
        assertEquals(0, result.getDate().getMinute());
        assertEquals(0, result.getDate().getSecond());
        assertEquals(0, result.getDate().getNano());
        assertEquals(0, result.getDate().getOffset().getTotalSeconds());
        
        result = instance.unmarshal("2018-03-14-07:30");
        assertEquals(2018, result.getDate().getYear());
        assertEquals(3, result.getDate().getMonthValue());
        assertEquals(14, result.getDate().getDayOfMonth());
        assertEquals(0, result.getDate().getHour());
        assertEquals(0, result.getDate().getMinute());
        assertEquals(0, result.getDate().getSecond());
        assertEquals(0, result.getDate().getNano());
        assertEquals(-((7*60*60) + (30*60)), result.getDate().getOffset().getTotalSeconds());
        
        result = instance.unmarshal("2018-03-14");
        assertEquals(2018, result.getDate().getYear());
        assertEquals(3, result.getDate().getMonthValue());
        assertEquals(14, result.getDate().getDayOfMonth());
        assertEquals(0, result.getDate().getHour());
        assertEquals(0, result.getDate().getMinute());
        assertEquals(0, result.getDate().getSecond());
        assertEquals(0, result.getDate().getNano());
        assertEquals(OFFSET_SECONDS_TEST_VALUE, result.getDate().getOffset().getTotalSeconds());
        
    }

    @Test
    public void testMarshal() {
        OffsetDateClassXmlAdapter instance = new OffsetDateClassXmlAdapter();
        OffsetDate t;
        
        t = OffsetDate.of(2018, 3, 14, ZoneOffset.UTC);
        assertEquals("2018-03-14Z", instance.marshal(t));        
        
        t = OffsetDate.of(2018, 3, 14, ZoneOffset.ofHoursMinutes(2, 0));
        assertEquals("2018-03-14+02:00", instance.marshal(t));
    }
    
}
