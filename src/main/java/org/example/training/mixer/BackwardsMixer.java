package org.example.training.mixer;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BackwardsMixer implements Mixer<String>
{

    @Override
    public List<String> mix( String[] input )
    {
        return IntStream.range( 0, input.length )
            .map( i -> ( input.length - 1 ) - i )
            .mapToObj( o -> input[o] )
            .collect( Collectors.toList() );
    }

}
