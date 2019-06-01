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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;

/**
 * JAXB Schema utilities
 */
public class JaxbUtils {
    
    /**
     * Generates an .XSD file for a given JAXB annotated (top)class.
     *
     * @param clazz
     * @param file file name, should have ".xsd" suffix.
     * @throws javax.xml.bind.JAXBException
     */
    public static void generateXMLSchemaFile(Class clazz, final File file) throws JAXBException {
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(clazz);
        } catch (JAXBException ex) {
            String errText = "Cannot generate Schema for class " + clazz.getName();
            throw new JAXBException(errText, ex);
        }
        try {
            jaxbContext.generateSchema(new SchemaOutputResolver() {

                @Override
                public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException {
                    return new StreamResult(file);
                }
            });
        } catch (IOException ex) {
            String errText = "Cannot generate Schema for class " + clazz.getName() + ". Error writing output file.";
            throw new JAXBException(errText, ex);
        }
    }

    /**
     * Generates in-memory schema of the given JAXB context.
     * Typically this method is not used directly.
     * 
     * @param context
     * @return
     * @throws SAXException
     */
    public static Schema getSchemaFromJAXBContext(JAXBContext context) throws SAXException, IOException {
        String schemaLang = "http://www.w3.org/2001/XMLSchema";
        SchemaFactory sFactory = SchemaFactory.newInstance(schemaLang);

        Schema schema = sFactory.newSchema(generateJaxbSources(context));
        return schema;
    }

    /**
     * Generates in-memory DOMSource's from the given JAXB context.
     * Typically this method is not used directly.
     *
     * @param context
     * @return
     */
    protected static DOMSource[] generateJaxbSources(JAXBContext context) throws IOException {
        final List<DOMResult> domResults = new ArrayList<>();
        context.generateSchema(new SchemaOutputResolver() {
            @Override
            public Result createOutput(String ns, String file) throws IOException {
                DOMResult result = new DOMResult();
                result.setSystemId(file);
                domResults.add(result);
                return result;
            }
        });
        List<DOMSource> dsList = new ArrayList<>();
        for (DOMResult domresult : domResults) {
            dsList.add(new DOMSource(domresult.getNode()));
        }
        return dsList.toArray(new DOMSource[dsList.size()]);
    }
    
}
