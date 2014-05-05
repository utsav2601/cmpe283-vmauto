/**
 * 
 */
package srkarra.cmpe283.p1.config;

import java.rmi.RemoteException;

import com.vmware.vim25.VirtualMachineCloneSpec;
import com.vmware.vim25.VirtualMachineRelocateSpec;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;

/**
 * @author Asim Mughni
 *
 */
public class CloneVM {
	private String vmname;
	private String cloneName;
	private ServiceInstance si;
	/**
	 * @param vmname
	 * @param cloneName
	 */
	public CloneVM(String vmname, String cloneName, ServiceInstance si) {
		super();
		this.vmname = vmname;
		this.cloneName = cloneName;
		this.si = si;
	}
	
	protected void start() {
		new Runnable() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				Folder rootFolder = si.getRootFolder();
			    VirtualMachine vm;
				try {
					vm = (VirtualMachine) new InventoryNavigator(
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
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		};
	}
}
