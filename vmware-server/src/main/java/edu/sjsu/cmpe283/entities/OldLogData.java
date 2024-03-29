package edu.sjsu.cmpe283.entities;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class OldLogData {
    
    @Id
    private String id;
    
    private Date timeStamp;
    private String vmName;
    private String fileName;
    private String fileContent;
    
    public OldLogData() {
    
    }

    public OldLogData(String id, Date timeStamp, String vmName, String fileName, String fileContent) {
        super();
        this.id = id;
        this.timeStamp = timeStamp;
        this.vmName = vmName;
        this.fileName = fileName;
        this.fileContent = fileContent;
    }

    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public Date getTimeStamp() {
        return timeStamp;
    }
    
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
    
    public String getVmName() {
        return vmName;
    }
    
    public void setVmName(String vmName) {
        this.vmName = vmName;
    }

    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileContent() {
        return fileContent;
    }
    
    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }
    
}
