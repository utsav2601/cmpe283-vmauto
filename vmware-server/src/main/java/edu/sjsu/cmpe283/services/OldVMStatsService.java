package edu.sjsu.cmpe283.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.cmpe283.entities.OldVMStats;
import edu.sjsu.cmpe283.repositories.OldVMStatsRepository;

@Service
public class OldVMStatsService {
    
    @Autowired
    OldVMStatsRepository statsRepo;
    
    public List<OldVMStats> getAllStats() {
        return statsRepo.findAll();
    }
    
    
    public OldVMStats saveStats(OldVMStats vmstats) {
        return statsRepo.save(vmstats);
    }
    
    
}
