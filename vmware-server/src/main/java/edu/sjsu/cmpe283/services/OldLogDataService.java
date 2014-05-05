package edu.sjsu.cmpe283.services;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.cmpe283.entities.OldLogData;
import edu.sjsu.cmpe283.repositories.OldLogDataRepository;

@Service
public class OldLogDataService {
    
    @Autowired
    OldLogDataRepository logRepo;
    
    public List<OldLogData> getAllStats() {
        return logRepo.findAll();
    }
    
    public OldLogData saveLogData(OldLogData logData) {
        return logRepo.save(logData);
    }
    
    public String[] getUniqueList() {
        Set<String> list = new TreeSet<String>();
        for (OldLogData log : getAllStats()) {
            list.add(log.getVmName() + "," + log.getFileName());
        }
        return list.toArray(new String[list.size()]);
    }
}
