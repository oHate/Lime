package dev.ohate.lime.util.json.adapters;

import com.google.gson.*;
import dev.ohate.lime.util.json.JsonChain;
import org.bson.types.ObjectId;

import java.lang.reflect.Type;

public class BSONObjectIdAdapter implements JsonDeserializer<ObjectId>, JsonSerializer<ObjectId> {

    @Override
    public ObjectId deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new ObjectId(json.getAsJsonObject().get("$oid").getAsString());
    }

    @Override
    public JsonElement serialize(ObjectId src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonChain().addProperty("$oid", src.toHexString()).get();
    }

}
