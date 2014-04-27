package edu.sjsu.cmpe283.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.cmpe283.entities.VMStats;
import edu.sjsu.cmpe283.repositories.VMStatsRepository;

@Service
public class VMStatsService {
    
    @Autowired
    VMStatsRepository statsRepo;
    
    public List<VMStats> getAllStats() {
        return statsRepo.findAll();
    }
    
    
    public VMStats saveStats(VMStats vmstats) {
        return statsRepo.save(vmstats);
    }
    
    
}
