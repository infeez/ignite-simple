package com.infeez.ignite.controller;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterGroup;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class MainController {

    private Ignite ignite;

    @PostConstruct
    public void onCreate() {
        Ignition.setClientMode(false);
        ignite = Ignition.start("ignite-server-node.xml");
        ignite.active(true);
    }

    public Ignite getIgnite() {
        return ignite;
    }

    public ClusterGroup getClusterGroup() {
        return ignite.cluster().forServers();
    }

}
