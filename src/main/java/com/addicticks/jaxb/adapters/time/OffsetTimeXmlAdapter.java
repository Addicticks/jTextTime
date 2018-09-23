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
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;

/**
 * JAXB adapter to convert between {@code xs:time} and {@code OffsetTime}.
 * 
 * <p>
 * If the adapter is used for unmarshalling (parsing XML to object) and
 * there is no zone offset in the input data then customizing the
 * {@link #getCurrentZoneOffset()} method may be needed.
 */
public class OffsetTimeXmlAdapter extends OffsetDateTimeXmlAdapterBase<OffsetTime> {

    private final DateTimeFormatter dateTimeFormatter;
    
    public OffsetTimeXmlAdapter() {
        dateTimeFormatter = new DateTimeFormatterBuilder()
                .append(DateTimeFormatter.ISO_LOCAL_TIME)
                .optionalStart()
                .appendOffsetId()
                .optionalEnd()
                .toFormatter()
                .withResolverStyle(ResolverStyle.STRICT)
                .withChronology(IsoChronology.INSTANCE);
    }


    /**
     * Converts from {@code xs:time} format.
     */    
    @Override
    public OffsetTime unmarshal(String v) {
        return dateTimeFormatter.parse(v, this::from);
    }

    /**
     * Converts to {@code xs:time} format.
     */
    @Override
    public String marshal(OffsetTime v) {
        return dateTimeFormatter.format(v);
    }
    
     
    private OffsetTime from(TemporalAccessor temporal) {
        if (temporal instanceof OffsetTime) {
            return (OffsetTime) temporal;
        }
        ZoneOffset offset = temporal.query(TemporalQueries.offset());

        if (offset == null) {
            offset = getCurrentZoneOffset();
        }
        LocalTime time = temporal.query(TemporalQueries.localTime());
        if (time != null) {
            return OffsetTime.of(time, offset);
        } else {
            throw new DateTimeException("Unable to obtain time value from : " + temporal);
        }
    }
    
}
