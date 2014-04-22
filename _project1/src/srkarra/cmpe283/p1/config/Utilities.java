package srkarra.cmpe283.p1.config;

import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import srkarra.cmpe283.p1.config.Config.ConfigIdents;

import com.vmware.vim25.AlarmAction;
import com.vmware.vim25.AlarmSpec;
import com.vmware.vim25.AlarmTriggeringAction;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.MethodAction;
import com.vmware.vim25.MethodActionArgument;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.StateAlarmExpression;
import com.vmware.vim25.StateAlarmOperator;
import com.vmware.vim25.VirtualMachineCapability;
import com.vmware.vim25.VirtualMachineConfigInfo;
import com.vmware.vim25.VirtualMachineMovePriority;
import com.vmware.vim25.VirtualMachinePowerState;
import com.vmware.vim25.VirtualMachineQuickStats;
import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.VirtualMachineSnapshotTree;
import com.vmware.vim25.VirtualMachineSummary;
import com.vmware.vim25.mo.Alarm;
import com.vmware.vim25.mo.ComputeResource;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;
import com.vmware.vim25.mo.VirtualMachineSnapshot;

/**
 * Utilities.java
 * Responsible for various minor functions for the system
 * Includes:
 * 	- Reading a properties file 
 *  - Ping a host with ICMP  
 *  - Find a Virtual Machine by name
 *  - Get some basic VM Statistics
 *  - Create vm snapshots
 *  - Retrieve the newest snapshot
 *  - Restore a VM to a snapshot
 *  - Migrate a VM to a new host
 *  
 * @author Sai Karra
 */
public class Utilities {
	
	
	/**
	 * Reads specified properties file and returns an instance of Properties
	 * @param file location of the properties file
	 * @return Properties instance containing the properties
	 * @throws IOException thrown when there is an error reading properties file (such as file not found, unauthorized read) 
	 */
	public static Properties readProperties(String file) throws IOException {
		Properties properties = new Properties();
		properties.load(new FileReader(file));
		return properties;
	}
	
	
	
	
	
	
	/**
	 * Pings the specified system, NOTE: ICMP must be enabled on the target system
	 * @param host the address of the system, either the FQDN or the IP Address
	 * @return true if the host is reachable via ICMP, else false
	 */
	public static boolean pingSystem(String host) {
		if(host == null || host.trim().isEmpty()) {
			return false;
		}
		try {
			// Create a system process for ping
			Process ping = Runtime.getRuntime().exec(String.format("ping -%s 1 %s", System.getProperty("os.name").startsWith("Windows") ? "n" : "c", host));
			ping.waitFor();
			return ping.exitValue() == 0;
		}
		catch (Exception e) {}
		return false;
	}
	
	
	/**
	 * Check if the Virtual Machine is Powered on
	 * @param vm Virtual Machine to check
	 * @return true if virtual machine is in the Powered On state
	 */
	public static boolean isVirtualMachinePoweredOn(VirtualMachine vm) {
		return vm.getRuntime().getPowerState().toString().equalsIgnoreCase(VirtualMachinePowerState.poweredOn.toString());
	}
	
	/**
	 * Returns the IP Address of the Virtual Machine
	 * @param vm the Virtual Machine to look up
	 * @return the ip address of the system
	 */
	public static String getIPAddress(VirtualMachine vm) {
		return vm.getSummary().getGuest().getIpAddress();
	}
	
	/**
	 * Returns a list of VMs that match the specified name qualifier 
	 * @param serviceInstance VMware connection to query against
	 * @param nameQualifier name to look up 
	 * @return list of virtual machines that match the specified name
	 */
	public static VirtualMachine[] findVMsByName(ServiceInstance serviceInstance, String nameQualifier) {
		try {
			if(nameQualifier != null) {
				nameQualifier = nameQualifier.replaceAll("\\s+", "").trim();
				if(!nameQualifier.isEmpty()) {
					String nameQualifierLCase = nameQualifier.toLowerCase();
					List<VirtualMachine> vms = new ArrayList<VirtualMachine>();
					ManagedEntity[] mes = new InventoryNavigator(serviceInstance.getRootFolder()).searchManagedEntities(Config.VMWARE_IDENTIFIER_VIRTUAL_MACHINE);
					for (ManagedEntity managedEntity : mes) {
						VirtualMachine vm = (VirtualMachine) managedEntity;
						String vmNameLCase = vm.getName().trim().toLowerCase();
						if(vmNameLCase.contains(nameQualifierLCase) || vmNameLCase.equals(nameQualifierLCase)) {
							vms.add(vm);
						}
					}
					
					return vms.toArray(new VirtualMachine[vms.size()]);
				}
			}
		}
		catch (Exception e) {}
		return null;
	}
	
	
	
	
	
	/**
	 * Retrieves and formats some quick statistics of the virtual machine including but not limited to:
	 * 	Name, OS Type, Snapshot, Power State, Running Time, CPU/Memory/HardDisk Usage
	 * @param vm instance of the virtual machine, must not be null
	 * @return a formatted String object containing some quick facts of the virtual machine 
	 * @throws RuntimeFault failed to retrieve vm's information
	 * @throws RemoteException failed to retrieve vm's information
	 */
	public static String getVmStatistics(VirtualMachine vm) throws RuntimeFault, RemoteException {
		if(vm == null) {
			return null;
		}
		
		VirtualMachineConfigInfo vminfo 		= vm.getConfig();
		VirtualMachineCapability vmc 			= vm.getCapability();
		VirtualMachineRuntimeInfo vmruntime 	= vm.getRuntime();
		VirtualMachineSummary vmsum 			= vm.getSummary();
		VirtualMachineQuickStats vmqstats 		= vmsum.getQuickStats();

		vm.refreshStorageInfo();
		Calendar bootTime = vm.getRuntime().getBootTime();
		String poweredState = vmruntime.getPowerState().toString();
		String poweredOnTime = bootTime == null ? "<data missing>" : String.format("%d mins", ((GregorianCalendar.getInstance().getTimeInMillis() - bootTime.getTimeInMillis())/1000/60));
		String commitedStorage = String.format("%d MB", (vmsum.getStorage().getCommitted()/1024/1024));
		
		return  String.format(
				" Virtual Machine: %s                       \n" +
				" ------------------------------------------\n" +
				" Guest OS:               %-20s             \n" +
				" Guest IP:               %-20s             \n" +
				" Supports Snapshots:     %-20s             \n" +
				" Power State:            %-20s             \n" +
				" System Uptime (sec):    %-20s             \n" +
				" Consumed Host CPU:      %-20s             \n" +
				" Consumed Host Memory:   %-20s             \n" +
				" Maximum Host Memory:    %-20s             \n" +
				" Used Storage:           %-20s             \n" +
				" ==============================================================\n",
						vm.getName().trim(), vminfo.getGuestFullName().trim(), vmsum.getGuest().getIpAddress(),
						vmc.isMultipleSnapshotsSupported(), poweredState, poweredOnTime, vmqstats.getOverallCpuUsage(),
						vmqstats.getGuestMemoryUsage(), vmqstats.getPrivateMemory(), commitedStorage);
	}
	
	
	
	/**
	 * Create a snapshot for a virtual machine
	 * @param vm - vm instance to take a snapshot
	 * @return true if the snapshot was successful, else false
	 */
	public static boolean createSnapshot(VirtualMachine vm, String name, String description, boolean memory, boolean quiesce) {
		try {
			if(!vm.getRuntime().getPowerState().equals("poweredOff")) {
				Task task = vm.createSnapshot_Task(name, description, memory, quiesce);
				return task.waitForTask().equalsIgnoreCase(Config.VMWARE_TASK_SUCCESS_STRING);
			}
		}
		catch (Exception e) {} 
		return false;
	}
	
	
	
	/**
	 * Convenience method to call the recursive function of the same name
	 * @param vm the virtual machine to look at
	 * @return the lastest in snapshot tree
	 */
	public static VirtualMachineSnapshot getLatestSnapshot(VirtualMachine vm) {
		ManagedObjectReference managedObjectReference =  getLatestSnapshot(vm.getSnapshot().getRootSnapshotList()).getSnapshot();
		return managedObjectReference == null ? null : new VirtualMachineSnapshot(vm.getServerConnection(), managedObjectReference);
	}
	
	
	
	
	
	/**
	 * Returns the latest snapshot in the snapshot tree based on the create time (recursive function)
	 * @param rootVirtualMachineSnapshotTree starts with the root node on the snapshot tree
	 * @return the latest snapshot in the vm snapshot tree
	 */
	private static VirtualMachineSnapshotTree getLatestSnapshot(VirtualMachineSnapshotTree[] rootVirtualMachineSnapshotTree) {
		// Return null if there isn't a tree
		if (rootVirtualMachineSnapshotTree == null) {
			return null;
		}
		
		VirtualMachineSnapshotTree lowest = null;
		for (VirtualMachineSnapshotTree node : rootVirtualMachineSnapshotTree) {
			// Store the first node for comparison, then compare
			if (lowest == null || lowest.getCreateTime().compareTo(node.getCreateTime()) < 0) {
				lowest = node;
			}
			
			// If the current node is also a tree then check for the lowest RECURSIVE
			VirtualMachineSnapshotTree[] childTree = node.getChildSnapshotList();
			if (childTree != null) {
				VirtualMachineSnapshotTree lowestInTree = getLatestSnapshot(childTree);
				if (lowest.getCreateTime().compareTo(lowestInTree.getCreateTime()) < 0) { 
					lowest = lowestInTree;
				}
			}
		}

		return lowest;
	}
	
	
	/**
	 * Restore Virtual Machine to the specified snapshot
	 * @param snapshot Snapshot to restore to 
	 * @return true if the restore was successful, else false
	 */
	public static boolean restoreVMToSnapshot(VirtualMachine virtualMachine, VirtualMachineSnapshot snapshot) {
		try {
			if(snapshot != null) {
				Task task = snapshot.revertToSnapshot_Task(null);
				if(task.waitForTask().equalsIgnoreCase(Config.VMWARE_TASK_SUCCESS_STRING)) {
					Task powerOnTask = virtualMachine.powerOnVM_Task(null);
					return powerOnTask.waitForTask().equalsIgnoreCase(Config.VMWARE_TASK_SUCCESS_STRING);
				}
				else {
					return false;
				}
			}
		}
		catch (Exception e) {}
		return false;
	}
	
	
	
	/**
	 * Migrates a VM to the new specified host
	 * @param vm virtual machine to migrate
	 * @param newHost the host to migrate the vm to
	 * @return true if successfully migrated, else false
	 */
	public static boolean migrateSnapshot(VirtualMachine vm, HostSystem newHost) {
		if(vm == null || newHost == null) {
			return false;
		}
		try {
			if(vm != null && newHost != null) {
				ComputeResource computeResource = (ComputeResource) newHost.getParent();
				Task task = vm.migrateVM_Task(computeResource.getResourcePool(), newHost, VirtualMachineMovePriority.highPriority, VirtualMachinePowerState.poweredOn);
				return task.waitForTask().equalsIgnoreCase(Config.VMWARE_TASK_SUCCESS_STRING);
			}
		}
		catch (Exception e) {}
		return false;
	}
	
	
	/**
	 * Powers On the Specified System
	 * @param host vHost to PowerOn
	 * @param timeout timeout for the powerOn, if less than 5 seconds, program will default to Interger.MAX_VALUE
	 * @return true if power on was successful, else false
	 */
	public static boolean powerOnVHost(HostSystem host, int timeout) {
		try {
			if(host != null) {
				Task task = host.powerUpHostFromStandBy(timeout > 5 ? timeout : Integer.MAX_VALUE);
				return task.waitForTask().equals(Config.VMWARE_TASK_SUCCESS_STRING);
			}
		}
		catch (Exception e) {}
		return false;
	}
	
	
	/**
	 * Creates a trigger to turn an vm if it was powered down
	 * @param serviceInstance VMware connection
	 * @param vm the vm to add the alarm to 
	 * @return true if the alarm + trigger was successfully added to the vm
 	 */
	public static boolean createPowerOnAlarmAction(ServiceInstance serviceInstance, VirtualMachine vm) {
		try {
			if(serviceInstance != null && vm != null) {
				StateAlarmExpression stateAlarmExpression = new StateAlarmExpression();
				stateAlarmExpression.setOperator(StateAlarmOperator.isEqual);
				stateAlarmExpression.setRed("poweredOff");
				stateAlarmExpression.setYellow(null);
				stateAlarmExpression.setStatePath("runtime.powerState");
				stateAlarmExpression.setType("VirtualMachine");

				MethodActionArgument methodActionArgument = new MethodActionArgument();
				methodActionArgument.setValue(null);

				MethodAction methodAction = new MethodAction();
				methodAction.setName("PowerOnVM_Task");
				methodAction.setArgument(new MethodActionArgument[] { methodActionArgument });

				AlarmTriggeringAction alarmTriggeringAction = new AlarmTriggeringAction();
				alarmTriggeringAction.setYellow2red(true);
				alarmTriggeringAction.setAction(methodAction);
				
				AlarmSpec alarmSpec = new AlarmSpec();
				alarmSpec.setName(vm.getName() + Config.VMWARE_POWER_ON_ALARM_TRIG_SUFFIX);
				alarmSpec.setAction((AlarmAction) alarmTriggeringAction);
				alarmSpec.setExpression(stateAlarmExpression);
				alarmSpec.setDescription("Power On VM if it's powered down by the user");
				alarmSpec.setEnabled(true);
				
				serviceInstance.getAlarmManager().createAlarm(vm, alarmSpec);
				return true;
			}
		}
		catch (Exception e) {}
		return false;
	}
	
	
	/**
	 * Removes all power on triggers 
	 * @param si VMware conneciton 
	 * @param vm vm to remove the alarms for
	 * @return true if successfully removed the alarm + trigger, else false
	 */
	public static boolean removePowerOnAlarms(ServiceInstance si, VirtualMachine vm) {
		try {
			if(si != null && vm != null) {
				Alarm[] alarms  = si.getAlarmManager().getAlarm(vm);
				if(alarms != null) {
					for (Alarm alarm : alarms) {
						if(alarm.getAlarmInfo().getName().endsWith(Config.VMWARE_POWER_ON_ALARM_TRIG_SUFFIX)) {
							alarm.removeAlarm();
						}
					}
				}
				return true;
			}
		}
		catch (Exception e) {}
		return false;
	}
	
	/**
	 * Creates a snapshot for all powered on vms
	 * @param serviceInstance vmware server connection
 	 */
	public static void createASnapShotOnAllVMs(ServiceInstance serviceInstance){
		boolean memory = false;
		boolean quiesce = false;
		
		try {
			memory = Boolean.parseBoolean(Config.getProperty(ConfigIdents.VM_SNAPSHOT_MEMORY));
			quiesce = Boolean.parseBoolean(Config.getProperty(ConfigIdents.VM_SNAPSHOT_QUIESCE));
		} catch (Exception e) {}
		
		try {
			ManagedEntity[] virtualMachines = new InventoryNavigator(serviceInstance.getRootFolder()).searchManagedEntities(Config.VMWARE_IDENTIFIER_VIRTUAL_MACHINE);
			for (ManagedEntity managedEntity : virtualMachines) {
				VirtualMachine virtualMachine = (VirtualMachine) managedEntity;
				
				System.out.println(virtualMachine.getName());
				try {
					if(Utilities.isVirtualMachinePoweredOn(virtualMachine)) {
						Utilities.createSnapshot(virtualMachine, virtualMachine.getName() + Config.VMWARE_SNAPSHOT_NAME_SUFFIX, "Auto backup at " + new Date(), memory, quiesce);
					}
				} catch (Exception e) {}
			}
		}
		catch (Exception e) {}
	}
	

	
	/**
	 * Returns a Host System on the system
	 * @param serviceInstance vmware server connection
	 * @param ignoreHostSystemWithName hostname that should not be returned
	 * @return HostSystem if available
	 */
	public static HostSystem getNewHostSystem(ServiceInstance serviceInstance, String ignoreHostSystemWithName) {
		try {
			ManagedEntity[] hostMachines = new InventoryNavigator(serviceInstance.getRootFolder()).searchManagedEntities("HostSystem");
			for (ManagedEntity managedEntity : hostMachines) {
				HostSystem hostSystem = (HostSystem) managedEntity;
				if(!hostSystem.getName().equalsIgnoreCase(ignoreHostSystemWithName)) {
					return hostSystem;
				}
			}
		}
		catch (Exception e) {}
		return null;
	}
	
	
	
}