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
     * @param input      {@link JsonObject} input data for the {@link Program}.
     * @param output {@link OutputStream} to which the data gets written.
     * @param procedures A list of {@link Procedure} applied during {@link Program} execution.
     */
    default void run( InputStream input, OutputStream output, List<Procedure> procedures )
    {
        try (JsonWriter jsonWriter = Json.createWriter( output );
            JsonGenerator jsonGenerator = Json.createGenerator( output ).writeStartObject();
            JsonParser jsonParser = Json.createParser( input )
        )
        {
            //TODO create final transformation event that tells the generator to write a copy of the event
            Function<Transformation, Transformation> procedureChain = IntStream.range( 0, procedures.size() - 1 )
                .mapToObj( i -> procedures.get( i )
                    .andThen( procedures.get( i + 1 ) ) )
                .findFirst()
                .orElse( Procedure.NOTHING );
            while ( jsonParser.hasNext() )
            {
                procedureChain.apply( new Transformation( jsonGenerator, jsonParser, jsonParser.next() ) );
            }
//            jsonWriter.writeObject( IntStream.range( 0, procedures.size() - 1 )
//                .mapToObj( i -> procedures.get( i ).andThen( procedures.get( i + 1 ) ) )
//                .findFirst()
//                .orElse( Procedure.NOTHING )
//                .apply( input ) );
        }
    }

    default void run( InputStream input, OutputStream output, Procedure... procedures )
    {
        this.run( input, output, Arrays.asList( procedures ) );
    }
}
