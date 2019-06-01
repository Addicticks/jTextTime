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

import java.time.DateTimeException;

/**
 * An exception thrown when an error occurs during a temporal query during a
 * parse operation from classes in this package. The exception is used when the
 * expected elements are not found in the temporal. Typically the exception gets
 * wrapped in a
 * {@link java.time.format.DateTimeParseException DateTimeParseException} so
 * that this exception is mainly used as a linked exception, not the primary
 * (top-level) exception.
 *
 * @see java.time.temporal.TemporalQuery
 */
public class DateTimeTemporalQueryException extends DateTimeException {

    public DateTimeTemporalQueryException(String message,
                         Throwable cause) {
        super(message, cause);
    }
    
    
    public DateTimeTemporalQueryException(String message) {
        super(message);
    }
}
