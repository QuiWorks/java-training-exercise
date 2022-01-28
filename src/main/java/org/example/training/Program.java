package org.example.training;

import org.example.training.mixer.Mixer;
import org.example.training.mixer.Type;

import java.util.Optional;

public class Program
{

    public static void main(String[] args) {
        Optional.ofNullable(System.getProperties().getOrDefault( "mixer-type", Type.BROKEN.name()))
                .map(String::valueOf)
                .map(Mixer::getMixer)
                .ifPresent(mixer -> mixer.mix(args).forEach(System.out::println));
    }

}