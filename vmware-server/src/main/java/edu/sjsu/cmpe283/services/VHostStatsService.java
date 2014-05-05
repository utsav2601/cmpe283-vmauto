package edu.sjsu.cmpe283.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
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
    
    public VHostStats saveStats(VHostStats vhoststats) {
        return statsRepo.save(vhoststats);
    }

    public String[] getUniqueList() {
        Set<String> list = new TreeSet<String>();
        for (VHostStats vhost : getAllStats()) {
            list.add(vhost.getName());
        }
        return list.toArray(new String[list.size()]);
    }
    
    
    public List<VHostStats> getLastKnownState() {
        List<VHostStats> list = new ArrayList<VHostStats>();
        for (String host : getUniqueList()) {
            list.add(statsRepo.findByName(host, new Order(Direction.DESC, "timeStamp")).get(0));
        }
        return list;
    }
}
