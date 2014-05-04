package srkarra.cmpe283.p1.config;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import srkarra.cmpe283.p1.*;

import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;

public class Manager {
	
	protected ServiceInstance si;
	protected List<VHost> vHosts;
	private int high = 60;
	private int low = 30;
	
	public Manager(ServiceInstance si) throws Exception {
		this.si = si;
		setHosts();
	}
	
	protected void setHosts() throws Exception {
		this.vHosts = new ArrayList<VHost>();
		Folder vCenterFolder = si.getRootFolder();
		ManagedEntity[] vHosts = new InventoryNavigator(vCenterFolder)
				.searchManagedEntities("HostSystem");
		if (vHosts.length != 0) {
			for (int i = 0; i < vHosts.length; i++) {
				this.vHosts.add(new VHost((HostSystem) vHosts[i]));
			}
			System.out.println("All connected hosts retrieved.");
		} else {
			System.out.println("No host connected.");
		}
	}
	
	protected boolean isOverload (VHost host) {
		long total = host.totalCpuMhz();
		long usage = host.cpuUsageMhz();
		return (usage * 100.0 / total) > high;
	}
	
	protected boolean isUnderload (VHost host) {
		long total = host.totalCpuMhz();
		long usage = host.cpuUsageMhz();
		return (usage * 100.0 / total) < low;
	}
	
	protected boolean isOverloadAfterMigrate (VHost host, long adjustment) {
		long total = host.totalCpuMhz();
		long usage = host.cpuUsageMhz() + adjustment;
		return (usage * 100.0 / total) > high;
	}
	
	public void cloneVM(String vmname, String cloneName) throws InvalidProperty, RuntimeFault, RemoteException
	{
		Folder rootFolder = si.getRootFolder();
	    VirtualMachine vm = (VirtualMachine) new InventoryNavigator(
	        rootFolder).searchManagedEntity(
	            "VirtualMachine", vmname);

	    if(vm==null)
	    {
	      System.out.println("No VM " + vmname + " found");
	      si.getServerConnection().logout();
	      return;
	    }

	    VirtualMachineCloneSpec cloneSpec = 
	      new VirtualMachineCloneSpec();
	    cloneSpec.setLocation(new VirtualMachineRelocateSpec());
	    cloneSpec.setPowerOn(true);
	    cloneSpec.setTemplate(false);

	    Task task = vm.cloneVM_Task((Folder) vm.getParent(), 
	        cloneName, cloneSpec);
	    System.out.println("Launching the VM clone task. " +
	            "Please wait ...");

	    String status = task.waitForMe();
	    if(status==Task.SUCCESS)
	    {
	      System.out.println("VM got cloned successfully.");
	    }
	    else
	    {
	      System.out.println("Failure -: VM cannot be cloned");
	    }
	}
	
	public void createVM(String datacenter, String newVmName, VHost vHost) throws Exception
	{
		String dcName = datacenter;
	    String vmName = newVmName;
	    long memorySizeMB = 512;
	    int cupCount = 1;

	    String guestOsId = "ubuntuGuest";
	    long diskSizeKB = 7000000;

	    String diskMode = "persistent";
	    String datastoreName = "nfs2team04";
	    String netName = "VM Network";
	    String nicName = "Network Adapter 1";
	    
	    Folder rootFolder = si.getRootFolder();
	    
	    Datacenter dc = (Datacenter) new InventoryNavigator(
	        rootFolder).searchManagedEntity("Datacenter", dcName);
	    ComputeResource cr = (ComputeResource) vHost.getHost().getParent();
	    ResourcePool rp = cr.getResourcePool();
	    
	    Folder vmFolder = dc.getVmFolder();

	    // create vm config spec
	    VirtualMachineConfigSpec vmSpec = 
	      new VirtualMachineConfigSpec();
	    vmSpec.setName(vmName);
	    vmSpec.setAnnotation("VirtualMachine Annotation");
	    vmSpec.setMemoryMB(memorySizeMB);
	    vmSpec.setNumCPUs(cupCount);
	    vmSpec.setGuestId(guestOsId);

	    // create virtual devices
	    int cKey = 1000;
	    VirtualDeviceConfigSpec scsiSpec = createScsiSpec(cKey);
	    VirtualDeviceConfigSpec diskSpec = createDiskSpec(
	        datastoreName, cKey, diskSizeKB, diskMode);
	    VirtualDeviceConfigSpec nicSpec = createNicSpec(
	        netName, nicName);

	    vmSpec.setDeviceChange(new VirtualDeviceConfigSpec[] 
	        {scsiSpec, diskSpec, nicSpec});
	    
	    // create vm file info for the vmx file
	    VirtualMachineFileInfo vmfi = new VirtualMachineFileInfo();
	    vmfi.setVmPathName("["+ datastoreName +"]");
	    vmSpec.setFiles(vmfi);

	    // call the createVM_Task method on the vm folder
	    Task task = vmFolder.createVM_Task(vmSpec, rp, null);
	    @SuppressWarnings("deprecation")
	    String result = task.waitForMe();       
	    if(result == Task.SUCCESS) 
	    {
	      System.out.println("VM Created Sucessfully");
	    }
	    else 
	    {
	      System.out.println("VM could not be created. ");
	    }
	  }

	static VirtualDeviceConfigSpec createNicSpec(String netName, 
	      String nicName) throws Exception
	  {
	    VirtualDeviceConfigSpec nicSpec = 
	        new VirtualDeviceConfigSpec();
	    nicSpec.setOperation(VirtualDeviceConfigSpecOperation.add);

	    VirtualEthernetCard nic =  new VirtualPCNet32();
	    VirtualEthernetCardNetworkBackingInfo nicBacking = 
	        new VirtualEthernetCardNetworkBackingInfo();
	    nicBacking.setDeviceName(netName);

	    Description info = new Description();
	    info.setLabel(nicName);
	    info.setSummary(netName);
	    nic.setDeviceInfo(info);
	    
	    // type: "generated", "manual", "assigned" by VC
	    nic.setAddressType("generated");
	    nic.setBacking(nicBacking);
	    nic.setKey(0);
	   
	    nicSpec.setDevice(nic);
	    return nicSpec;
	}
	
	static VirtualDeviceConfigSpec createScsiSpec(int cKey)
	  {
	    VirtualDeviceConfigSpec scsiSpec = 
	      new VirtualDeviceConfigSpec();
	    scsiSpec.setOperation(VirtualDeviceConfigSpecOperation.add);
	    VirtualLsiLogicController scsiCtrl = 
	        new VirtualLsiLogicController();
	    scsiCtrl.setKey(cKey);
	    scsiCtrl.setBusNumber(0);
	    scsiCtrl.setSharedBus(VirtualSCSISharing.noSharing);
	    scsiSpec.setDevice(scsiCtrl);
	    return scsiSpec;
	  }
	  
	static VirtualDeviceConfigSpec createDiskSpec(String dsName, 
	      int cKey, long diskSizeKB, String diskMode)
	  {
	    VirtualDeviceConfigSpec diskSpec = 
	        new VirtualDeviceConfigSpec();
	    diskSpec.setOperation(VirtualDeviceConfigSpecOperation.add);
	    diskSpec.setFileOperation(
	        VirtualDeviceConfigSpecFileOperation.create);
	    
	    VirtualDisk vd = new VirtualDisk();
	    vd.setCapacityInKB(diskSizeKB);
	    diskSpec.setDevice(vd);
	    vd.setKey(0);
	    vd.setUnitNumber(0);
	    vd.setControllerKey(cKey);

	    VirtualDiskFlatVer2BackingInfo diskfileBacking = 
	        new VirtualDiskFlatVer2BackingInfo();
	    String fileName = "["+ dsName +"]";
	    diskfileBacking.setFileName(fileName);
	    diskfileBacking.setDiskMode(diskMode);
	    diskfileBacking.setThinProvisioned(true);
	    vd.setBacking(diskfileBacking);
	    return diskSpec;
	  }  
	  
	public void start() throws Exception{
		//a) DRS 1
		//creating a vm
		//find cpu usgae of vohst
		//take the lesser one
		//add the new vm there
		
		long cpuUsage = 0;
		VHost hostWithLowestCpuUsage = findLowestCpuUsageHost(cpuUsage);
		if(hostWithLowestCpuUsage != null)
		{//TODO: Uncomment below
//			createVM("datacenter_t04", "DRS1_VM", hostWithLowestCpuUsage);
		}
		
		
		
		//b) DRS 2
		//compare existing vhost and check threshold
		//and then find vm load of that higher vhost
		//select the highest vm and migrate it to the new vhost
		
		VHost hostWithHighestCpuUsage = null;
		for (VHost vHost : vHosts) {
			if(cpuUsage == 0 || cpuUsage < vHost.cpuUsageMhz())
			{
				cpuUsage = vHost.cpuUsageMhz();
				hostWithHighestCpuUsage = vHost;
				List<VM> vmList = hostWithHighestCpuUsage.getVMs();
				migrateMyVm(hostWithLowestCpuUsage.getHost(), vmList.get(0).getVM());
			}	
		}
		
		//c) DPM
		// Check cpu usage off all vHost
		// Find the vHost that has below threshold usage
		// Migrate the vms of lower vhost to the one that has limit, 
		// if the limit exceeds kill vms or do nothing
		// Standby vHost, power off
		
		
	}

	private VHost findLowestCpuUsageHost(long cpuUsage) {
		VHost hostWithLowestCpuUsage = null;
		for (VHost vHost : vHosts) {
			if(cpuUsage == 0 || cpuUsage > vHost.cpuUsageMhz())
			{
				cpuUsage = vHost.cpuUsageMhz();
				hostWithLowestCpuUsage = vHost;
			}	
		}
		return hostWithLowestCpuUsage;
	}
	
	public void migrateMyVm(HostSystem newHost, VirtualMachine vm) throws Exception {

	        ComputeResource cr = (ComputeResource) newHost.getParent();

	        String[] checks = new String[] {"cpu", "software"};
	        HostVMotionCompatibility[] vmcs =
	                si.queryVMotionCompatibility(vm, new HostSystem[]
	                        {newHost},checks );

	        String[] comps = vmcs[0].getCompatibility();
	        if(checks.length != comps.length)
	        {
	            System.out.println("CPU/software NOT compatible. Exit.");
	            si.getServerConnection().logout();
	            return;
	        }

	        Task task1 = vm.migrateVM_Task(cr.getResourcePool(), newHost,
	                VirtualMachineMovePriority.highPriority,
	                VirtualMachinePowerState.poweredOn);

	        if(task1.waitForTask()==Task.SUCCESS)
	        {
	            System.out.println("VMotioned!");
	            //Need to check whether the poweron op works or not
	            /*
	            Task task2 = vm.powerOnVM_Task(newHost);
	            if (task2.waitForTask() == Task.SUCCESS) {
	                System.out.println(vmname + " powered on");
	            } else {
	                System.out.println(vmname + " failed to powered on");
	            }
	            */
	        }
	        else
	        {
	            System.out.println("VMotion failed!");
	            TaskInfo info = task1.getTaskInfo();
	            System.out.println(info.getError().getFault());
	        }

	        /*
	        Task task0 = vm.powerOffVM_Task();
	        if (task0.waitForTask() == Task.SUCCESS) {
	            System.out.println(vmname + " powered off");
	            Task task1 = vm.migrateVM_Task(cr.getResourcePool(), newHost,
	                    VirtualMachineMovePriority.highPriority,
	                    VirtualMachinePowerState.poweredOff);
	            if(task1.waitForTask()==Task.SUCCESS)
	            {
	                System.out.println("VMotioned!");
	                //Need to check whether the poweron op works or not
	                Task task2 = vm.powerOnVM_Task(newHost);
	                if (task2.waitForTask() == Task.SUCCESS) {
	                    System.out.println(vmname + " powered on");
	                } else {
	                    System.out.println(vmname + " failed to powered on");
	                }
	            }
	            else
	            {
	                System.out.println("VMotion failed!");
	                TaskInfo info = task1.getTaskInfo();
	                System.out.println(info.getError().getFault());
	            }
	        } else {
	            System.out.println(vmname + " failed to powered off");
	        }
	        */
	    }

}
