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
package org.leadpony.justify.cli;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * The entry class of this program.
 *
 * @author leadpony
 */
public class Launcher {

    private final Console console = new Console(System.out, System.err);

    public Status launch(String[] args) {
        return launch(new LinkedList<>(Arrays.asList(args)));
    }

    private Status launch(List<String> args) {
        try {
            Command command = createCommand(args);
            return command.execute(args);
        } catch (CommandException e) {
            console.error(e);
            return Status.FAILED;
        } catch (Exception e) {
            console.error(e);
            return Status.FAILED;
        }
    }

    private Command createCommand(List<String> args) {
        if (args.isEmpty() || args.contains("-h")) {
            return new Help(console);
        }
        return new Validate(console);
    }

    /**
     * The entry point of this program.
     *
     * @param args the arguments given to this program.
     */
    public static void main(String[] args) {
        System.exit(new Launcher().launch(args).code());
    }
}
