package org.example.training.mixer;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface Mixer
{

    /**
     * Mixes up an array of strings.
     *
     * @param input The original order of the input.
     * @return A list of the input in a different order.
     */
    List<String> mix( String[] input );

    static Mixer getMixer( String name )
    {
        try
        {
            return Type.getByName( name )
                .getMixerClass()
                .getConstructor()
                .newInstance();
        }
        catch( InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e )
        {
            return new BrokenMixer();
        }
    }

}
