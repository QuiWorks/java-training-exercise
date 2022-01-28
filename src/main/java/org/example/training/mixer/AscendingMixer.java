package org.example.training.mixer;

import org.example.training.exception.MixerException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AscendingMixer implements Mixer<Integer>
{
    @Override
    public List<Integer> mix( String[] input )
    {
        try
        {
            return Arrays.stream(input)
                .map( Integer::valueOf )
                .sorted( Integer::compareTo )
                .collect( Collectors.toList());
        }
        catch( Exception e )
        {
            throw new MixerException(e);
        }
    }
}
