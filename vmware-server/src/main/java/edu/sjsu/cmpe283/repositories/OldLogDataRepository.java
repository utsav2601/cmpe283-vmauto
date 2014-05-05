package edu.sjsu.cmpe283.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.sjsu.cmpe283.entities.OldLogData;

public interface OldLogDataRepository extends MongoRepository<OldLogData, Long> {

	List<OldLogData> findByVmNameAndFileName(String vmName, String fileName);
}
