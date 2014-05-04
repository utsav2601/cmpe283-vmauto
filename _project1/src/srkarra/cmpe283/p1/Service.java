/**
 * 
 */
package srkarra.cmpe283.p1;

import java.util.Date;

import srkarra.cmpe283.p1.config.Config;
import srkarra.cmpe283.p1.config.Manager;
import srkarra.cmpe283.p1.config.Utilities;

import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

/**
 * @author Asim Mughni
 *
 */
public class Service extends Thread {
	private static ServiceInstance serviceInstance;
	public void run() {
		while(true){
			try{
				System.out.println("Storing Stats at " + new Date().getTime());
				serviceInstance = Config.getServiceInstance();
//				storeStats();
//				Thread.sleep(10800000);
				Manager mgr = new Manager(serviceInstance);
				mgr.start();
			}
			catch(Exception ee){
			}
		}
	}

	private void storeStats() {
		try {
			
			//Store VM Stats
			ManagedEntity[] mes = new InventoryNavigator(serviceInstance.getRootFolder()).searchManagedEntities(Config.VMWARE_IDENTIFIER_VIRTUAL_MACHINE);
			for (ManagedEntity managedEntity : mes) {
				VirtualMachine vm = (VirtualMachine) managedEntity;
				System.out.println(Utilities.getVmStatistics(vm, serviceInstance));
				
				//Store Log Stats
//				System.out.println(Utilities.getLogStatistics("vmstat"  , vm.getName()));
//				System.out.println(Utilities.getLogStatistics("iostat", vm.getName()));
//				System.out.println(Utilities.getLogStatistics("mpstat", vm.getName()));
			}
			
			//Store Host Stats
			ManagedEntity[] hosts = new InventoryNavigator(serviceInstance.getRootFolder()).searchManagedEntities(new String[][] { {Config.VMWARE_IDENTIFIER_VHOSTS, Config.VMWARE_IDENTIFIER_VHOSTS_NAME }, }, true);
			for(int i=0; i < hosts.length; i++)
			{
				HostSystem hostSystem = (HostSystem) hosts[i];
				System.out.println(Utilities.getHostStatistics(hostSystem, serviceInstance));
			}
		}
		catch (Exception e) {
			System.out.println("Failed to fetch statistics. Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
