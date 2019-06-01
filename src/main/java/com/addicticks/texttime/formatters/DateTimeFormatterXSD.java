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
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.NANO_OF_SECOND;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import java.util.Locale;
import static com.addicticks.texttime.formatters.DateTimeFormatterUtils.parseDualAttempt;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.Function;

/**
 * Formatter-Parsers for XML Schema types {@code xs:date}, {@code xs:dateTime} and {@code xs:time}.
 * 
 * <p>
 * Predefined Formatter-Parsers for these value types are not included in
 * the JDK. 
 * 
 * <p>
 * It it not uncommon that XML documents (or JSON documents) use a format
 * for date and time values which is aligned with ISO-8601, but do not (strictly)
 * conform to the XML Schema Specification. The former allows much more freedom
 * than the latter. Freedom in the case of data interchange between two parties
 * is a really bad idea. This is why the XML Schema Specification in this respect
 * is superior to the ISO-8601 specification.
 * 
 * <p>While it is sad that the XML Schema Specification is sometimes 
 * not strictly followed in XML and JSON documents it is nevertheless a reality.
 * For this reason, this class has predefined parsers which are lenient and therefore
 * strictly speaking do not conform to the XML Schema Specification. However, only 
 * unambiguous deviations are accepted while parsing.
 * 
 * 
 * <p>
 * The closest JDK equivalent to {@code xs:dateTime}, {@code xs:date} and 
 * {@code xs:time} are {@link java.time.OffsetDateTime} and {@link java.time.OffsetTime}.
 * However, the XML Schema data types do not mandate the presence of the timezone offset
 * and therefore they have no direct equivalent in the JDK.
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
 * <td> {@link #XSD_DATETIME_PARSER}</td>
 * <td> Parsing </td> 
 * <td> For {@code xs:dateTime} lenient parsing </td> 
 * <td> {@code '2018-12-03T10:15:30+01:00'} <br>{@code '2018-12-03 10:15:30+01:00'} <br>{@code '2018-12-03T10:15:30+01'} <br>{@code '2018-12-03t10:15:30+01'} <br>{@code '2018-12-03T10:15:30Z'} <br>{@code '2018-12-03T10:15:30z'}</td>
 * </tr>
 * <tr class="altColor">
 * <td> {@link #XSD_DATETIME}</td>
 * <td> Parsing<br>Formatting </td> 
 * <td> For {@code xs:dateTime} formatting or strict parsing </td> 
 * <td> {@code '2018-12-03T10:15:30+01:00'} <br> {@code '2018-12-03T10:15:30Z'}</td>
 * </tr>
 * <tr class="rowColor">
 * <td> {@link #XSD_DATE_PARSER}</td>
 * <td> Parsing </td> 
 * <td> For {@code xs:date} lenient parsing </td> 
 * <td> {@code '2018-12-03+01:00'} <br>{@code '2018-12-03+01'} <br>{@code '2018-12-03Z'} <br>{@code '2018-12-03z'}</td>
 * </tr>
 * <tr class="altColor">
 * <td> {@link #XSD_DATE}</td>
 * <td> Parsing<br>Formatting </td> 
 * <td> For {@code xs:date} formatting or strict parsing </td> 
 * <td> {@code '2018-12-03+01:00'} <br>{@code '2018-12-03Z'} </td>
 * <tr class="rowColor">
 * <td> {@link #XSD_TIME_PARSER}</td>
 * <td> Parsing </td> 
 * <td> For {@code xs:time} lenient parsing </td> 
 * <td> {@code '10:15:30+01:00'} <br>{@code '10:15:30+01'} <br>{@code '10:15:30Z'} <br>{@code '10:15:30z'}</td>
 * </tr>
 * <tr class="altColor">
 * <td> {@link #XSD_TIME}</td>
 * <td> Parsing<br>Formatting </td> 
 * <td> For {@code xs:time} formatting or strict parsing </td> 
 * <td> {@code '10:15:30+01:00'} <br>{@code '10:15:30Z'} </td>
 * </tr>
 * </tbody>
 * </table>
 * 
 * <p>
 * <h3 id="parserMethods">Parsing</h3>
 * This class also provides convenience methods for parsing XML Schema
 * date/time string values into their best match JDK class.
 * 
 * @see  <a href="https://www.w3.org/TR/xmlschema-2/">XML Schema Specification</a>.
 */
public class DateTimeFormatterXSD {
    
    private static final String XSD_TZ_OFFSET_PATTERN_STRICT = "+HH:MM";
    private static final String XSD_TZ_OFFSET_PATTERN_LENIENT = "+HH:mm";
    
    private DateTimeFormatterXSD() {
    }
    

    
    /**
     * For lenient parsing values of type {@code xs:dateTime}.
     * 
     * <p>
     * This Formatter-Parser is suitable only for parsing, not for formatting.
     * When used for formatting it may produce values which are not compliant
     * with the specification for {@code xs:dateTime}. Instead {@link #XSD_DATETIME}
     * should be used for formatting.
     * 
     * <p>
     * Lenient parsing is performed, meaning that certain non-compliant 
     * formats are accepted. These formats should not be encouraged but are
     * accepted by this parser as a pragmatic compromise. 
     * Compared to the formal definition of {@code xs:dateTime} the following
     * is accepted while parsing:
     * 
     * <ul>
     *   <li>In the time value the seconds field may be omitted. Hence 
     *       {@code "2019-03-12T14:45Z"} is an acceptable value. The formal 
     *       specification mandates that the seconds field is present, even if zero.</li>
     *   <li>The delimiter between the date and the time value may be a {@code 'T'} or
     *       a {@code ' '} (space). The formal definition only allows a {@code 'T'}. </li>
     *   <li>Literals in the string, i.e. {@code 'T'} and {@code 'Z'}, are accepted regardless 
     *       of upper/lower-case. The formal specification mandates that these are
     *       in upper-case.</li>
     *   <li>If a time zone offset is specified it doesn't have to include minutes,
     *       meaning that {@code "+05"} is an acceptable value for an offset. 
     *       The formal definition mandates that the minutes <i>must</i> be specified, 
     *       i.e. {@code "+05:00"}. </li>
     *   <li>Trailing zeroes in the fractional second is acceptable, 
     *       meaning that a value such as {@code "2019-03-12T14:45:28.340Z"} will 
     *       be accepted. The formal specification says "the fractional second 
     *       string, if present, must not end in {@code '0'}".</li>
     * </ul>
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
     *           DateTimeFormatter dtf = DateTimeFormatterXSD.XSD_DATETIME_PARSER.withZone(ZoneId.of("America/New_York"));
     *           // Parse the string value. Since no offset is given in the input, the New York TZ is assumed.
     *           OffsetDateTime value = dtf.parse("2018-03-19:22:35:58", OffsetDateTime::from);
     *        </pre></li>
     * </ol>
     * 
     * <p>
     * <b>Compliance</b><br>
     * <ul>
     *    <li>Formatting: Should not be used for formatting. </li>
     *    <li>Parsing: Compliant with the formal definition for {@code xs:dateTme}, except for:
     *      <ul> 
     *         <li>A number of formally incorrect forms are accepted as explained above.</li>
     *         <li>The seconds fraction field may contain no more than 9 digits. 
     *             In the formal definition, the seconds fractional field has "arbitrary level of precision".
     *             This implementation will throw {@link java.time.format.DateTimeParseException DateTimeParseException}
     *             if the input has more than 9 fractional digits. 
     *             If this is a problem then {@link #parseIntoOffsetDateTime(java.lang.String, java.time.ZoneOffset) parseIntoOffsetDateTime()}
     *             can be used.</li>
     *      </ul>
     * </ul>
     * @see <a href="https://www.w3.org/TR/xmlschema-2/#dateTime">https://www.w3.org/TR/xmlschema-2/#dateTime</a>
     */
    public static final DateTimeFormatter XSD_DATETIME_PARSER;
    static {
        XSD_DATETIME_PARSER = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(ISO_LOCAL_DATE)
                .appendText(NoValueField.DATE_TIME_DELIMITER, DATETIME_DELIMETER_LENIENT_MAP)
                .append(ISO_LOCAL_TIME)
                .optionalStart()
                .appendOffset(XSD_TZ_OFFSET_PATTERN_LENIENT, "Z")
                .optionalEnd()
                .toFormatter(Locale.US)
                .withResolverStyle(ResolverStyle.STRICT)
                .withChronology(IsoChronology.INSTANCE);
    }
    
 
    
    /**
     * For parsing and formatting of {@code xs:dateTime} values
     * as defined in <a href="https://www.w3.org/TR/xmlschema-2/#dateTime">XML Schema</a>..
     * 
     * <p>
     * This Formatter-Parser is suitable for both parsing and formatting.
     * When used as a formatter it produces output compliant with the 
     * specification for {@code xs:dateTime}. When used as a parser
     * it mandates strict compliance with the specification for {@code xs:dateTime}.
     * In most scenarios, {@link #XSD_DATETIME_PARSER} is probably a better
     * fit for parsing as it is more forgiving.
     * 
     * 
     * <p>
     * <b>Compliance</b><br>
     * <ul>
     *    <li>Formatting: Produces output fully compliant with
     *        formal definition for {@code xs:dateTime} assuming that
     *        a {@link java.time.OffsetDateTime} or {@link java.time.LocalDateTime} is used as input. </li>
     *    <li>Parsing: Compliant with the formal definition for {@code xs:dateTime}, except for:
     * <ul> 
     *   <li>The seconds fraction field may contain no more than 9 digits. 
     *       In the formal definition, the seconds fractional field has "arbitrary level of precision".
     *       This implementation will throw {@link java.time.format.DateTimeParseException DateTimeParseException}
     *       if the input has more than 9 fractional digits. </li>
     *   <li>Trailing zeroes in the fractional second is acceptable, 
     *       meaning that a value such as {@code "2019-03-12T14:45:28.340Z"} will 
     *       be accepted. The formal specification says "the fractional second 
     *       string, if present, must not end in {@code '0'}".</li>
     * </ul>
     * </ul>
     * 
     * 
     * @see <a href="https://www.w3.org/TR/xmlschema-2/#dateTime">https://www.w3.org/TR/xmlschema-2/#dateTime</a>
     */
    public static final DateTimeFormatter XSD_DATETIME;
    static {
        XSD_DATETIME = new DateTimeFormatterBuilder()
                .parseCaseSensitive()
                .append(ISO_LOCAL_DATE)
                .appendLiteral('T')
                .appendValue(HOUR_OF_DAY, 2)
                .appendLiteral(':')
                .appendValue(MINUTE_OF_HOUR, 2)
                .appendLiteral(':')
                .appendValue(SECOND_OF_MINUTE, 2)
                .optionalStart()
                .appendFraction(NANO_OF_SECOND, 0, 9, true)
                .optionalEnd()
                .optionalStart()
                .appendOffset(XSD_TZ_OFFSET_PATTERN_STRICT, "Z")
                .optionalEnd()
                .toFormatter(Locale.US)
                .withResolverStyle(ResolverStyle.STRICT)
                .withChronology(IsoChronology.INSTANCE);
    }    
    
    /**
     * For lenient parsing values of type {@code xs:date}.
     * 
     * <p>
     * This Formatter-Parser is suitable only for parsing, not for formatting.
     * Instead {@link #XSD_DATE} should be used for formatting.
     * 
     * <p>
     * Lenient parsing is performed, meaning that certain non-compliant 
     * formats are accepted. These formats should not be encouraged but are
     * accepted by this parser as a pragmatic compromise. 
     * Compared to the formal definition of {@code xs:date} the following
     * is accepted while parsing:
     * 
     * <ul>
     *    <li>The literal {@code 'Z'} in a timezone offset is
     *        accepted both in upper- and lower-case.</li>    
     *    <li>If a time zone offset is specified it doesn't have to include minutes,
     *        meaning that {@code '+05'} is an acceptable value. The formal definition mandates
     *        that the minutes <i>must</i> be specified, i.e. {@code '+05:00'}. </li>
     * </ul>
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
     *           DateTimeFormatter dtf = DateTimeFormatterXSD.XSD_DATE_PARSER.withZone(ZoneId.of("America/New_York"));
     *           // Parse the string value. Since no offset is given in the input, the New York TZ is assumed.
     *           OffsetDate value = dtf.parse("2018-03-19", OffsetDate::from);
     *        </pre></li>
     * </ol>
     * 
     * <p>
     * <b>Compliance</b><br>
     * <ul>
     *    <li>Formatting: Should not be used for formatting. </li>
     *    <li>Parsing: Compliant with the formal definition for {@code xs:date}, except for
     *        the forms accepted during parsing as listed above.</li>
     * </ul>
     * 
     * @see <a href="https://www.w3.org/TR/xmlschema-2/#date">https://www.w3.org/TR/xmlschema-2/#date</a>
     */
    public static final DateTimeFormatter XSD_DATE_PARSER;
    static {
        XSD_DATE_PARSER = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(DateTimeFormatter.ISO_LOCAL_DATE)
                .optionalStart()
                .appendOffset(XSD_TZ_OFFSET_PATTERN_LENIENT, "Z")
                .optionalEnd()
                .toFormatter(Locale.US)
                .withResolverStyle(ResolverStyle.STRICT)
                .withChronology(IsoChronology.INSTANCE);
    }

    /**
     * For parsing and formatting of {@code xs:date} values
     * as defined in <a href="https://www.w3.org/TR/xmlschema-2/#date">XML Schema</a>.
     * 
     * <p>
     * This Formatter-Parser is suitable for both parsing and formatting.
     * When used as a formatter it produces output compliant with the 
     * specification for {@code xs:date}. When used as a parser
     * it mandates strict compliance with the specification for {@code xs:date}.
     * In most scenarios, {@link #XSD_DATE_PARSER} is probably a better
     * fit for parsing as it is more forgiving.
     * 
     * 
     * <p>
     * <b>Compliance</b><br>
     * <ul>
     *    <li>Formatting: Produces output fully compliant with the formal definition for {@code xs:date}
     *        assuming that a {@link java.time.OffsetDateTime} or {@link java.time.LocalDate} is used as input.
     *        </li>
     *    <li>Parsing: Compliant with the formal definition for {@code xs:date}.
     *        </li>
     * </ul>
     * @see <a href="https://www.w3.org/TR/xmlschema-2/#date">https://www.w3.org/TR/xmlschema-2/#date</a>
     */ 
    public static final DateTimeFormatter XSD_DATE;
    static {
        XSD_DATE = new DateTimeFormatterBuilder()
                .parseCaseSensitive()
                .append(DateTimeFormatter.ISO_LOCAL_DATE)
                .optionalStart()
                .appendOffset(XSD_TZ_OFFSET_PATTERN_STRICT, "Z")
                .optionalEnd()
                .toFormatter(Locale.US)
                .withResolverStyle(ResolverStyle.STRICT)
                .withChronology(IsoChronology.INSTANCE);
    }
    
    /**
     * For lenient parsing values of type {@code xs:time}.
     * 
     * <p>
     * This Formatter-Parser is suitable only for parsing, not for formatting.
     * Instead {@link #XSD_TIME} should be used for formatting.
     * 
     * <p>
     * Lenient parsing is performed, meaning that certain non-compliant 
     * formats are accepted. These formats should not be encouraged but are
     * accepted by this parser as a pragmatic compromise. 
     * Compared to the formal definition of {@code xs:time} the following
     * is accepted while parsing:
     * 
     * <ul>
     *    <li>In the time value the seconds field may be omitted. Hence 
     *       {@code "14:45Z"} is an acceptable value. The formal 
     *       specification mandates that the seconds field is present, even if zero.</li>
     *    <li>The literal {@code 'Z'} in a timezone offset is
     *        accepted both in upper- and lower-case.</li>    
     *    <li>If a time zone offset is specified it doesn't have to include minutes,
     *        meaning that {@code '+05'} is an acceptable value. The formal definition mandates
     *        that the minutes <i>must</i> be specified, i.e. {@code '+05:00'}. </li>
     *    <li>Trailing zeroes in the fractional second is acceptable, 
     *        meaning that a value such as {@code "2019-03-12T14:45:28.340Z"} will 
     *        be accepted. The formal specification says "the fractional second 
     *        string, if present, must not end in {@code '0'}".</li>
     * </ul>
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
     *           DateTimeFormatter dtf = DateTimeFormatterXSD.XSD_TIME_PARSER.withZone(ZoneId.of("America/New_York"));
     *           // Parse the string value. Since no offset is given in the input, the New York TZ is assumed.
     *           OffsetTime value = dtf.parse("22:33:58", OffsetTime::from);
     *        </pre></li>
     * </ol>
     * 
     * <p>
     * <b>Compliance</b><br>
     * <ul>
     *    <li>Formatting: Should not be used for formatting. </li>
     *    <li>Parsing: Compliant with the formal definition for {@code xs:time}, except for:
     *      <ul> 
     *         <li>A number of formally incorrect forms are accepted as explained above.</li>
     *         <li>The seconds fraction field may contain no more than 9 digits. 
     *             In the formal definition, the seconds fractional field has "arbitrary level of precision".
     *             This implementation will throw {@link java.time.format.DateTimeParseException DateTimeParseException}
     *             if the input has more than 9 fractional digits.
     *             If this is a problem then {@link #parseIntoOffsetTime(java.lang.String, java.time.ZoneOffset) parseIntoOffsetTime()}
     *             can be used.</li>
     *      </ul>
     * </ul>
     * 
     * @see <a href="https://www.w3.org/TR/xmlschema-2/#time">https://www.w3.org/TR/xmlschema-2/#time</a>
     */
    public static final DateTimeFormatter XSD_TIME_PARSER;
    static {
        XSD_TIME_PARSER = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(DateTimeFormatter.ISO_LOCAL_TIME)
                .optionalStart()
                .appendOffset(XSD_TZ_OFFSET_PATTERN_LENIENT, "Z")
                .optionalEnd()
                .toFormatter(Locale.US)
                .withResolverStyle(ResolverStyle.STRICT)
                .withChronology(IsoChronology.INSTANCE);
    }

    
    /**
     * For parsing and formatting of {@code xs:time} values
     * as defined in <a href="https://www.w3.org/TR/xmlschema-2/#time">XML Schema</a>.
     * 
     * <p>
     * This Formatter-Parser is suitable for both parsing and formatting.
     * When used as a formatter it produces output compliant with the 
     * specification for {@code xs:time}. When used as a parser
     * it mandates strict compliance with the specification for {@code xsdtime}.
     * In most scenarios, {@link #XSD_TIME_PARSER} is probably a better
     * fit for parsing as it is more forgiving.
     * 
     * 
     * <p>
     * <b>Compliance</b><br>
     * <ul>
     *    <li>Formatting: Produces output fully compliant with formal definition 
     *        for {@code xs:time} assuming that a {@link java.time.OffsetTime} 
     *        or {@link java.time.LocalTime} is used as input. </li>
     *    <li>Parsing: Compliant with the formal definition for {@code xs:time}, except for:
     * <ul> 
     *   <li>The seconds fraction field may contain no more than 9 digits. 
     *       In the formal definition, the seconds fractional field has "arbitrary level of precision".
     *       This implementation will throw {@link java.time.format.DateTimeParseException DateTimeParseException}
     *       if the input has more than 9 fractional digits. </li>
     *   <li>Trailing zeroes in the fractional second is acceptable, 
     *       meaning that a value such as {@code "2019-03-12T14:45:28.340Z"} will 
     *       be accepted. The formal specification says "the fractional second 
     *       string, if present, must not end in {@code '0'}".</li>
     * </ul>
     * </ul>
     * 
     * @see <a href="https://www.w3.org/TR/xmlschema-2/#time">https://www.w3.org/TR/xmlschema-2/#time</a>
     */
    public static final DateTimeFormatter XSD_TIME;
    static {
        XSD_TIME = new DateTimeFormatterBuilder()
                .parseCaseSensitive()
                .append(DateTimeFormatter.ISO_LOCAL_TIME)
                .optionalStart()
                .appendOffset(XSD_TZ_OFFSET_PATTERN_STRICT, "Z")
                .optionalEnd()
                .toFormatter(Locale.US)
                .withResolverStyle(ResolverStyle.STRICT)
                .withChronology(IsoChronology.INSTANCE);
    }
    
    
    /**
     * Parses a {@code xs:dateTime} string into an {@link java.time.OffsetDateTime}.
     * For example a string on the form {@code "2018-12-03T10:15:30+01:00"} (value with
     * offset) or {@code "2018-12-03T10:15:30"} (value without offset).
     * 
     * <p>
     * The string is parsed using {@link DateTimeFormatterXSD#XSD_DATETIME_PARSER}
     * which is forgiving and accepts minor deviations from the official specification.
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
     *        is present in the {@code temporal}.
     * @return parsed value
     * @throws java.time.format.DateTimeParseException if unable to parse the string
     */
    public static OffsetDateTime parseIntoOffsetDateTime(String str, Function<LocalDateTime, ZoneOffset> zoneOffsetProvider) {
        return parseDualAttempt(DateTimeFormatterXSD.XSD_DATETIME_PARSER, str, (temporal) -> {
            return DateTimeFormatterUtils.fromTemporalAccessorToOffsetDateTime(temporal, zoneOffsetProvider, -1, -1);
        });
    }

    /**
     * Parses a {@code xs:dateTime} string into an {@link java.time.OffsetDateTime}.
     * 
     * <p>
     * This is convenience method for the more generalized 
     * {@link #parseIntoOffsetDateTime(java.lang.String, java.util.function.Function)}
     * method.
 
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
     * Parses a {@code xs:date} string into an {@link OffsetDate OffsetDate}.
     * For example a string on the form {@code "2018-12-03+01:00"} (value with
     * offset) or {@code "2018-12-03"} (value without offset).
     * 
     * <p>
     * The string is parsed using {@link DateTimeFormatterXSD#XSD_DATE_PARSER}
     * which is forgiving and accepts minor deviations from the official specification.
     * 
     * 
     * @param str the string to parse
     * @param zoneOffsetProvider a function which will be called if no zone offset
     *        is present in the input string.
     * @return parsed value
     * @throws java.time.format.DateTimeParseException if unable to parse the string
     */
    public static OffsetDate parseIntoOffsetDate(String str, Function<LocalDate, ZoneOffset> zoneOffsetProvider) {
        return DateTimeFormatterXSD.XSD_DATE_PARSER.parse(str, (temporal) -> {
            return DateTimeFormatterUtils.fromTemporalAccessorToOffsetDate(temporal, zoneOffsetProvider, -1, -1);
        });
    }
    
    /**
     * Parses a {@code xs:date} string into an {@link OffsetDate OffsetDate}.
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
     * Parses a {@code xs:date} string into an {@link OffsetDateTime OffsetDateTime}.
     * For example a string on the form {@code "2018-12-03+01:00"} (value with
     * offset) or {@code "2018-12-03"} (value without offset).
     * 
     * <p>
     * The string is parsed using {@link DateTimeFormatterXSD#XSD_DATE_PARSER}
     * which is forgiving and accepts minor deviations from the official specification.
     * 
     * 
     * @param str the string to parse
     * @param zoneOffsetProvider a function which will be called if no zone offset
     *        is present in the input string.
     * @return parsed value (with "no" time value, meaning time value is set to midnight)
     * @throws java.time.format.DateTimeParseException if unable to parse the string
     */
    public static OffsetDateTime parseIntoOffsetDateTimeNoTime(String str, Function<LocalDate, ZoneOffset> zoneOffsetProvider) {
        return DateTimeFormatterXSD.XSD_DATE_PARSER.parse(str, (temporal) -> {
            return DateTimeFormatterUtils.fromTemporalAccessorToOffsetDateTimeNoTime(temporal, zoneOffsetProvider, -1, -1);
        });
    }

    /**
     * Parses a {@code xs:date} string into an {@link OffsetDateTime OffsetDateTime}.
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
     * @return parsed value (with "no" time value, meaning time value is set to midnight)
     * @throws java.time.format.DateTimeParseException if unable to parse the string
     */
    public static OffsetDateTime parseIntoOffsetDateTimeNoTime(String str, ZoneOffset defaultZoneOffset) {
        return parseIntoOffsetDateTimeNoTime(str, (t) -> {
            return defaultZoneOffset;
        });
    }

    /**
     * Parses a {@code xs:time} string into an {@link java.time.OffsetTime}.
     * For example a string on the form {@code "10:15:30+01:00"} (value with
     * offset) or {@code "10:15:30"} (value without offset).
     * 
     * <p>
     * The string is parsed using {@link DateTimeFormatterXSD#XSD_TIME_PARSER}
     * which is forgiving and accepts minor deviations from the official specification.
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
        return parseDualAttempt(DateTimeFormatterXSD.XSD_TIME_PARSER, str, (temporal) -> {
            return DateTimeFormatterUtils.fromTemporalAccessorToOffsetTime(temporal, zoneOffsetProvider);
        });
    }
  
    /**
     * Parses a {@code xs:time} string into an {@link java.time.OffsetTime}.
     * 
     * <p>
     * This is a convenience method for the more generalized
     * {@link #parseIntoOffsetTime(java.lang.String, java.util.function.Function)}
     * method.
     * 
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
