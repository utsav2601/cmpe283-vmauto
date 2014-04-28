/**
 * 
 */
package srkarra.cmpe283.p1.config;

import java.util.Date;

/**
 * @author Asim Mughni
 *
 */
public class LogStatistics {
	
	private Date 	timeStamp   = null;
	private String	vmName		= null;
	private String 	fileName	= null;
	private String	fileContent	= null;
	
	/**
	 * @return the vmName
	 */
	public String getVmName() {
		return vmName;
	}

	/**
	 * @param vmName the vmName to set
	 */
	public void setVmName(String vmName) {
		this.vmName = vmName;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the fileContent
	 */
	public String getFileContent() {
		return fileContent;
	}

	/**
	 * @param fileContent the fileContent to set
	 */
	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
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
		return "LogStatistics [timeStamp=" + timeStamp + ", vmName=" + vmName
				+ ", fileName=" + fileName + ", fileContent=" + fileContent
				+ "]";
	}
	
	
}
