package dev.ohate.lime.util.json.adapters;

import com.google.gson.*;
import dev.ohate.lime.util.json.JsonChain;

import java.lang.reflect.Type;

public class BSONLongAdapter implements JsonDeserializer<Long>, JsonSerializer<Long> {

    @Override
    public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Long.parseLong(json.getAsJsonObject().get("$numberLong").getAsString());
    }

    @Override
    public JsonElement serialize(Long src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonChain().addProperty("$numberLong", src.toString()).get();
    }

}
