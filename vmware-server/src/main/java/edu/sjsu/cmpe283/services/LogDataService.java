package edu.sjsu.cmpe283.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.cmpe283.entities.LogData;
import edu.sjsu.cmpe283.repositories.LogDataRepository;

@Service
public class LogDataService {
    
    @Autowired
    LogDataRepository logRepo;
    
    public List<LogData> getAllStats() {
        return logRepo.findAll();
    }
    
    public LogData saveLogData(LogData logData) {
        return logRepo.save(logData);
    }
}
