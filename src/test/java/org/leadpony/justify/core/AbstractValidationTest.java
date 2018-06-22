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

package org.leadpony.justify.core;

import static org.assertj.core.api.Assertions.assertThat;

import javax.json.stream.JsonParser;

import org.junit.Test;

/**
 * @author leadpony
 */
public abstract class AbstractValidationTest extends AbstractSpecTest {

    protected AbstractValidationTest(String name, int testIndex, String description, Fixture fixture) {
        super(name, testIndex, description, fixture);
    }
    
    @Test
    public void testValidationResult() {
        JsonParser parser = createValidatingParser();
        while (parser.hasNext()) {
            parser.next();
        }
        parser.close();
        JsonValidator validator = (JsonValidator)parser;
        assertThat(!validator.hasProblem()).isEqualTo(getFixture().result());
        if (validator.hasProblem()) {
            printProblems(validator.getProblems());
        }
    }
}