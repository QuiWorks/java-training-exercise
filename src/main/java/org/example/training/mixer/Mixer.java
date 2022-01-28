package org.example.training.mixer;

import org.example.training.exception.MixerException;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Interface for mixing input.
 * @param <RETURN_TYPE> the type returned.
 */
public interface Mixer<RETURN_TYPE>
{

    /**
     * Mixes up an array of strings.
     *
     * @param input The original order of the input.
     * @return A list of the input in a different order.
     */
    List<RETURN_TYPE> mix( String[] input ) throws MixerException;

    static Mixer<?> getMixer( String name )
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
