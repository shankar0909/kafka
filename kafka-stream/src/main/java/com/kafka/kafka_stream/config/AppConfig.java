package com.kafka.kafka_stream.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public NewTopic greetingSourceTopic() {
        return new NewTopic("greetings-source", 2, (short) 1);
    }

    @Bean
    public NewTopic greetingOutputTopic() {
        return new NewTopic("greetings-output", 2, (short) 1);
    }

}
