package org.example.training.mixer;

import org.example.training.exception.MixerException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DescendingMixer implements Mixer<Integer>
{
    @Override
    public List<Integer> mix( String[] input )
    {
        try
        {
            return Arrays.stream(input)
                .map( Integer::valueOf )
                .sorted( Integer::compareTo )
                .sorted( Collections.reverseOrder() )
                .collect( Collectors.toList());
        }
        catch( Exception e )
        {
            throw new MixerException(e);
        }
    }
}
