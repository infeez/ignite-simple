package com.infeez.ignite.services;

import com.infeez.ignite.services.proxy.SimpleCacheServiceProxy;
import com.infeez.ignite.services.proxy.SimpleServiceProxy;
import org.apache.ignite.Ignite;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.resources.ServiceResource;
import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceContext;

public class SimpleService implements Service, SimpleServiceProxy {

    @IgniteInstanceResource
    private Ignite ignite;
    @ServiceResource(serviceName = SimpleCacheServiceProxy.PROXY_NAME, proxyInterface = SimpleCacheServiceProxy.class)
    private transient SimpleCacheServiceProxy<String, String> simpleCacheServiceProxy;

    public void cancel(ServiceContext serviceContext) {

    }

    public void init(ServiceContext serviceContext) throws Exception {

    }

    public void execute(ServiceContext serviceContext) throws Exception {
        System.out.println("Hey I'am " + getClass().getName());
        simpleMethod("Started");
    }

    public <T> void simpleMethod(T t) {
        System.out.println(getClass().getName() + " simpleMethod called and T type is " + t.getClass());
    }

    public void putInCache(String value) {
        simpleCacheServiceProxy.put("key", value);
    }

    public String getFromCache() {
        return simpleCacheServiceProxy.get("key");
    }
}
