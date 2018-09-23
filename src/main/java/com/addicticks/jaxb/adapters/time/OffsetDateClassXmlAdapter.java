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
package com.addicticks.jaxb.adapters.time;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;

/**
 * JAXB adapter to convert between {@code xs:date} and custom class 
 * {@code OffsetDate}. 
 * 
 * <p>
 * If the adapter is used for unmarshalling (parsing XML to object) and
 * there is no zone offset in the input data then customizing the
 * {@link #getCurrentZoneOffset()} method may be needed. 
 * 
 * @see OffsetDateXmlAdapter
 */
public class OffsetDateClassXmlAdapter extends OffsetDateTimeXmlAdapterBase<OffsetDate> {

    private final DateTimeFormatter dateTimeFormatter;

    public OffsetDateClassXmlAdapter() {
        dateTimeFormatter = new DateTimeFormatterBuilder()
                .append(DateTimeFormatter.ISO_LOCAL_DATE)
                .optionalStart()
                .appendOffsetId()
                .optionalEnd()
                .toFormatter()
                .withResolverStyle(ResolverStyle.STRICT)
                .withChronology(IsoChronology.INSTANCE);
    }

    /**
     * Converts from {@code xs:date} format. 
     */    
    @Override
    public OffsetDate unmarshal(String v) {
        return dateTimeFormatter.parse(v, this::from);
    }

    /**
     * Converts to {@code xs:date} format.
     */    
    @Override
    public String marshal(OffsetDate v) {
        return dateTimeFormatter.format(v.getDate());
    }
    
    private OffsetDate from(TemporalAccessor temporal) {
        ZoneOffset offset = temporal.query(TemporalQueries.offset());
        LocalDate date = temporal.query(TemporalQueries.localDate());

        if (offset == null) {
            offset = getCurrentZoneOffset();
        }
        if (date != null) {
            return OffsetDate.of(date, offset);
        } else {
            throw new DateTimeException("Unable to obtain date value from : " + temporal);
        }
    }

    
}
