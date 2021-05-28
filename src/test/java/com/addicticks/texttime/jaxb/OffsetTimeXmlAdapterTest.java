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
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import org.junit.Test;
import static org.junit.Assert.*;

public class OffsetTimeXmlAdapterTest {
    
  
    @Test
    public void testUnmarshal() {
        System.out.println("unmarshal");
        OffsetTimeXmlAdapter instance = new OffsetTimeXmlAdapter() {
            @Override
            public ZoneOffset getZoneOffsetForTime(LocalTime time) {
                return ZoneOffset.ofTotalSeconds(OFFSET_SECONDS_TEST_VALUE);  // +03:45:49
            }
        };
        
        OffsetTime result;
        
        
        // More than 9 decimals on the seconds value should be 
        // gracefully handled using truncation.
        assertEquals( 
                instance.unmarshal("23:30:28.123456789012345678901234567890Z"),
                instance.unmarshal("23:30:28.123456789Z")
        );
        
        assertEquals( 
                instance.unmarshal("23:30:28.123456789012345678901234567890"),
                instance.unmarshal("23:30:28.123456789")
        );
        
        assertEquals( 
                instance.unmarshal("23:30:28.123456789012345678901234567890+08:00"),
                instance.unmarshal("23:30:28.123456789+08:00")
        );
        assertEquals( 
                instance.unmarshal("23:30:28.123456789012345678901234567890-06:00"),
                instance.unmarshal("23:30:28.123456789-06:00")
        );
        
        assertNotEquals( 
                instance.unmarshal("23:30:28.123456789012345678901234567890Z"),
                instance.unmarshal("23:30:28.123456790Z")
        );
        
        
        result = instance.unmarshal("23:30:28.123456789Z");
        assertEquals(23, result.getHour());
        assertEquals(30, result.getMinute());
        assertEquals(28, result.getSecond());
        assertEquals(123456789, result.getNano());
        assertEquals(0, result.getOffset().getTotalSeconds());
        
        result = instance.unmarshal("23:30:28.123456789-07:30");
        assertEquals(23, result.getHour());
        assertEquals(30, result.getMinute());
        assertEquals(28, result.getSecond());
        assertEquals(123456789, result.getNano());
        assertEquals(-((7*60*60) + (30*60)), result.getOffset().getTotalSeconds());
        
        result = instance.unmarshal("23:30:28.123456789");
        assertEquals(23, result.getHour());
        assertEquals(30, result.getMinute());
        assertEquals(28, result.getSecond());
        assertEquals(123456789, result.getNano());
        assertEquals(OFFSET_SECONDS_TEST_VALUE, result.getOffset().getTotalSeconds());
        
    }

    /**
     * Test of marshal method, of class OffsetTimeXmlAdapter.
     */
    @Test
    public void testMarshal() {
        System.out.println("marshal");
        OffsetTimeXmlAdapter instance = new OffsetTimeXmlAdapter();
        OffsetTime t;
        t = OffsetTime.of(23, 30, 28, 123456789, ZoneOffset.UTC);
        assertEquals("23:30:28.123456789Z", instance.marshal(t));
        
        t = OffsetTime.of(23, 30, 28, 0, ZoneOffset.UTC);
        assertEquals("23:30:28Z", instance.marshal(t));
        
        t = OffsetTime.of(23, 30, 0, 0, ZoneOffset.UTC);
        assertEquals("23:30:00Z", instance.marshal(t));
    }
    
    /**
     * Test of marshal method, of class OffsetTimeXmlAdapter with null value. (Ex. optional attributes)
     */
    @Test
    public void testMarshalNullValue() {
        
        OffsetTimeXmlAdapter instance = new OffsetTimeXmlAdapter();
        OffsetTime t = null;
        
        assertNull(instance.marshal(t));
    }
    
}
