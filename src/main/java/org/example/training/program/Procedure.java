package org.example.training.program;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.stream.JsonParser;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Steps in a {@link Program}
 */
public enum Procedure implements Function<Transformation, Transformation> {

    /**
     * Does nothing.
     */
    NOTHING((transformation, parameters) -> transformation, "nothing", "do nothing", "do nothing to"),

    /**
     * Totals an integer field of an object.
     */
    TOTAL((transformation, parameters) -> Json.createReader(new StringReader(
            Optional.of(transformation.getJsonArray(parameters.getString("name")).stream()
                            .map(JsonValue::asJsonObject)
                            .map(obj -> obj.getInt(parameters.getString("key")))
                            .reduce(0, Integer::sum))
                    .map(total -> "{\"total\":" + total + "}")
                    .orElse("{\"total\":0}"))).readObject()),


    RENAME_KEY((transformation, parameters) -> {
        if( JsonParser.Event.KEY_NAME.equals( transformation.getEvent() )
            && transformation.getJsonParser().getString().equals( parameters.getString( "key" ) )
        )
        {
            transformation.getJsonGenerator().write( )
        }
    });


    /**
     * Gets a {@link Procedure} instance by matching text.
     *
     * @param text       String input used to help identify the procedure to provide.
     * @param parameters The parameters used by the procedure.
     * @return any matching procedure.
     */
    public static Procedure get(String text, JsonObject parameters) {
        return Arrays.stream(Procedure.values())
                .filter(i -> i.getKeywords().contains(text))
                .findAny()
                .orElse(NOTHING)
                .setParameters(parameters);
    }

    public static Procedure get(String text) {
        return Arrays.stream(Procedure.values())
                .filter(i -> i.getKeywords().contains(text))
                .findAny()
                .orElse(NOTHING);
    }

    private final BiFunction<Transformation, JsonObject, Transformation> biFunction;
    private final Set<String> keywords;
    private JsonObject parameters;

    Procedure(BiFunction<Transformation, JsonObject, Transformation> biFunction, String... keywords) {
        this.biFunction = biFunction;
        this.keywords = Arrays.stream(keywords)
                .map(String::toLowerCase)
                .map(String::trim)
                .collect(Collectors.toSet());
        this.keywords.add(this.name().toLowerCase().trim());
        this.parameters = JsonValue.EMPTY_JSON_OBJECT;
    }

    @Override
    public Transformation apply(Transformation transformation) {
        return biFunction.apply(transformation, parameters);
    }

    public Set<String> getKeywords() {
        return keywords;
    }

    public Procedure setParameters(JsonObject parameters) {
        this.parameters = parameters;
        return this;
    }
}
