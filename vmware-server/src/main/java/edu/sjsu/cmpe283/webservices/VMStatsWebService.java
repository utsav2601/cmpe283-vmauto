package edu.sjsu.cmpe283.webservices;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.cmpe283.entities.VMStats;
import edu.sjsu.cmpe283.services.VMStatsService;

@RestController
@RequestMapping("/statistics/vm")
public class VMStatsWebService {
    private static Log logger = LogFactory.getLog(VMStatsWebService.class);
    
    @Autowired
    VMStatsService statsService;

    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity getData() {
        
        try {
            List<VMStats> stats = statsService.getAllStats();
            return new ResponseEntity<List<VMStats>>(stats, HttpStatus.OK);
        }
        catch (Exception e) {
            logger.info("Error: " + e.getMessage());
            logger.info(e);
            return new ResponseEntity<String>(String.format("{\"err\":\"%s\"}",e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
