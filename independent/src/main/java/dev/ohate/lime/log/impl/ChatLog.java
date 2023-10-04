package dev.ohate.lime.log.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.TextSearchOptions;
import dev.ohate.lime.Lime;
import dev.ohate.lime.log.Log;
import dev.ohate.lime.util.json.JsonUtil;
import org.bson.BsonDateTime;
import org.bson.Document;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ChatLog extends Log {

    private final UUID playerId;
    private final String message;

    public static void main(String[] args) {
        createIndex();

        UUID uuid = UUID.fromString("239c8573-0675-4a78-82f3-d178c72d5dc9");

        new ChatLog("Hub", uuid, "Test Message");
    }

    public ChatLog(String origin, UUID playerId, String message) {
        super(origin);

        this.playerId = playerId;
        this.message = message;
    }

    public static MongoCollection<Document> getCollection() {
        return Lime.getInstance().getDatabase().getCollection("chatlog");
    }

    public static void createIndex() {
        getCollection().createIndex(Indexes.compoundIndex(
                Indexes.ascending("playerId"),
                Indexes.text("message")
        ));
    }

    public static void getLogs(UUID playerId, String textSearch, Instant startDate, Instant endDate) {
        getCollection().find(Filters.and(
                        Filters.eq(playerId),
                        Filters.gte("creation", new BsonDateTime(startDate.toEpochMilli())),
                        Filters.lt("creation", new BsonDateTime(endDate.toEpochMilli())),
                        Filters.text(textSearch, new TextSearchOptions().caseSensitive(false)))
                ).sort(Sorts.metaTextScore("score")).forEach(document -> System.out.println(JsonUtil.writeToPrettyJson(document)));
    }

}
