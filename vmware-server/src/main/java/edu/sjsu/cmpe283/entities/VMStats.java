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
    private String vmName;
    private String guestFullName;
    private String guestIpAddress;
    private String powerState;
    private String systemUpTime;
    private String storageUsed;
    private Date timeStamp;
    
    
    public VMStats() {
        
    }
    
    public VMStats(String id, boolean supportsSnapShot, Integer cpuUsage, Integer guestMemoryUsage, Integer maxHostMemory, String vmName, String guestFullName, String guestIpAddress, String powerState, String systemUpTime,
            String storageUsed, Date timeStamp) {
        super();
        this.id = id;
        this.supportsSnapShot = supportsSnapShot;
        this.cpuUsage = cpuUsage;
        this.guestMemoryUsage = guestMemoryUsage;
        this.maxHostMemory = maxHostMemory;
        this.vmName = vmName;
        this.guestFullName = guestFullName;
        this.guestIpAddress = guestIpAddress;
        this.powerState = powerState;
        this.systemUpTime = systemUpTime;
        this.storageUsed = storageUsed;
        this.timeStamp = timeStamp;
    }

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
    
    public Date getTimeStamp() {
        return timeStamp;
    }
    
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
