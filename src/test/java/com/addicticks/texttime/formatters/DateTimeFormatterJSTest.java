/*
 * Copyright 2019 Addicticks.
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
package com.addicticks.texttime.formatters;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author lars
 */
public class DateTimeFormatterJSTest {
    
   

    @Test
    public void testParseIntoOffsetDateTime() {
        System.out.println("parseIntoOffsetDateTime");

        OffsetDateTime t;

        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 28, 123456789, ZoneOffset.UTC);
        assertEquals(t, DateTimeFormatterJS.parseIntoOffsetDateTime("2018-03-14T23:30:28.123456789Z"));
        
        t = OffsetDateTime.of(2018, 3, 14, 0, 0, 0, 0, ZoneOffset.UTC);
        assertEquals(t, DateTimeFormatterJS.parseIntoOffsetDateTime("2018-03-14"));

        t = OffsetDateTime.of(2018, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
        assertEquals(t, DateTimeFormatterJS.parseIntoOffsetDateTime("2018"));

        t = OffsetDateTime.of(2018, 1, 1, 23, 0, 0, 0, ZoneOffset.UTC);
        assertEquals(t, DateTimeFormatterJS.parseIntoOffsetDateTime("2018T23"));

        t = OffsetDateTime.of(2018, 4, 1, 23, 0, 0, 0, ZoneOffset.UTC);
        assertEquals(t, DateTimeFormatterJS.parseIntoOffsetDateTime("2018-04T23"));

        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 28, 123456789, ZoneOffset.ofHoursMinutes(2, 30));
        assertEquals(t, DateTimeFormatterJS.parseIntoOffsetDateTime("2018-03-14T23:30:28.123456789+02:30"));
        
        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 28, 123456789, ZoneOffset.ofHoursMinutes(2, 0));
        assertEquals(t, DateTimeFormatterJS.parseIntoOffsetDateTime("2018-03-14T23:30:28.123456789+02"));

        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 28, 123456789, ZoneOffset.ofHoursMinutes(2, 0));
        assertEquals(t, DateTimeFormatterJS.parseIntoOffsetDateTime("2018-03-14T23:30:28.123456789+02"));

        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 0, 0, ZoneOffset.ofHoursMinutes(2, 0));
        assertEquals(t, DateTimeFormatterJS.parseIntoOffsetDateTime("2018-03-14T23:30+02"));
        
        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 28, 0, ZoneOffset.ofHoursMinutes(2, 0));
        assertEquals(t, DateTimeFormatterJS.parseIntoOffsetDateTime("2018-03-14T23:30:28+02"));

        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 0, 0, ZoneOffset.UTC);
        assertEquals(t, DateTimeFormatterJS.parseIntoOffsetDateTime("2018-03-14T23:30"));
        
        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 28, 0, ZoneOffset.UTC);
        assertEquals(t, DateTimeFormatterJS.parseIntoOffsetDateTime("2018-03-14T23:30:28"));

        
    }

    
    @Test
    public void testFormatJSDateTime() { 
        System.out.println("DateTimeFormatterJS.format()");
        OffsetDateTime t;

        LocalTime now = LocalTime.now();
        System.out.println("Pre-Truncate:  " + now);
        DateTimeFormatter dtf = DateTimeFormatter.ISO_TIME;
        System.out.println("Post-Truncate: " + now.truncatedTo(ChronoUnit.SECONDS).format(dtf));

        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 28, 123456789, ZoneOffset.UTC);
        assertEquals("2018-03-14T23:30:28.123Z", DateTimeFormatterJS.format(t));

        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 28, 0, ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS);
        assertEquals("2018-03-14T23:30:28.000Z", DateTimeFormatterJS.format(t));
        
        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 28, 100_000_000, ZoneOffset.UTC);
        assertEquals("2018-03-14T23:30:28.100Z", DateTimeFormatterJS.format(t));
        
        t = OffsetDateTime.of(2018, 3, 14, 0, 0, 0, 0, ZoneOffset.UTC);
        assertEquals("2018-03-14", DateTimeFormatterJS.format(t));
        
        t = DateTimeFormatterJS.parseIntoOffsetDateTime("2018-03-14");
        assertEquals("2018-03-14", DateTimeFormatterJS.format(t));
        
        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 0, 0, ZoneOffset.ofHoursMinutes(2, 0));
        assertEquals("2018-03-14T23:30:00.000+02:00", DateTimeFormatterJS.format(t));

        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 0, 400000000, ZoneOffset.ofHoursMinutes(2, 0));
        assertEquals("2018-03-14T23:30:00.400+02:00", DateTimeFormatterJS.format(t));
        
        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 28, 123098623, ZoneOffset.UTC);
        assertEquals("2018-03-14T23:30:28.123Z", DateTimeFormatterJS.format(t));
        
        
    }



    
}
