package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class QueueService implements Service {
    private final ConcurrentMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp resp = null;
        if ("POST".equals(req.getHttpRequestType())) {
            resp = processPost(req);
        }
        if ("GET".equals(req.getHttpRequestType())) {
            resp = processGet(req);
        }
        return resp;
    }

    private Resp processGet(Req req) {
        String valueQueue = queue.getOrDefault(req.getSourceName(), new ConcurrentLinkedQueue<>()).poll();
        String status = "200";
        if (valueQueue == null) {
            valueQueue = "";
            status = "204";
        }
        return new Resp(valueQueue, status);
    }

    private Resp processPost(Req req) {
        queue.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
        queue.get(req.getSourceName()).add(req.getParam());
        return new Resp("", "200");
    }
}
