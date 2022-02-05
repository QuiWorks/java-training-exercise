package org.example.training;

import org.example.training.program.Instruction;
import org.example.training.program.Program;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.example.training.program.Instruction.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProgramAbstractionTest {

    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void alphabeticalSortingWorks() {
        final String[] args = new String[]{"c","b","a"};
        new CommandLineProgram<>(String.class){}
                .run(args, SORT_STREAM_STRING.getProcedure());
        assertEquals("abc", outContent.toString());
    }

    @Test
    void ascendingSortingWorks() {
        final String[] args = new String[]{"3","2","1"};
        new CommandLineProgram<>(Integer.class) {
        }.run(args,
                CONVERT_STREAM_INTEGER.getProcedure(),
                SORT_STREAM_INT.getProcedure()
        );
        assertEquals("123", outContent.toString());
    }

    @Test
    void backwardsWorks() {
        final String[] args = new String[]{"c","b","a"};
        new Program() {
        }.run(args,
                REVERSE_STREAM_STRING.getProcedure(),
                PRINT_STREAM_TO_SYS_OUT_STRING.getProcedure()
        );
        assertEquals("abc", outContent.toString());
    }

    @Test
    void brokenWorks() {
        final String[] args = new String[]{"c","b","a"};
        new CommandLineProgram<>(String.class){}.run(args);
        assertEquals("cba", outContent.toString());
    }

    @Test
    void descendingWorks() {
        final String[] args = new String[]{"1","2","3"};
        new CommandLineProgram<>(Integer.class){}.run(args,
                CONVERT_STREAM_INTEGER.getProcedure(),
                SORT_STREAM_INT.getProcedure(),
                REVERSE_SORT_INT.getProcedure()
        );
        assertEquals("321", outContent.toString());
    }

}