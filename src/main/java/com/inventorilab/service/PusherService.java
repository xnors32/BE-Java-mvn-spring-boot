package com.inventorilab.service;

import com.pusher.rest.Pusher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Map;

@Service
public class PusherService {

    @Value("${pusher.app-id}")
    private String appId;

    @Value("${pusher.key}")
    private String key;

    @Value("${pusher.secret}")
    private String secret;

    @Value("${pusher.cluster}")
    private String cluster;

    private Pusher pusher;

    @PostConstruct
    void init() {
        pusher = new Pusher(appId, key, secret);
        pusher.setCluster(cluster);
        pusher.setEncrypted(true);
    }

    public void trigger(String channel, String event, Map<String, Object> data) {
        try {
            pusher.trigger(channel, event, data);
        } catch (Exception e) {
            System.err.println("Pusher trigger failed: " + e.getMessage());
        }
    }
}
