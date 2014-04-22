package srkarra.cmpe283.p1.manager;

import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.VirtualMachine;

/**
 * FailedVMPair.java
 * A Domain class used to link a failed VM and it's host systmer
 * 
 * @author Sai Karra
 */
public class FailedVMPair {
	
	
	private HostSystem hostSystem;
	private VirtualMachine virtualMachine;
	
	
	
	/**
	 * Instanciate the vm and host pair  
	 * @param hostSystem Host System in the pair
	 * @param virtualMachine Virtual Machine in the pair
	 */
	public FailedVMPair(HostSystem hostSystem, VirtualMachine virtualMachine) {
		this.hostSystem = hostSystem;
		this.virtualMachine = virtualMachine;
	}

	/**
	 * Returns the host system
	 * @return Returns the host system
	 */
	public HostSystem getHostSystem() {
		return hostSystem;
	}

	/**
	 * Set the host system
	 * @param hostSystem the host system to set to
	 */
	public void setHostSystem(HostSystem hostSystem) {
		this.hostSystem = hostSystem;
	}
	
	/**
	 * Returns the virtual machine
	 * @return Returns the virtual machine
	 */
	public VirtualMachine getVirtualMachine() {
		return virtualMachine;
	}


	/**
	 * Set the virtual machine
	 * @param virtualMachine the virtual machine to set
	 */
	public void setVirtualMachine(VirtualMachine virtualMachine) {
		this.virtualMachine = virtualMachine;
	}

	/**
	 * To String method used for logging, prints only the name of the vm, must not be null
	 */
	public String toString() {
		return "FailedVMPair [hostSystem=" + hostSystem.getName() + ", virtualMachine=" + virtualMachine.getName() + "]";
	}
	

}
