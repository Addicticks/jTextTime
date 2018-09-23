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

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

/**
 * A date with an offset from UTC/Greenwich in the ISO-8601 calendar system, 
 * such as 2007-12-03+01:00. 
 * 
 * <p>
 * The class is merely a thin wrapper around {@link OffsetDateTime} because a
 * date-only class is missing from Java 8. It exists here mainly for the purpose
 * of JAXB mapping to/from XML Schema type {@code xs:date}.
 */
public class OffsetDate {

    private final OffsetDateTime date;

    private OffsetDate(OffsetDateTime date) {
        this.date = date;
    }
    
    
    /**
     * Obtains an instance of {@code OffsetDate} from a year, month, day,
     * and offset. 
     *
     * @param year  the year to represent, for example 2018.
     * @param month  the month-of-year to represent, from 1 (January) to 12 (December)
     * @param dayOfMonth  the day-of-month to represent, from 1 to 31
     * @param offset  the zone offset, not null
     * @return the offset date, not null
     * @see OffsetDateTime#of(int, int, int, int, int, int, int, java.time.ZoneOffset) 
     */
    public static OffsetDate of(int year, int month, int dayOfMonth, ZoneOffset offset) {
        return new OffsetDate(OffsetDateTime.of(year, month, dayOfMonth, 0, 0, 0, 0, offset));
    }

    /**
     * Obtains an instance of {@code OffsetDate} from a {@code LocalDate}
     * and an offset. 
     *  
     * @param date local date, not null
     * @param offset  the zone offset, not null
     * @return the offset date, not null
     * @see OffsetDateTime#of(java.time.LocalDate, java.time.LocalTime, java.time.ZoneOffset) 
     */
    public static OffsetDate of(LocalDate date, ZoneOffset offset) {
        return new OffsetDate(OffsetDateTime.of(date, LocalTime.MIDNIGHT, offset));
    }
    
    /**
     * Obtains an instance of {@code OffsetDate} from an {@code OffsetDateTime}
     * The time part of the input value will be removed.
     *
     * @param datetime datetime value, not null
     * @return the offset date, not null
     */
    public static OffsetDate of(OffsetDateTime datetime) {
        return new OffsetDate(datetime.truncatedTo(ChronoUnit.DAYS));
    }

    /**
     * Gets the date value.
     * @return datetime value with time part set to midnight
     */
    public OffsetDateTime getDate() {
        return date;
    }
    
    
}
