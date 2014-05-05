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

import edu.sjsu.cmpe283.entities.OldVHostStats;
import edu.sjsu.cmpe283.services.OldVHostStatsService;

@RestController
@RequestMapping("/v1/stats/vhost")
public class OldVHostStatsWebService {
    private static Log logger = LogFactory.getLog(OldVHostStatsWebService.class);
    
    @Autowired
    OldVHostStatsService vHostStatsService;

    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity getData() {
        
        try {
            logger.info("Get All Old VHost Statistics");
            List<OldVHostStats> stats = vHostStatsService.getAllStats();
            return new ResponseEntity<List<OldVHostStats>>(stats, HttpStatus.OK);
        }
        catch (Exception e) {
            logger.info("Error: " + e.getMessage());
            logger.info(e);
            return new ResponseEntity<String>(String.format("{\"err\":\"%s\"}",e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity postNewVHostStats(@RequestBody(required = true) @Valid OldVHostStats vhoststats) {
        try {
            logger.info("New old vhost stat data: " + vhoststats);
            return new ResponseEntity<OldVHostStats>(vHostStatsService.saveStats(vhoststats), HttpStatus.OK);
        }
        catch (Exception e) {
            logger.info("Error: " + e.getMessage());
            logger.info(e);
            return new ResponseEntity<String>(String.format("{\"err\":\"%s\"}",e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
