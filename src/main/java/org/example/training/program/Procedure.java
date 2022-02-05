package org.example.training.program;

import java.util.function.Function;

/**
 * A Procedure runs an {@link Instruction} during {@link org.example.training.program.Program} execution.
 */
public interface Procedure {

    /**
     * @return The implementation of the data operation.
     * See {@link Instruction}.
     */
    Function<Object, Object> getFunction();

    /**
     * @return The {@link Type} of the procedure.
     */
    Type getType();

    /**
     * A breakdown of {@link Procedure}.
     * Used to group procedures so the groups can be ordered and run sequentially.
     */
    enum Type {
        PREP(0), PROC(1), PRINT(2);
    
        private final Integer order;
    
        Type(Integer order) {
    
            this.order = order;
        }
    
        public Integer getOrder() {
            return order;
        }
    }
}
