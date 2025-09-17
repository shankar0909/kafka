package com.kafka.kafka_stream.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public NewTopic inputTopic() {
        return new NewTopic("input-topic", 2, (short) 1);
    }

    @Bean
    public NewTopic outputTopic() {
        return new NewTopic("output-topic", 2, (short) 1);
    }

}
