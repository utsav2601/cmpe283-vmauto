package edu.sjsu.cmpe283.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.sjsu.cmpe283.entities.OldVHostStats;

public interface OldVHostStatsRepository extends MongoRepository<OldVHostStats, Long> {
}
