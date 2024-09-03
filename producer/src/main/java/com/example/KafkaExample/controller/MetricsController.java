package com.example.KafkaExample.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/metrics")
public class MetricsController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping
    public ResponseEntity<String> addMetrics(@RequestBody String message) {
        log.info(message);
        try {
            Object jsonObject = objectMapper.readValue(message, Object.class);

            String validJson = objectMapper.writeValueAsString(jsonObject);

            kafkaTemplate.send("metrics-topic", validJson);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("Successfully added metrics");
        return ResponseEntity.ok().body("Successfully added metrics");
    }

}
