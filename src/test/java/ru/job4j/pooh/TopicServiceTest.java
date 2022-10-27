package ru.job4j.pooh;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TopicServiceTest {

    @Test
    void whenTopic() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForPublisher1 = "client407";
        String paramForPublisher2 = "client6565";
        topicService.process(new Req("GET", "topic", "weather", paramForPublisher1));
        topicService.process(new Req("POST", "topic", "weather", paramForPublisher));
        Resp result1 = topicService.process(
                new Req("GET", "topic", "weather", paramForPublisher1)
        );
        Resp result2 = topicService.process(
                new Req("GET", "topic", "weather", paramForPublisher2)
        );
        Assertions.assertThat(result1.getText()).isEqualTo("temperature=18");
        Assertions.assertThat(result1.getText()).isEmpty();
    }
}