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

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.util.Locale;

/**
 * Temporal Fields with no value.
 * 
 * <p>
 * Strictly speaking these are anomalies. This class exists for the sole purpose
 * of being able to express different allowed values for literals in a
 * {@code DateTimeFormatter}.
 */
enum NoValueField implements TemporalField {

    DATE_TIME_DELIMITER {
        @Override
        public String toString() {
            return "Date Time delimiter";
        }
      
    };

    @Override
    public String getDisplayName(Locale locale) {
        return toString();
    }

    @Override
    public TemporalUnit getBaseUnit() {
        return ChronoUnit.FOREVER; // Irrelevant, but we must return something.
    }

    @Override
    public TemporalUnit getRangeUnit() {
        return ChronoUnit.FOREVER; // Irrelevant, but we must return something.
    }

    @Override
    public ValueRange range() {
        return ValueRange.of(1, 99);
    }

    @Override
    public boolean isDateBased() {
        return false;
    }

    @Override
    public boolean isTimeBased() {
        return false;
    }

    @Override
    public boolean isSupportedBy(TemporalAccessor temporal) {
        return false;
    }

    @Override
    public ValueRange rangeRefinedBy(TemporalAccessor temporal) {
        return range();
    }

    @Override
    public long getFrom(TemporalAccessor temporal) {
        return 1;   // Field has no value so we always return 1
    }

    @Override
    public <R extends Temporal> R adjustInto(R temporal, long newValue) {
        return temporal;
    }
    
}
