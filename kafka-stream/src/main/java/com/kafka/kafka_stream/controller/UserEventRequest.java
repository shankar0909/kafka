package com.kafka.kafka_stream.controller;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserEventRequest {
    private String userId;
    private String action;
}

