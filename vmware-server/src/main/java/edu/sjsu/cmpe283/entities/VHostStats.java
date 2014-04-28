package edu.sjsu.cmpe283.entities;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class VHostStats {
    
    @Id
    private String id;
    
    private String name;
    private Integer diskReadAverage;
    private Integer diskWriteAverage;
    private Integer diskTotalLantency;
    private Integer netUsageAverage;
    private Integer datastoreReadAverage;
    private Integer dataStoreWriteAverage;
    private Integer netBytesRxAverage;
    private Integer netBytesTxAverage;
    private Date timeStamp;
    
    
    public VHostStats() {
        
    }
    
    public VHostStats(String id, String name, Integer diskReadAverage, Integer diskWriteAverage, Integer diskTotalLantency, Integer netUsageAverage, Integer datastoreReadAverage, Integer dataStoreWriteAverage, Integer netBytesRxAverage,
            Integer netBytesTxAverage, Date timeStamp) {
        super();
        this.id = id;
        this.name = name;
        this.diskReadAverage = diskReadAverage;
        this.diskWriteAverage = diskWriteAverage;
        this.diskTotalLantency = diskTotalLantency;
        this.netUsageAverage = netUsageAverage;
        this.datastoreReadAverage = datastoreReadAverage;
        this.dataStoreWriteAverage = dataStoreWriteAverage;
        this.netBytesRxAverage = netBytesRxAverage;
        this.netBytesTxAverage = netBytesTxAverage;
        this.timeStamp = timeStamp;
    }

    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getDiskReadAverage() {
        return diskReadAverage;
    }
    
    public void setDiskReadAverage(Integer diskReadAverage) {
        this.diskReadAverage = diskReadAverage;
    }
    
    public Integer getDiskWriteAverage() {
        return diskWriteAverage;
    }
    
    public void setDiskWriteAverage(Integer diskWriteAverage) {
        this.diskWriteAverage = diskWriteAverage;
    }
    
    public Integer getDiskTotalLantency() {
        return diskTotalLantency;
    }
    
    public void setDiskTotalLantency(Integer diskTotalLantency) {
        this.diskTotalLantency = diskTotalLantency;
    }
    
    public Integer getNetUsageAverage() {
        return netUsageAverage;
    }
    
    public void setNetUsageAverage(Integer netUsageAverage) {
        this.netUsageAverage = netUsageAverage;
    }
    
    public Integer getDatastoreReadAverage() {
        return datastoreReadAverage;
    }
    
    public void setDatastoreReadAverage(Integer datastoreReadAverage) {
        this.datastoreReadAverage = datastoreReadAverage;
    }
    
    public Integer getDataStoreWriteAverage() {
        return dataStoreWriteAverage;
    }
    
    public void setDataStoreWriteAverage(Integer dataStoreWriteAverage) {
        this.dataStoreWriteAverage = dataStoreWriteAverage;
    }
    
    public Integer getNetBytesRxAverage() {
        return netBytesRxAverage;
    }
    
    public void setNetBytesRxAverage(Integer netBytesRxAverage) {
        this.netBytesRxAverage = netBytesRxAverage;
    }
    
    public Integer getNetBytesTxAverage() {
        return netBytesTxAverage;
    }
    
    public void setNetBytesTxAverage(Integer netBytesTxAverage) {
        this.netBytesTxAverage = netBytesTxAverage;
    }
    
    public Date getTimeStamp() {
        return timeStamp;
    }
    
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
    
}
