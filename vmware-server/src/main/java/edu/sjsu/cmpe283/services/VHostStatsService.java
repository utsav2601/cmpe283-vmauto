package edu.sjsu.cmpe283.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.cmpe283.entities.VHostStats;
import edu.sjsu.cmpe283.repositories.VHostStatsRepository;

@Service
public class VHostStatsService {
    
    @Autowired
    VHostStatsRepository statsRepo;
    
    public List<VHostStats> getAllStats() {
        return statsRepo.findAll();
    }
    
    
}
