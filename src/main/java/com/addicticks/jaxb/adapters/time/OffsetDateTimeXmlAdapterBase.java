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

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * JAXB adapter base class to map between XML date-time values and 
 * Java 8 Date-Time. (more specifically the {@code Offset}-classes).
 * 
 * 
 */
abstract class OffsetDateTimeXmlAdapterBase<T> extends XmlAdapter<String, T> {

    
    @Override
    public abstract T unmarshal(String v); 

    @Override
    public abstract String marshal(T v);
    
  
    /**
     * Gets the current ZoneOffset. Sub-classes may override this.
     * 
     * <p>
     * This method is needed because the XML Schema {@code date}, {@code time} and
     * {@code dateTime} data types are allowed to leave out the offset. Therefore,
     * when unmarshalling there may be no offset in the input data. If this is
     * the case then this method will be called.
     * 
     * <p>
     * The default implementation provides the offset at the current 
     * time using the system's default zone id 
     * (from {@link java.time.ZoneOffset#systemDefault()}).
     * 
     * @return offset to use when none is present in XML input data
     */
    public ZoneOffset getCurrentZoneOffset() {
        ZoneId systemDefaultZoneId = ZoneOffset.systemDefault();
        return  systemDefaultZoneId.getRules().getOffset(Instant.now());
    }    
    
}
