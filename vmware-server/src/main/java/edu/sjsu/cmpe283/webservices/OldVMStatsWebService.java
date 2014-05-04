package edu.sjsu.cmpe283.webservices;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.cmpe283.entities.OldVMStats;
import edu.sjsu.cmpe283.services.OldVMStatsService;

@RestController
@RequestMapping("/v1/stats/vm")
public class OldVMStatsWebService {
    private static Log logger = LogFactory.getLog(OldVMStatsWebService.class);
    
    @Autowired
    OldVMStatsService vmStatsService;

    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity getData() {
        
        try {
            List<OldVMStats> stats = vmStatsService.getAllStats();
            return new ResponseEntity<List<OldVMStats>>(stats, HttpStatus.OK);
        }
        catch (Exception e) {
            logger.info("Error: " + e.getMessage());
            logger.info(e);
            return new ResponseEntity<String>(String.format("{\"err\":\"%s\"}",e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity postNewVMStats(@RequestBody(required = true) @Valid OldVMStats vmstats) {
        try {
            return new ResponseEntity<OldVMStats>(vmStatsService.saveStats(vmstats), HttpStatus.OK);
        }
        catch (Exception e) {
            logger.info("Error: " + e.getMessage());
            logger.info(e);
            return new ResponseEntity<String>(String.format("{\"err\":\"%s\"}",e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
