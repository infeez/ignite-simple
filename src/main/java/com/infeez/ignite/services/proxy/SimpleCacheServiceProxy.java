package com.infeez.ignite.services.proxy;

public interface SimpleCacheServiceProxy<K, V> {

    String PROXY_NAME = "simpleCacheServiceProxy";

    void put(K key, V value);

    V get(K key);
}
