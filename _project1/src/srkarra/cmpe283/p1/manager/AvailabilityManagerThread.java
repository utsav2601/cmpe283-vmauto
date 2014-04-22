package srkarra.cmpe283.p1.manager;

import java.util.ArrayList;
import java.util.List;

import srkarra.cmpe283.p1.config.Config;
import srkarra.cmpe283.p1.config.Utilities;
import srkarra.cmpe283.p1.config.Config.ConfigIdents;

import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

/**
 * Class: AvailabilityManagerThread
 * Responsible for watching the system on the network using 
 * a combination of VMware Power State and an ICMP Network Ping  
 * 
 * @author Sai Karra
 */
public class AvailabilityManagerThread implements Runnable {
	
	private List<String> vHostSystemBlackList;
	private List<String> virtualMachineBlackList;
	private ServiceInstance serviceInstance;
	private Thread thread;
	private long sleepInterval;
	private boolean enabled;
	
	
	/**
	 * Tnstanciate the Thread
	 */
	public AvailabilityManagerThread() {
		this.serviceInstance = Config.getServiceInstance();
		this.vHostSystemBlackList = new ArrayList<>();
		this.virtualMachineBlackList = new ArrayList<>();
		this.sleepInterval = Long.parseLong(Config.getProperty(ConfigIdents.VM_HEARTBEAT));
		enabled = true;
	}

	/**
	 * Runs the thread
	 */
	public void run() {
		while(enabled) {
			try {
				List<FailedVMPair> failedVMPair = new ArrayList<>();
				
				// Get a list of Virtual Machines
				ManagedEntity[] hostMachines = new InventoryNavigator(serviceInstance.getRootFolder()).searchManagedEntities("HostSystem");
				for (ManagedEntity managedEntity : hostMachines) {
					HostSystem hostSystem = (HostSystem) managedEntity;
					
					// Check if it's not blacklisted
					if(!vHostSystemBlackList.contains(hostSystem.getName())) {
						
						// Get a list of VirtualMachines
						VirtualMachine[] virtualMachines = hostSystem.getVms();
						for (VirtualMachine virtualMachine : virtualMachines) {
							
							// VM is failed if not blacklisted, powered on, and ping fails
							if(!virtualMachineBlackList.contains(virtualMachine.getName())
									&& Utilities.isVirtualMachinePoweredOn(virtualMachine)
									&& !Utilities.pingSystem(Utilities.getIPAddress(virtualMachine))) {
								failedVMPair.add(new FailedVMPair(hostSystem, virtualMachine));
							}
						}
					}
				}
				
				
				// Attempt to bring back Virtual Machine
				List<HostSystem> failedVHosts = new ArrayList<>();
				for (FailedVMPair failedVm : failedVMPair) {
					System.err.println("Failed VM detected: " + failedVm);
					
					// Ping vHost
					if(!failedVHosts.contains(failedVm.getHostSystem().getName()) && Utilities.pingSystem(failedVm.getHostSystem().getName())) {
						try {
							System.err.println("VHost is alive. Attempting to restore Virtual Machine");
							// Restore to last good snaphot
							if(Utilities.restoreVMToSnapshot(failedVm.getVirtualMachine(), Utilities.getLatestSnapshot(failedVm.getVirtualMachine()))) {
								System.err.println("Virtual Machine Alive!");
							}
							else {
								System.err.println("Failed to Restore Virtual Machine");
								throw new Exception();
							}
						}
						catch (Exception e) {
							addVirtualMachineToBlackList(failedVm.getVirtualMachine().getName());
						}
					} 
					else {
						try {
							System.err.println("VHost failed. Attempting to migrated Virtual Machine to new VHost");
							// Migrate the Virtual Machine to a new Host System
							HostSystem hostSystem = Utilities.getNewHostSystem(serviceInstance, failedVm.getHostSystem().getName());
							if(Utilities.migrateSnapshot(failedVm.getVirtualMachine(), hostSystem)) {
								// Check if Virtual Machine is Alive
								if(!Utilities.pingSystem(Utilities.getIPAddress(failedVm.getVirtualMachine()))) {
									System.err.println("Virtual Machine restored");
									throw new Exception();
								}
								else {
									System.err.println("Virtual Machine restored");
								}
							}
							else  {
								System.err.println("Failed to migrate virtual machine");
								throw new Exception();
							}
								
						} catch (Exception e) {
							// BlackList if there was an error
							addVirtualMachineToBlackList(failedVm.getVirtualMachine().getName());
						}
						
					}
				}
				
				VHostAvailabilityThread failedVHost = new VHostAvailabilityThread(this, failedVHosts);
				Thread failedVHostThread = new Thread(failedVHost);
				failedVHostThread.start();
				
				
				try {
					Thread.sleep(sleepInterval);
				} catch (Exception e) {}
			} catch (Exception e) {}
		}
	}
	
	/**
	 * Enable the Thread
	 */
	public void enable() {
		enabled = true;
	}
	
	/**
	 * Disable the Thread
	 */
	public void disable() {
		enabled = false;
	}
	
	/**
	 * Add a Host to the ignore list
	 * @param vHostName name of the system to ignore
	 * @return if successfully added to the list
	 */
	public synchronized boolean addVHostToBlackList(String vHostName) {
		return vHostSystemBlackList.add(vHostName);
	}
	
	/**
	 * Remove a Host to the ignore list
	 * @param vHostName name of the system to remove
	 * @return if successfully remove to the list
	 */
	public synchronized boolean removeVHostFromBlackList(String vHostName) {
		return vHostSystemBlackList.remove(vHostName);
	}
	
	/**
	 * Check whether a host system is blacklist
	 * @param vHostName name of the host system to check
	 * @return check if the host is black listed
	 */
	public boolean isVHostBlackListed(String vHostName){
		return vHostSystemBlackList.contains(vHostName);
	}
	
	
	/**
	 * Add a virtual machine to the ignore list
	 * @param virtualMachineName name of the virtual machine to ignore
	 * @return if successfully added to the list
	 */
	public synchronized boolean addVirtualMachineToBlackList(String virtualMachineName) {
		return virtualMachineBlackList.add(virtualMachineName);
	}
	
	
	/**
	 * Remove a virtual machineto the ignore list
	 * @param virtualMachineName name of the virtual machine to remove
	 * @return if successfully remove to the list
	 */
	public synchronized boolean removeVirtualMachineFromBlackList(String virtualMachineName) {
		return virtualMachineBlackList.remove(virtualMachineName);
	}
	
	/**
	 * Check whether a virtual machine is blacklist
	 * @param virtualMachineName name of the virtual machine to check
	 * @return check if the host is black listed
	 */
	public boolean isVirtualMachineBlackListed(String virtualMachineName){
		return virtualMachineBlackList.contains(virtualMachineName);
	}

	
	/**
	 * Returns the thread this runnable class is running on
	 * @return Thread instance current 
	 */
	public Thread getThread() {
		return thread;
	}
	
	/**
	 * Set the thread that this runnable is currently running on
	 * @param thread Sets the thread that should be currently returned
	 */
	public void setThread(Thread thread) {
		this.thread = thread;
	}
}
