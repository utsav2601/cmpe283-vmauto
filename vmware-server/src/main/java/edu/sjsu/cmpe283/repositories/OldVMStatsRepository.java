package edu.sjsu.cmpe283.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.sjsu.cmpe283.entities.OldVMStats;

public interface OldVMStatsRepository extends MongoRepository<OldVMStats, Long> {
}
