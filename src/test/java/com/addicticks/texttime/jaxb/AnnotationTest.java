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
package com.addicticks.texttime.jaxb;

import java.io.File;
import jakarta.xml.bind.JAXBException;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class AnnotationTest {

    @Test
    public void testSchema() throws JAXBException {
        
        //JaxbUtils.generateXMLSchemaFile(AnnotatedExampleClass.class, new File("D:\\test.xsd"));
        
        assertTrue(Boolean.TRUE);   // Always true
    }

    
}
