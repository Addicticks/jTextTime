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
package com.addicticks.texttime;

import java.io.Serializable;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;
import java.util.Objects;

/**
 * A date with an offset from UTC/Greenwich in the ISO-8601 calendar system, 
 * such as {@code 2007-12-03+01:00}. 
 * 
 * <p>
 * The class is merely a thin wrapper around {@link OffsetDateTime} because a
 * date-only class is missing from JDK. The class exist mainly for the purpose
 * of JAXB mapping to/from XML Schema type {@code xs:date}.

* <p>
 * This is a <a href="{@docRoot}/java/lang/doc-files/ValueBased.html">value-based</a>
 * class; use of identity-sensitive operations (including reference equality
 * ({@code ==}), identity hash code, or synchronization) on instances of
 * {@code OffsetDate} may have unpredictable results and should be avoided.
 * The {@code equals} method should be used for comparisons.
 *
 * <p>
 * This class is immutable and thread-safe.
 */
public final class OffsetDate implements Serializable, Comparable<OffsetDate> {

    private static final long serialVersionUID = -5039366221066381967L;
    
    private final OffsetDateTime wrappedDateTime;

    private OffsetDate(OffsetDateTime date) {
        this.wrappedDateTime = date;
    }
    
    
    /**
     * Obtains an instance of {@code OffsetDate} from a year, month, day,
     * and offset. 
     *
     * @param year  the year to represent, for example 2018.
     * @param month  the month-of-year to represent, from 1 (January) to 12 (December)
     * @param dayOfMonth  the day-of-month to represent, from 1 to 31
     * @param offset  the zone offset, not null
     * @return a date, not null
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
     * @return a date, not null
     * @see OffsetDateTime#of(java.time.LocalDate, java.time.LocalTime, java.time.ZoneOffset) 
     */
    public static OffsetDate of(LocalDate date, ZoneOffset offset) {
        Objects.requireNonNull(date, "date argument must not be null");
        Objects.requireNonNull(date, "offset argument must not be null");
        return new OffsetDate(OffsetDateTime.of(date, LocalTime.MIDNIGHT, offset));
    }
    
    /**
     * Obtains an instance of {@code OffsetDate} from an {@code OffsetDateTime}
     * The time part of the input value will be 
     * {@link java.time.OffsetDateTime#truncatedTo(java.time.temporal.TemporalUnit)  removed}.
     *
     * @param datetime datetime value, not null
     * @return a date, not null
     */
    public static OffsetDate of(OffsetDateTime datetime) {
        Objects.requireNonNull(datetime, "datetime argument must not be null");
        return new OffsetDate(datetime.truncatedTo(ChronoUnit.DAYS));
    }

    /**
     * Gets the date value.
     * @return datetime value with time part set to midnight, never null
     */
    public OffsetDateTime getDate() {
        return wrappedDateTime;
    }

    /**
     * A hash code for this date.
     *
     * @return a suitable hash code
     */    
    @Override
    public int hashCode() {
        return wrappedDateTime.hashCode();
    }

    /**
     * Checks if this date is equal to another date.
     * <p>
     * The comparison is delegated to the wrapped {@code OffsetDateTime}.
     * To compare for the same instant on the time-line, use {@link java.time.OffsetDateTime#isEqual(java.time.OffsetDateTime) getDate().isEqual()}.
     * Only objects of type {@code OffsetDate} are compared, other types return false.
     *
     * @see java.time.OffsetDateTime#equals(java.lang.Object) 
     * @param obj  the object to check, null returns false
     * @return true if this is equal to the other date
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof OffsetDate) {
            OffsetDate other = (OffsetDate) obj;
            return wrappedDateTime.equals(other.getDate());
        }
        return false;
    }

    /**
     * Compares this date to another date.
     * <p>
     * The comparison is delegated to the wrapped {@code OffsetDateTime}.
     *
     * @see java.time.OffsetDateTime#compareTo(java.time.OffsetDateTime) 
     * @param other  the other date to compare to, not null
     * @return the comparator value, negative if less, positive if greater
     */
    @Override
    public int compareTo(OffsetDate other) {
        return wrappedDateTime.compareTo(other.getDate());
    }
       
    /**
     * Obtains an instance of {@code OffsetDate} from a temporal object.
     * <p>
     * This obtains an offset date based on the specified temporal.
     * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
     * which this factory converts to an instance of {@code OffsetDate}. Any
     * time information in the {@code temporal} will be ignored.
     * 
     * <p>
     * The conversion will first obtain a {@code ZoneOffset} from the temporal object.
     * It will then try to obtain a {@code LocalDate}, falling back to an {@code Instant} if necessary.
     * The result will be the combination of {@code ZoneOffset} with either
     * with {@code LocalDate} or {@code Instant}.
     * 
     * <p>
     * This method matches the signature of the functional interface {@link java.time.temporal.TemporalQuery}
     * allowing it to be used as a query via method reference, {@code OffsetDate::from}.
     *
     * @param temporal  the temporal object to convert, not null
     * @return the offset date, not null
     * @throws DateTimeException if unable to convert to an {@code OffsetDate}
     */
    public static OffsetDate from(TemporalAccessor temporal) {
        Objects.requireNonNull(temporal, "temporal argument must not be null");
        if (temporal instanceof OffsetDateTime) {
            OffsetDateTime dt = (OffsetDateTime) temporal;
            return OffsetDate.of(dt);
        }
        try {
            ZoneOffset offset = temporal.query(TemporalQueries.offset());
            if (offset == null) {
                throw new DateTimeException("There is no timezone offset in the input and no default has been provided by the DateTimeFormatter in use.");
            }
            LocalDate date = temporal.query(TemporalQueries.localDate());
            if (date != null) {
                return OffsetDate.of(date, offset);
            } else {
                Instant instant = Instant.from(temporal);
                OffsetDateTime dt = OffsetDateTime.ofInstant(instant, offset);
                return OffsetDate.of(dt);
            }
        } catch (DateTimeException ex) {
            throw new DateTimeException("Unable to obtain OffsetDate from TemporalAccessor: "
                    + temporal + " of type " + temporal.getClass().getName(), ex);
        }
    }
    
}
