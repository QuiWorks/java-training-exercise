package org.example.training.program;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;
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
public enum Procedure implements Function<JsonObject, JsonObject> {

    /**
     * Does nothing.
     */
    NOTHING((data, parameters) -> data, "nothing", "do nothing", "do nothing to"),

    /**
     * Totals an integer field of an object.
     */
    TOTAL((data, parameters) -> Json.createReader(new StringReader(
            Optional.of(data.getJsonArray(parameters.getString("name")).stream()
                            .map(JsonValue::asJsonObject)
                            .map(obj -> obj.getInt(parameters.getString("key")))
                            .reduce(0, Integer::sum))
                    .map(total -> "{\"total\":" + total + "}")
                    .orElse("{\"total\":0}"))).readObject(), "total", "sum"),


    /**
     * Totals an integer field of an object.
     */
    COUNT((data, parameters) -> Json.createReader(new StringReader(
            Optional.of(data.getJsonArray(parameters.getString("name")).stream()
                            .map(JsonValue::asJsonObject)
                            .map(obj -> obj.getInt(parameters.getString("key")))
                            .count())
                    .map(total -> "{\"count\":" + total + "}")
                    .orElse("{\"count\":0}"))).readObject(), "count"),


    /**
     * Gets min/max value of a field of an object.
     */
    MINMAX((data, parameters) -> Json.createReader(new StringReader(
            Optional.of(data.getJsonArray(parameters.getString("name")).stream()
                            .map(JsonValue::asJsonObject)
                            .map(obj -> obj.getInt(parameters.getString("key")))
                            .reduce(0
                                   ,parameters.getString("stat").equalsIgnoreCase("max")
                                            ? Integer::max
                                            : Integer::min)
                    )
                    .map(stat -> "{\"stat\":" + stat + "}")
                    .orElse("{\"stat\":0}"))).readObject(), "minmax","stat","min/max"),

    /**
     * Prints data.
     */
    PRINT((data, parameters) -> {
        System.out.print(data);
        return data;
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

    private final BiFunction<JsonObject, JsonObject, JsonObject> biFunction;
    private final Set<String> keywords;
    private JsonObject parameters;

    Procedure(BiFunction<JsonObject, JsonObject, JsonObject> biFunction, String... keywords) {
        this.biFunction = biFunction;
        this.keywords = Arrays.stream(keywords)
                .map(String::toLowerCase)
                .map(String::trim)
                .collect(Collectors.toSet());
        this.keywords.add(this.name().toLowerCase().trim());
    }

    @Override
    public JsonObject apply(JsonObject data) {
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
