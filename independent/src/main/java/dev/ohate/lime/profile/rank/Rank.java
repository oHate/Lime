package dev.ohate.lime.profile.rank;

import com.google.gson.annotations.SerializedName;
import com.mongodb.client.MongoCollection;
import dev.ohate.lime.Lime;
import dev.ohate.lime.profile.model.Permission;
import dev.ohate.lime.util.json.JsonUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bson.Document;

import java.awt.*;
import java.util.List;
import java.util.*;

@Data
@EqualsAndHashCode(of = "name")
public class Rank implements Comparable<Rank> {

    private static final Map<String, Rank> RANKS = new HashMap<>();

    @Getter
    private static Rank defaultRank;

    @SerializedName("_id")
    private final String name;
    private int weight;
    private Color color;
    private String prefix;

    private String suffix;
    private boolean italic;
    private boolean bold;
    private RankType type;

    private final Set<String> scopes = new HashSet<>();
    private final Set<Permission> permissions = new HashSet<>();
    private final Set<String> inherits = new HashSet<>();

    public Rank(String name, Color color, int weight) {
        this.name = name;
        this.color = color;
        this.weight = weight;
    }

    public static MongoCollection<Document> collection() {
        return Lime.getInstance().getDatabase().getCollection("ranks");
    }

    public static void initializeRanks() {
        RANKS.clear();

        for (Document document : collection().find()) {
            Rank rank = JsonUtil.readFromDocument(document, Rank.class);

            if (rank.type == RankType.DEFAULT) {
                if (defaultRank != null) {
                    throw new RuntimeException("Duplicate default ranks were found: " + rank.getName() + " and " + defaultRank.getName());
                }

                defaultRank = rank;
            }

            RANKS.put(rank.getName().toLowerCase(), rank);
        }
    }

    public void setType(RankType type) {
        if (type == RankType.DEFAULT) {
            defaultRank = this;


        }

        this.type = type;
    }

    public boolean addPermission(String permission, String scope) {
        return permissions.add(new Permission(
                permission.toLowerCase(Locale.ENGLISH),
                scope.toLowerCase(Locale.ENGLISH)
        ));
    }

    public boolean removePermission(String permission, String scope) {
        return permissions.removeIf(perm -> perm.getPermission().equalsIgnoreCase(permission) && perm.getScope().equalsIgnoreCase(scope));
    }

    public Set<String> getAllInherits() {
        Set<String> processed = new HashSet<>();
        processed.add(name);
        getAllInherits(processed);
        return processed;
    }

//    public static Rank getDefaultRank() {
//        for (Rank rank : RANKS.values()) {
//            if (rank.isDefaultRank()) {
//
//            }
//        }
//
//        return getRank("Member");
//    }

    public static Rank getRank(String rankName) {
        return RANKS.get(rankName.toLowerCase());
    }

    private void getAllInherits(Set<String> processed) {
        List<String> rankInherits = new ArrayList<>(inherits);

        for (String rankName : rankInherits) {
            if (processed.contains(rankName)) {
                continue;
            }

            processed.add(rankName);

            Rank rank = Rank.getRank(rankName);

            if (rank == null) {
                continue;
            }

            rank.getAllInherits(processed);
        }
    }

    public static Map<String, List<Rank>> getScopeMap() {
        Map<String, List<Rank>> scopeMap = new HashMap<>();

        for (Rank rank : RANKS.values()) {
            List<String> scopes = new ArrayList<>(rank.getScopes());

            if (scopes.isEmpty()) {
                scopes.add("global");
            }

            for (String scope : scopes) {
                scope = scope.toLowerCase();

                if (scopeMap.containsKey(scope)) {
                    scopeMap.get(scope).add(rank);
                } else {
                    scopeMap.put(scope, new ArrayList<>(List.of(rank)));
                }
            }
        }

        return scopeMap;
    }

    @Override
    public int compareTo(Rank otherRank) {
        return Integer.compare(otherRank.getWeight(), weight);
    }

}