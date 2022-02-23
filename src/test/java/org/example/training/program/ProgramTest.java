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
    void totalTest() throws FileNotFoundException {

        JsonReader expectReader = Json.createReader(new StringReader("{\"total\":3}"));

        FileInputStream input = new FileInputStream( "src/test/resources/data.json" );

        JsonReader parameterReader = Json.createReader(new FileReader("src/test/resources/parameters.json"));
        JsonObject parameters = parameterReader.readObject();

        new Program() {
        }.run( input, outContent, Procedure.get( "total", parameters),
               Procedure.get("print", parameters)
        );

        String string = outContent.toString();
        JsonReader outputReader = Json.createReader(new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8)));
        assertEquals(expectReader.readObject(), outputReader.readObject());

    }

    @Test
    void jsonDataTest() throws IOException {

        JsonReader expectedReader = Json.createReader(new FileReader("src/test/resources/data.json"));
        JsonObject expected = expectedReader.readObject();

        FileInputStream input = new FileInputStream( "src/test/resources/data.json" );

        new Program() {
        }.run(
            input, outContent, Procedure.get( "do nothing to"),
            Procedure.get("print")
        );

        String string = outContent.toString();
        JsonReader outputReader = Json.createReader(new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8)));
        assertEquals(expected, outputReader.readObject());

    }
}