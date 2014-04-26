/**
 * 
 */
package srkarra.cmpe283.p1.config;

/**
 * @author Asim Mughni
 * Object that will contain all the VM Stats with their respective getter and setters
 */
public class VmStatistics {
	
	private static boolean s_supportsSnapShot = false;
	private static Integer s_cpuUsage = 0;
	private static Integer s_guestMemoryUsage = 0;
	private static Integer s_maxHostMemory = 0;
	private static String  s_vmName = "";
	private static String  s_guestFullName = "";
	private static String  s_guestIpAddress = "";
	private static String  s_powerState = "";
	private static String  s_systemUpTime = "";
	private static String  s_storageUsed = "";
	
	public  String getGuestIpAddress() {
		return s_guestIpAddress;
	}

	public  void setGuestIpAddress(String guestIpAddress) {
		VmStatistics.s_guestIpAddress = guestIpAddress;
	}

	public  boolean getSupportsSnapShot() {
		return s_supportsSnapShot;
	}

	public  void setSupportsSnapShot(boolean b) {
		VmStatistics.s_supportsSnapShot = b;
	}

	public  String getPowerState() {
		return s_powerState;
	}

	public  void setPowerState(String powerState) {
		VmStatistics.s_powerState = powerState;
	}

	public  String getSystemUpTime() {
		return s_systemUpTime;
	}

	public  void setSystemUpTime(String systemUpTime) {
		VmStatistics.s_systemUpTime = systemUpTime;
	}

	public  Integer getCpuUsage() {
		return s_cpuUsage;
	}

	public  void setCpuUsage(Integer integer) {
		VmStatistics.s_cpuUsage = integer;
	}

	public  Integer getGuestMemoryUsage() {
		return s_guestMemoryUsage;
	}

	public  void setGuestMemoryUsage(Integer integer) {
		VmStatistics.s_guestMemoryUsage = integer;
	}

	public  Integer getMaxHostMemory() {
		return s_maxHostMemory;
	}

	public  void setMaxHostMemory(Integer integer) {
		VmStatistics.s_maxHostMemory = integer;
	}

	public  String getStorageUsed() {
		return s_storageUsed;
	}

	public  void setStorageUsed(String storageUsed) {
		VmStatistics.s_storageUsed = storageUsed;
	}

	public  String getGuestFullName() {
		return s_guestFullName;
	}

	public  void setGuestFullName(String guestFullName) {
		VmStatistics.s_guestFullName = guestFullName;
	}

	public  String getVmName() {
		return s_vmName;
	}

	public  void setVmName(String vmName) {
		VmStatistics.s_vmName = vmName;
	}
}
