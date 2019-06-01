# Introduction

jTextTime is a small no-dep library to convert between various derivatives
of the ISO 8601 text format and the `java.time` classes 
`OffsetDateTime` and `OffsetTime`.

[ISO 8601](https://en.wikipedia.org/wiki/ISO_8601) is a very versatile format. 
Its 'extended form' is what most derivatives are based on and also what this 
library focuses on. Unfortunately, because of its flexibility, it rarely 
makes sense for an application to state that it "publishes date-time values
according to the ISO 8601 format". Yet, this is what many applications do.

jTextTime focuses on 3 date-time formats:

* XML Date-Time Schema types. This is a well-defined, narrowed, derivative
of the ISO 8601 Extended Format. jTextTime provides parsing and formatting
features. In addition, it provides ready-made JAXB type adapters for smooth 
interaction with JAXB.

* ECMAScript5 Date Time format. jTextTime provides parsing and formatting
features.

* Generic ISO 8601 Extended format. There is already support for this in the JDK, but 
jTextTime adds support for lenient parsing and support for the format where
the timezone offset may be absent.

When *parsing* from text to date-time objects the library allows optional
lenient parsing, for example accepting a space as the separator between
the date and time value. Lenient parsing is only done where the interpretation
is unambiguous.

When *formatting* the library places emphasis on strict compliance with
the given specification.


## Download

The library is available from Central Maven:

```xml
<dependency>
    <groupId>com.addicticks.oss.jaxb</groupId>
    <artifactId>java8datetime</artifactId>
    <version> ... latest ...</version>
</dependency>
```

The library has no transitive dependencies.

Requires Java 8 or later.

## JAXB

The library makes working with `java.time` values in JAXB a breeze.
Normally you'll be forced to work with XMLGregorianCalendar, but not anymore.

See [Wiki](https://github.com/Addicticks/jTexttime/wiki/JAXB-type-adapter-classes-for-java.time)
for more information.



## License

[Apache License, version 2](https://www.apache.org/licenses/LICENSE-2.0).


## Support

Log an issue here in GitHub. Pull requests are welcome.
