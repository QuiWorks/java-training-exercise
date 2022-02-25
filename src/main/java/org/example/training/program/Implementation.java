package org.example.training.program;

import javax.json.JsonObject;
import javax.json.JsonValue;
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
public enum Implementation implements Procedure
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

    RENAME_KEY( JsonParser.Event.KEY_NAME, ( transformation, parameters ) -> {
        return transformation;
//        final String keyName = transformation.getJsonParser().getString();
//        return  new Transformation( transformation.getJsonParser(), keyName.equals( parameters.getString( "keyName" ) )
//            ? transformation.getJsonGenerator().write( parameters.getString( "keyReplacement" ) )
//            : transformation.getJsonGenerator(), transformation.getJsonParser(),  )
    } );

    /**
     * Gets a {@link Implementation} instance by matching text.
     *
     * @param text       String input used to help identify the procedure to provide.
     * @param parameters The parameters used by the procedure.
     * @return any matching procedure.
     */
    public static Optional<Implementation> get( JsonParser.Event event, String text, JsonObject parameters )
    {
        return get( text, parameters )
            .filter( p -> p.getEvent().equals( event ) );
    }

    public static Optional<Implementation> get( String text, JsonObject parameters )
    {
        return Arrays.stream( Implementation.values() )
            .filter( i -> i.getKeywords().contains( text ) )
            .peek( p -> p.setParameters( parameters ) )
            .findAny();
    }

//    public static Implementation ofElse( String text, JsonObject parameters, String... alternatives )
//    {
//        return Arrays.stream( Implementation.values() )
//            .filter( i -> i.getKeywords().contains( text ) )
//            .peek( p -> p.setParameters( parameters ) )
//            .findAny()
//            .orElse( ofElse(  ) )
//            ;
//    }

    public static Optional<Implementation> get( String text )
    {
        return get( text, JsonValue.EMPTY_JSON_OBJECT )
            .filter( i -> i.getKeywords().contains( text ) );
    }

    private final BiFunction<Transformation, JsonObject, Transformation> biFunction;
    private final JsonParser.Event event;
    private final Set<String> keywords;
    private JsonObject parameters;

    Implementation( JsonParser.Event event, BiFunction<Transformation, JsonObject, Transformation> biFunction, String... keywords )
    {
        this.event = event;
        this.biFunction = biFunction;
        this.keywords = Arrays.stream( keywords ).map( String::toLowerCase ).map( String::trim ).collect( Collectors.toSet() );
        this.keywords.add( this.name().toLowerCase().trim() );
        this.parameters = JsonValue.EMPTY_JSON_OBJECT;
    }

    @Override
    public Transformation apply( Transformation transformation )
    {
        return JsonParser.Event.KEY_NAME.equals( transformation.getEvent() )
            ? biFunction.apply( transformation, parameters )
            : transformation;
    }

    public Set<String> getKeywords()
    {
        return keywords;
    }

    public Implementation setParameters( JsonObject parameters )
    {
        this.parameters = parameters;
        return this;
    }

    public JsonParser.Event getEvent()
    {
        return event;
    }
}
