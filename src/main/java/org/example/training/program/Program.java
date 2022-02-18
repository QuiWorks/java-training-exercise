package org.example.training.program;

import javax.json.JsonObject;
import java.util.Arrays;
import java.util.List;

/**
 * An abstraction of a software program.
 */
public interface Program {

    /**
     * Runs input data through a list of procedures.
     *
     * @param input      {@link JsonObject} input data for the {@link Program}.
     * @param procedures A list of {@link Procedure} applied during {@link Program} execution.
     */
    default void run(JsonObject input, List<Procedure> procedures) {
        Memory memory = new Memory().setData(input);
        procedures.forEach(procedure -> memory.setData(procedure.apply(memory.getData())));
    }

    default void run(JsonObject input, Procedure... procedures) {
        this.run(input, Arrays.asList(procedures));
    }

    /**
     * An object that holds mutable data.
     * Used during {@link Program} execution.
     */
    class Memory {
        private JsonObject data;

        public JsonObject getData() {
            return data;
        }

        public Memory setData(JsonObject data) {
            this.data = data;
            return this;
        }

    }
}
