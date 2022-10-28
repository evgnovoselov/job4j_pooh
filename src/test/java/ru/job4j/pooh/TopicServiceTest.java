package ru.job4j.pooh;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TopicServiceTest {
    /**
     * Проверяем работу топика с 2мя клиентами, один подписан на сообщения, другой нет.
     */
    @Test
    void whenHaveTwoClientsAndOneSubThenGivenContentOnlyOneSub() {
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
        assertThat(result1.getText()).isEqualTo("temperature=18");
        assertThat(result1.getStatus()).isEqualTo("200");
        assertThat(result2.getText()).isEmpty();
        assertThat(result2.getStatus()).isEqualTo("204");
    }

    /**
     * Проверяем, что сообщение доходят всем пользователям подписанным на топик.
     */
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
        assertThat(result1.getText()).isEqualTo("temperature=18");
        assertThat(result1.getStatus()).isEqualTo("200");
        assertThat(result2.getText()).isEqualTo("temperature=18");
        assertThat(result2.getStatus()).isEqualTo("200");
        assertThat(result3.getText()).isEmpty();
        assertThat(result3.getStatus()).isEqualTo("204");
    }

    /**
     * Проверяем, что сообщение удалилось у пользователя, а у другого осталось
     */
    @Test
    void whenGiveMoreMsgFromUserThenGivenEmptyResponse() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client777";
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
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        assertThat(result1.getText()).isEqualTo("temperature=18");
        assertThat(result1.getStatus()).isEqualTo("200");
        assertThat(result2.getText()).isEqualTo("temperature=18");
        assertThat(result2.getStatus()).isEqualTo("200");
        assertThat(result3.getText()).isEmpty();
        assertThat(result3.getStatus()).isEqualTo("204");
    }
}