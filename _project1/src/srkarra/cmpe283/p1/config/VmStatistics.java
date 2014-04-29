/**
 * 
 */
package srkarra.cmpe283.p1.config;

import java.util.Date;


/**
 * @author Asim Mughni
 * Object that will contain all the VM Stats with their respective getter and setters
 */
public class VmStatistics {
	
	private boolean supportsSnapShot = false;
	private Integer cpuUsage = 0;
	private Integer guestMemoryUsage = 0;
	private Integer maxHostMemory = 0;
	private String  vmName = "";
	private String  guestFullName = "";
	private String  guestIpAddress = "";
	private String  powerState = "";
	private String  systemUpTime = "";
	private String  storageUsed = "";
	private Date 	timeStamp = null;
	
	/**
	 * @return the timeStamp
	 */
	public Date getTimeStamp() {
		if(timeStamp == null)
			timeStamp = new Date();
		return timeStamp;
	}

	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getGuestIpAddress() {
		return guestIpAddress;
	}

	public void setGuestIpAddress(String guestIpAddress) {
		this.guestIpAddress = guestIpAddress;
	}

	public boolean getSupportsSnapShot() {
		return supportsSnapShot;
	}

	public void setSupportsSnapShot(boolean b) {
		this.supportsSnapShot = b;
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

	public Integer getCpuUsage() {
		return cpuUsage;
	}

	public void setCpuUsage(Integer integer) {
		this.cpuUsage = integer;
	}

	public Integer getGuestMemoryUsage() {
		return guestMemoryUsage;
	}

	public void setGuestMemoryUsage(Integer integer) {
		this.guestMemoryUsage = integer;
	}

	public Integer getMaxHostMemory() {
		return maxHostMemory;
	}

	public void setMaxHostMemory(Integer integer) {
		this.maxHostMemory = integer;
	}

	public String getStorageUsed() {
		return storageUsed;
	}

	public void setStorageUsed(String storageUsed) {
		this.storageUsed = storageUsed;
	}

	public String getGuestFullName() {
		return guestFullName;
	}

	public void setGuestFullName(String guestFullName) {
		this.guestFullName = guestFullName;
	}

	public String getVmName() {
		return vmName;
	}

	public void setVmName(String vmName) {
		this.vmName = vmName;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VmStatistics [supportsSnapShot=" + supportsSnapShot
				+ ", cpuUsage=" + cpuUsage + ", guestMemoryUsage="
				+ guestMemoryUsage + ", maxHostMemory=" + maxHostMemory
				+ ", vmName=" + vmName + ", guestFullName="
				+ guestFullName + ", guestIpAddress=" + guestIpAddress
				+ ", powerState=" + powerState + ", systemUpTime="
				+ systemUpTime + ", storageUsed=" + storageUsed
				+ ", timeStamp=" + timeStamp + "]";
	}
	
	
	
}
