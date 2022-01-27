package org.example.training;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Scratch {

    public static boolean IS_AWESOME_PROGRAMMER = true;

    public static void main(String[] args) {

        Mixer inputMixer = IS_AWESOME_PROGRAMMER ? new BackwardsMixer() : new BrokenMixer();
        inputMixer.mix(args)
                .forEach(System.out::println);
    }


    interface Mixer {

        /**
         * Mixes up an array of strings.
         *
         * @param input The original order of the input.
         * @return A list of the input in a different order.
         */
        List<String> mix(String[] input);

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