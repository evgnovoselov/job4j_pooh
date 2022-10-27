package ru.job4j.pooh;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QueueServiceTest {

    @Test
    void whenPostThenGetQueue() {
        QueueService queueService = new QueueService();
        String paramForPostMethod = "temperature=18";
        queueService.process(new Req("POST", "queue", "weather", paramForPostMethod));
        Resp result = queueService.process(new Req("GET", "queue", "weather", null));
        assertThat(result.getText()).isEqualTo("temperature=18");
    }
}