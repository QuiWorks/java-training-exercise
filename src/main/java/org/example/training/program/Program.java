package org.example.training.program;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A simple software program abstraction.
 */
public interface Program {

    /**
     * Runtime memory.
     */
    DataPojo WORKING_DATA = new DataPojo();

    /**
     * Runs input data through a list of procedures.
     * Grouped by {@link Procedure.Type}
     * Ordered by {@link Procedure.Type#getOrder()}
     * @param input Any data input of any type.
     * @param procedures A list of {@link Procedure}.
     */
    default void run(Object input, List<Procedure> procedures)
    {
        WORKING_DATA.setData(input);
        procedures.stream()
                .collect(Collectors.groupingBy(Procedure::getType))
                .entrySet().stream()
                .sorted(Comparator.comparing(entry -> entry.getKey().getOrder()))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .map(Procedure::getFunction)
                .forEach(function -> WORKING_DATA.setData(function.apply(WORKING_DATA.getData())));
    }

    default void run(Object input, Procedure... procedures)
    {
        this.run(input, Arrays.asList(procedures));
    }

    /**
     * Prepend a procedure to the programs list of procedures.
     * @param procedure The {@link Procedure} to prepend.
     * @param procedures The current list of procedures.
     * @return A new array of program procedures.
     */
    static Procedure[] prepend(Procedure procedure, Procedure[] procedures) {
        Procedure[] newProcedures = new Procedure[procedures.length + 1];
        newProcedures[procedures.length] = procedure;
        IntStream.range(0, procedures.length)
                .forEach(index -> newProcedures[index] = procedures[index]);
        return newProcedures;
    }

    /**
     * append a procedure to the programs list of procedures.
     * @param procedure The {@link Procedure} to append.
     * @param procedures The current list of procedures.
     * @return A new array of program procedures.
     */
    static Procedure[] append(Procedure procedure, Procedure[] procedures) {
        int newLength = procedures.length + 1;
        Procedure[] newProcedures = new Procedure[newLength];
        newProcedures[0] = procedure;
        IntStream.range(1, newLength)
                .forEach(index -> newProcedures[index] = procedures[index - 1]);
        return newProcedures;
    }

    /**
     * A Plain Old Java Object.
     */
    class DataPojo {
        private Object data;

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

    }
}
