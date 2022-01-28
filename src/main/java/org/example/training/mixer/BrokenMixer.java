package org.example.training.mixer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BrokenMixer implements Mixer<String>
{

    @Override
    public List<String> mix( String[] input )
    {
        return Arrays.stream( input )
            .collect( Collectors.toList() );
    }

}
