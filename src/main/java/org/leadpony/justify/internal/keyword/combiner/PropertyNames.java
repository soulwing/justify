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

package org.leadpony.justify.internal.keyword.combiner;

import javax.json.JsonBuilderFactory;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

import org.leadpony.justify.core.Evaluator;
import org.leadpony.justify.core.InstanceType;
import org.leadpony.justify.core.JsonSchema;
import org.leadpony.justify.internal.evaluator.EvaluatorAppender;

/**
 * @author leadpony
 */
class PropertyNames extends UnaryCombiner implements Evaluator {

    PropertyNames(JsonSchema subschema) {
        super(subschema);
    }

    @Override
    public String name() {
        return "propertyNames";
    }

    @Override
    public void createEvaluator(InstanceType type, EvaluatorAppender appender, JsonBuilderFactory builderFactory) {
        if (type == InstanceType.OBJECT) {
            appender.append(this);
        }
    }

    @Override
    public Result evaluate(Event event, JsonParser parser, int depth, Reporter reporter) {
        if (depth == 0 && event == Event.END_OBJECT) {
            return Result.TRUE;
        } else if (depth == 1 && event == Event.KEY_NAME) {
            evaluateName(event, parser, depth, reporter);
        }
        return Result.PENDING;
    }
    
    private void evaluateName(Event event, JsonParser parser, int depth, Reporter reporter) {
        Evaluator evaluator = getSubschema().createEvaluator(InstanceType.STRING);
        if (evaluator != null) {
            evaluator.evaluate(event, parser, depth, reporter);
        }
    }
}