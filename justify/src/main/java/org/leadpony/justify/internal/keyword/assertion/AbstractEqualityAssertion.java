/*
 * Copyright 2018-2019 the Justify authors.
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

package org.leadpony.justify.internal.keyword.assertion;

import javax.json.JsonBuilderFactory;
import javax.json.JsonValue;
import javax.json.stream.JsonParser;

import org.leadpony.justify.api.Evaluator;
import org.leadpony.justify.api.InstanceType;
import org.leadpony.justify.api.ProblemDispatcher;
import org.leadpony.justify.api.Evaluator.Result;
import org.leadpony.justify.internal.base.JsonInstanceBuilder;

/**
 * @author leadpony
 */
abstract class AbstractEqualityAssertion extends AbstractAssertion {
    
    @Override
    protected Evaluator doCreateEvaluator(InstanceType type, JsonBuilderFactory builderFactory) {
        final JsonInstanceBuilder builder = new JsonInstanceBuilder(builderFactory);
        return (event, parser, depth, dispatcher)->{
                if (builder.append(event, parser)) {
                    return Result.PENDING;
                }
                return assertEquals(builder.build(), parser, dispatcher);
            };
    }
    
    @Override
    protected Evaluator doCreateNegatedEvaluator(InstanceType type, JsonBuilderFactory builderFactory) {
        final JsonInstanceBuilder builder = new JsonInstanceBuilder(builderFactory);
        return (event, parser, depth, dispatcher)->{
                if (builder.append(event, parser)) {
                    return Result.PENDING;
                }
                return assertNotEquals(builder.build(), parser, dispatcher);
            };
    }

    protected abstract Result assertEquals(JsonValue actual, JsonParser parser, ProblemDispatcher dispatcher);

    protected abstract Result assertNotEquals(JsonValue actual, JsonParser parser, ProblemDispatcher dispatcher);
}