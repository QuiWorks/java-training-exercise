package org.example.training.program;

import javax.json.JsonObject;
import java.util.Arrays;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Steps in a {@link Program}
 */
public enum Procedure implements Function<JsonObject, JsonObject> {

    NOTHING((data, parameters) -> data, "nothing", "do nothing", "do nothing to"),
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
