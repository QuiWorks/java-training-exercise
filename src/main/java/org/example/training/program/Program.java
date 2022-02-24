package org.example.training.program;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * An abstraction of a software program.
 */
public interface Program
{

    /**
     * Runs input data through a list of procedures.
     *
     * @param input      {@link InputStream} input data for the {@link Program}.
     * @param output     {@link OutputStream} to which the data gets written.
     * @param procedures A list of {@link Procedure} applied during {@link Program} execution.
     */
    default void run( InputStream input, OutputStream output, List<Procedure> procedures )
    {
        //TODO need to group, sort by parse location, then sort by enum.compareTo.
        // THEN chain the procedures.
        // Top-down, Left-right.
        IntStream.range( 0, procedures.size() - 1 )
            .mapToObj( i -> procedures.get( i )
                .andThen( procedures.get( i + 1 ) ) )
            .sorted()
            .findFirst()
            .ifPresent( procedureChain -> {
                try (JsonGenerator jsonGenerator = Json.createGenerator( output ).writeStartObject(); JsonParser jsonParser = Json.createParser( input ))
                {
                    while ( jsonParser.hasNext() )
                    {
                        // TODO apply the last event that compares the input with the output and fill in the gaps.
                        Transformer finalForm = getFinalTransformation()
                            .compose( procedureChain )
                            .apply( new Transformer( jsonGenerator, jsonParser, jsonParser.next() ) );
                    }

                }
            } );
    }


    default Function<Transformer, Transformer> getFinalTransformation()
    {
        //TODO create final transformation event that tells the generator to write a copy of the event
        return t -> t;
    }

    default void run( InputStream input, OutputStream output, Procedure... procedures )
    {
        this.run( input, output, Arrays.asList( procedures ) );
    }
}
