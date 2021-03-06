package com.newsletter.cstoday.slack.application;

import com.newsletter.cstoday.slack.application.event.SlackJoinEvent;
import com.newsletter.cstoday.slack.application.event.SlackMailEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SlackService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${logging.slack.webhook-uri}")
    private String slackUrl;

    @Async
    @TransactionalEventListener
    public void sendSlackJoinMessage(SlackJoinEvent slackJoinEvent) {
        sendSlackMessage(slackJoinEvent.getMessage());
    }

    @EventListener
    public void sendSlackMailMessage(SlackMailEvent slackMailEvent) {
        sendSlackMessage(slackMailEvent.getMessage());
    }

    private void sendSlackMessage(String message) {
        Map<String, String> body = new HashMap<>();
        body.put("text", message);
        restTemplate.postForObject(slackUrl, body, String.class);
    }
}
