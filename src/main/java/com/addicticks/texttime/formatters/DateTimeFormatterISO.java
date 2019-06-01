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
import static com.addicticks.texttime.formatters.DateTimeFormatterUtils.DATETIME_DELIMETER_LENIENT_MAP;
import static com.addicticks.texttime.formatters.DateTimeFormatterUtils.parseDualAttempt;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.util.function.Function;

/**
 * Formatter-Parsers for ISO 8601 date and time values.
 * 
 * <p>
 * These classes complement those in the JDK and provide additional features
 * such as accepting a space as a the separator between the date and the
 * time value and accepting the absence of the timezone offset.
 * 
 * <h3 id="predefined">Predefined Formatters-Parsers overview</h3>
 * <table summary="Predefined Formatters" cellpadding="2" cellspacing="3" border="0" >
 * <thead>
 * <tr class="tableSubHeadingColor">
 * <th class="colFirst" align="left">Formatter</th>
 * <th class="colFirst" align="left">Usage</th>
 * <th class="colLast" align="left">Description</th>
 * <th class="colLast" align="left">Examples</th>
 * </tr>
 * </thead>
 * <tbody>
 * <tr class="rowColor">
 * <td> {@link #ISO_OFFSET_DATE_TIME_PARSER}</td>
 * <td> Parsing </td> 
 * <td> For ISO-8601 date/time value parsing with mandatory offset</td> 
 * <td> {@code '2018-12-03T10:15:30+01:00'} <br>{@code '2018-12-03 10:15:30+01:00'} <br>{@code '2018-12-03T10:15:30+01'} <br>{@code '2018-12-03t10:15:30+01'} <br>{@code '2018-12-03T10:15:30Z'} <br>{@code '2018-12-03T10:15:30z'}</td>
 * </tr>
 * <tr class="altColor">
 * <td> {@link #ISO_OPT_OFFSET_DATE_TIME_PARSER}</td>
 * <td> Parsing </td> 
 * <td> For ISO-8601 date/time value parsing with optional offset </td> 
 * <td> {@code '2018-12-03T10:15:30+01:00'} <br>{@code '2018-12-03 10:15:30+01:00'} <br>{@code '2018-12-03T10:15:30+01'} <br>{@code '2018-12-03t10:15:30+01'} <br>{@code '2018-12-03T10:15:30Z'} <br>{@code '2018-12-03T10:15:30z'} <br>{@code '2018-12-03T10:15:30'}</td>
 * </tr>
 * <tr class="rowColor">
 * <td> {@link #ISO_OPT_OFFSET_DATE_PARSER}</td>
 * <td> Parsing </td> 
 * <td> For ISO-8601 date value parsing with optional offset </td> 
 * <td> {@code '2018-12-03+01:00'} <br>{@code '2018-12-03+01'} <br>{@code '2018-12-03Z'} <br>{@code '2018-12-03z'} <br>{@code '2018-12-03'}</td>
 * </tr>
 * <tr class="altColor">
 * <td> {@link #ISO_OPT_OFFSET_TIME_PARSER}</td>
 * <td> Parsing </td> 
 * <td> For ISO-8601 time value parsing with optional offset </td> 
 * <td> {@code '10:15:30+01:00'} <br>{@code '10:15:30+01'} <br>{@code '10:15:30Z'} <br>{@code '10:15:30z'} <br>{@code '10:15:30'}</td>
 * </tr>
 * </tbody>
 * </table>
 * 
 * <p>
 * <h3 id="parserMethods">Parsing</h3>
 * This class also provides convenience methods for parsing ISO-8601
 * date/time string values with optional offset.
 * 
 * <p>
 * <h3 id="formatting">Formatting</h3>
 * For producing ISO-8601 compliant string values you can simply use
 * the classes in the JDK.
 * 
 * 
 * @see <a href="https://en.wikipedia.org/wiki/ISO_8601">Wikipedia ISO 8601</a>
 * 
 */
public class DateTimeFormatterISO {
    
    
   

    
    
    /**
     * For parsing of ISO 8601 date-time values with mandatory offset.
     * 
     * <p>
     * This Formatter-Parser is similar to the JDK's
     * {@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE_TIME ISO_OFFSET_DATE_TIME} except
     * that while <i>parsing</i> it also accepts a space as the separator
     * between the date and the time value (see note below). 
     *
     * <p>
     * This Formatter-Parser is primarily intended for parsing. 
     * For formatting the JDK's 
     * {@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE_TIME ISO_OFFSET_DATE_TIME}
     * is a better option.
     * 
     * <p>
     * <b>Parsing</b><br>
     * This class is only intended for the use-case where the timezone offset is
     * mandatory in the input and the absence of same will generate an
     * exception. Very often the presence of the timezone offset cannot be
     * guaranteed and in this case it is much better to use
     * {@link #ISO_OPT_OFFSET_DATE_TIME_PARSER} instead.
     * <p>
     * There are two methods for parsing a date-time string with mandatory offset into an {@code OffsetDateTime} 
     * depending on your scenario:
     * <ol> 
     *    <li>Use the static {@link #parseIntoOffsetDateTime(java.lang.String) parse()} method on this class.
     *        This method gracefully handles the situation where there are more then 9 digits
     *        on the seconds fractional value.</li>
     *    <li>Use this DateTimeFormatter directly:
     *        <pre class="brush:java">
     *           OffsetDateTime value 
     *               = DateTimeFormatterISO.ISO_OPT_OFFSET_DATE_TIME_PARSER.parse("2018-03-19:22:35:58", OffsetDateTime::from);
     *        </pre></li>
     * </ol>
     * 
     * 
     * <br>
     * <br>
     * <p>
     * Note: The date-time pattern where a space is used to separate
     * the date and the time value is not a pattern which should be encouraged
     * except perhaps for display purpose. The pattern is allowed in the
     * ISO 8601 specification (allowed as an exception: "by mutual agreement 
     * of the partners in information interchange"), but it is strictly
     * forbidden in the definition of {@code xs:dateTime} in the XML Schema
     * Specification and also strictly forbidden in the 
     * <a href="https://www.w3.org/TR/NOTE-datetime">W3C Date and Time Format</a>. 
     * In conclusion: The pattern <i>may</i> be justifiable
     * for display purpose, but for data exchange purpose the separator
     * should always be an upper-case {@code 'T'}.
     * 
     * @see DateTimeFormatter#ISO_OFFSET_DATE_TIME
     */
    public static final DateTimeFormatter ISO_OFFSET_DATE_TIME_PARSER;
    static {
        ISO_OFFSET_DATE_TIME_PARSER = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(ISO_LOCAL_DATE)
                .appendText(NoValueField.DATE_TIME_DELIMITER, DATETIME_DELIMETER_LENIENT_MAP)
                .append(ISO_LOCAL_TIME)
                .appendOffsetId()
                .toFormatter()
                .withResolverStyle(ResolverStyle.STRICT)
                .withChronology(IsoChronology.INSTANCE);
    }
    
    /**
     * For parsing of ISO 8601 date-time values with optional offset.
     * 
     * <p>
     * This Formatter-Parser is similar to {@link #ISO_OFFSET_DATE_TIME_PARSER},
     * however with the following extra features:
     * <ul>
     *    <li>The timezone offset is optional while parsing.</li>
     *    <li>The delimiter between the date and time value can optionally 
     *        be a space character (see note below).</li>
     * </ul>
     *
     * <p>
     * This Formatter-Parser is primarily intended for parsing. 
     * For formatting the JDK's {@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE_TIME ISO_OFFSET_DATE_TIME}
     * is a better option.
     * 
     * <p>
     * <b>Parsing</b><br>
     * There are two methods for parsing a string into an {@code OffsetDateTime} 
     * depending on your scenario:
     * <ol> 
     *    <li>Use the static {@link #parseIntoOffsetDateTime(java.lang.String, java.time.ZoneOffset) parse()} method on this class.
     *        This method gracefully handles the situation where there are more then 9 digits
     *        on the seconds fractional value.</li>
     *    <li>Creating a new {@code DateTimeFormatter} based on this one, but with a default ZoneId
     *        which is used during parsing if none is provided in the input.
     *        <pre class="brush:java">
     *           // This only need to be created once and can be re-used
     *           // (DateTimeFormatters are immutable and thread-safe)
     *           DateTimeFormatter dtf = DateTimeFormatterISO.ISO_OPT_OFFSET_DATE_TIME_PARSER.withZone(ZoneId.of("America/New_York"));
     *           // Parse the string value. Since no offset is given in the input, the New York TZ is assumed.
     *           OffsetDateTime value = dtf.parse("2018-03-19:22:35:58", OffsetDateTime::from);
     *        </pre></li>
     * </ol>
     * 
     * 
     * <br>
     * <br>
     * <p>
     * Note: The date-time pattern where a space is used to separate
     * the date and the time value is not a pattern which should be encouraged
     * except perhaps for display purpose. The pattern is allowed in the
     * ISO 8601 specification (allowed as an exception: "by mutual agreement 
     * of the partners in information interchange"), but it is strictly
     * forbidden in the definition of {@code xs:dateTime} in the XML Schema
     * Specification and also strictly forbidden in the  
     * <a href="https://www.w3.org/TR/NOTE-datetime">W3C Date and Time Format</a>. 
     * In conclusion: The pattern <i>may</i> be justifiable
     * for display purpose, but for data exchange purpose the separator
     * should always be an upper-case {@code 'T'}.
     * 
     * @see #ISO_OFFSET_DATE_TIME_PARSER
     * @see DateTimeFormatter#ISO_OFFSET_DATE_TIME
     */
    public static final DateTimeFormatter ISO_OPT_OFFSET_DATE_TIME_PARSER;
    static {
        ISO_OPT_OFFSET_DATE_TIME_PARSER = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(ISO_LOCAL_DATE)
                .appendText(NoValueField.DATE_TIME_DELIMITER, DATETIME_DELIMETER_LENIENT_MAP)
                .append(ISO_LOCAL_TIME)
                .optionalStart()
                .appendOffsetId()
                .optionalEnd()
                .toFormatter()
                .withResolverStyle(ResolverStyle.STRICT)
                .withChronology(IsoChronology.INSTANCE);
    }
    


    /**
     * For parsing of ISO 8601 date values with optional offset.
     * 
     * <p>
     * This Formatter-Parser is similar to
     * the JDK's {@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE}
     * except that the timezone offset is optional while parsing.
     *
     * <p>
     * This Formatter-Parser is primarily intended for parsing. 
     * For formatting the JDK's {@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE}
     * is a better option.
     * 
     * <p>
     * <b>Parsing</b><br>
     * There are two methods for parsing a date string into an 
     * {@code OffsetDate}/{@code OffsetDateTime} depending on your scenario:
     * <ol> 
     *    <li>Use either of the static methods:
     *           <ul>
     *             <li>{@link #parseIntoOffsetDate(java.lang.String, java.time.ZoneOffset) parseIntoOffsetDate()} </li>
     *             <li>{@link #parseIntoOffsetDateTimeNoTime(java.lang.String, java.time.ZoneOffset)  parseIntoOffsetDateTimeNoTime()}</li>
     *           </ul>
     *        on this class.</li>
     *    <li>Creating a new {@code DateTimeFormatter} based on this one, but with a default ZoneId
     *        which is used during parsing if none is provided in the input.
     *        <pre class="brush:java">
     *           // This only need to be created once and can be re-used
     *           // (DateTimeFormatters are immutable and thread-safe)
     *           DateTimeFormatter dtf = DateTimeFormatterISO.ISO_OPT_OFFSET_DATE_PARSER.withZone(ZoneId.of("America/New_York"));
     *           // Parse the string value. Since no offset is given in the input, the New York TZ is assumed.
     *           OffsetDate value = dtf.parse("2018-03-19", OffsetDate::from);
     *        </pre></li>
     * </ol>
     * 
     * 
     */
    public static final DateTimeFormatter ISO_OPT_OFFSET_DATE_PARSER;
    static {
        ISO_OPT_OFFSET_DATE_PARSER = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(ISO_LOCAL_DATE)
                .optionalStart()
                .appendOffsetId()
                .optionalEnd()
                .toFormatter()
                .withResolverStyle(ResolverStyle.STRICT)
                .withChronology(IsoChronology.INSTANCE);
    }
        
    
    /**
     * For parsing of ISO 8601 time values with optional offset.
     * 
     * <p>
     * This Formatter-Parser is similar to
     * the JDK's {@link java.time.format.DateTimeFormatter#ISO_OFFSET_TIME}
     * except that the timezone offset is optional while parsing.
     *
     * <p>
     * This Formatter-Parser is primarily intended for parsing. 
     * For formatting the JDK's {@link java.time.format.DateTimeFormatter#ISO_OFFSET_TIME}
     * is a better option.
     * 
     * <p>
     * <b>Parsing</b><br>
     * There are two methods for parsing a string into an {@code OffsetTime} 
     * depending on your scenario:
     * <ol> 
     *    <li>Use the static {@link #parseIntoOffsetTime(java.lang.String, java.time.ZoneOffset) parse()} 
     *        on this class.
     *        This method gracefully handles the situation where there are more then 9 digits
     *        on the seconds fractional value.</li>
     *    <li>Creating a new {@code DateTimeFormatter} based on this one, but with a default ZoneId
     *        which is used during parsing if none is provided in the input.
     *        <pre class="brush:java">
     *           // This only need to be created once and can be re-used
     *           // (DateTimeFormatters are immutable and thread-safe)
     *           DateTimeFormatter dtf = DateTimeFormatterISO.ISO_OPT_OFFSET_TIME_PARSER.withZone(ZoneId.of("America/New_York"));
     *           // Parse the string value. Since no offset is given in the input, the New York TZ is assumed.
     *           OffsetTime value = dtf.parse("22:33:58", OffsetTime::from);
     *        </pre></li>
     * </ol>
     */
    public static final DateTimeFormatter ISO_OPT_OFFSET_TIME_PARSER;
    static {
        ISO_OPT_OFFSET_TIME_PARSER = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(ISO_LOCAL_TIME)
                .optionalStart()
                .appendOffsetId()
                .optionalEnd()
                .toFormatter()
                .withResolverStyle(ResolverStyle.STRICT)
                .withChronology(IsoChronology.INSTANCE);
    }

    private DateTimeFormatterISO() {
    }


    /**
     * Parses an ISO-8601 date-time string with offset into an {@link java.time.OffsetDateTime}.
     * For example a string on the form {@code "2018-12-03T10:15:30+01:00"}. The
     * timezone offset part is mandatory in the input.
     * 
     * <p>
     * The string is parsed using {@link DateTimeFormatterISO#ISO_OFFSET_DATE_TIME_PARSER}.
     * 
     * <p>
     * Furthermore, parsing is attempted in a 
     * {@link DateTimeFormatterUtils#parseDualAttempt(java.time.format.DateTimeFormatter, java.lang.String, java.time.temporal.TemporalQuery)  two-step procedure}: 
     * If first parsing attempt fails the method will evaluate if the problem is
     * something which can likely be
     * {@link DateTimeFormatterUtils#fixTooManySecondsDecs(java.lang.String, java.time.format.DateTimeParseException)  fixed}
     * and if so it will "fix" the input value, {@code str}, and perform a new
     * parsing attempt.
 
     * @param str the string to parse
     * @return parsed value
     * @throws java.time.format.DateTimeParseException if unable to parse the 
     *    string, for example if the string has no timezone offset.
     */
    public static OffsetDateTime parseIntoOffsetDateTime(String str) {
        return parseDualAttempt(DateTimeFormatterISO.ISO_OFFSET_DATE_TIME_PARSER, str, OffsetDateTime::from);
    }

    
    /**
     * Parses an ISO-8601 date-time string into an {@link java.time.OffsetDateTime}.
     * For example a string on the form {@code "2018-12-03T10:15:30+01:00"} (value with
     * offset) or {@code "2018-12-03T10:15:30"} (value without offset).
     * 
     * <p>
     * The string is parsed using {@link DateTimeFormatterISO#ISO_OPT_OFFSET_DATE_TIME_PARSER}.
     * 
     * <p>
     * Furthermore, parsing is attempted in a 
     * {@link DateTimeFormatterUtils#parseDualAttempt(java.time.format.DateTimeFormatter, java.lang.String, java.time.temporal.TemporalQuery)  two-step procedure}: 
     * If first parsing attempt fails the method will evaluate if the problem is
     * something which can likely be
     * {@link DateTimeFormatterUtils#fixTooManySecondsDecs(java.lang.String, java.time.format.DateTimeParseException)  fixed}
     * and if so it will "fix" the input value, {@code str}, and perform a new
     * parsing attempt.
 
     * @param str the string to parse
     * @param zoneOffsetProvider a function which will be called if no zone offset
     *        is present in the input string.
     * @return parsed value
     * @throws java.time.format.DateTimeParseException if unable to parse the string
     */
    public static OffsetDateTime parseIntoOffsetDateTime(String str, Function<LocalDateTime, ZoneOffset> zoneOffsetProvider) {
        return parseDualAttempt(DateTimeFormatterISO.ISO_OPT_OFFSET_DATE_TIME_PARSER, str, (temporal) -> {
            return DateTimeFormatterUtils.fromTemporalAccessorToOffsetDateTime(temporal, zoneOffsetProvider, -1, -1);
        });
    }
    
    /**
     * Parses an ISO-8601 date-time string into an {@link java.time.OffsetDateTime}.
     * 
     * <p>
     * This is a convenience method for the more generalized
     * {@link #parseIntoOffsetDateTime(java.lang.String, java.util.function.Function)}
     * method.
     * 
     * @param str the string to parse
     * @param defaultZoneOffset the zone offset to use if none is present in the
     *        input string.
     * @return parsed value
     * @throws java.time.format.DateTimeParseException if unable to parse the string
     */
    public static OffsetDateTime parseIntoOffsetDateTime(String str, ZoneOffset defaultZoneOffset) {
        return parseIntoOffsetDateTime(str, (t) -> {
            return defaultZoneOffset;
        });
    }

    /**
     * Parses a ISO-8601 date string into an {@link OffsetDate OffsetDate}.
     * For example a string on the form {@code "2018-12-03+01:00"} (value with
     * offset) or {@code "2018-12-03"} (value without offset).
     * 
     * <p>
     * The string is parsed using {@link DateTimeFormatterISO#ISO_OPT_OFFSET_DATE_PARSER}.
     * 
     * 
     * @param str the string to parse
     * @param zoneOffsetProvider a function which will be called if no zone offset
     *        is present in the input string.
     * @return parsed value
     * @throws java.time.format.DateTimeParseException if unable to parse the string
     */
    public static OffsetDate parseIntoOffsetDate(String str, Function<LocalDate,ZoneOffset> zoneOffsetProvider) {
        return DateTimeFormatterISO.ISO_OPT_OFFSET_DATE_PARSER.parse(str, (temporal) -> {
            return DateTimeFormatterUtils.fromTemporalAccessorToOffsetDate(temporal, zoneOffsetProvider, -1, -1);
        });
    }
    
    /**
     * Parses a ISO-8601 date string into an {@link OffsetDate OffsetDate}.
     * 
     * <p>
     * This is a convenience method for the more generalized
     * {@link #parseIntoOffsetDate(java.lang.String, java.util.function.Function)}
     * method.
     * 
     * 
     * @param str the string to parse
     * @param defaultZoneOffset the zone offset to use if none is present in the
     *        input string.
     * @return parsed value
     * @throws java.time.format.DateTimeParseException if unable to parse the string
     */
    public static OffsetDate parseIntoOffsetDate(String str, ZoneOffset defaultZoneOffset) {
        return parseIntoOffsetDate(str, (t) -> {
            return defaultZoneOffset;
        });
    }
    
    /**
     * Parses a ISO-8601 date string into an {@link OffsetDateTime OffsetDateTime}.
     * For example a string on the form {@code "2018-12-03+01:00"} (value with
     * offset) or {@code "2018-12-03"} (value without offset).
     * 
     * <p>
     * The string is parsed using {@link DateTimeFormatterISO#ISO_OPT_OFFSET_DATE_PARSER}.
     * 
     * 
     * @param str the string to parse
     * @param zoneOffsetProvider a function which will be called if no zone offset
     *        is present in the input string.
     * @return parsed value (with no time value, meaning time value is set to midnight)
     * @throws java.time.format.DateTimeParseException if unable to parse the string
     */
    public static OffsetDateTime parseIntoOffsetDateTimeNoTime(String str, Function<LocalDate, ZoneOffset> zoneOffsetProvider) {
        return DateTimeFormatterISO.ISO_OPT_OFFSET_DATE_PARSER.parse(str, (temporal) -> {
            return DateTimeFormatterUtils.fromTemporalAccessorToOffsetDateTimeNoTime(temporal, zoneOffsetProvider, -1, -1);
        });
    }

    /**
     * Parses a ISO-8601 date string into an {@link OffsetDateTime OffsetDateTime}.
     * 
     * <p>
     * This is a convenience method for the more generalized
     * {@link #parseIntoOffsetDateTimeNoTime(java.lang.String, java.util.function.Function)}
     * method.
     * 
     * 
     * @param str the string to parse
     * @param defaultZoneOffset the zone offset to use if none is present in the
     *        input string.
     * @return parsed value (with no time value, meaning time value is set to midnight)
     * @throws java.time.format.DateTimeParseException if unable to parse the string
     */
    public static OffsetDateTime parseIntoOffsetDateTimeNoTime(String str, ZoneOffset defaultZoneOffset) {
        return parseIntoOffsetDateTimeNoTime(str, (t) -> {
            return defaultZoneOffset;
        });
    }
    
    /**
     * Parses a ISO-8601 time string into an {@link java.time.OffsetTime}.
     * For example a string on the form {@code "10:15:30+01:00"} (value with
     * offset) or {@code "10:15:30"} (value without offset).
     * 
     * <p>
     * The string is parsed using {@link DateTimeFormatterISO#ISO_OPT_OFFSET_TIME_PARSER}.
     * 
     * <p>
     * Furthermore, parsing is attempted in a 
     * {@link DateTimeFormatterUtils#parseDualAttempt(java.time.format.DateTimeFormatter, java.lang.String, java.time.temporal.TemporalQuery)  two-step procedure}: 
     * If first parsing attempt fails the method will evaluate if the problem is
     * something which can likely be
     * {@link DateTimeFormatterUtils#fixTooManySecondsDecs(java.lang.String, java.time.format.DateTimeParseException)  fixed}
     * and if so it will "fix" the input value, {@code str}, and perform a new
     * parsing attempt.
 
     * @param str the string to parse
     * @param zoneOffsetProvider a function which will be called if no zone offset
     *        is present in the input string.
     * @return parsed value
     * @throws java.time.format.DateTimeParseException if unable to parse the string
     */
    public static OffsetTime parseIntoOffsetTime(String str, Function<LocalTime, ZoneOffset> zoneOffsetProvider) {
        return parseDualAttempt(DateTimeFormatterISO.ISO_OPT_OFFSET_TIME_PARSER, str, (temporal) -> {
            return DateTimeFormatterUtils.fromTemporalAccessorToOffsetTime(temporal, zoneOffsetProvider);
        });
    }
    
    /**
     * Parses a ISO-8601 time string into an {@link java.time.OffsetTime}.
     * 
     * <p>
     * This is convenience method for the more generalized 
     * {@link #parseIntoOffsetTime(java.lang.String, java.util.function.Function)}
     * method.
 
     * @param str the string to parse
     * @param defaultZoneOffset the zone offset to use if none is present in the
     *        input string.
     * @return parsed value
     * @throws java.time.format.DateTimeParseException if unable to parse the string
     */
    public static OffsetTime parseIntoOffsetTime(String str, ZoneOffset defaultZoneOffset) {
        return parseIntoOffsetTime(str, (t) -> {
            return defaultZoneOffset;
        });
    }
    
    
}
