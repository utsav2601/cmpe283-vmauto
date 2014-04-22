package srkarra.cmpe283.p1.manager;

import java.util.ArrayList;
import java.util.List;

import srkarra.cmpe283.p1.config.Config;
import srkarra.cmpe283.p1.config.Utilities;
import srkarra.cmpe283.p1.config.Config.ConfigIdents;

import com.vmware.vim25.mo.HostSystem;

/**
 * Class: VHostAvailabilityThread
 * 
 * Responsible for trying to revive 
 * the Host Systems that have failed
 * 
 * @author Sai Karra
 */
public class VHostAvailabilityThread implements Runnable {
	
	private AvailabilityManagerThread availabilityManagerThread; 
	private List<HostSystem> failedVHosts;
	
	/**
	 * Instanciate the thread
	 * @param availabilityManagerThread reference to the manager that currently holds the blacklisted hosts and vms 
	 * @param failedVHosts host systems to attempt to revive
	 */
	public VHostAvailabilityThread(AvailabilityManagerThread availabilityManagerThread, List<HostSystem> failedVHosts) {
		this.availabilityManagerThread = availabilityManagerThread;
		this.failedVHosts = failedVHosts;
	}
	
	/**
	 * Run the thread
	 */
	public void run() {
		if(failedVHosts.size() > 0) {
			System.err.println("Attempting to restore failed VHosts!");
			int sleepInterval =  Integer.parseInt(Config.getProperty(ConfigIdents.VHOST_REVIVE_TIMEOUT));
			
			for (int i = 0; i < Integer.parseInt(Config.getProperty(ConfigIdents.VHOST_REVIVE_ATTEMPTS)) && failedVHosts.size() > 0; i++) {
				System.err.println("Attempt # " + i + " to bring back VHosts");
				List<HostSystem> successfulHostSystems = new ArrayList<>();
				
				// Attempt to biring back host system
				for (HostSystem failedVHost : failedVHosts) {
					try {
						if(Utilities.powerOnVHost(failedVHost, sleepInterval)) {
							System.err.println("Successfully powered on vHost: " + failedVHost.getName());
							successfulHostSystems.add(failedVHost);
							availabilityManagerThread.removeVHostFromBlackList(failedVHost.getName());
						}
					} catch (Exception e) {}
				}
				
				// Remove Powered On Host Systems
				for (HostSystem hostSystem : successfulHostSystems) {
					failedVHosts.remove(hostSystem);
				}
			}
			
			// Log the failed one, they are already blacklisted
			if(failedVHosts.size() > 0) {
				String list = "";
				for (HostSystem hostSystem : failedVHosts) {
					list +=  hostSystem.getName() + ", ";
				}
				System.err.println("Maximum Attempts to revive the following vhosts reached: " + list);
			}
		}
	}
}
