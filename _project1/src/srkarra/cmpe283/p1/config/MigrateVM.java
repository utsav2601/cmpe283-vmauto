/**
 * 
 */
package srkarra.cmpe283.p1.config;

import java.rmi.RemoteException;

import com.vmware.vim25.HostVMotionCompatibility;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.TaskInfo;
import com.vmware.vim25.VirtualMachineMovePriority;
import com.vmware.vim25.VirtualMachinePowerState;
import com.vmware.vim25.mo.ComputeResource;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;

/**
 * @author Asim Mughni
 *
 */
public class MigrateVM {
	private HostSystem newHost;
	private VirtualMachine vm;
	private ServiceInstance si;
	/**
	 * @param newHost
	 * @param vm
	 * @param si
	 */
	public MigrateVM(HostSystem newHost, VirtualMachine vm, ServiceInstance si) {
		super();
		this.newHost = newHost;
		this.vm = vm;
		this.si = si;
	}
	
	protected void start()
	{
		ComputeResource cr = (ComputeResource) newHost.getParent();

        String[] checks = new String[] {"cpu", "software"};
        HostVMotionCompatibility[] vmcs;
		try {
			vmcs = si.queryVMotionCompatibility(vm, new HostSystem[]
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
	            System.out.println("Migration Started Successfully!");
	        }
	        else
	        {
	            System.out.println("Migration failed!");
	            TaskInfo info = task1.getTaskInfo();
	            System.out.println(info.getError().getFault());
	        }
		} catch (RuntimeFault e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
