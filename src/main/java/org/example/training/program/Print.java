package org.example.training.program;

/**
 * A {@link Procedure} that prints to an output stream.
 * This can be any form of output to which the process chain needs to write.
 * Ex: System.out, a file, etc...
 */
interface Print extends Procedure {
    @Override
    default Type getType()
    {
        return Type.PRINT;
    }
}
