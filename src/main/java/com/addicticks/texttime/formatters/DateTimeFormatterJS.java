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

import static com.addicticks.texttime.formatters.DateTimeFormatterUtils.DATETIME_DELIMETER_LENIENT_MAP;
import static com.addicticks.texttime.formatters.DateTimeFormatterUtils.parseDualAttempt;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.format.SignStyle;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.NANO_OF_SECOND;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import static java.time.temporal.ChronoField.YEAR;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

/**
 * Formatter-Parsers for ECMAScript (JavaScript) date-time values.
 * 
 * <p>The ECMAScript Date Time String format is based upon a simplification 
 * of the ISO 8601 Extended Format. The general format is as follows:
 * <pre>
 *    YYYY-MM-DDTHH:mm:ss.sssZ
 * </pre>
 * 
 * <p>
 * This format allows:
 * <ul>
 *   <li>In the date-part:
 *      <ul>
 *        <li>Leaving out the month value. It will then default to 1 (January).</li>
 *        <li>Leaving out the day-of-month value. It will then default to 1.</li>
 *      </ul>
 *   </li>
 *   <li>In the time-part:
 *      <ul>
 *        <li>Leaving out the minute value. It will then default to 0.</li>
 *        <li>Leaving out the seconds value. It will then default to 0.</li>
 *        <li>Leaving out the milliseconds value. It will then default to 0.</li>
 *      </ul>
 *   </li>
 *   <li>Suffix:
 *      <ul>
 *        <li>Leaving out timezone offset. It will then default to UTC. (same as using a 'Z' suffix).</li>
 *      </ul>
 *   </li>
 * </ul>
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
 * <td> {@link #JS_DATETIME_FORMATTER}</td>
 * <td> Formatting</td> 
 * <td> For formatting into ECMAScript Date-Time string value</td> 
 * <td> {@code '2018-12-03T10:15:30+01:00'} <br>{@code '2018-12-03T10:15:30Z'} </td>
 * </tr>
 * <tr class="altColor">
 * <td> {@link #JS_DATETIME_PARSER}</td>
 * <td> Parsing </td> 
 * <td> For parsing ECMAScript Date-Time string value</td> 
 * <td> {@code '2018-12-03T10:15:30+01:00'} <br>{@code '2018-12-03 10:15:30+01:00'} <br>{@code '2018-12-03T10:15:30+01'} <br>{@code '2018-12-03t10:15:30+01'} <br>{@code '2018-12-03T10:15:30Z'} <br>{@code '2018-12-03T10:15:30z'} <br>{@code '2018-12-03T10:15:30'} <br>{@code '2018'} <br>{@code '2018T10'} <br>{@code '2018-12T10:15:30'} </td>
 * </tr>
 * </tbody>
 * </table>
 * 
 * 
 * 
 * @see <a href="http://es5.github.io/#x15.9.1.15">ECMAScript Date Time String Format</a>
 * 
 */
public class DateTimeFormatterJS {
    
    
    private static final String JS_TZ_OFFSET_PATTERN_STRICT = "+HH:MM";
    private static final String JS_TZ_OFFSET_PATTERN_LENIENT = "+HH:mm";
    

    
    
    /**
     * For formatting into ECMAScript date-time string value.
     * 
     * <p>
     * This Formatter-Parser is intended for formatting. It is unsuitable 
     * for parsing.
     * 
     * <p>
     * All fields will be output even if zero. The output will always have 
     * all fields included.
     * 
     * @see <a href="http://es5.github.io/#x15.9.1.15">ECMAScript Date Time String Format</a>
     * @see #format(java.time.OffsetDateTime) 
     */
    public static final DateTimeFormatter JS_DATETIME_FORMATTER;
    static {
        JS_DATETIME_FORMATTER = new DateTimeFormatterBuilder()
                .parseCaseSensitive()
                .append(ISO_LOCAL_DATE)
                .optionalStart()
                .appendLiteral('T')
                .appendValue(HOUR_OF_DAY, 2)
                .appendLiteral(':')
                .appendValue(MINUTE_OF_HOUR, 2)
                .appendLiteral(':')
                .appendValue(SECOND_OF_MINUTE, 2)
                .optionalStart()
                .appendFraction(NANO_OF_SECOND, 3, 3, true)
                .optionalEnd()
                .appendOffset(JS_TZ_OFFSET_PATTERN_STRICT, "Z")
                .optionalEnd()
                .toFormatter(Locale.US)
                .withResolverStyle(ResolverStyle.STRICT)
                .withChronology(IsoChronology.INSTANCE);
    }
    
    /**
     * For parsing of ECMAScript date-time value.
     * 
     * <p>
     * This Formatter-Parser is suitable only for parsing, not for formatting.
     * When used for formatting it may produce values which are not compliant
     * with the ECMAScript specification. Instead {@link #JS_DATETIME_FORMATTER}
     * should be used for formatting.
     * 
     * <p>
     * Lenient parsing is performed, meaning that certain non-compliant 
     * formats are accepted. These formats should not be encouraged but are
     * accepted by this parser as a pragmatic compromise. 
     * Compared to the <a href="http://es5.github.io/#x15.9.1.15">formal definition</a> 
     * the following is accepted while parsing:
     * 
     * <ul>
     *   <li>The delimiter between the date and the time value may be a {@code 'T'} or
     *       a {@code ' '} (space). The formal definition only allows a {@code 'T'}. </li>
     *   <li>Literals in the string, i.e. {@code 'T'} and {@code 'Z'}, are accepted regardless 
     *       of upper/lower-case. The formal specification mandates that these are
     *       in upper-case.</li>
     *   <li>The seconds fraction field - if present - can have up to 9 digits.
     *       The formal definition mandates that this part of the string
     *       is exactly 3 digits (if present).</li>
     * </ul>
     * 
     * <p>
     * <b>Incompliance</b>
     * The formal definition says that {@code '24:00'} is a valid representation
     * for midnight. This parser will not accept such value because it only
     * accepts hour values in the range from 0 to 23.
     * <br>
     * <br>
     * <p>
     * Note: The date-time pattern where a space is used to separate the date
     * and the time value is not a pattern which should be encouraged. The
     * pattern is allowed in the ISO 8601 specification (allowed as an
     * exception: "by mutual agreement of the partners in information
     * interchange"), but is indeed not allowed in the <i>ECMAScript Date Time
     * String format</i>. It is allowed by this method as an exception because
     * allowing it does not create ambiguity.
     * 
     * @see <a href="http://es5.github.io/#x15.9.1.15">ECMAScript Date Time String Format</a>
     */
    public static final DateTimeFormatter JS_DATETIME_PARSER;
    static {
        JS_DATETIME_PARSER = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendValue(YEAR, 4, 6, SignStyle.EXCEEDS_PAD)  // ECMAScript allows at most 6 digigts on the year
                .optionalStart()
                .appendLiteral('-')
                .appendValue(MONTH_OF_YEAR, 2)
                .optionalStart()
                .appendLiteral('-')
                .appendValue(DAY_OF_MONTH, 2)
                .optionalEnd()
                .optionalEnd()
                
                .optionalStart()
                .appendText(NoValueField.DATE_TIME_DELIMITER, DATETIME_DELIMETER_LENIENT_MAP)
                .appendValue(HOUR_OF_DAY, 2)
                .optionalStart()
                .appendLiteral(':')
                .appendValue(MINUTE_OF_HOUR, 2)
                .optionalStart()
                .appendLiteral(':')
                .appendValue(SECOND_OF_MINUTE, 2)
                .optionalStart()
                .appendFraction(NANO_OF_SECOND, 0, 9, true)
                .optionalEnd()
                .optionalEnd()
                .optionalEnd()
                .optionalStart()
                .appendOffset(JS_TZ_OFFSET_PATTERN_LENIENT, "Z")
                .optionalEnd()
                .optionalEnd()
                .toFormatter(Locale.US)
                .withResolverStyle(ResolverStyle.STRICT)
                .withZone(ZoneOffset.UTC.normalized())
                .withChronology(IsoChronology.INSTANCE);
    }
    
    
    /**
     * Parses an ECMAScript date-time string into an {@link java.time.OffsetDateTime}.
     * For example a string on the form {@code "2018-12-03T10:15:30+01:00"} (value with
     * offset) or {@code "2018-12-03T10:15:30"} (value without offset, meaning {@code UTC}).
     * 
     * <p>
     * The string is parsed using {@link DateTimeFormatterJS#JS_DATETIME_PARSER}.
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
     * @throws java.time.format.DateTimeParseException if unable to parse the string
     */
    public static OffsetDateTime parseIntoOffsetDateTime(String str) {
        return parseDualAttempt(DateTimeFormatterJS.JS_DATETIME_PARSER, str, (temporal) -> {
            return DateTimeFormatterUtils.fromTemporalAccessorToOffsetDateTime(temporal, (t) -> {return ZoneOffset.UTC;}, 1, 1);
        });
    }

    /**
     * Formats an {@code OffsetDateTime} into a ECMAScript date-time string.
     * 
     * <p>
     * If the input's time value is {@code MIDNIGHT} and input's timezone
     * offset is {@code UTC} then the value is assumed to be a date-only
     * value and will be output without time part, e.g. {@code '2018-03-14'}.
     * 
     * @param dt
     * @return ECMAScript date-time string
     */
    public static String format(OffsetDateTime dt) {
        TemporalAccessor x = dt;
        if (dt.toLocalTime().equals(LocalTime.MIDNIGHT) && dt.getOffset().equals(ZoneOffset.UTC)) {
            x = dt.toLocalDate();
        }
        return  DateTimeFormatterJS.JS_DATETIME_FORMATTER.format(x);
    }
    
    
    private DateTimeFormatterJS() {
    }
    
}
