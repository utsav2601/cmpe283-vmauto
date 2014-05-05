/**
 * 
 */
package srkarra.cmpe283.p1.config;

import java.util.Date;

/**
 * @author Asim Mughni
 *
 */
public class HostStatistics {
	
	private String  name = null;
	private Integer diskReadAverage = 0;
	private Integer diskWriteAverage = 0;
	private Integer diskTotalLantency = 0;
	private Integer netUsageAverage = 0;
	private Integer datastoreReadAverage = 0;
	private Integer dataStoreWriteAverage = 0;
	private Integer netBytesRxAverage = 0;
	private Integer netBytesTxAverage = 0;
	private Integer diskUsageAverage = 0;
	private Integer cpuUsage = 0;
	private Integer memUsage = 0;
	private Integer cpuFairness = 0;
	private Integer memFairness = 0;
	private long 	cpuHz = 0;
	private Date 	timeStamp = null;
	
	/**
	 * @return the cpuHz
	 */
	public long getCpuHz() {
		return cpuHz;
	}
	/**
	 * @param cpuHz the cpuHz to set
	 */
	public void setCpuHz(long cpuHz) {
		this.cpuHz = cpuHz;
	}
	
	/**
	 * @return the cpuUsage
	 */
	public Integer getCpuUsage() {
		return cpuUsage;
	}
	/**
	 * @param cpuUsage the cpuUsage to set
	 */
	public void setCpuUsage(Integer cpuUsage) {
		this.cpuUsage = cpuUsage;
	}
	/**
	 * @return the memUsage
	 */
	public Integer getMemUsage() {
		return memUsage;
	}
	/**
	 * @param memUsage the memUsage to set
	 */
	public void setMemUsage(Integer memUsage) {
		this.memUsage = memUsage;
	}
	/**
	 * @return the cpuFairness
	 */
	public Integer getCpuFairness() {
		return cpuFairness;
	}
	/**
	 * @param cpuFairness the cpuFairness to set
	 */
	public void setCpuFairness(Integer cpuFairness) {
		this.cpuFairness = cpuFairness;
	}
	/**
	 * @return the memFairness
	 */
	public Integer getMemFairness() {
		return memFairness;
	}
	/**
	 * @param memFairness the memFairness to set
	 */
	public void setMemFairness(Integer memFairness) {
		this.memFairness = memFairness;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the diskUsageAverage
	 */
	public Integer getDiskUsageAverage() {
		return diskUsageAverage;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HostStatistics [name=" + name + ", diskReadAverage="
				+ diskReadAverage + ", diskWriteAverage=" + diskWriteAverage
				+ ", diskTotalLantency=" + diskTotalLantency
				+ ", netUsageAverage=" + netUsageAverage
				+ ", datastoreReadAverage=" + datastoreReadAverage
				+ ", dataStoreWriteAverage=" + dataStoreWriteAverage
				+ ", netBytesRxAverage=" + netBytesRxAverage
				+ ", netBytesTxAverage=" + netBytesTxAverage
				+ ", diskUsageAverage=" + diskUsageAverage + ", cpuUsage="
				+ cpuUsage + ", memUsage=" + memUsage + ", cpuFairness="
				+ cpuFairness + ", memFairness=" + memFairness + ", cpuHz="
				+ cpuHz + ", timeStamp=" + timeStamp + "]";
	}
	
	
}
