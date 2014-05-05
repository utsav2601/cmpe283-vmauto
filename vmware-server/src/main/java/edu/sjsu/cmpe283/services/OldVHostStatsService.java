package edu.sjsu.cmpe283.services;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.cmpe283.entities.OldVHostStats;
import edu.sjsu.cmpe283.repositories.OldVHostStatsRepository;

@Service
public class OldVHostStatsService {
    
    @Autowired
    OldVHostStatsRepository statsRepo;
    
    public List<OldVHostStats> getAllStats() {
        return statsRepo.findAll();
    }
    
    public OldVHostStats saveStats(OldVHostStats vhoststats) {
        return statsRepo.save(vhoststats);
    }
    
    public String[] getUniqueList() {
        Set<String> list = new TreeSet<String>();
        for (OldVHostStats vhost : getAllStats()) {
            list.add(vhost.getName());
        }
        return list.toArray(new String[list.size()]);
    }
}
