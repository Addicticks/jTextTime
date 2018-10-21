# JAXB type adapter classes for java.time

Working with date and time values in JAXB can be cumbersome because you are 
(normally) forced to work with `XMLGregorianCalendar`. However, there is a 
simple solution for that: make JAXB use Java's `OffsetDateTime` and 
`OffsetTime` instead. This library provides a set of `XmlAdapters` to
achieve this goal.

Adapters:

| XmlAdapter class | XML schema data type | Java class
| --- | --- | ---
| `OffsetDateTimeXmlAdapter` | `xs:dateTime` | `OffsetDateTime`
| `OffsetTimeXmlAdapter` | `xs:time` | `OffsetTime`
| `OffsetDateXmlAdapter` | `xs:date` | `OffsetDateTime` (with time fields set to midnight)
| `OffsetDateClassXmlAdapter` | `xs:date` | `OffsetDate` (custom class)

## Download

The library is available from Central Maven:

```xml
<dependency>
    <groupId>com.addicticks.oss.jaxb</groupId>
    <artifactId>java8datetime</artifactId>
    <version> ... latest ...</version>
</dependency>
```

## Usage in classes

It is outside the scope of this guide to explain how JAXB XmlAdapters are applied. 
But here's a little taste:

```java
public class Customer {

    @XmlElement
    @XmlJavaTypeAdapter(OffsetDateTimeXmlAdapter.class)
    @XmlSchemaType(name="dateTime")
    public OffsetDateTime getLastOrderTime() {
        ....
    }
    
    @XmlElement
    @XmlJavaTypeAdapter(OffsetDateXmlAdapter.class)
    @XmlSchemaType(name="date")
    public OffsetDateTime getDateOfBirth() {   // returns a date-only value
        ....
    }
}
```

The trick is the [XmlJavaTypeAdapter](https://docs.oracle.com/javase/8/docs/api/javax/xml/bind/annotation/adapters/XmlJavaTypeAdapter.html) annotation which can be applied to fields, getters/setters, packages, etc. 
In fact the most convenient usage of this library is probably to apply the 
annotations at the *package level*. Thereby you do not have to annotate each element/attribute 
individually. Simply put something like the following in your `package-info.java` file:

```java
@XmlJavaTypeAdapters
({
    @XmlJavaTypeAdapter(value=OffsetDateTimeXmlAdapter.class,  type=OffsetDateTime.class),
    @XmlJavaTypeAdapter(value=OffsetTimeXmlAdapter.class,      type=OffsetTime.class),
    @XmlJavaTypeAdapter(value=OffsetDateClassXmlAdapter.class, type=OffsetDate.class),
})
@XmlSchemaTypes
({
    @XmlSchemaType(name="dateTime", type=OffsetDateTime.class),
    @XmlSchemaType(name="time", type=OffsetTime.class),
    @XmlSchemaType(name="date", type=OffsetDate.class)
})
package org.example.mydtopackage;

import com.addicticks.jaxb.adapters.time.OffsetDate;
import com.addicticks.jaxb.adapters.time.OffsetDateTimeXmlAdapter;
import com.addicticks.jaxb.adapters.time.OffsetDateClassXmlAdapter;
import com.addicticks.jaxb.adapters.time.OffsetTimeXmlAdapter;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSchemaTypes;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;

```

The adapter will now apply to all classes in the package.
Furthermore, because of the `@XmlSchemaTypes` annotation, if you
generate an XML Schema from your classes, it will use correct schema types.
(otherwise you'll see `xs:string` as the schema type for such elements/attributes,
which is *not* what you want)


## Differences between XML date/time types and Java's Offset datetime classes

The `OffsetTime` and `OffsetDateTime` classes introduced in Java 8 are
the closest equivalents from the java.time package to the XML Schema date 
and time values. However, there are a few subtle differences:

* The XML date/time data types allows for the timezone offset to be left out. 
Therefore, when unmarshalling, we may have to supply our own value for the offset. 
This value should depend entirely on the scenario. By default the library will 
supply the current offset from the JVM's default `ZoneId`, but this may not be 
what you want. In this case then extend the adapters and override 
the `getCurrentZoneOffset()` method.

* Java doesn't have a date-only data type. Two different adapters are
  provided for converting to/from `xs:date`:
    * `OffsetDateClassXmlAdapter` uses a custom `OffsetDate` class which is merely 
       a thin wrapper around the `OffsetDateTime` class from the JDK. This is
       likely to be the most convenient alternative if you want to apply
       the `@XmlJavaTypeAdapter` annotation at the package level.
    * `OffsetDateXmlAdapter` uses the JDK's own `OffsetDateTimeClass`
      with the time set to midnight. This is likely to be the most convenient
      alternative if you are using XJC to generate classes from XML schema.

* The XML `xs:time` and `xs:dateTime` data types allow for unlimited number of 
digits in the fractional part of the seconds element. For example the following 
is a perfectly valid `xs:time` value: "23:30:28.123456789012345678901234567890". 
Parsing (unmarshalling) this will fail because Java's `OffsetTime` (and `OffsetDateTime`) 
only allows up to nano seconds precision. This means no more than 9 digits 
after the decimal point.


## Usage with XJC

If you generate Java classes from XML schema using the `xjc` tool then you must 
use a so-called bindings file to instruct the xjc tool which adapter you want to use.

Using the out-of-the-box adapters from this library then the bindings file 
should look like this:

```xml 
<?xml version="1.0" encoding="UTF-8"?>
    <!-- This file is automatically picked up by the jaxb2-maven-plugin
         if it lives in src/main/xjb                                -->
<jxb:bindings   
        xmlns:jxb="http://java.sun.com/xml/ns/jaxb" 
        xmlns:xs="http://www.w3.org/2001/XMLSchema"
        xmlns:xjc="http://java.sun.com/xml/ns/jaxb/xjc"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://java.sun.com/xml/ns/jaxb http://java.sun.com/xml/ns/jaxb/bindingschema_2_0.xsd"
        version="2.1">

    <jxb:globalBindings>
        <!-- Avoid having to work with XMLGregorianCalendar. 
             Instead, map as follows:
             
                 XML dateTime   :   OffsetDateTime  
                 XML date       :   OffsetDateTime  (time value truncated)
                 XML time       :   OffsetTime                             -->
             
        <xjc:javaType adapter="com.addicticks.jaxb.adapters.time.OffsetDateTimeXmlAdapter"
                      name="java.time.OffsetDateTime" xmlType="xs:dateTime"/>
        <xjc:javaType adapter="com.addicticks.jaxb.adapters.time.OffsetDateXmlAdapter"
                      name="java.time.OffsetDateTime" xmlType="xs:date"/>
        <xjc:javaType adapter="com.addicticks.jaxb.adapters.time.OffsetTimeXmlAdapter"
                      name="java.time.OffsetTime" xmlType="xs:time"/>
        
    </jxb:globalBindings>

</jxb:bindings>
``` 

If you are using the [JAXB2 Maven Plugin](https://www.mojohaus.org/jaxb2-maven-plugin/)
then name the file `jaxb-datetime-bindings.xjb` and place it in `src/main/xjb`.
Thereby it will automatically be picked up by the plugin.



## License

[Apache License, version 2](https://www.apache.org/licenses/LICENSE-2.0).
