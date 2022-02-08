package org.example.training.program;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.example.training.program.Procedure.Type.*;

/**
 * The implementations of {@link Procedure}.
 */
@SuppressWarnings("unchecked")
public enum Instruction {
    ERROR( PROC, Void.class, obj -> obj, "error"),
    STREAM_ARRAY_STRING( PREP, String.class, obj -> Arrays.stream( (String[]) obj), "stream array"),
    SORT_STREAM_STRING( PREP, String.class, obj -> ((Stream<String>) obj).sorted( String::compareTo), "sort stream", "sort"),
    CONVERT_STREAM_INTEGER( PREP, Integer.class, obj -> ((Stream<String>) obj).map( Integer::valueOf), "convert type"),
    SORT_STREAM_INT( PREP, Integer.class, obj -> ((Stream<Integer>) obj).sorted( Integer::compareTo), "sort stream"),
    REVERSE_STREAM_STRING( PROC, String.class, obj -> IntStream.range( 0, ((String[]) obj).length)
            .map(i -> (((String[]) obj).length - 1) - i)
            .mapToObj(o -> ((String[]) obj)[o]), "reverse stream"),
    REVERSE_SORT_INT( PROC, Integer.class, obj -> ((Stream<Integer>)obj).sorted( Comparator.reverseOrder()), "reverse sort"),
    PRINT_STREAM_TO_SYS_OUT_STRING(Procedure.Type.PRINT, String.class, obj -> {
        (((Stream<String>) obj)).collect( Collectors.toList()).forEach( System.out::print);
        return obj;
    }, "print to system out"),
    PRINT_STREAM_TO_SYS_OUT_INT(Procedure.Type.PRINT, Integer.class, obj -> {
        (((Stream<Integer>) obj)).collect( Collectors.toList()).forEach( System.out::print);
        return obj;
    }, "print to system out"),
    PRINT_INT_TO_SYS_OUT_INT(Procedure.Type.PRINT, Integer.class, obj -> {
        System.out.print( obj );
        return obj;
    }, "print to system out"),
    ADD_INT(PROC, Integer.class, obj -> ((Stream<Integer>)obj).reduce( 0, Integer::sum ), "add");

    /**
     * Gets a {@link Procedure} by matching text and type.
     * @param text String input used to help identify the procedure to provide.
     * @param type The data type the instruction deals with.
     * @return any matching procedure.
     */
    public static Procedure get(String text, Class<?> type)
    {
        return Arrays.stream(Instruction.values())
                .filter(i -> i.phrases.contains(text))
                .filter(i -> i.dataType.equals(type))
                .findAny()
                .orElse(ERROR)
                .getProcedure();
    }

    private final Procedure.Type procedureType;
    private final Class<?> dataType;
    private final Function<Object, Object> function;
    private final Set<String> phrases;

    /**
     * Each {@link Procedure} implementation needs to know the following:
     * @param procedureType The {@link Procedure.Type}.
     * @param dataType The type of data it returns.
     * @param function A {@link Function} that implementations the procedure.
     * @param phrases An array of phrases that describe the procedure.
     */
    Instruction(Procedure.Type procedureType, Class<?> dataType, Function<Object, Object> function, String... phrases) {
        this.procedureType = procedureType;
        this.dataType = dataType;
        this.function = function;
        this.phrases = Arrays.stream(phrases).collect(Collectors.toSet());
    }

    /**
     * @return the function casted as it's {@link Procedure.Type}
     */
    public Procedure getProcedure()
    {
        if(procedureType.equals( PREP ))
        {
            return (Preparation) () -> function;
        }
        else if (procedureType.equals( PROC ))
        {
            return (Process) () -> function;
        }
        else if (procedureType.equals( PRINT ))
        {
            return (Print) () -> function;
        }else{
            throw new RuntimeException("ERRR");
        }
    }
}
