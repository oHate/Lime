package dev.ohate.lime.util.json.adapters;

import com.google.gson.*;
import dev.ohate.lime.util.json.JsonChain;

import java.lang.reflect.Type;
import java.time.Instant;

public class BSONDateAdapter implements JsonDeserializer<Instant>, JsonSerializer<Instant> {

    @Override
    public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Instant.parse(json.getAsJsonObject().get("$date").getAsString());
    }

    @Override
    public JsonElement serialize(Instant src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonChain().addProperty("$date", src.toString()).get();
    }

}
