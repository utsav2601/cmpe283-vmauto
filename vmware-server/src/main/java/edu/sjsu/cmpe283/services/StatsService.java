package edu.sjsu.cmpe283.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.cmpe283.entities.Stats;
import edu.sjsu.cmpe283.repositories.StatsRepository;

@Service
public class StatsService {
    
    @Autowired
    StatsRepository statsRepo;
    
    public List<Stats> getAllStats() {
        return statsRepo.findAll();
    }
    
    
}
