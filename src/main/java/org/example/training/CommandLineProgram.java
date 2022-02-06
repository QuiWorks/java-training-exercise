package org.example.training;

import org.example.training.program.Instruction;
import org.example.training.program.Procedure;
import org.example.training.program.Program;

import java.util.Arrays;
import java.util.List;

import static org.example.training.program.Instruction.STREAM_ARRAY_STRING;

/**
 * An abstraction implementation of a command line {@link Program}.
 * @param <TYPE> The single type this command line deals with.
 */
public abstract class CommandLineProgram<TYPE> implements Program {

    private final Class<TYPE> type;

    public CommandLineProgram(Class<TYPE> type) {
        this.type = type;
    }

    /**
     * Overrides {@link Program#run(Object, List)}
     * inorder to add common {@link Procedure}s that all command line programs need.d
     * @param input Any data input of any type.
     * @param procedures A list of {@link Procedure}.
     */
    @Override
    public void run(Object input, Procedure... procedures) {
        Procedure[] appended = Program.append(STREAM_ARRAY_STRING.getProcedure(), procedures);
        Procedure[] prepended = Program.prepend(Instruction.get("print to system out", type), appended);
        Program.super.run(input, Arrays.stream(prepended).toArray(Procedure[]::new));
    }

}
