package com.infeez.ignite.services.proxy;

import java.util.Map;
import java.util.Set;

public interface SimpleCacheServiceProxy<K, V> {

    String PROXY_NAME = "simpleCacheServiceProxy";

    void put(K key, V value);

    V get(K key);

    Map<K, V> getAll(Set<K> keys);
}
