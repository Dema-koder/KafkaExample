package com.example.KafkaExample.api;

import com.example.KafkaExample.service.MongoDBService;
import com.mongodb.BasicDBObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/metrics")
public class MetricsController {

    private static final String COLLECTION_NAME = "metrics";

    @Autowired
    private MongoDBService mongoDBService;

    @GetMapping
    public ResponseEntity<String> getAllMetrics() {
        return ResponseEntity.ok(mongoDBService.getAllDocuments(COLLECTION_NAME).stream()
                .map(BasicDBObject::toString)
                .reduce((a, b) -> a + "\n" + b).get());
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getMetricsById(@PathVariable String id) {
        log.info("Received request to retrieve metrics by id: {}", id);
        return ResponseEntity.ok(mongoDBService.getDocumentById(COLLECTION_NAME, id).toString());
    }
}

