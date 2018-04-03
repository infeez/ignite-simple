package com.infeez.ignite.services;

import com.infeez.ignite.services.proxy.SimpleCacheServiceProxy;
import com.infeez.ignite.services.proxy.SimpleServiceProxy;
import org.apache.ignite.Ignite;
import org.apache.ignite.lang.IgniteRunnable;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.resources.ServiceResource;
import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceContext;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SimpleService implements Service, SimpleServiceProxy {

    public static final int SIZE = 3000;
    private static final int BATCH_SIZE = 200;

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


        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                simpleCacheServiceProxy.put(i + "|" + j,  "V" + i);
            }
        }

        System.out.println("Complete " + getClass().getName());
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

    public void startInComputeGrid(int count, int timeout) {
        ignite.compute(ignite.cluster().forServers()).runAsync(new IgniteRunnable() {
            public void run() {
                int i = Runtime.getRuntime().availableProcessors() - 1;
                System.out.println("Core size = " + i);
                ExecutorService service = Executors.newFixedThreadPool(i);
                List<Callable<String>> tasks = new LinkedList<>();
                for (int j = 0; j < i * count; j++) {
                    final int jj = j;
                    tasks.add(new Callable<String>() {
                        public String call() throws Exception {
                            long startTime = System.currentTimeMillis();
                            int i = 0;
                            Random random = new Random();
                            while (i++ < Integer.MAX_VALUE) {
                                random.nextGaussian();
                                if(System.currentTimeMillis() - startTime > timeout) {
                                    System.out.println(Thread.currentThread().getName() + ": calc " + i);
                                    break;
                                }
                            }
                            return "Test " + jj;
                        }
                    });
                }

                try {
                    service.invokeAll(tasks, 10, TimeUnit.SECONDS).forEach(f -> {
                        try {
                            System.out.println(f.isDone() + " " + f.isCancelled() + " " + f.get());
                        }
                        catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    });
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                    service.submit(new Runnable() {
//                        public void run() {
//                            String name = Thread.currentThread().getName();
//                            System.out.println("Thread name = " + name + " started");
//                            while (true) {
//                            }
//                        }
//                    });

                while (true) {
                }
            }
        });
    }

    public void calculate() {
        long start = System.currentTimeMillis();
        calculateByOne();
        System.out.println("Calculate by one result:");
        System.out.println(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        calculateByBatch();
        System.out.println("Calculate by batch result:");
        System.out.println(System.currentTimeMillis() - start);
    }


    private void calculateByOne() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                simpleCacheServiceProxy.get(i + "|" + j);
            }
        }
    }

    private void calculateByBatch() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE / BATCH_SIZE; j++) {
                int finalI = i;
                Set<String> keys = IntStream.range(j * BATCH_SIZE, j * BATCH_SIZE + BATCH_SIZE).mapToObj(v -> finalI + "|" + v).collect(Collectors.toSet());

                Map<String, String> all = simpleCacheServiceProxy.getAll(keys);
            }
        }
    }
}
