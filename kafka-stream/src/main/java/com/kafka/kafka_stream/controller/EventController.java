package com.kafka.kafka_stream.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.kafka.kafka_stream.request.UserEventRequest;
import com.kafka.kafka_stream.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/events")
@Slf4j
public class EventController {



private final EventService eventService;


public EventController( EventService eventService) {
    this.eventService = eventService;
}


@PostMapping
public String publish(@RequestBody UserEventRequest req) throws JsonProcessingException {
// Serialize to JSON (KafkaTemplate uses StringSerializer)
log.info("{" +
"\"userId\":\"" + req.userId() + "\"," +
"\"action\":\"" + req.action() + "\"," +
"}");

eventService.pushMessage(req);

return "queued";
}
}