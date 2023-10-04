package dev.ohate.lime.log;

import com.google.gson.annotations.SerializedName;
import com.mongodb.client.MongoCollection;
import lombok.Getter;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.*;

@Getter
public abstract class Log {

    @SerializedName("_id")
    private final ObjectId id;
    private final String origin;

    public Log(String origin) {
        this.id = new ObjectId();
        this.origin = origin;
    }

    public Instant getCreationDate() {
        return id.getDate().toInstant();
    }

}
