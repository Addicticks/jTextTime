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
import com.addicticks.texttime.formatters.DateTimeFormatterXSD;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

/**
 * JAXB adapter to convert between {@code xs:date} and custom class 
 * {@code OffsetDate}. 
 * 
 * <p>
 * If the adapter is used for unmarshalling (parsing XML to object) and
 * there is no zone offset in the input data then it may be needed
 * to extend this class and customize the
 * {@link #getZoneOffsetForDate(java.time.LocalDate)} method.
 * 
 * @see OffsetDateXmlAdapter
 */
public class OffsetDateClassXmlAdapter extends XmlAdapter<String, OffsetDate> {

  
    /**
     * Converts from {@code xs:date} format. 
     * 
     * <p>
     * A number of minor deviations from the standard are accepted while parsing. 
     * See {@link DateTimeFormatterXSD#XSD_DATE_PARSER} for more information.
     * 
     */    
    @Override
    public final OffsetDate unmarshal(String v) {
        return DateTimeFormatterXSD.parseIntoOffsetDate(v, this::getZoneOffsetForDate);
    }

    /**
     * Converts to {@code xs:date} format.
     */    
    @Override
    public final String marshal(OffsetDate v) {
        if (v == null) return null;
        return DateTimeFormatterXSD.XSD_DATE.format(v.getDate());
    }
    

    /**
     * Gets ZoneOffset for a given {@code LocalDate} value. 
     * Sub-classes may override this.
     * 
     * <p>
     * This method is needed because the XML Schema 
     * {@code date} data type allows to leave out the offset. Therefore,
     * when unmarshalling there may be no offset in the input data. If this is
     * the case then this method will be called. In summary, the method
     * is only called when unmarshalling and only when input data has no offset.
     * 
     * <p>
     * The default implementation uses the the system's default zone id 
     * (from {@link java.time.ZoneOffset#systemDefault()}) and then finds 
     * the appropriate offset for that zone given a point in time of
     * {@code localDate} + current-LocalTime.
     * 
     * @param localDate
     * @return offset to use when none is present in XML input data, never null
     */
    public ZoneOffset getZoneOffsetForDate(LocalDate localDate) {
        ZoneId systemDefaultZoneId = ZoneOffset.systemDefault();
        LocalTime localTime = LocalTime.now(systemDefaultZoneId);
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        return  systemDefaultZoneId.getRules().getOffset(localDateTime);
    }    
}
