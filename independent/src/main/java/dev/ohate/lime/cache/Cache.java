package dev.ohate.lime.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface Cache<T> {

    T get(Object key);
    void update(String key, T value);
    Map<String, T> bulkLoad(List<Object> keys);
    void purge();
    void rebuild();

}
