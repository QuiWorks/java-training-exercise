package org.example.training.program;

/**
 * A {@link Procedure} that transforms data.
 * This can be anything that changes the data in some way.
 * Ex: like sorting, math, etc...
 */
interface Process extends Procedure {
    @Override
    default Type getType()
    {
        return Type.PROC;
    }
}
