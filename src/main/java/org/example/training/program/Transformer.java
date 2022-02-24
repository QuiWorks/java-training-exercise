package org.example.training.program;

import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;

public class Transformer
{
    private final JsonGenerator jsonGenerator;
    private final JsonParser jsonParser;
    private final JsonParser.Event event;

    public Transformer( JsonGenerator jsonGenerator, JsonParser jsonParser, JsonParser.Event event )
    {
        this.jsonGenerator = jsonGenerator;
        this.jsonParser = jsonParser;
        this.event = event;
    }

    public JsonGenerator getJsonGenerator()
    {
        return jsonGenerator;
    }

    public JsonParser getJsonParser()
    {
        return jsonParser;
    }

    public JsonParser.Event getEvent()
    {
        return event;
    }
}
