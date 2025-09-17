package com.kafka.kafka_stream.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kafka.kafka_stream.model.UserEvent;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EventController {

    @Autowired
private KafkaTemplate<String, UserEvent> kafkaTemplate;


    @PostMapping("/send")
public void sendEvent(@RequestBody UserEventRequest request) {
    log.info("Received event: {}", request);
    UserEvent event = UserEvent.newBuilder()
            .setUserId(request.getUserId())
            .setAction(request.getAction())
            .build();

    kafkaTemplate.send("input-topic", (String) event.getUserId(), event);
}
}


