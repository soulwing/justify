/*
 * Copyright 2018 the Justify authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.leadpony.justify.internal.provider;

import java.io.InputStream;
import java.io.Reader;
import java.util.Objects;

import javax.json.spi.JsonProvider;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;

import org.leadpony.justify.internal.validator.DefaultJsonValidatorFactory;
import org.leadpony.justify.core.JsonSchema;
import org.leadpony.justify.core.JsonSchemaReader;
import org.leadpony.justify.core.JsonValidatorFactory;
import org.leadpony.justify.core.spi.JsonValidationServiceProvider;
import org.leadpony.justify.internal.schema.DefaultSchemaBuilderFactory;
import org.leadpony.justify.internal.schema.io.DefaultSchemaReader;

/**
 * Default implementation of {@link JsonValidationServiceProvider}.
 * 
 * @author leadpony
 */
public class DefaultJsonValidationServiceProvider extends JsonValidationServiceProvider {
    
    private JsonProvider jsonProvider;
    private JsonParserFactory parserFactory;
    
    private JsonSchema metaschema;
    
    private static final String METASCHEMA_NAME = "metaschema-draft-07.json";
    
    public DefaultJsonValidationServiceProvider() {
    }

    @Override
    public JsonSchemaReader createReader(InputStream in) {
        return createSimpleReader(in).withExternalSchema(metaschema);
    }
  
    @Override
    public JsonSchemaReader createReader(Reader reader) {
        return createSimpleReader(reader).withExternalSchema(metaschema);
    }
    
    @Override
    public DefaultSchemaBuilderFactory createSchemaBuilderFactory() {
        return new DefaultSchemaBuilderFactory(this.jsonProvider);
    }

    @Override
    public JsonValidatorFactory createValidatorFactory(JsonSchema schema) {
        return new DefaultJsonValidatorFactory(schema, this.jsonProvider);
    }
    
    @Override
    protected void initialize(JsonProvider jsonProvider) {
        this.jsonProvider = jsonProvider;
        this.parserFactory = jsonProvider.createParserFactory(null);
        this.metaschema = loadMetaschema(METASCHEMA_NAME);
    }
    
    private JsonSchema loadMetaschema(String name) {
        InputStream in = getClass().getResourceAsStream(name);
        try (JsonSchemaReader reader = createSimpleReader(in)) {
            return reader.read();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private JsonSchemaReader createSimpleReader(InputStream in) {
        Objects.requireNonNull(in, "in must not be null.");
        JsonParser parser = this.parserFactory.createParser(in);
        return new DefaultSchemaReader(parser, this.createSchemaBuilderFactory());
    }

    private JsonSchemaReader createSimpleReader(Reader reader) {
        Objects.requireNonNull(reader, "reader must not be null.");
        JsonParser parser = this.parserFactory.createParser(reader);
        return new DefaultSchemaReader(parser, this.createSchemaBuilderFactory());
    }
}
