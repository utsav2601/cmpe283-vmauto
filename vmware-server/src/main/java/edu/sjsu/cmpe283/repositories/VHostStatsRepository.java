package edu.sjsu.cmpe283.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.sjsu.cmpe283.entities.VHostStats;

public interface VHostStatsRepository extends MongoRepository<VHostStats, Long> {
}
