package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class TopicService implements Service {
    private final ConcurrentMap<String, ConcurrentMap<String, ConcurrentLinkedQueue<String>>> topics = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp resp = null;
        if ("GET".equals(req.getHttpRequestType())) {
            resp = processGet(req);
        } else if ("POST".equals(req.getHttpRequestType())) {
            resp = processPost(req);
        }
        return resp != null ? resp : new Resp("", "501");
    }

    /**
     * Каждый пользователь получает сообщение из своей очереди, или если сообщения нет, то статус 204.
     *
     * @param req Запрос.
     * @return Ответ.
     */
    private Resp processGet(Req req) {
        topics.putIfAbsent(req.getSourceName(), new ConcurrentHashMap<>());
        topics.get(req.getSourceName()).putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>());
        String valueTopic = topics.get(req.getSourceName()).get(req.getParam()).poll();
        String status = "200";
        if (valueTopic == null) {
            valueTopic = "";
            status = "204";
        }
        return new Resp(valueTopic, status);
    }

    /**
     * Добавляется сообщение всем подписанным получателям у топика.
     *
     * @param req Запрос.
     * @return Ответ.
     */
    private Resp processPost(Req req) {
        ConcurrentMap<String, ConcurrentLinkedQueue<String>> subs = topics.getOrDefault(req.getSourceName(), new ConcurrentHashMap<>());
        subs.forEach((sub, queue) -> queue.add(req.getParam()));
        return new Resp("", "200");
    }
}
