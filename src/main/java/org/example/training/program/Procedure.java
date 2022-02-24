package org.example.training.program;

import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Steps in a {@link Program}
 */
public enum Procedure implements Function<Transformer, Transformer>
{
    /**
     * Does nothing.
     */
    //    NOTHING((transformation, parameters) -> transformation, "nothing", "do nothing", "do nothing to"),

    /**
     * Totals an integer field of an object.
     */
    //    TOTAL((transformation, parameters) -> Json.createReader(new StringReader(
    //            Optional.of(transformation.getJsonArray(parameters.getString("name")).stream()
    //                            .map(JsonValue::asJsonObject)
    //                            .map(obj -> obj.getInt(parameters.getString("key")))
    //                            .reduce(0, Integer::sum))
    //                    .map(total -> "{\"total\":" + total + "}")
    //                    .orElse("{\"total\":0}"))).readObject()),

    RENAME_KEY( JsonParser.Event.KEY_NAME, ( transformer, parameters ) -> {
        final String keyName = transformer.getJsonParser().getString();
        JsonGenerator jsonGenerator = keyName.equals( parameters.getString( "keyName" ) )
            ? transformer.getJsonGenerator().write( parameters.getString( "keyReplacement" ) )
            : transformer.getJsonGenerator();
        return  new Transformer( transformer.getJsonParser(), keyName.equals( parameters.getString( "keyName" ) )
            ? transformer.getJsonGenerator().write( parameters.getString( "keyReplacement" ) )
            : transformer.getJsonGenerator(),  )
    } );

    /**
     * Gets a {@link Procedure} instance by matching text.
     *
     * @param text       String input used to help identify the procedure to provide.
     * @param parameters The parameters used by the procedure.
     * @return any matching procedure.
     */
    public static Optional<Procedure> get( JsonParser.Event event, String text, JsonObject parameters )
    {
        return get( text, parameters )
            .filter( p -> p.getEvent().equals( event ) );
    }

    public static Optional<Procedure> get( String text, JsonObject parameters )
    {
        return Arrays.stream( Procedure.values() )
            .filter( i -> i.getKeywords().contains( text ) )
            .peek( p -> p.setParameters( parameters ) )
            .findAny();
    }

    public static Optional<Procedure> get( String text )
    {
        return get( text, JsonValue.EMPTY_JSON_OBJECT )
            .filter( i -> i.getKeywords().contains( text ) );
    }

    private final BiFunction<Transformer, JsonObject, Transformer> biFunction;
    private final JsonParser.Event event;
    private final Set<String> keywords;
    private JsonObject parameters;

    Procedure( JsonParser.Event event, BiFunction<Transformer, JsonObject, Transformer> biFunction, String... keywords )
    {
        this.event = event;
        this.biFunction = biFunction;
        this.keywords = Arrays.stream( keywords ).map( String::toLowerCase ).map( String::trim ).collect( Collectors.toSet() );
        this.keywords.add( this.name().toLowerCase().trim() );
        this.parameters = JsonValue.EMPTY_JSON_OBJECT;
    }

    @Override
    public Transformer apply( Transformer transformer )
    {
        return JsonParser.Event.KEY_NAME.equals( transformer.getEvent() )
            ? biFunction.apply( transformer, parameters )
            : transformer;
    }

    public Set<String> getKeywords()
    {
        return keywords;
    }

    public Procedure setParameters( JsonObject parameters )
    {
        this.parameters = parameters;
        return this;
    }

    public JsonParser.Event getEvent()
    {
        return event;
    }
}
