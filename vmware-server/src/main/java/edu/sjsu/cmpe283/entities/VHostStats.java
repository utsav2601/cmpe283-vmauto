package edu.sjsu.cmpe283.entities;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class VHostStats {
    
    @Id
    private String id;
    
    private String  name;
    private Integer diskReadAverage;
    private Integer diskWriteAverage;
    private Integer diskTotalLantency;
    private Integer netUsageAverage;
    private Integer datastoreReadAverage;
    private Integer dataStoreWriteAverage;
    private Integer netBytesRxAverage;
    private Integer netBytesTxAverage;
    private Integer diskUsageAverage;
    private Date    timeStamp;
    private Integer cpuUsage;
    private Integer memUsage;
    private Integer cpuFairness;
    private Integer memFairness;
    private Long    cpuHz;
    
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
    
    public Integer getDiskUsageAverage() {
        return diskUsageAverage;
    }
    
    public void setDiskUsageAverage(Integer diskUsageAverage) {
        this.diskUsageAverage = diskUsageAverage;
    }
    
    public Date getTimeStamp() {
        return timeStamp;
    }
    
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
    
    public Integer getCpuUsage() {
        return cpuUsage;
    }
    
    public void setCpuUsage(Integer cpuUsage) {
        this.cpuUsage = cpuUsage;
    }
    
    public Integer getMemUsage() {
        return memUsage;
    }
    
    public void setMemUsage(Integer memUsage) {
        this.memUsage = memUsage;
    }
    
    public Integer getCpuFairness() {
        return cpuFairness;
    }
    
    public void setCpuFairness(Integer cpuFairness) {
        this.cpuFairness = cpuFairness;
    }
    
    public Integer getMemFairness() {
        return memFairness;
    }
    
    public void setMemFairness(Integer memFairness) {
        this.memFairness = memFairness;
    }
    
    public Long getCpuHz() {
        return cpuHz;
    }

    public void setCpuHz(Long cpuHz) {
        this.cpuHz = cpuHz;
    }

    @Override
    public String toString() {
        return "VHostStats [id=" + id + ", name=" + name + ", diskReadAverage=" + diskReadAverage + ", diskWriteAverage=" + diskWriteAverage + ", diskTotalLantency=" + diskTotalLantency + ", netUsageAverage=" + netUsageAverage
                + ", datastoreReadAverage=" + datastoreReadAverage + ", dataStoreWriteAverage=" + dataStoreWriteAverage + ", netBytesRxAverage=" + netBytesRxAverage + ", netBytesTxAverage=" + netBytesTxAverage + ", diskUsageAverage="
                + diskUsageAverage + ", timeStamp=" + timeStamp + ", cpuUsage=" + cpuUsage + ", memUsage=" + memUsage + ", cpuFairness=" + cpuFairness + ", memFairness=" + memFairness + ", cpuHz=" + cpuHz + "]";
    }
    
}
