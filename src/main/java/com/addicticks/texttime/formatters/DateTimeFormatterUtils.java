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

import com.addicticks.texttime.OffsetDate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;
import java.time.temporal.TemporalQuery;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility methods for parsing Date/Time string values.
 * 
 * <p>
 * There should rarely be a need to use these methods directly.
 */
public class DateTimeFormatterUtils {
    
   
    /**
     * Map with the acceptable values for the delimiter 
     * between date and the time value.
     */
    protected static final 
            Map<Long, String> DATETIME_DELIMETER_LENIENT_MAP = new HashMap<>();
    static {
        DATETIME_DELIMETER_LENIENT_MAP.put(1L, "T");
        DATETIME_DELIMETER_LENIENT_MAP.put(2L, " ");
    }    

    private DateTimeFormatterUtils() {
    }
    

    /**
     * Creates a {@code OffsetDateTime} from a {@code TemporalAccessor}. If no
     * timezone offset if found in the temporal then the supplied default timezone offset is
     * used.
     * 
     * <p>
     * If no time-part is found in the temporal then time is set to {@code MIDNIGHT}
     * (start of day).
     * 
     * @param temporal the input
     * @param zoneOffsetProvider a function which will be called if no zone offset
     *        is present in the {@code temporal}.
     * @param defaultMonth the month value to use (1 - 12) if no month can be obtained
     *    from the temporal, or -1 if the month value is considered mandatory in the temporal.
     * @param defaultDayOfMonth the day-of-month value to use (1 - 31) if no day-of-month 
     *    can be obtained from the temporal, or -1 if the day-of-month value is 
     *    considered mandatory in the temporal.
     * @throws DateTimeTemporalQueryException if the temporal does not contain the 
     *    elements needed to construct an OffsetDateTime
     * @return 
     */
    public static OffsetDateTime fromTemporalAccessorToOffsetDateTime(TemporalAccessor temporal, Function<LocalDateTime, ZoneOffset> zoneOffsetProvider, int defaultMonth, int defaultDayOfMonth) {
        if (temporal instanceof OffsetDateTime) {
            return (OffsetDateTime) temporal;
        }

        LocalDate date = getLocalDate(temporal, defaultMonth, defaultDayOfMonth);
        LocalTime time = temporal.query(TemporalQueries.localTime());
        if (time == null) {
            time = LocalTime.MIDNIGHT;
        }
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        ZoneOffset offset = temporal.query(TemporalQueries.offset());
        offset = (offset == null) ? zoneOffsetProvider.apply(dateTime) : offset;
       
        return OffsetDateTime.of(date, time, offset);
    }
    
    /**
     * Creates a {@code OffsetDate} from a {@code TemporalAccessor}. Certain 
     * elements may be absent from the temporal in which case the supplied
     * defaults are used.
     * 
     * <p>
     * {@link #fromTemporalAccessorToOffsetDateTimeNoTime(java.time.temporal.TemporalAccessor, java.util.function.Function, int, int) 
     * fromTemporalAccessorToOffsetDateTimeNoTime()} is an alternative to this method.
     * 
     * @param temporal the input
     * @param zoneOffsetProvider a function which will be called if no zone offset
     *        is present in the {@code temporal}.
     * @param defaultMonth the month value to use (1 - 12) if no month can be obtained
     *    from the temporal, or -1 if the month value is considered mandatory in the temporal.
     * @param defaultDayOfMonth the day-of-month value to use (1 - 31) if no day-of-month 
     *    can be obtained from the temporal, or -1 if the day-of-month value is 
     *    considered mandatory in the temporal.
     * @throws DateTimeTemporalQueryException if the temporal does not contain the 
     *    elements needed to construct an OffsetDate
     * @return 
     */
    public static OffsetDate fromTemporalAccessorToOffsetDate(TemporalAccessor temporal, Function<LocalDate, ZoneOffset> zoneOffsetProvider, int defaultMonth, int defaultDayOfMonth) {
        LocalDate date = getLocalDate(temporal, defaultMonth, defaultDayOfMonth);
        ZoneOffset offset = temporal.query(TemporalQueries.offset());
        offset = (offset == null) ? zoneOffsetProvider.apply(date) : offset;
        return OffsetDate.of(date, offset);
    }
    
    
    /**
     * Creates a {@code OffsetDateTime} from a {@code TemporalAccessor}. If no
     * timezone offset if found in the input then the supplied default timezone offset is
     * used.
     * 
     * <p>
     * Any time information on the input will be ignored. The returned value
     * will always have its time information set to midnight.
     * 
     * <p>
     * This method is an alternative to {@link #fromTemporalAccessorToOffsetDate(java.time.temporal.TemporalAccessor, java.util.function.Function, int, int) 
     * fromTemporalAccessorToOffsetDate()} method when use of the proprietary
     * {@link com.addicticks.texttime.OffsetDate OffsetDate} is unacceptable or not
     * needed.
     * 
     * @param temporal the input
     * @param zoneOffsetProvider a function which will be called if no zone offset
     *        is present in the {@code temporal}.
     * @param defaultMonth the month value to use (1 - 12) if no month can be obtained
     *    from the temporal, or -1 if the month value is considered mandatory in the temporal.
     * @param defaultDayOfMonth the day-of-month value to use (1 - 31) if no day-of-month 
     *    can be obtained from the temporal, or -1 if the day-of-month value is 
     *    considered mandatory in the temporal.
     * @throws DateTimeTemporalQueryException if the temporal does not contain the 
     *    elements needed to construct an OffsetDateTime
     * @return 
     */
   public static OffsetDateTime fromTemporalAccessorToOffsetDateTimeNoTime(TemporalAccessor temporal, Function<LocalDate, ZoneOffset> zoneOffsetProvider, int defaultMonth, int defaultDayOfMonth) {
        if (temporal instanceof OffsetDateTime) {
            return (OffsetDateTime) temporal;
        }
       LocalDate date = getLocalDate(temporal, defaultMonth, defaultDayOfMonth);
       LocalDateTime time = LocalDateTime.of(date, LocalTime.MIDNIGHT);
       ZoneOffset offset = temporal.query(TemporalQueries.offset());
       offset = (offset == null) ? zoneOffsetProvider.apply(date) : offset;
       return OffsetDateTime.of(time, offset);
    }    
   
    /**
     * Creates a {@code OffsetTime} from a {@code TemporalAccessor}. If no
     * timezone offset if found in the input then the supplied default timezone offset is
     * used.
     * 
     * @param temporal the input
     * @param zoneOffsetProvider a function which will be called if no zone offset
     *        is present in the {@code temporal}.
     * @throws DateTimeTemporalQueryException if the temporal does not contain the 
     *    elements needed to construct an OffsetTime
     * @return 
     */
    public static OffsetTime fromTemporalAccessorToOffsetTime(TemporalAccessor temporal, Function<LocalTime, ZoneOffset> zoneOffsetProvider) {
        if (temporal instanceof OffsetTime) {
            return (OffsetTime) temporal;
        }        
        LocalTime time = temporal.query(TemporalQueries.localTime());
        ZoneOffset offset = temporal.query(TemporalQueries.offset());
        offset = (offset == null) ? zoneOffsetProvider.apply(time) : offset;
        return OffsetTime.of(time, offset);
    }   
   

    /**
     * Attempts to parse string value into a Java date/time value. If first
     * attempt fails the method will evaluate if it is something which can be
     * {@link #fixTooManySecondsDecs(java.lang.String, java.time.format.DateTimeParseException)  fixed}
     * and if so it will "fix" the input value, {@code str}, and perform a new parsing
     * attempt.
     *
     * @param <T> type is inferred from q, typically it will be an OffsetTime or OffsetDateTime type.
     * @param f the formatter to be used for parsing
     * @param str the string value to be parsed
     * @param q
     * @return parsed value, for example an OffsetDateTime.
     */
    public static <T> T parseDualAttempt(DateTimeFormatter f, String str,
            TemporalQuery<T> q) {

        try {
            return f.parse(str, q);
        } catch (DateTimeParseException ex) {

            String fixedValue = fixTooManySecondsDecs(str, ex);

            if (fixedValue != null) {
                return f.parse(fixedValue, q); // try again
            } else {
                throw ex;
            }
        }
    }
    
    
  

    /**
     * Removes excessive seconds decimals from value {@code str}, given a previous
     * parse error, {@code ex}. The {@code OffsetDateTime} and {@code OffsetTime}
     * classes only allow a maximum of 9 decimals on the seconds value.
     * In contrast, the {@code xs:dateTime} and {@code xs:time} data
     * types allow an unlimited number of decimals on the seconds value.
     * This method aims to fix this discrepancy by simply removing
     * decimal digits after the 9th digit.
     * 
     * <p>
     * The method is optimized for speed, meaning that it uses very
     * few resources to check if it is likely that the parse error is 
     * caused by too many decimals on the seconds value. Only then will 
     * the method attempt to create a new (fixed) string value.
     * 
     * <p>
     * A return value of {@code null} means that it is unlikely
     * that the parsing error was due to too many seconds decimals. 
     * In contrast, a non-null return value will be a modified version
     * of {@code str}, which is likely to be able to pass a new parsing operation.
     * For example for input value {@code "23:30:28.123456789012345678901234567890Z"}
     * (parse error at position 18) the method will return {@code "23:30:28.123456789Z"}.
     * 
     * <p>
     * The method errs on the side of caution, which means that
     * it doesn't attempt to fix other semantical problems in the value
     * and it does a number of checks before it concludes that the error
     * is caused by too many decimals on the seconds value.
     * 
     * @param str string value, either a date-time value or a time-only value.
     * @param ex exception from prior attempt to parse
     * @return fixed string or null
     */
    public static String fixTooManySecondsDecs(String str, DateTimeParseException ex) {
        
        int errorIndex = ex.getErrorIndex();
        int goodPos = likelyTooManyDecs(str, errorIndex);

        if (goodPos != -1) {
            String newVal;
            if (goodPos >= str.length()) {
                newVal = str.substring(0, errorIndex); // It's a value without tz offset
            } else {
                newVal = str.substring(0, errorIndex) + str.substring(goodPos); // It's a value with tz offset
            }
            return newVal;
        }
        // We cannot 'fix' the value. 
        return null;
    }

    /**
     * Method tries to - as fast as possible - to figure out if an error
     * in parsing {@code xs:time} or {@code xs:dateTime} value was simply due to too
     * many decimals on the seconds value. 
     * 
     * <p>
     * A return value of -1 means that the parsing error likely to be caused
     * by something else in the string. (meaning it is not something we
     * have a fix for)
     * 
     * <p>
     * A return value of >0 means that the parsing error is likely to be caused
     * by too many decimals on the seconds value. The returned value indicates
     * the first position after the error position which is <i>not</i>
     * a digit. Hence, the returned value can be used as an indicator to 
     * what part of the string should be removed so that it is likely that
     * such a 'fixed' string can pass a new parse operation.
     * 
     * @param str
     * @param errorIndex
     * @return 
     */
    private static int likelyTooManyDecs(String str, int errorIndex) {
        if (str == null) { // sanity check 1
            return -1;
        }
        if (errorIndex >= str.length() || errorIndex <= 0) {  // sanity check 2
            return -1;
        }
        if (((errorIndex - 10) >= 8) &&  
                isAsciiDigit(str.charAt(errorIndex))) {
            char cDot = str.charAt(errorIndex - 10);
            if (cDot == '.') {
                char cColon = str.charAt(errorIndex - 13);
                if (cColon == ':') {
                    // Check that there are 9 preceeding digits
                    for (int i = errorIndex; i >= (errorIndex - 9); i--) {
                        char cDigit = str.charAt(i);
                        if (!isAsciiDigit(cDigit)) {
                            return -1;  // it is not a digit
                        }
                    }
                    // Check that all proceeding characters are digits
                    // (up until the end or until the tz specification)
                    for (int i = errorIndex; i < str.length(); i++) {
                        char c = str.charAt(i);
                        if (!isAsciiDigit(c)) {
                            if ((c == 'Z') || (c == '+') || (c == '-')) {
                                return i; // all checks passed, string seems to end with tz
                            } else {
                                return -1;
                            }
                        }
                    }
                    return str.length();  // all checks passed, it is value without tz
                }
            }
        }
        return -1;
    }
    
    private static boolean isAsciiDigit(char c) {
       return (c >= '0' && c <= '9');
    }
    
    
    private static ZoneOffset getZoneOffset(TemporalAccessor temporal, ZoneOffset defaultZoneOffset) {
        ZoneOffset offset = temporal.query(TemporalQueries.offset());
        return (offset == null) ? defaultZoneOffset : offset;
    }
    
    /**
     * Gets a LocalDate from a temporal. 
     * 
     * @param temporal
     * @param defaultMonth the month value to use (1 - 12) if no month can be obtained
     *    from the temporal, or -1 if the month value is considered mandatory in the temporal.
     * @param defaultDayOfMonth the day-of-month value to use (1 - 31) if no day-of-month 
     *    can be obtained from the temporal, or -1 if the day-of-month value is 
     *    considered mandatory in the temporal.
     * @return date or null if no date can be obtained
     * @throws DateTimeTemporalQueryException if no LocalDate can be obtained 
     *    from temporal.
     */
    private static LocalDate getLocalDate(TemporalAccessor temporal, int defaultMonth, int defaultDayOfMonth) {
        LocalDate date = temporal.query(TemporalQueries.localDate());
        if (date == null) {
            if (temporal.isSupported(ChronoField.YEAR)) {
                int year = temporal.get(ChronoField.YEAR);
                int month = defaultMonth;
                int dayOfMonth = defaultDayOfMonth;
                if (temporal.isSupported(ChronoField.MONTH_OF_YEAR)) {
                    month = temporal.get(ChronoField.MONTH_OF_YEAR);
                    if (temporal.isSupported(ChronoField.DAY_OF_MONTH)) {
                        dayOfMonth = temporal.get(ChronoField.DAY_OF_MONTH);
                    }
                }
                if (month != -1 && dayOfMonth != -1) {
                    date = LocalDate.of(year, month, dayOfMonth);
                }
            }
        }
        if (date == null) {
             throw new DateTimeTemporalQueryException("Unable to obtain LocalDate from TemporalAccessor: " +
                    temporal + " of type " + temporal.getClass().getName());
        }
        return date;
    }
}
