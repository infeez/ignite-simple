package com.infeez.ignite.services;

import com.infeez.ignite.services.proxy.SimpleCacheServiceProxy;
import com.infeez.ignite.services.proxy.SimpleServiceProxy;
import org.apache.ignite.Ignite;
import org.apache.ignite.lang.IgniteRunnable;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.resources.ServiceResource;
import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public void startInComputeGrid() {
        ignite.compute(ignite.cluster().forServers()).runAsync(new IgniteRunnable() {
            public void run() {
                int i = Runtime.getRuntime().availableProcessors() - 1;
                System.out.println("Core size = " + i);
                ExecutorService service = Executors.newFixedThreadPool(i);
                for (int j = 0; j < i; j++) {
                    service.submit(new Runnable() {
                        public void run() {
                            String name = Thread.currentThread().getName();
                            System.out.println("Thread name = " + name + " started");
                            while (true){}
                        }
                    });
                }
                while (true){}
            }
        });
    }
}
