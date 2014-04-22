package srkarra.cmpe283.p1.manager;

import java.util.Scanner;
import java.util.Timer;

import srkarra.cmpe283.p1.config.Config;
import srkarra.cmpe283.p1.config.Utilities;
import srkarra.cmpe283.p1.config.Config.ConfigIdents;

import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

/**
 * Class: UserPrompt
 * 
 * Responsible for managing the system threads
 * for Availability and Backup Cache. It is also
 * resposnible for interacting with the user to 
 * complete any necessary tasks such as maintenance.
 * 
 * @author Sai Karra
 *
 */
public class UserPrompt {
	
	private ServiceInstance serviceInstance;
	private AvailabilityManagerThread availabilityManagerThread;
	private RefreshBackupCacheThread refreshBackupCacheThread;
	
	/**
	 * Instanciate the User Prompt class
	 * Also creates and starts the backup 
	 * cache thread and the availability thread 
	 */
	public UserPrompt() {
		serviceInstance = Config.getServiceInstance();
		
		availabilityManagerThread = new AvailabilityManagerThread();
		Thread thread = new Thread(availabilityManagerThread);
		thread.start();
		
		long sleepInterval = Long.parseLong(Config.getProperty(ConfigIdents.VM_SNAPSHOT_INTERVAL)) * 1000;
		RefreshBackupCacheThread backupthread = new RefreshBackupCacheThread();
		Timer timer = new Timer();
		backupthread.setTimer(timer);
		timer.schedule(backupthread, sleepInterval);
	}
	
	
	/**
	 * Start the prompt to the user
	 */
	public void enterUserPrompt() {
		while(true) {
			try {
				System.out.print(getCommandList());
				System.out.flush();
				System.err.flush();
				Scanner console = new Scanner(System.in);
				
				String useroptionString = console.nextLine();
				int useroption = Integer.parseInt(useroptionString.replaceAll("\\s+", ""));
				
				System.out.println();
				
				switch (useroption) {
					case 0:
						restartAvailabilityThread();
						break;
					case 1:
						printAllVMInfo();
						break;
					case 2:
						printVMInformation(console);
						break;
					case 3:
						snapshotAllActiveSystems();
						break;
					case 4:
						blackListAHostSystem(console);
						break;
					case 5:
						blackListAVirtualMachine(console);
						break;
					case 6:
						removeBlackListedHostSystem(console);
						break;
					case 7:
						removeBlackListedVirtualMachine(console);
						break;
					case 8:
						createPowerOnActionTrigger(console);
						break;
					case 9:
						removePowerOnActionTrigger(console);
						break;
					case 10:
						callExit();
						break;
					default:
						console.close();
						throw new Exception();
				}
				console.close();
			}
			catch (Exception e) {
				System.out.println("Error: " + e.getMessage() );
			}
		}
	}
	
	
	
	/**
	 * Returns the valid command list
	 * @return the valid command list
	 */
	private String getCommandList() {
		return "\n\n\nUser Options:                                        \n" + 
				"   0) Start Availability Manager                           \n" +
				"   1) Print Information for all Virtual Machine            \n" + 
				"   2) Search and Print Information for VMs                 \n" + 
				"   3) Force Refresh the backup cache on all powered on VMs \n" +
				"   4) Blacklist a Host System                              \n" +
				"   5) Blacklist a Virtual Machine                          \n" +
				"   6) Remove Host System from Blacklist                    \n" +
				"   7) Remove Virtual Machine from Blacklist                \n" +
				"   8) Create a Power On Trigger for Powered Off System     \n" +
				"   9) Remove Power On Trigger from a Virtual Machine       \n" +
				"   10) Exit the System                                     \n" +
				"                                                           \n" +
				"Select an option (the number): ";
	}
	
	
	/**
	 * Restarts the availability thread
	 * Note this wipes the ignore list
	 */
	private void restartAvailabilityThread() {
		if(availabilityManagerThread == null) {
			System.out.println("Stopping Current Instance!");
			availabilityManagerThread.disable();
			try {
				availabilityManagerThread.getThread().join();
			} catch (Exception e) {}
		}
		System.out.println("Starting Availability Manager");
		availabilityManagerThread = new AvailabilityManagerThread();
		Thread thread = new Thread(availabilityManagerThread);
		thread.start();
		System.out.println("Availability Manager Started");
	}
	
	
	/**
	 * Prints the information all vms
	 */
	private void printAllVMInfo() {
		try {
			ManagedEntity[] mes = new InventoryNavigator(serviceInstance.getRootFolder()).searchManagedEntities(Config.VMWARE_IDENTIFIER_VIRTUAL_MACHINE);
			for (ManagedEntity managedEntity : mes) {
				VirtualMachine vm = (VirtualMachine) managedEntity;
				System.out.println(Utilities.getVmStatistics(vm));
			}
		}
		catch (Exception e) {
			System.out.println("Failed to fetch statistics. Error: " + e.getMessage());
		}
	}
	
	
	/**
	 * Prints information of the specified virtual machines
	 * @param console Scanner class that is used to interact with the user
	 */
	private void printVMInformation(Scanner console) {
		try {
			System.out.print("Enter vm name qualifier: ");
			String vmqualifier = console.nextLine();
			vmqualifier = vmqualifier.replaceAll("\\s+", "");
			
			for (VirtualMachine vm : Utilities.findVMsByName(serviceInstance, vmqualifier)) {
				System.out.println(Utilities.getVmStatistics(vm));
			}
		}
		catch (Exception e) {
			System.out.println("Failed to fetch statistics for requested vms. Error: " + e.getMessage());
		}
		
	}
	
	
	/**
	 * Snapshots all active systems
	 */
	private void snapshotAllActiveSystems() {
		try {
			Utilities.createASnapShotOnAllVMs(serviceInstance);
			System.out.println("Snapshots for all powered on vms created");
		} catch (Exception e) {
			System.out.println("Failed to create snapshots, Error: " + e.getMessage());
		}
	}
	
	
	/**
	 * Adds a host system to the ignore list
	 * @param console Scanner class that is used to interact with the user
	 */
	private void blackListAHostSystem(Scanner console) {
		try {
			System.out.print("Enter Host System to blacklist: ");
			String vmqualifier = console.nextLine();
			vmqualifier = vmqualifier.replaceAll("\\s+", "");
			availabilityManagerThread.addVHostToBlackList(vmqualifier);
			System.out.println("Black Listed the following: " + vmqualifier);
		}
		catch (Exception e) {
			System.out.println("Failed to blacklist the specified vhost, Error: " + e.getMessage());
		}
	}
	
	
	/**
	 * Adds a Virtual Machine to the ignore list
	 * @param console Scanner class that is used to interact with the user
	 */
	private void blackListAVirtualMachine(Scanner console) {
		try {
			System.out.print("Enter Virtual Machine to Blacklist: ");
			String vmqualifier = console.nextLine();
			vmqualifier = vmqualifier.replaceAll("\\s+", "");
			availabilityManagerThread.addVHostToBlackList(vmqualifier);
			System.out.println("Black Listed the following: " + vmqualifier);
		}
		catch (Exception e) {
			System.out.println("Failed to blacklist the specified VM, Error: " + e.getMessage());
		}
	}
	
	
	/**
	 * Removes host system from the ignore list
	 * @param console Scanner class that is used to interact with the user
	 */
	private void removeBlackListedHostSystem(Scanner console) {
		try {
			System.out.print("Enter Host System to remove from the blacklist: ");
			String vmqualifier = console.nextLine();
			vmqualifier = vmqualifier.replaceAll("\\s+", "");
			availabilityManagerThread.removeVHostFromBlackList(vmqualifier);
			System.out.println("Black Listed the following: " + vmqualifier);
		}
		catch (Exception e) {
			System.out.println("Failed to remove the specified blacklisted host, Error: " + e.getMessage());
		}
	}
	
	
	/**
	 * Removes vm from the ignore list
	 * @param console Scanner class that is used to interact with the user
	 */
	private void removeBlackListedVirtualMachine(Scanner console) {
		try {
			System.out.print("Enter Virtual Machine to remove from the blacklist: ");
			String vmqualifier = console.nextLine();
			vmqualifier = vmqualifier.replaceAll("\\s+", "");
			availabilityManagerThread.removeVHostFromBlackList(vmqualifier);
			System.out.println("Black Listed the following: " + vmqualifier);
		}
		catch (Exception e) {
			System.out.println("Failed to remove the specified blacklisted vm, Error: " + e.getMessage());
		}
	}
	
	
	/**
	 * Calls the function to add the power on action trigger
	 * @param console Scanner class that is used to interact with the user
	 */
	private void createPowerOnActionTrigger(Scanner console) {
		try {
			System.out.print("Enter Virtual Machine qualifier name to add an alarm: ");
			String vmqualifier = console.nextLine();
			vmqualifier = vmqualifier.replaceAll("\\s+", "");
			
			for (VirtualMachine vm : Utilities.findVMsByName(serviceInstance, vmqualifier)) {
				if(Utilities.createPowerOnAlarmAction(serviceInstance, vm)) {
					System.out.println("Added a Power On Alarm for " + vm.getName());
				}
			}
		}
		catch (Exception e) {
			System.out.println("Failed to add a power on alarm action, Error: " + e.getMessage());
		}
	}
	
	
	/**
	 * Calls the function to remove the power on action trigger
	 * @param console Scanner class that is used to interact with the user
	 */
	private void removePowerOnActionTrigger(Scanner console) {
		try {
			System.out.print("Enter Virtual Machine qualifier name to remove the alarm: ");
			String vmqualifier = console.nextLine();
			vmqualifier = vmqualifier.replaceAll("\\s+", "");
			
			for (VirtualMachine vm : Utilities.findVMsByName(serviceInstance, vmqualifier)) {
				if(Utilities.createPowerOnAlarmAction(serviceInstance, vm)) {
					System.out.println("Removed a Power On Alarm for " + vm.getName());
				}
			}
		}
		catch (Exception e) {
			System.out.println("Failed to remove power on alarm action, Error: " + e.getMessage());
		}
	}
	
	
	/**
	 * Starts the exit process
	 * Kills threads gracefully
	 */
	private void callExit() {
		if(availabilityManagerThread == null) {
			System.out.println("Stopping Availablity Manager!");
			availabilityManagerThread.disable();
			try {
				availabilityManagerThread.getThread().join();
			} catch (Exception e) {}
		}
		if(refreshBackupCacheThread == null) {
			refreshBackupCacheThread.disable();
			refreshBackupCacheThread.getTimer().cancel();
		}
		
		// Destory the Config Instance
		Config.getInstance().destroy();
		System.exit(0);
	}
	
}
