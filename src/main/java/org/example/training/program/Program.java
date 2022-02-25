package org.example.training.program;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;
import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.function.Function;

/**
 * An abstraction of a software program.
 */
public interface Program extends AutoCloseable
{

    /**
     * Runs input data through a list of procedures.
     *
     * @param procedures A list of {@link Implementation} applied during {@link Program} execution.
     */
    default void run( List<Procedure> procedures )
    {
        //TODO need to group, sort by parse location, then sort by enum.compareTo.
        // THEN chain the procedures.
        // Top-down, Left-right.
//        IntStream.range( 0, procedures.size() - 1 )
//            .mapToObj( i -> procedures.get( i )
//                .andThen( procedures.get( i + 1 ) ) )

        OutputStream output = getOutputStream();
        InputStream input = getInputStream();

        procedures.stream()
            .sorted()
            .findFirst()
            .ifPresent( procedureChain -> {
                try (JsonGenerator jsonGenerator = Json.createGenerator( output ).writeStartObject(); JsonParser jsonParser = Json.createParser( input ))
                {
                    while ( jsonParser.hasNext() )
                    {
                        // TODO apply the last event that compares the input with the output and fill in the gaps.
                        Transformation finalForm = getFinalTransformation()
                            .compose( procedureChain )
                            .apply( new Transformation( jsonGenerator, jsonParser, jsonParser.next() ) );
                    }

                }
            } );
    }

    @Override
    default void close() throws Exception
    {
        for( Closeable closeable : getStreams().values() )
        {
            closeable.close();
        }
    }

    Map<String, Closeable> getStreams();

    default InputStream getInputStream()
    {
        return Optional.ofNullable( getStreams().get( "INPUT" ) )
            .filter( closeable -> closeable instanceof InputStream )
            .map( inputStream -> (InputStream) inputStream )
            .orElseThrow(() -> new RuntimeException("Valid input stream not found."));
    }

    default OutputStream getOutputStream()
    {
        return Optional.ofNullable( getStreams().get( "OUTPUT" ) )
            .filter( closeable -> closeable instanceof OutputStream )
            .map( outputStream -> (OutputStream) outputStream )
            .orElseThrow(() -> new RuntimeException("Valid output stream not found."));
    }

    default Function<Transformation, Transformation> getFinalTransformation()
    {
        //TODO create final transformation event that tells the generator to write a copy of the event
        return t -> t;
    }

    default void run( Procedure procedures )
    {
        this.run( List.of( procedures ) );
    }
    default void run( Procedure... procedures )
    {
        this.run( getInputStream(), getOutputStream(), List.of(procedures) );
    }
}
