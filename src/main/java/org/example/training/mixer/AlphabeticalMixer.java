package org.example.training.mixer;

import org.example.training.exception.MixerException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AlphabeticalMixer implements Mixer<String>
{
    @Override
    public List<String> mix( String[] input )
    {
        try
        {
            return Arrays.stream(input)
                .sorted( String::compareTo )
                .collect( Collectors.toList());
        }
        catch( Exception e )
        {
            throw new MixerException(e);
        }
    }
}
