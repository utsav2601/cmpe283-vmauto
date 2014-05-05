/**
 * 
 */
package srkarra.cmpe283.p1.config;
import com.vmware.vim25.Description;
import com.vmware.vim25.VirtualDeviceConfigSpec;
import com.vmware.vim25.VirtualDeviceConfigSpecFileOperation;
import com.vmware.vim25.VirtualDeviceConfigSpecOperation;
import com.vmware.vim25.VirtualDisk;
import com.vmware.vim25.VirtualDiskFlatVer2BackingInfo;
import com.vmware.vim25.VirtualEthernetCard;
import com.vmware.vim25.VirtualEthernetCardNetworkBackingInfo;
import com.vmware.vim25.VirtualLsiLogicController;
import com.vmware.vim25.VirtualMachineConfigSpec;
import com.vmware.vim25.VirtualMachineFileInfo;
import com.vmware.vim25.VirtualPCNet32;
import com.vmware.vim25.VirtualSCSISharing;
import com.vmware.vim25.mo.ComputeResource;
import com.vmware.vim25.mo.Datacenter;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ResourcePool;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;

import srkarra.cmpe283.p1.VHost;

/**
 * @author Asim Mughni
 *
 */
public class CreateVM {
	private String datacenter;
	private String newVmName;
	private VHost vHost;
	private ServiceInstance si;
	/**
	 * @param datacenter
	 * @param newVmName
	 * @param vHost
	 */
	public CreateVM(String datacenter, String newVmName, VHost vHost, ServiceInstance si) {
		super();
		this.datacenter = datacenter;
		this.newVmName = newVmName;
		this.vHost = vHost;
		this.si = si;
	}
	
	protected void start() {
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
	    
	    Datacenter dc;
		try {
			dc = (Datacenter) new InventoryNavigator(
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static VirtualDeviceConfigSpec createNicSpec(String netName, String nicName) throws Exception {
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
	
	private static VirtualDeviceConfigSpec createScsiSpec(int cKey) {
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
	
	private static VirtualDeviceConfigSpec createDiskSpec(String dsName, 
	  int cKey, long diskSizeKB, String diskMode) {
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
}
