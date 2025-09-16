package com.kafka.kafka_stream.topology;


import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GreetingsTopology {

    public static String GREETINGS_SOURCE_TOPIC = "greetings-source";
    public static String GREETINGS_OUTPUT_TOPIC = "greetings-output";



    @Autowired
    public void process(StreamsBuilder builder) {
        log.info("Building Kafka Stream Topology");
     var greetingStream =    builder.stream(GREETINGS_SOURCE_TOPIC, Consumed.with(Serdes.String(), Serdes.String()));

     greetingStream.peek((key, value) -> log.info("Key : {} , Value : {}", key, value));

     var modifiedGreetingStream = greetingStream.mapValues((readOnly, value) -> value.toUpperCase());

     modifiedGreetingStream.peek((key, value) -> log.info("Modified Key : {} , Modified Value : {}", key, value));

     modifiedGreetingStream.to(GREETINGS_OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.String())); //We can do all this in one line also
        log.info("Topology Built Successfully");
    }
    
}
