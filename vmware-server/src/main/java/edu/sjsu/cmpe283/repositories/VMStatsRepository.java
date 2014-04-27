package edu.sjsu.cmpe283.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.sjsu.cmpe283.entities.VMStats;

public interface VMStatsRepository extends MongoRepository<VMStats, Long> {
}
