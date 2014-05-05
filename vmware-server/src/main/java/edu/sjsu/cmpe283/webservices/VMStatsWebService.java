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

import edu.sjsu.cmpe283.entities.VMStats;
import edu.sjsu.cmpe283.services.VMStatsService;

@RestController
@RequestMapping("/v2/stats/vm")
public class VMStatsWebService {
    private static Log logger = LogFactory.getLog(VMStatsWebService.class);
    
    @Autowired
    VMStatsService vmStatsService;

    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity getData() {
        
        try {
            logger.info("Get All VM Statistics");
            List<VMStats> stats = vmStatsService.getAllStats();
            return new ResponseEntity<List<VMStats>>(stats, HttpStatus.OK);
        }
        catch (Exception e) {
            logger.info("Error: " + e.getMessage());
            logger.info(e);
            return new ResponseEntity<String>(String.format("{\"err\":\"%s\"}",e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/unique", method = RequestMethod.GET)
    public ResponseEntity getUniqueVm() {
        try {
            logger.info("Get All Unique VM Names");
            return new ResponseEntity<String[]>(vmStatsService.getUniqueList(), HttpStatus.OK);
        }
        catch (Exception e) {
            logger.info("Error: " + e.getMessage());
            logger.info(e);
            return new ResponseEntity<String>(String.format("{\"err\":\"%s\"}",e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity postNewVMStats(@RequestBody(required = true) @Valid VMStats vmstats) {
        try {
            logger.info("New vm stat data: " + vmstats);
            return new ResponseEntity<VMStats>(vmStatsService.saveStats(vmstats), HttpStatus.OK);
        }
        catch (Exception e) {
            logger.info("Error: " + e.getMessage());
            logger.info(e);
            return new ResponseEntity<String>(String.format("{\"err\":\"%s\"}",e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
