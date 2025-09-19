package com.kafka.kafka_stream.topology;


import java.util.Map;
import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kafka.kafka_stream.model.EnrichedUserEvent;
import com.kafka.kafka_stream.model.UserEvent;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class GreetingsTopology {

    public static String INPUT_TOPIC = "input-topic";
    public static String OUTPUT_TOPIC = "output-topic";


    @Autowired
    public void process(StreamsBuilder builder) {
        log.info("Building Kafka Stream Topology");

        // Reusable Avro Serdes
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-stream-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put("schema.registry.url", "http://localhost:8081");

        //TODO: Fix below for custome Avro Serde
        // Serde<UserEvent> userEventSerde = new AvroSerde<>(UserEvent.class, props);
        // Serde<EnrichedUserEvent> enrichedSerde = new AvroSerde<>(EnrichedUserEvent.class, props);


        var inputStream = builder.stream(INPUT_TOPIC, Consumed.with(Serdes.String(), userEventSerde(props)));

        inputStream.peek((key, value) -> log.info("Key : {} , Value : {}", key, value));

        KStream<String, EnrichedUserEvent> enriched = inputStream.mapValues(event ->
                EnrichedUserEvent.newBuilder()
                        .setUserId(event.getUserId())
                        .setAction(event.getAction())
                        .setTimestamp(System.currentTimeMillis())
                        .setSource("streams-app")
                        .build()
        );

        enriched.peek((key, value) -> log.info("Enriched Key : {} , Enriched Value : {}", key, value));

        enriched.to(OUTPUT_TOPIC, Produced.with(Serdes.String(), enrichedSerde(props))); //We can do all this in one line also
        log.info("Topology Built Successfully");
    }


    // helper methods
    private static SpecificAvroSerde<UserEvent> userEventSerde(Properties props) {
        SpecificAvroSerde<UserEvent> serde = new SpecificAvroSerde<>();
        serde.configure(
                Map.of("schema.registry.url", props.getProperty("schema.registry.url")),
                false // 'false' = value (not key)
        );
        return serde;
    }

    private static SpecificAvroSerde<EnrichedUserEvent> enrichedSerde(Properties props) {
        SpecificAvroSerde<EnrichedUserEvent> serde = new SpecificAvroSerde<>();
        serde.configure(
                Map.of("schema.registry.url", props.getProperty("schema.registry.url")),
                false
        );
        return serde;
    }
}
