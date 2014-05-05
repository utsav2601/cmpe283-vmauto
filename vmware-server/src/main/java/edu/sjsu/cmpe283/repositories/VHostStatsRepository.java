package edu.sjsu.cmpe283.repositories;

import java.util.List;

import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import edu.sjsu.cmpe283.entities.VHostStats;

public interface VHostStatsRepository extends MongoRepository<VHostStats, Long> {
    
    public List<VHostStats> findByName(String name);
    public List<VHostStats> findByName(String name, Order order);
}
