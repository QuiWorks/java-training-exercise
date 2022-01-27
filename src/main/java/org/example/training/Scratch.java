package org.example.training;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Scratch {

    public static void main(String[] args) {
        Optional.ofNullable(System.getProperties().getOrDefault("mixer-type", Mixer.Type.BROKEN.name()))
                .map(String::valueOf)
                .map(Mixer::getMixer)
                .ifPresent(mixer -> mixer.mix(args).forEach(System.out::println));
    }


    interface Mixer {

        /**
         * Mixes up an array of strings.
         *
         * @param input The original order of the input.
         * @return A list of the input in a different order.
         */
        List<String> mix(String[] input);

        static Mixer getMixer(String name) {
            try {
                return Type.getByName(name)
                        .getMixerClass()
                        .getConstructor()
                        .newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                return new BrokenMixer();
            }
        }

        enum Type{
            BACKWARDS(BackwardsMixer.class),
            BROKEN(BackwardsMixer.class);

            private final Class<? extends Mixer> mixerClass;

            Type(Class<? extends Mixer> mixerClass) {
                this.mixerClass = mixerClass;
            }

            public Class<? extends Mixer> getMixerClass() {
                return mixerClass;
            }

            public static Type getByName(String name)
            {
                return Optional.ofNullable(name)
                        .map(String::toUpperCase)
                        .map(n -> Arrays.stream(values())
                                .filter(v -> n.equals(v.name()))
                                .findAny())
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .orElse(BROKEN);
            }
        }

    }


    static class BackwardsMixer implements Mixer {

        @Override
        public List<String> mix(String[] input) {
            return IntStream.range(0, input.length)
                    .map(i -> (input.length - 1) - i)
                    .mapToObj(o -> input[o])
                    .collect(Collectors.toList());
        }

    }


    static class BrokenMixer implements Mixer {

        @Override
        public List<String> mix(String[] input) {
            return Arrays.stream(input)
                    .collect(Collectors.toList());
        }

    }

}