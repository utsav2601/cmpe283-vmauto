package edu.sjsu.cmpe283.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.sjsu.cmpe283.entities.LogData;

public interface LogDataRepository extends MongoRepository<LogData, Long> {
    
    public List<LogData> findByVmNameAndFileName(String vmName, String filename);
}
