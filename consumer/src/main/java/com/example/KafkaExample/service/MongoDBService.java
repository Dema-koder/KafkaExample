package com.example.KafkaExample.service;

import com.mongodb.BasicDBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MongoDBService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public BasicDBObject getDocumentById(String collectionName, String id) {
        return mongoTemplate.findById(id, BasicDBObject.class, collectionName);
    }

    public List<BasicDBObject> getAllDocuments(String collectionName) {
        return mongoTemplate.findAll(BasicDBObject.class, collectionName);
    }
}
