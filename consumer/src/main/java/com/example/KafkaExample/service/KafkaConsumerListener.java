package com.example.KafkaExample.service;

import com.mongodb.BasicDBObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerListener {

    @Autowired
    private MongoTemplate mongoTemplate;

    @KafkaListener(id = "metricsTopic", topics = "metrics-topic")
    public void listen(String message) {
        log.info("Received message: " + message);
        try {
            if (isValidJson(message)) {
                BasicDBObject dbObject = BasicDBObject.parse(message);
                mongoTemplate.save(dbObject, "metrics");
            } else {
                log.error("Invalid JSON format: " + message);
            }
        } catch (Exception e) {
            log.error("Failed to save message to MongoDB: " + e.getMessage());
        }
    }

    private boolean isValidJson(String message) {
        try {
            BasicDBObject.parse(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
