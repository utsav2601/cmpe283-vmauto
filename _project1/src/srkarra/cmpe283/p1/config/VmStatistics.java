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
	private Integer diskWriteAverage = 0;
	private Integer diskTotalLantency = 0;
	private Integer netUsageAverage = 0;
	private Integer datastoreReadAverage = 0;
	private Integer dataStoreWriteAverage = 0;
	private Integer netBytesRxAverage = 0;
	private Integer netBytesTxAverage = 0;
	private Integer diskReadAverage = 0;
	private Integer diskUsageAverage = 0;
	private Date 	timeStamp = null;
	
	/**
	 * @return the diskReadAverage
	 */
	public Integer getDiskReadAverage() {
		return diskReadAverage;
	}

	/**
	 * @param diskReadAverage the diskReadAverage to set
	 */
	public void setDiskReadAverage(Integer diskReadAverage) {
		this.diskReadAverage = diskReadAverage;
	}

	/**
	 * @return the diskWriteAverage
	 */
	public Integer getDiskWriteAverage() {
		return diskWriteAverage;
	}

	/**
	 * @param diskWriteAverage the diskWriteAverage to set
	 */
	public void setDiskWriteAverage(Integer diskWriteAverage) {
		this.diskWriteAverage = diskWriteAverage;
	}

	/**
	 * @return the diskTotalLantency
	 */
	public Integer getDiskTotalLantency() {
		return diskTotalLantency;
	}

	/**
	 * @param diskTotalLantency the diskTotalLantency to set
	 */
	public void setDiskTotalLantency(Integer diskTotalLantency) {
		this.diskTotalLantency = diskTotalLantency;
	}

	/**
	 * @return the netUsageAverage
	 */
	public Integer getNetUsageAverage() {
		return netUsageAverage;
	}

	/**
	 * @param netUsageAverage the netUsageAverage to set
	 */
	public void setNetUsageAverage(Integer netUsageAverage) {
		this.netUsageAverage = netUsageAverage;
	}

	/**
	 * @return the datastoreReadAverage
	 */
	public Integer getDatastoreReadAverage() {
		return datastoreReadAverage;
	}

	/**
	 * @param datastoreReadAverage the datastoreReadAverage to set
	 */
	public void setDatastoreReadAverage(Integer datastoreReadAverage) {
		this.datastoreReadAverage = datastoreReadAverage;
	}

	/**
	 * @return the dataStoreWriteAverage
	 */
	public Integer getDataStoreWriteAverage() {
		return dataStoreWriteAverage;
	}

	/**
	 * @param dataStoreWriteAverage the dataStoreWriteAverage to set
	 */
	public void setDataStoreWriteAverage(Integer dataStoreWriteAverage) {
		this.dataStoreWriteAverage = dataStoreWriteAverage;
	}

	/**
	 * @return the netBytesRxAverage
	 */
	public Integer getNetBytesRxAverage() {
		return netBytesRxAverage;
	}

	/**
	 * @param netBytesRxAverage the netBytesRxAverage to set
	 */
	public void setNetBytesRxAverage(Integer netBytesRxAverage) {
		this.netBytesRxAverage = netBytesRxAverage;
	}

	/**
	 * @return the netBytesTxAverage
	 */
	public Integer getNetBytesTxAverage() {
		return netBytesTxAverage;
	}

	/**
	 * @param netBytesTxAverage the netBytesTxAverage to set
	 */
	public void setNetBytesTxAverage(Integer netBytesTxAverage) {
		this.netBytesTxAverage = netBytesTxAverage;
	}

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
	
	/**
	 * @param diskUsageAverage the diskUsageAverage to set
	 */
	public void setDiskUsageAverage(Integer diskUsageAverage) {
		this.diskUsageAverage = diskUsageAverage;
	}
	/**
	 * @return the diskReadAverage
	 */
	public Integer getDiskUsageAverage() {
		return diskUsageAverage;
	}

	public String getName() {
		return vmName;
	}

	public void setName(String vmName) {
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
