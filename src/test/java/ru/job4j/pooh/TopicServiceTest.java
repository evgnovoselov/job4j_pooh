package ru.job4j.pooh;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TopicServiceTest {
    @Test
    void whenTopic() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        topicService.process(new Req("GET", "topic", "weather", paramForSubscriber1));
        topicService.process(new Req("POST", "topic", "weather", paramForPublisher));
        Resp result1 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        Resp result2 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        Assertions.assertThat(result1.getText()).isEqualTo("temperature=18");
        Assertions.assertThat(result2.getText()).isEmpty();
    }

    @Test
    void whenTopicTwoPublishersThenHaveTwoAnswer() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client777";
        String paramForSubscriber3 = "client6565";
        topicService.process(new Req("GET", "topic", "weather", paramForSubscriber1));
        topicService.process(new Req("GET", "topic", "weather", paramForSubscriber2));
        topicService.process(new Req("POST", "topic", "weather", paramForPublisher));
        Resp result1 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        Resp result2 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        Resp result3 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber3)
        );
        Assertions.assertThat(result1.getText()).isEqualTo("temperature=18");
        Assertions.assertThat(result2.getText()).isEqualTo("temperature=18");
        Assertions.assertThat(result3.getText()).isEmpty();
    }
}