package edu.sjsu.cmpe283.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.sjsu.cmpe283.entities.OldLogData;

public interface OldLogDataRepository extends MongoRepository<OldLogData, Long> {
}
