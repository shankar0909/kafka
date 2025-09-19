package com.kafka.kafka_stream.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.kafka_stream.request.UserEventRequest;
import com.kafka.kafka_stream.transformer.EventTransformer;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class EventService {


    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private EventTransformer transformer;


    public void pushMessage(UserEventRequest request) throws JsonProcessingException {
        GenericRecord gr = transformer.toGenericRecord("input-topic-value", null, new ObjectMapper().writeValueAsString(request));
        ProducerRecord pr = new ProducerRecord<>("input-topic", gr);
        kafkaTemplate.send(pr);

    }

}
