package edu.sjsu.cmpe283.entities;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class VMStats {
    
    @Id
    private String id;
    
    private boolean supportsSnapShot;
    private Integer cpuUsage;
    private Integer guestMemoryUsage;
    private Integer maxHostMemory;
    private String  vmName;
    private String  guestFullName;
    private String  guestIpAddress;
    private String  powerState;
    private String  systemUpTime;
    private String  storageUsed;
    private Integer diskWriteAverage;
    private Integer diskTotalLantency;
    private Integer netUsageAverage;
    private Integer datastoreReadAverage;
    private Integer dataStoreWriteAverage;
    private Integer netBytesRxAverage;
    private Integer netBytesTxAverage;
    private Integer diskReadAverage;
    private Integer diskUsageAverage;
    private Date    timeStamp;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public boolean isSupportsSnapShot() {
        return supportsSnapShot;
    }
    
    public void setSupportsSnapShot(boolean supportsSnapShot) {
        this.supportsSnapShot = supportsSnapShot;
    }
    
    public Integer getCpuUsage() {
        return cpuUsage;
    }
    
    public void setCpuUsage(Integer cpuUsage) {
        this.cpuUsage = cpuUsage;
    }
    
    public Integer getGuestMemoryUsage() {
        return guestMemoryUsage;
    }
    
    public void setGuestMemoryUsage(Integer guestMemoryUsage) {
        this.guestMemoryUsage = guestMemoryUsage;
    }
    
    public Integer getMaxHostMemory() {
        return maxHostMemory;
    }
    
    public void setMaxHostMemory(Integer maxHostMemory) {
        this.maxHostMemory = maxHostMemory;
    }
    
    public String getVmName() {
        return vmName;
    }
    
    public void setVmName(String vmName) {
        this.vmName = vmName;
    }
    
    public String getGuestFullName() {
        return guestFullName;
    }
    
    public void setGuestFullName(String guestFullName) {
        this.guestFullName = guestFullName;
    }
    
    public String getGuestIpAddress() {
        return guestIpAddress;
    }
    
    public void setGuestIpAddress(String guestIpAddress) {
        this.guestIpAddress = guestIpAddress;
    }
    
    public String getPowerState() {
        return powerState;
    }
    
    public void setPowerState(String powerState) {
        this.powerState = powerState;
    }
    
    public String getSystemUpTime() {
        return systemUpTime;
    }
    
    public void setSystemUpTime(String systemUpTime) {
        this.systemUpTime = systemUpTime;
    }
    
    public String getStorageUsed() {
        return storageUsed;
    }
    
    public void setStorageUsed(String storageUsed) {
        this.storageUsed = storageUsed;
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
    
    public Integer getDiskReadAverage() {
        return diskReadAverage;
    }
    
    public void setDiskReadAverage(Integer diskReadAverage) {
        this.diskReadAverage = diskReadAverage;
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
}
