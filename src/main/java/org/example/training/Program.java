package org.example.training;

import org.example.training.mixer.Mixer;
import org.example.training.mixer.Type;

import java.util.Optional;

/**
 * Mixes command line input.
 * Expects the class name of the {@link Mixer} implementation as a system property with a key of "mixer-type".
 * Default implementation is the {@link org.example.training.mixer.BrokenMixer}.
 */
public class Program
{

    public static final String SYSTEM_PROP = "mixer-type";

    public static void main( String[] args) {
        Optional.ofNullable(System.getProperties().getOrDefault( SYSTEM_PROP, Type.BROKEN.name()))
                .map(String::valueOf)
                .map(Mixer::getMixer)
                .ifPresent(mixer -> mixer.mix(args).forEach(System.out::println));
    }

}