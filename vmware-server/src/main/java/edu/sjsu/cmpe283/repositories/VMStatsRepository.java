package edu.sjsu.cmpe283.repositories;

import java.util.List;

import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import edu.sjsu.cmpe283.entities.VMStats;

public interface VMStatsRepository extends MongoRepository<VMStats, Long> {
    
    public List<VMStats> findByVmName(String name);
    public List<VMStats> findByVmName(String name, Order order);
}
