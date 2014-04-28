package edu.sjsu.cmpe283.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.sjsu.cmpe283.entities.LogData;

public interface LogDataRepository extends MongoRepository<LogData, Long> {
}
