package org.example.training;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        default void run(JsonObject input, List<Procedure> procedures) {
            DataPojo workingData = new DataPojo().setData(input);
            procedures.forEach(function -> workingData.setData(function.apply(workingData.getData())));
        }

        default void run(JsonObject input, Procedure... procedures) {
            this.run(input, Arrays.asList(procedures));
        }

        class DataPojo {
            private JsonObject data;

            public JsonObject getData() {
                return data;
            }

            public DataPojo setData(JsonObject data) {
                this.data = data;
                return this;
            }

        }
    }

    interface Procedure extends Function<JsonObject, JsonObject> {

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
            public static Procedure get(String text, JsonObject parameters) {
                return Arrays.stream(Impl.values())
                        .filter(i -> i.getKeywords().contains(text))
                        .findAny()
                        .orElse(NOTHING)
                        .setParameters(parameters);
            }

            private final BiFunction<JsonObject, JsonObject, JsonObject> biFunction;
            private final Set<String> keywords;
            private JsonObject parameters;

            Impl(BiFunction<JsonObject, JsonObject, JsonObject> biFunction, String... keywords) {
                this.biFunction = biFunction;
                this.keywords = Arrays.stream(keywords)
                        .map(String::toLowerCase)
                        .map(String::trim)
                        .collect(Collectors.toSet());
                this.keywords.add(this.name());
            }

            @Override
            public JsonObject apply(JsonObject data) {
                // MECHANISM FOR HOW TO APPLY the correct PARAMETERs for the correct procedure goes here.
                return biFunction.apply(data, parameters);
            }

            public Set<String> getKeywords() {
                return keywords;
            }

            public Procedure setParameters(JsonObject parameters) {
                this.parameters = parameters;
                return this;
            }
        }

    }

    @Test
    void openApiTest() throws IOException {

        JsonReader reader = Json.createReader(new FileReader("/home/dev/IdeaProjects/java-dev-training/src/test/resources/data.json"));
        JsonObject data = reader.readObject();

        new Program() {
        }.run(
                data,
                Procedure.Impl.get("do nothing to",data ),
                Procedure.Impl.get("print", data)
        );

        assertEquals("{\"players\":[{\"id\":1,\"name\":\"Eric\"},{\"id\":2,\"name\":\"Jason\"}],\"points\":[{\"id\":1,\"value\":1},{\"id\":2,\"value\":2}]}", outContent.toString());
    }

}