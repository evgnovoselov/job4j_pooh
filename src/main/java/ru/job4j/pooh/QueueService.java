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
        } else if ("GET".equals(req.getHttpRequestType())) {
            resp = processGet(req);
        }
        return resp != null ? resp : new Resp("", "501");
    }

    /**
     * Получаем ответ с сообщением из нужной очереди, если сообщения или очереди нет возвращаем статус ответа 204.
     *
     * @param req Запрос.
     * @return Ответ.
     */
    private Resp processGet(Req req) {
        String valueQueue = queue.getOrDefault(req.getSourceName(), new ConcurrentLinkedQueue<>()).poll();
        String status = "200";
        if (valueQueue == null) {
            valueQueue = "";
            status = "204";
        }
        return new Resp(valueQueue, status);
    }

    /**
     * Если есть очередь, то добавляем в неё сообщение, если нет, то создаем очередь и добавляем.
     *
     * @param req Запрос.
     * @return Ответ.
     */
    private Resp processPost(Req req) {
        queue.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
        queue.get(req.getSourceName()).add(req.getParam());
        return new Resp("", "200");
    }
}
