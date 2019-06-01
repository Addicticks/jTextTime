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

import com.addicticks.texttime.formatters.DateTimeFormatterUtils;
import java.time.format.DateTimeParseException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author lars
 */
public class DateTimeFormatterUtilsTest {
    

    /**
     * Test of fixTooManySecondsDecs method, of class DateTimeFormatterUtils.
     */
    @Test
    public void testFixTooManySecondsDecs() {
        System.out.println("fixTooManySecondsDecs");
        String str;
        DateTimeParseException ex ;
        String expResult;
        String result;


        str = "2018-03-14T23:30:28.12x3456789Z";
        ex = new DateTimeParseException("Some msg", str, 22);
        expResult = null;
        result = DateTimeFormatterUtils.fixTooManySecondsDecs(str, ex);
        assertEquals(expResult, result);


        str = "2018-03-14T23:30:28.1234567890123456789Z";
        ex = new DateTimeParseException("Some msg", str, 29);
        expResult = "2018-03-14T23:30:28.123456789Z";
        result = DateTimeFormatterUtils.fixTooManySecondsDecs(str, ex);
        assertEquals(expResult, result);

        str = "23:30:28.1234567890123456789Z";
        ex = new DateTimeParseException("Some msg", str, 18);
        expResult = "23:30:28.123456789Z";
        result = DateTimeFormatterUtils.fixTooManySecondsDecs(str, ex);
        assertEquals(expResult, result);


        str = "23:30:28.1234567890123456789";
        ex = new DateTimeParseException("Some msg", str, 18);
        expResult = "23:30:28.123456789";
        result = DateTimeFormatterUtils.fixTooManySecondsDecs(str, ex);
        assertEquals(expResult, result);

        str = "23:30:28.1234567890123456789x3453Z";
        ex = new DateTimeParseException("Some msg", str, 18);
        expResult = null;
        result = DateTimeFormatterUtils.fixTooManySecondsDecs(str, ex);
        assertEquals(expResult, result);

        str = "23:30:28.12345678901234567893453-01:00";
        ex = new DateTimeParseException("Some msg", str, 18);
        expResult = "23:30:28.123456789-01:00";
        result = DateTimeFormatterUtils.fixTooManySecondsDecs(str, ex);
        assertEquals(expResult, result);
    }
    
}
