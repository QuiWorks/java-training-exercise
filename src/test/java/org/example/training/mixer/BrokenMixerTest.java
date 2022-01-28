package org.example.training.mixer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BrokenMixerTest
{

    private Mixer<String> mixer;

    @BeforeEach
    void setUp()
    {
        mixer = new BrokenMixer();
    }

    @Test
    void mix()
    {
        String[] testInput = new String[]{"one","two","three"};
        List<String> mixture = mixer.mix( testInput );
        assertEquals( Arrays.asList(testInput), mixture, "Broken Mixer should not change the input." );
    }
}