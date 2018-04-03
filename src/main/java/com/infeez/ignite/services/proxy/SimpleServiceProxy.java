package com.infeez.ignite.services.proxy;

public interface SimpleServiceProxy {

    String PROXY_NAME = "simpleServiceProxy";

    <T> void simpleMethod(T t);

    void putInCache(String value);

    String getFromCache();

    void startInComputeGrid(int c, int t);

    void calculate();

}
