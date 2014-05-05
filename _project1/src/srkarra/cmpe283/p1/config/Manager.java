package srkarra.cmpe283.p1.config;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import srkarra.cmpe283.p1.*;
import srkarra.cmpe283.p1.config.Config.ConfigIdents;
import com.vmware.vim25.InvalidProperty;
import com.vmware.vim25.InvalidState;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.TaskInProgress;
import com.vmware.vim25.mo.*;

public class Manager {
	
	protected ServiceInstance si;
	protected List<VHost> vHosts;
	private int high = 60;
	private int low = 30;
	
	public Manager(ServiceInstance si) throws Exception {
		this.si = si;
		loadAllHosts();
	}
	
	protected void loadAllHosts() throws Exception {
		this.vHosts = new ArrayList<VHost>();
		Folder vCenterFolder = si.getRootFolder();
		ManagedEntity[] vHosts = new InventoryNavigator(vCenterFolder)
				.searchManagedEntities("HostSystem");
		if (vHosts.length != 0) {
			for (int i = 0; i < vHosts.length; i++) {
				this.vHosts.add(new VHost((HostSystem) vHosts[i]));
			}
			System.out.println("All connected hosts are loaded.");
		} else {
			System.out.println("No host found.");
		}
	}
	
	public void start() throws MalformedURLException {
		try {
			long cpuUsage = 0;
			VHost hostWithLowestCpuUsage = findLowestCpuUsageHost(cpuUsage);
			VHost hostWithHighestCpuUsage = findHighestCpuUsageHost(cpuUsage);
			
			startDRS1(hostWithLowestCpuUsage);
			
			if( isOverload( hostWithHighestCpuUsage ) )
				startDRS2(hostWithLowestCpuUsage, hostWithHighestCpuUsage);
			
			if(isUnderload(hostWithLowestCpuUsage)) {
				startDPM(hostWithHighestCpuUsage, hostWithLowestCpuUsage);
			}
		}
		catch (Exception e) {
			System.out.println("Failed to fetch statistics. Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Description: Check cpu usage off all vHost
	 * Find the vHost that has below threshold usage 
	 * Migrate the vms of lower vhost to the one that has limit,
	 * Standby source vHost
	 * 
	 * @param hostWithLowestCpuUsage
	 * @throws MalformedURLException
	 * @throws InvalidProperty
	 * @throws RuntimeFault
	 * @throws RemoteException
	 * @throws TaskInProgress
	 * @throws InvalidState
	 */
	private void startDPM(VHost hostWithHighestCpuUsage, VHost hostWithLowestCpuUsage)
			throws MalformedURLException, InvalidProperty, RuntimeFault,
			RemoteException, TaskInProgress, InvalidState {
		List<VM> vmList = hostWithLowestCpuUsage.getVMs();
		for (VM vm : vmList) {
					migrate(hostWithHighestCpuUsage, hostWithLowestCpuUsage, vm.getVM());
		}
		ServiceInstance parentSi = Config.getServiceInstance( new URL(Config.getProperty( ConfigIdents.VMWARE_PARENTHOST )) );
		ManagedEntity[] mes = new InventoryNavigator(parentSi.getRootFolder()).searchManagedEntities(Config.VMWARE_IDENTIFIER_VIRTUAL_MACHINE);
		for (ManagedEntity managedEntity : mes) {
			VirtualMachine vm2 = (VirtualMachine) managedEntity;
			String[] tokens = hostWithLowestCpuUsage.getName().split("\\.");
			if(vm2.getName().contains(tokens[3]))
			{
				System.out.println(vm2.getName());
				Task suspendTask = vm2.suspendVM_Task();
				String result = suspendTask.waitForMe();       
			    if(result == Task.SUCCESS) 
			    {
			      System.out.println("Host Suspended Sucessfully");
			      break;
			    }
			    else 
			    {
			      System.out.println("Host could not be created. ");
			    }
			}
		}
	}

	/**
	 * Description: Compare existing vhost and check its threshold
	 * and then find vm load of that higher vhost
	 * select the highest vm and migrate it to the new vhost
	 * @param hostWithLowestCpuUsage
	 * @param hostWithHighestCpuUsage
	 */
	private void startDRS2(VHost hostWithLowestCpuUsage,
			VHost hostWithHighestCpuUsage) {
		List<VM> vmList = hostWithHighestCpuUsage.getVMs();
		migrate(hostWithLowestCpuUsage, hostWithHighestCpuUsage, vmList.get(0).getVM());
	}

	/**
	 * @param destination host
	 * @param source host
	 * @param VM to migrate
	 */
	private void migrate(VHost destination, VHost source, VirtualMachine vmToMigrate) {
		
		MigrateVM migrateVM = new MigrateVM(destination.getHost(), vmToMigrate, si);
		migrateVM.start();
	}

	/**
	 * Description: When creating a vm 
	 * find CPU usage of vhost 
	 * then determine host with lesser CPU usage 
	 * add the new vm to the selected host
	 * @param hostWithLowestCpuUsage
	 */
	private void startDRS1(VHost hostWithLowestCpuUsage) {
		if(hostWithLowestCpuUsage != null)
		{
			CreateVM createVM = new CreateVM("datacenter_t04", "DRS1_VM_" + new Random().nextInt(), hostWithLowestCpuUsage, si);
			createVM.start();
		}
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
	
	/**
	 * @param cpuUsage
	 * @return
	 */
	private VHost findHighestCpuUsageHost(long cpuUsage) {
		VHost hostWithHighestCpuUsage = null;
		for (VHost vHost : vHosts) {
			if(cpuUsage == 0 || cpuUsage < vHost.cpuUsageMhz())
			{
				cpuUsage = vHost.cpuUsageMhz();
				hostWithHighestCpuUsage = vHost;
			}	
		}
		return hostWithHighestCpuUsage;
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
}
