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

import edu.sjsu.cmpe283.entities.OldLogData;
import edu.sjsu.cmpe283.services.OldLogDataService;


@RestController
@RequestMapping("/v1/stats/log")
public class OldLogDataWebService {
    private static Log logger = LogFactory.getLog(OldLogDataWebService.class);
    
    @Autowired
    OldLogDataService logDataService;
    
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity getData() {
        
        try {
            logger.info("Get All Old Log Statistics");
            List<OldLogData> stats = logDataService.getAllStats();
            return new ResponseEntity<List<OldLogData>>(stats, HttpStatus.OK);
        }
        catch (Exception e) {
            logger.info("Error: " + e.getMessage());
            logger.info(e);
            return new ResponseEntity<String>(String.format("{\"err\":\"%s\"}",e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/single", method = RequestMethod.GET)
    public ResponseEntity getSingleLogData(
            @RequestParam(value = "vmname", defaultValue = "t04_vm01_ubuntu32", required = false) String vmName,
            @RequestParam(value = "filename", defaultValue = "vmstat", required = false) String fileName
            ) {
        try {
            logger.info("Get All Log Files for: " + vmName + ", filename" + fileName);
            return new ResponseEntity<List<OldLogData>>(logDataService.getLogs(vmName, fileName), HttpStatus.OK);
        }
        catch (Exception e) {
            logger.info("Error: " + e.getMessage());
            logger.info(e);
            return new ResponseEntity<String>(String.format("{\"err\":\"%s\"}",e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/unique", method = RequestMethod.GET)
    public ResponseEntity getUniqueLogData() {
        try {
            logger.info("Get All Unique Log File Names per Virtual Machine");
            return new ResponseEntity<String[]>(logDataService.getUniqueList(), HttpStatus.OK);
        }
        catch (Exception e) {
            logger.info("Error: " + e.getMessage());
            logger.info(e);
            return new ResponseEntity<String>(String.format("{\"err\":\"%s\"}",e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity postNewLogData(@RequestBody(required = true) @Valid OldLogData logdata) {
        try {
            logger.info("New old stat data: " + logdata);
            return new ResponseEntity<OldLogData>(logDataService.saveLogData(logdata), HttpStatus.OK);
        }
        catch (Exception e) {
            logger.info("Error: " + e.getMessage());
            logger.info(e);
            return new ResponseEntity<String>(String.format("{\"err\":\"%s\"}",e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
