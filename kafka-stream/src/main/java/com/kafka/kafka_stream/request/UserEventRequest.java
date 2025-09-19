package com.kafka.kafka_stream.request;

public record UserEventRequest(String userId,
                               String action) {
}
