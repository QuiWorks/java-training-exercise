package org.example.training.mixer;

import java.util.Arrays;
import java.util.Optional;

public enum Type
{
    BACKWARDS( BackwardsMixer.class ),
    BROKEN( BackwardsMixer.class );

    private final Class<? extends Mixer> mixerClass;

    Type( Class<? extends Mixer> mixerClass )
    {
        this.mixerClass = mixerClass;
    }

    public Class<? extends Mixer> getMixerClass()
    {
        return mixerClass;
    }

    public static Type getByName( String name )
    {
        return Optional.ofNullable( name )
            .map( String::toUpperCase )
            .map( n -> Arrays.stream( values() )
                .filter( v -> n.equals( v.name() ) )
                .findAny())
            .filter( Optional::isPresent )
            .map( Optional::get )
            .orElse( BROKEN );
    }
}
