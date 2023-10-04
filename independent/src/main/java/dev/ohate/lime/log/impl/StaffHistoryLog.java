package dev.ohate.lime.log.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import dev.ohate.lime.Lime;
import dev.ohate.lime.log.Log;
import dev.ohate.lime.log.Rollback;
import dev.ohate.lime.profile.model.ProfileAction;
import lombok.Getter;
import org.bson.BsonDateTime;
import org.bson.Document;

import java.time.Instant;
import java.util.*;

@Getter
public class StaffHistoryLog extends Log {

    private final String punishmentId;
    private final ProfileAction action;

    public StaffHistoryLog(String origin, String punishmentId, ProfileAction action) {
        super(origin);
        this.punishmentId = punishmentId.toUpperCase(Locale.ENGLISH);

        if (action == ProfileAction.EXPIRE) {
            throw new RuntimeException("Action in StaffHistoryLog cannot be EXPIRE.");
        }

        this.action = action;
    }

    public static MongoCollection<Document> getCollection() {
        return Lime.getInstance().getDatabase().getCollection("staffHistory");
    }

    public static List<StaffHistoryLog> getEntirePlayerHistory(UUID playerId) {
        List<StaffHistoryLog> historyLogs = new ArrayList<>();

//        getCollection().find(Filters.and(
//                Filters.gte("creation", new BsonDateTime()),
//                Filters.lt("creation", endDate),
//                Filters.text(searchString)))
//                .sort(Sorts.metaTextScore(""))
//                .sort(Sorts.descending("date"))
//                .forEach(document -> historyLogs.add(gson.fromJson(document.toJson(), StaffHistoryLog.class)));
//
//        database.getCollection("test").find(Filters.eq("staff", playerId))
//                .sort(Sorts.descending("date"))
//                .forEach(document -> historyLogs.add(gson.fromJson(document.toJson(), StaffHistoryLog.class)));

        return historyLogs;
    }

    public static void rollback(List<StaffHistoryLog> logs) {
//        Rollback rollback = Rollback.builder().build();
//        List<WriteModel<Document>> models = new ArrayList<>();
//
//        for (StaffHistoryLog log : logs) {
//            Instant creation = log.getCreation();
//
//            if (creation.before(rollback.getBefore()) || creation.after(rollback.getAfter())) {
//                continue;
//            }
//
//            if (log.getAction() == ProfileAction.ADD) {
//                models.add(new DeleteOneModel<>(Filters.eq(log.getPunishmentId())));
//                // TODO Sync
//            } else if (log.getAction() == ProfileAction.REMOVE) {
//                models.add(new UpdateOneModel<>(Filters.eq(log.getPunishmentId())));
//                // TODO Sync
//            }
//        }

    }


//    public static List<StaffHistoryLog> getPaginatedPlayerHistory(int currentPage, Instant date) {
//        currentPage = Math.max(currentPage, 1);
//
//        int skipCount = (currentPage - 1) * Pagination.PAGE_COUNT;
//
//        Gson gson = new Gson();
//        MongoClient client = MongoClients.create("");
//        MongoDatabase database = client.getDatabase("");
//
//
//        database.getCollection("test")
//                .find(Filters.lt("date", date))
//                .sort(Sorts.descending("date"))
//                .limit(10)
//                .skip(skipCount);
//    }


}
