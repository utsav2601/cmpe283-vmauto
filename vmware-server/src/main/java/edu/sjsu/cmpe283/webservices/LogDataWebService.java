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

import edu.sjsu.cmpe283.entities.LogData;
import edu.sjsu.cmpe283.services.LogDataService;


@RestController
@RequestMapping("/v2/stats/log")
public class LogDataWebService {
    private static Log logger = LogFactory.getLog(LogDataWebService.class);
    
    @Autowired
    LogDataService logDataService;
    
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity getData() {
        
        try {
            List<LogData> stats = logDataService.getAllStats();
            return new ResponseEntity<List<LogData>>(stats, HttpStatus.OK);
        }
        catch (Exception e) {
            logger.info("Error: " + e.getMessage());
            logger.info(e);
            return new ResponseEntity<String>(String.format("{\"err\":\"%s\"}",e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity postNewLogData(@RequestBody(required = true) @Valid LogData logdata) {
        try {
            return new ResponseEntity<LogData>(logDataService.saveLogData(logdata), HttpStatus.OK);
        }
        catch (Exception e) {
            logger.info("Error: " + e.getMessage());
            logger.info(e);
            return new ResponseEntity<String>(String.format("{\"err\":\"%s\"}",e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
