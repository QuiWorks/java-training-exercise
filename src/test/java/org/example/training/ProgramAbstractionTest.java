package org.example.training;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    interface Program {
        default void run(Map<String, Object> input, List<Procedure> procedures) {
            DataPojo workingData = new DataPojo().setData(input);
            procedures.forEach(function -> workingData.setData(function.apply(workingData.getData())));
        }

        default void run(Map<String, Object> input, Procedure... procedures) {
            this.run(input, Arrays.asList(procedures));
        }

        class DataPojo {
            private Map<String, Object> data;

            public Map<String, Object> getData() {
                return data;
            }

            public DataPojo setData(Map<String, Object> data) {
                this.data = data;
                return this;
            }

        }
    }

    interface Procedure extends Function<Map<String, Object>, Map<String, Object>> {

        enum Impl implements Procedure {

            NOTHING((data, parameters) -> data, "nothing", "do nothing", "do nothing to"),
            PRINT((data, parameters) -> {
                System.out.print(data);
                return data;
            }, "print");


            /**
             * Gets a {@link Procedure} by matching text.
             *
             * @param text String input used to help identify the procedure to provide.
             * @return any matching procedure.
             */
            public static Procedure get(String text, Map<String, Object> parameters) {
                return Arrays.stream(Impl.values())
                        .filter(i -> i.getKeywords().contains(text))
                        .findAny()
                        .orElse(NOTHING)
                        .setParameters(parameters);
            }

            private final BiFunction<Map<String, Object>, Map<String, Object>, Map<String, Object>> biFunction;
            private final Set<String> keywords;
            private Map<String, Object> parameters;

            Impl(BiFunction<Map<String, Object>, Map<String, Object>, Map<String, Object>> biFunction, String... keywords) {
                this.biFunction = biFunction;
                this.keywords = Arrays.stream(keywords)
                        .map(String::toLowerCase)
                        .map(String::trim)
                        .collect(Collectors.toSet());
                this.keywords.add(this.name());
            }

            @Override
            public Map<String, Object> apply(Map<String, Object> data) {
                // MECHANISM FOR HOW TO APPLY the correct PARAMETERs for the correct procedure goes here.
                return biFunction.apply(data, parameters);
            }

            public Set<String> getKeywords() {
                return keywords;
            }

            public Procedure setParameters(Map<String, Object> parameters) {
                this.parameters = parameters;
                return this;
            }
        }

    }

    @Test
    void openApiTest() throws IOException {
        TypeReference<Map<String, Object>> valueTypeRef = new TypeReference<>() {};
        final Map<String, Object> data = new ObjectMapper().readValue(new File("/home/dev/IdeaProjects/java-dev-training/src/test/resources/data.json"), valueTypeRef);

        new Program() {
        }.run(
                data,
                Procedure.Impl.get("do nothing to", new HashMap<>()),
                Procedure.Impl.get("print", new HashMap<>())
        );

//        TODO use JSON standard
        assertEquals("{players=[{id=1, name=Eric}, {id=2, name=Jason}], points=[{id=1, value=1}, {id=2, value=2}]}", outContent.toString());
    }

    @Test
    void alphabeticalSortingWorks() {
        final String[] args = new String[]{"c", "b", "a"};
        new CommandLineProgram<>(String.class) {
        }
                .run(args, SORT_STREAM_STRING.getProcedure());
        assertEquals("abc", outContent.toString());
    }

    @Test
    void ascendingSortingWorks() {
        final String[] args = new String[]{"3", "2", "1"};
        new CommandLineProgram<>(Integer.class) {
        }.run(args,
                CONVERT_STREAM_INTEGER.getProcedure(),
                SORT_STREAM_INT.getProcedure()
        );
        assertEquals("123", outContent.toString());
    }

//    @Test
//    void addIntWorks() {
//        final String[] args = new String[]{"3", "2", "1"};
//        new Program() {
//        }.run(args,
//                STREAM_ARRAY_STRING.getProcedure(),
//                CONVERT_STREAM_INTEGER.getProcedure(),
//                ADD_INT.getProcedure(),
//                PRINT_INT_TO_SYS_OUT_INT.getProcedure()
//        );
//        assertEquals("6", outContent.toString());
//    }
//
//    @Test
//    void backwardsWorks() {
//        final String[] args = new String[]{"c", "b", "a"};
//        new Program() {
//        }.run(args,
//                REVERSE_STREAM_STRING.getProcedure(),
//                PRINT_STREAM_TO_SYS_OUT_STRING.getProcedure()
//        );
//        assertEquals("abc", outContent.toString());
//    }

    @Test
    void brokenWorks() {
        final String[] args = new String[]{"c", "b", "a"};
        new CommandLineProgram<>(String.class) {
        }.run(args);
        assertEquals("cba", outContent.toString());
    }

    @Test
    void descendingWorks() {
        final String[] args = new String[]{"1", "2", "3"};
        new CommandLineProgram<>(Integer.class) {
        }.run(args,
                CONVERT_STREAM_INTEGER.getProcedure(),
                SORT_STREAM_INT.getProcedure(),
                REVERSE_SORT_INT.getProcedure()
        );
        assertEquals("321", outContent.toString());
    }

}