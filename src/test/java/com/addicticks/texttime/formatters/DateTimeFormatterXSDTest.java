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

import com.addicticks.texttime.formatters.DateTimeFormatterXSD;
import com.addicticks.texttime.OffsetDate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author lars
 */
public class DateTimeFormatterXSDTest {
    
   

    /**
     * Test of parseIntoOffsetDateTime method, of class DateTimeFormatterXSDTest.
     */
    @Test
    public void testParseIntoOffsetDateTime() {
        System.out.println("parseIntoOffsetDateTime");

        OffsetDateTime t;

        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 28, 123456789, ZoneOffset.UTC);
        assertEquals(t, DateTimeFormatterXSD.parseIntoOffsetDateTime("2018-03-14T23:30:28.123456789Z", ZoneOffset.ofHoursMinutes(9, 0)));
        
        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 0, 0, ZoneOffset.UTC);
        assertEquals(t, DateTimeFormatterXSD.parseIntoOffsetDateTime("2018-03-14T23:30:00Z", ZoneOffset.ofHoursMinutes(9, 0)));

        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 0, 0, ZoneOffset.UTC);
        assertEquals(t, DateTimeFormatterXSD.parseIntoOffsetDateTime("2018-03-14T23:30:00z", ZoneOffset.ofHoursMinutes(9, 0)));
        
        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 0, 0, ZoneOffset.ofHoursMinutes(2, 0));
        assertEquals(t, DateTimeFormatterXSD.parseIntoOffsetDateTime("2018-03-14T23:30:00+02:00", ZoneOffset.ofHoursMinutes(9, 0)));

        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 0, 0, ZoneOffset.ofHoursMinutes(2, 0));
        assertEquals(t, DateTimeFormatterXSD.parseIntoOffsetDateTime("2018-03-14T23:30:00+02", ZoneOffset.ofHoursMinutes(9, 0)));

        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 0, 0, ZoneOffset.ofHoursMinutes(2, 0));
        assertEquals(t, DateTimeFormatterXSD.parseIntoOffsetDateTime("2018-03-14T23:30:00", ZoneOffset.ofHoursMinutes(2, 0)));

        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 0, 0, ZoneOffset.ofHoursMinutes(2, 0));
        assertEquals(t, DateTimeFormatterXSD.parseIntoOffsetDateTime("2018-03-14 23:30:00", ZoneOffset.ofHoursMinutes(2, 0)));

        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 0, 0, ZoneOffset.ofHoursMinutes(2, 0));
        assertEquals(t, DateTimeFormatterXSD.parseIntoOffsetDateTime("2018-03-14t23:30:00", ZoneOffset.ofHoursMinutes(2, 0)));
        
        
        t = OffsetDateTime.of(2018, 3, 14,23, 30, 28, 123456789, ZoneOffset.UTC);
        assertEquals(t, DateTimeFormatterXSD.parseIntoOffsetDateTime("2018-03-14T23:30:28.12345678901234567890123456789Z", ZoneOffset.ofHoursMinutes(9, 0)));

        t = OffsetDateTime.of(2018, 3, 14,23, 30, 28, 123456789, ZoneOffset.UTC);
        assertEquals(t, DateTimeFormatterXSD.parseIntoOffsetDateTime("2018-03-14T23:30:28.12345678901234567890123456789", ZoneOffset.UTC));
        
        t = OffsetDateTime.of(2018, 3, 14,23, 30, 28, 123456789, ZoneOffset.ofHoursMinutes(2, 0));
        assertEquals(t, DateTimeFormatterXSD.parseIntoOffsetDateTime("2018-03-14T23:30:28.12345678901234567890123456789+02:00", ZoneOffset.ofHoursMinutes(9, 0)));
        
        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 28, 123456789, ZoneOffset.ofHoursMinutes(2, 0));
        assertEquals(t, DateTimeFormatterXSD.parseIntoOffsetDateTime("2018-03-14T23:30:28.12345678901234567890123456789+02", ZoneOffset.ofHoursMinutes(9, 0)));
        
        
    }

    /**
     * Test of parseIntoOffsetDate method, of class DateTimeFormatterXSDTest.
     */
    @Test
    public void testParseIntoOffsetDate() {
        System.out.println("parseIntoOffsetDate");

        OffsetDate t;

        t = OffsetDate.of(2018, 3, 14, ZoneOffset.UTC);
        assertEquals(t, DateTimeFormatterXSD.parseIntoOffsetDate("2018-03-14Z", ZoneOffset.ofHoursMinutes(9, 0)));
        
        t = OffsetDate.of(2018, 3, 14, ZoneOffset.UTC);
        assertEquals(t, DateTimeFormatterXSD.parseIntoOffsetDate("2018-03-14z", ZoneOffset.ofHoursMinutes(9, 0)));
        
        
    }

    /**
     * Test of parseIntoOffsetDateTimeNoTime method, of class DateTimeFormatterXSDTest.
     */
    @Test
    public void testParseIntoOffsetDateTimeNoTime() {
        System.out.println("parseIntoOffsetDateTimeNoTime");

        OffsetDateTime t;

        t = OffsetDateTime.of(2018, 3, 14, 0, 0, 0, 0, ZoneOffset.UTC);
        assertEquals(t, DateTimeFormatterXSD.parseIntoOffsetDateTimeNoTime("2018-03-14Z", ZoneOffset.ofHoursMinutes(9, 0)));
        
        t = OffsetDateTime.of(2018, 3, 14, 0, 0, 0, 0, ZoneOffset.UTC);
        assertEquals(t, DateTimeFormatterXSD.parseIntoOffsetDateTimeNoTime("2018-03-14z", ZoneOffset.ofHoursMinutes(9, 0)));
        
        t = OffsetDateTime.of(2018, 3, 14, 0, 0, 0, 0, ZoneOffset.ofHoursMinutes(2, 0));
        assertEquals(t, DateTimeFormatterXSD.parseIntoOffsetDateTimeNoTime("2018-03-14+02:00", ZoneOffset.ofHoursMinutes(9, 0)));


    }

    /**
     * Test of parseIntoOffsetTime method, of class DateTimeFormatterXSDTest.
     */
    @Test
    public void testParseIntoOffsetTime() {
        System.out.println("parseIntoOffsetTime");

        OffsetTime t;
        t = OffsetTime.of(23, 30, 28, 123456789, ZoneOffset.UTC);
        assertEquals(t, DateTimeFormatterXSD.parseIntoOffsetTime("23:30:28.123456789Z", ZoneOffset.ofHoursMinutes(9, 0)));
        
        t = OffsetTime.of(23, 30, 28, 123456789, ZoneOffset.UTC);
        assertEquals(t, DateTimeFormatterXSD.parseIntoOffsetTime("23:30:28.123456789z", ZoneOffset.ofHoursMinutes(9, 0)));
        
        t = OffsetTime.of(23, 30, 0, 0, ZoneOffset.ofHoursMinutes(2, 0));
        assertEquals(t, DateTimeFormatterXSD.parseIntoOffsetTime("23:30:00+02:00", ZoneOffset.ofHoursMinutes(9, 0)));
        
        t = OffsetTime.of(23, 30, 0, 0, ZoneOffset.ofHoursMinutes(2, 0));
        assertEquals(t, DateTimeFormatterXSD.parseIntoOffsetTime("23:30:00+02", ZoneOffset.ofHoursMinutes(9, 0)));

        t = OffsetTime.of(23, 30, 28, 123456789, ZoneOffset.UTC);
        assertEquals(t, DateTimeFormatterXSD.parseIntoOffsetTime("23:30:28.12345678901234567890123456789Z", ZoneOffset.ofHoursMinutes(9, 0)));

        t = OffsetTime.of(23, 30, 28, 123456789, ZoneOffset.UTC);
        assertEquals(t, DateTimeFormatterXSD.parseIntoOffsetTime("23:30:28.12345678901234567890123456789", ZoneOffset.UTC));
        
        t = OffsetTime.of(23, 30, 28, 123456789, ZoneOffset.ofHoursMinutes(2, 0));
        assertEquals(t, DateTimeFormatterXSD.parseIntoOffsetTime("23:30:28.12345678901234567890123456789+02:00", ZoneOffset.ofHoursMinutes(9, 0)));
        
        t = OffsetTime.of(23, 30, 28, 123456789, ZoneOffset.ofHoursMinutes(2, 0));
        assertEquals(t, DateTimeFormatterXSD.parseIntoOffsetTime("23:30:28.12345678901234567890123456789+02", ZoneOffset.ofHoursMinutes(9, 0)));
    }
    
    @Test
    public void testFormatXMLDate() { 
        System.out.println("XSD_DATE.format()");
        
        OffsetDateTime t;
        OffsetDate t2;
        LocalDate t3;

        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 28, 123456789, ZoneOffset.UTC);
        assertEquals("2018-03-14Z", DateTimeFormatterXSD.XSD_DATE.format(t));

        t2 = OffsetDate.of(2018, 3, 14, ZoneOffset.UTC);
        assertEquals("2018-03-14Z", DateTimeFormatterXSD.XSD_DATE.format(t2.getDate()));

        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 28, 123456789, ZoneOffset.ofHoursMinutes(2, 0));
        assertEquals("2018-03-14+02:00", DateTimeFormatterXSD.XSD_DATE.format(t));

        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 28, 123456789, ZoneOffset.ofHours(2));
        assertEquals("2018-03-14+02:00", DateTimeFormatterXSD.XSD_DATE.format(t));

        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 28, 123456789, ZoneOffset.ofHoursMinutesSeconds(2, 0, 33));
        assertEquals("2018-03-14+02:00", DateTimeFormatterXSD.XSD_DATE.format(t));
        
        t3 = LocalDate.of(2018, 3, 14);
        assertEquals("2018-03-14", DateTimeFormatterXSD.XSD_DATE.format(t3));
        
        
    }



    @Test
    public void testFormatXMLDateTime() { 
        System.out.println("XSD_DATETIME.format()");
        
        OffsetDateTime t;
        OffsetDate t2;
        LocalDateTime t3;

        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 28, 123456789, ZoneOffset.UTC);
        assertEquals("2018-03-14T23:30:28.123456789Z", DateTimeFormatterXSD.XSD_DATETIME.format(t));

        t2 = OffsetDate.of(2018, 3, 14, ZoneOffset.UTC);
        assertEquals("2018-03-14T00:00:00Z", DateTimeFormatterXSD.XSD_DATETIME.format(t2.getDate()));

        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 28, 123456789, ZoneOffset.ofHoursMinutes(2, 0));
        assertEquals("2018-03-14T23:30:28.123456789+02:00", DateTimeFormatterXSD.XSD_DATETIME.format(t));

        t = OffsetDateTime.of(2018, 3, 14, 23, 30, 28, 90000, ZoneOffset.ofHoursMinutes(2, 0));
        assertEquals("2018-03-14T23:30:28.00009+02:00", DateTimeFormatterXSD.XSD_DATETIME.format(t));

        t3 = LocalDateTime.of(2018, 3, 14, 23, 30, 28);
        assertEquals("2018-03-14T23:30:28", DateTimeFormatterXSD.XSD_DATETIME.format(t3));
        
        
    }

    

    @Test
    public void testFormatXMLTime() { 
        System.out.println("XSD_TIME.format()");
        
        OffsetTime t;
        OffsetDate t2;
        LocalTime t3;

        t = OffsetTime.of(23, 30, 28, 123456789, ZoneOffset.UTC);
        assertEquals("23:30:28.123456789Z", DateTimeFormatterXSD.XSD_TIME.format(t));

        t2 = OffsetDate.of(2018, 3, 14, ZoneOffset.UTC);
        assertEquals("00:00:00Z", DateTimeFormatterXSD.XSD_TIME.format(t2.getDate()));

        t = OffsetTime.of(23, 30, 28, 123456789, ZoneOffset.ofHoursMinutes(2, 0));
        assertEquals("23:30:28.123456789+02:00", DateTimeFormatterXSD.XSD_TIME.format(t));

        t = OffsetTime.of(23, 30, 28, 90000, ZoneOffset.ofHoursMinutes(2, 0));
        assertEquals("23:30:28.00009+02:00", DateTimeFormatterXSD.XSD_TIME.format(t));

        t3 = LocalTime.of(23, 30, 28);
        assertEquals("23:30:28", DateTimeFormatterXSD.XSD_TIME.format(t3));
        
        
    }    
    
}
