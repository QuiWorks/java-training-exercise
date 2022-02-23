package org.example.training.program;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Automated testing for {@link Program} abstraction.
 */
class ProgramTest {

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
    public void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }



    @Test
    void maxTest() throws FileNotFoundException {

        JsonReader expectReader = Json.createReader(new StringReader("{\"stat\":2}"));
        JsonReader dataReader = Json.createReader(new FileReader("src/test/resources/data.json"));
        JsonObject data = dataReader.readObject();

        JsonReader parameterReader = Json.createReader(new FileReader("src/test/resources/parameters_max.json"));
        JsonObject parameters = parameterReader.readObject();

        new Program() {
        }.run(
                data,
                Procedure.get("min/max", parameters),
                Procedure.get("print", parameters)
        );

        String string = outContent.toString();
        JsonReader outputReader = Json.createReader(new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8)));
        assertEquals(expectReader.readObject(), outputReader.readObject());

    }


    @Test
    void countTest() throws FileNotFoundException {

        JsonReader expectReader = Json.createReader(new StringReader("{\"count\":2}"));
        JsonReader dataReader = Json.createReader(new FileReader("src/test/resources/data.json"));
        JsonObject data = dataReader.readObject();

        JsonReader parameterReader = Json.createReader(new FileReader("src/test/resources/parameters.json"));
        JsonObject parameters = parameterReader.readObject();

        new Program() {
        }.run(
                data,
                Procedure.get("count", parameters),
                Procedure.get("print", parameters)
        );

        String string = outContent.toString();
        JsonReader outputReader = Json.createReader(new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8)));
        assertEquals(expectReader.readObject(), outputReader.readObject());

    }

    @Test
    void totalTest() throws FileNotFoundException {

        JsonReader expectReader = Json.createReader(new StringReader("{\"total\":3}"));
        JsonReader dataReader = Json.createReader(new FileReader("src/test/resources/data.json"));
        JsonObject data = dataReader.readObject();

        JsonReader parameterReader = Json.createReader(new FileReader("src/test/resources/parameters.json"));
        JsonObject parameters = parameterReader.readObject();

        new Program() {
        }.run(
                data,
                Procedure.get("total", parameters),
                Procedure.get("print", parameters)
        );

        String string = outContent.toString();
        JsonReader outputReader = Json.createReader(new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8)));
        assertEquals(expectReader.readObject(), outputReader.readObject());

    }

    @Test
    void jsonDataTest() throws IOException {

        JsonReader reader = Json.createReader(new FileReader("src/test/resources/data.json"));
        JsonObject data = reader.readObject();

        new Program() {
        }.run(
                data,
                Procedure.get("do nothing to", data),
                Procedure.get("print", data)
        );

        String string = outContent.toString();
        JsonReader outputReader = Json.createReader(new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8)));
        assertEquals(data, outputReader.readObject());

    }
}