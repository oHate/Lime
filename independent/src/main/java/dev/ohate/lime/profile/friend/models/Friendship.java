package dev.ohate.lime.profile.friend.models;

import com.google.gson.annotations.SerializedName;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import dev.ohate.lime.Lime;
import lombok.*;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Data
@EqualsAndHashCode(of = "id")
public class Friendship {

    @SerializedName("_id")
    private final ObjectId id;
    private final UUID sender;
    private final UUID receiver;

    public Friendship(UUID sender, UUID receiver) {
        this.id = new ObjectId();
        this.sender = sender;
        this.receiver = receiver;
    }

    public static MongoCollection<Document> getCollection() {
        return Lime.getInstance().getDatabase().getCollection("friends");
    }

    public Instant getCreationDate() {
        return id.getDate().toInstant();
    }

    public CompletableFuture<DeleteResult> delete() {
        return CompletableFuture.supplyAsync(() -> getCollection().deleteOne(Filters.eq(id)));
    }

    public UUID getOther(UUID source) {
        if (sender.equals(source)) {
            return receiver;
        }

        return sender;
    }

}
