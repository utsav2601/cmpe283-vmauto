package edu.sjsu.cmpe283.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.sjsu.cmpe283.entities.Stats;

public interface StatsRepository extends MongoRepository<Stats, Long> {
}
