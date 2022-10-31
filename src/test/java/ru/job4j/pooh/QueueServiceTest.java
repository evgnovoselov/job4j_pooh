package ru.job4j.pooh;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QueueServiceTest {
    /**
     * Проверка очереди на получение значения.
     */
    @Test
    public void whenPostThenGetQueue() {
        QueueService queueService = new QueueService();
        String paramForPostMethod = "temperature=18";
        queueService.process(new Req("POST", "queue", "weather", paramForPostMethod));
        Resp result = queueService.process(new Req("GET", "queue", "weather", null));
        assertThat(result.getText()).isEqualTo("temperature=18");
    }

    /**
     * Проверка статуса получения значения.
     */
    @Test
    public void whenPostThenGetStatus200() {
        QueueService queueService = new QueueService();
        String paramForPostMethod = "temperature=18";
        queueService.process(new Req("POST", "queue", "weather", paramForPostMethod));
        Resp result = queueService.process(new Req("GET", "queue", "weather", null));
        assertThat(result.getStatus()).isEqualTo("200");
    }

    /**
     * Получение значения из пустой очереди, проверка статуса и значения.
     */
    @Test
    public void whenNoHaveDataThenStatus204() {
        QueueService queueService = new QueueService();
        String paramForPostMethod = "temperature=18";
        queueService.process(new Req("POST", "queue", "weather", paramForPostMethod));
        queueService.process(new Req("GET", "queue", "weather", null));
        Resp result = queueService.process(new Req("GET", "queue", "weather", null));
        assertThat(result.getText()).isEmpty();
        assertThat(result.getStatus()).isEqualTo("204");
    }

    /**
     * Создаем две очереди и одну опустошаем, и проверяем другую.
     */
    @Test
    public void whenCreateTwoQueueAndGetMoreMsgFromFirstThenFirstEmptyAndSecondHaveAnswer() {
        QueueService queueService = new QueueService();
        String paramForPostInQueue1 = "temperature=18";
        String paramForPostInQueue2 = "boiler=50";
        queueService.process(new Req("POST", "queue", "weather", paramForPostInQueue1));
        queueService.process(new Req("POST", "queue", "house", paramForPostInQueue2));
        Resp result1 = queueService.process(new Req("GET", "queue", "weather", null));
        Resp result2 = queueService.process(new Req("GET", "queue", "weather", null));
        Resp result3 = queueService.process(new Req("GET", "queue", "house", null));
        Resp result4 = queueService.process(new Req("GET", "queue", "house", null));
        assertThat(result1.getText()).isEqualTo("temperature=18");
        assertThat(result1.getStatus()).isEqualTo("200");
        assertThat(result2.getText()).isEmpty();
        assertThat(result2.getStatus()).isEqualTo("204");
        assertThat(result3.getText()).isEqualTo("boiler=50");
        assertThat(result3.getStatus()).isEqualTo("200");
        assertThat(result4.getText()).isEmpty();
        assertThat(result4.getStatus()).isEqualTo("204");
    }

    /**
     * Когда неверный тип запроса, получаем ответ со статусом 501.
     */
    @Test
    public void whenBadRequestTypeThenRespStatus501() {
        QueueService queueService = new QueueService();
        Resp result = queueService.process(new Req("TTT", "queue", "weather", null));
        assertThat(result.getText()).isEmpty();
        assertThat(result.getStatus()).isEqualTo("501");
    }
}