package com.infeez.ignite.services;

import com.infeez.ignite.services.proxy.SimpleCacheServiceProxy;
import org.apache.ignite.Ignite;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceContext;

public class SimpleCacheService implements Service, SimpleCacheServiceProxy<String, String> {

    @IgniteInstanceResource
    private Ignite ignite;

    public void cancel(ServiceContext serviceContext) {
    }

    public void init(ServiceContext serviceContext) throws Exception {
    }

    public void execute(ServiceContext serviceContext) throws Exception {
        System.out.println("Hey I'am " + getClass().getName());
    }

    public void put(String key, String value) {
        ignite.cache("main").put(key, value);
    }

    public String get(String key) {
        return (String) ignite.cache("main").get(key);
    }

}