package edu.sjsu.cmpe283.services;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import edu.sjsu.cmpe283.entities.VMStats;
import edu.sjsu.cmpe283.repositories.VMStatsRepository;

@Service
public class VMStatsService {
    
    @Autowired
    VMStatsRepository statsRepo;
    
    public List<VMStats> getAllStats() {
        return statsRepo.findAll(new Sort("timeStamp"));
    }
    
    
    public VMStats saveStats(VMStats vmstats) {
        return statsRepo.save(vmstats);
    }
    
    
    public String[] getUniqueList() {
        Set<String> list = new TreeSet<String>();
        for (VMStats vm : getAllStats()) {
            list.add(vm.getVmName());
        }
        return list.toArray(new String[list.size()]);
    }
    
    
    
    
    
}
