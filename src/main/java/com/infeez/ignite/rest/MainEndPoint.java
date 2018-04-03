package com.infeez.ignite.rest;

import com.infeez.ignite.controller.MainController;
import com.infeez.ignite.services.proxy.SimpleServiceProxy;
import org.apache.ignite.Ignite;
import org.apache.ignite.cluster.ClusterNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class MainEndPoint {

    private MainController mainController;

    @Autowired
    public MainEndPoint(MainController mainController) {
        this.mainController = mainController;
    }

    @RequestMapping(value = "/getNodes", method = RequestMethod.GET)
    public List<UUID> getAllNodes() {
        return mainController.getIgnite()
                .cluster()
                .forServers()
                .nodes()
                .stream()
                .map(ClusterNode::id)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/put", method = RequestMethod.GET)
    public void putToCache(@RequestParam("nodeId") String nodeId,
                           @RequestParam("value") String value) {
        Ignite ignite = mainController.getIgnite();
        SimpleServiceProxy simpleServiceProxy = ignite.services(ignite.cluster().forNodeId(UUID.fromString(nodeId)))
                .serviceProxy(SimpleServiceProxy.PROXY_NAME, SimpleServiceProxy.class, true);
        simpleServiceProxy.putInCache(value);

    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public String getFromCache(@RequestParam("nodeId") String nodeId) {
        Ignite ignite = mainController.getIgnite();
        SimpleServiceProxy simpleServiceProxy = ignite.services(ignite.cluster().forNodeId(UUID.fromString(nodeId)))
                .serviceProxy(SimpleServiceProxy.PROXY_NAME, SimpleServiceProxy.class, true);
        return simpleServiceProxy.getFromCache();
    }

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public void startInComputeGrid(@RequestParam("nodeId") String nodeId, @RequestParam("c") int c, @RequestParam("t") int t) {
        Ignite ignite = mainController.getIgnite();
        SimpleServiceProxy simpleServiceProxy = ignite.services(ignite.cluster().forNodeId(UUID.fromString(nodeId)))
                .serviceProxy(SimpleServiceProxy.PROXY_NAME, SimpleServiceProxy.class, true);
        simpleServiceProxy.startInComputeGrid(c, t);
    }

    @RequestMapping(value = "/calculate", method = RequestMethod.GET)
    public void startInComputeGrid(@RequestParam("nodeId") String nodeId) {
        Ignite ignite = mainController.getIgnite();
        SimpleServiceProxy simpleServiceProxy = ignite.services(ignite.cluster().forNodeId(UUID.fromString(nodeId)))
                .serviceProxy(SimpleServiceProxy.PROXY_NAME, SimpleServiceProxy.class, true);
        simpleServiceProxy.calculate();
    }

}
