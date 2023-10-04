package dev.ohate.lime.util.json;

import com.google.gson.*;
import dev.ohate.lime.util.json.adapters.BSONDateAdapter;
import dev.ohate.lime.util.json.adapters.BSONLongAdapter;
import dev.ohate.lime.util.json.adapters.BSONObjectIdAdapter;
import dev.ohate.lime.util.json.adapters.UUIDAdapter;
import lombok.Getter;
import org.bson.Document;
import org.bson.json.JsonMode;
import org.bson.json.JsonWriterSettings;
import org.bson.types.ObjectId;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JsonUtil {

    private static final JsonWriterSettings RELAXED_WRITER = JsonWriterSettings.builder()
            .outputMode(JsonMode.RELAXED)
            .build();

    private static final Map<Type, Object> TYPE_ADAPTERS = new HashMap<>();

    @Getter
    private static Gson gson;

    @Getter
    private static Gson prettyGson;

    @Getter
    private static Gson documentGson;

    static {
        registerTypeAdapter(UUID.class, new UUIDAdapter());
        rebuildGson();
    }

    public static void registerTypeAdapters(Map<Type, Object> adapters) {
        TYPE_ADAPTERS.putAll(adapters);
        rebuildGson();
    }

    public static void registerTypeAdapter(Type type, Object typeAdapter) {
        TYPE_ADAPTERS.put(type, typeAdapter);
        rebuildGson();
    }

    private static void rebuildGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        for (Type type : TYPE_ADAPTERS.keySet()) {
            gsonBuilder.registerTypeAdapter(type, TYPE_ADAPTERS.get(type));
        }

        gson = gsonBuilder.create();

        prettyGson = gson.newBuilder().setPrettyPrinting().create();

        documentGson = gson.newBuilder()
                .registerTypeAdapter(ObjectId.class, new BSONObjectIdAdapter())
                .registerTypeAdapter(Date.class, new BSONDateAdapter())
                .registerTypeAdapter(Long.class, new BSONLongAdapter())
                .create();
    }

    public static JsonObject writeToPrettyTree(Object object) {
        return (JsonObject) prettyGson.toJsonTree(object);
    }

    public static String writeToPrettyJson(Object object) {
        return prettyGson.toJson(object);
    }

    public static JsonObject writeToTree(Object object) {
        return (JsonObject) gson.toJsonTree(object);
    }

    public static String writeToJson(Object object) {
        return gson.toJson(object);
    }

    public static Document writeToDocument(Object object) {
        return Document.parse(documentGson.toJson(object));
    }

    public static <T> T readFromJson(String json, Class<T> clazz) throws JsonSyntaxException {
        return gson.fromJson(json, clazz);
    }

    public static <T> T readFromJson(JsonElement json, Class<T> clazz) throws JsonSyntaxException {
        return gson.fromJson(json, clazz);
    }

    public static <T> T readFromDocument(Document document, Class<T> clazz) throws JsonSyntaxException {
        return documentGson.fromJson(document.toJson(RELAXED_WRITER), clazz);
    }

}
