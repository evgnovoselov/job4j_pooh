package ru.job4j.pooh;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TopicServiceTest {
    /**
     * Проверяем работу топика с 2мя клиентами, один подписан на сообщения, другой нет.
     */
    @Test
    public void whenHaveTwoClientsAndOneSubThenGivenContentOnlyOneSub() {
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
    public void whenTopicTwoPublishersThenHaveTwoAnswer() {
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
    public void whenGiveMoreMsgFromUserThenGivenEmptyResponse() {
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

    /**
     * Когда неверный тип запроса, получаем ответ со статусом 501.
     */
    @Test
    public void whenBadRequestTypeThenRespStatus501() {
        TopicService topicService = new TopicService();
        Resp result = topicService.process(new Req("TTT", "topic", "weather", "cl"));
        assertThat(result.getText()).isEmpty();
        assertThat(result.getStatus()).isEqualTo("501");
    }

    /**
     * Смотрим какой ответ мы получили при добавлении значения.
     */
    @Test
    public void whenSendReqThenGetResp() {
        TopicService topicService = new TopicService();
        Resp result = topicService.process(new Req("POST", "topic", "weather", "temperature=18"));
        assertThat(result.getText()).isEqualTo("temperature=18");
        assertThat(result.getStatus()).isEqualTo("200");
    }
}