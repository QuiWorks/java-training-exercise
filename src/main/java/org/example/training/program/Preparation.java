package org.example.training.program;

/**
 * A {@link Procedure} that prepares data for a chain of {@link Process}.
 * This can be any configuration that the process chain needs in order to handle the input data.
 * Ex: Setting variables, input validation, type conversion, etc...
 */
interface Preparation extends Procedure {
    @Override
    default Type getType()
    {
        return Type.PREP;
    }
}
