package srkarra.cmpe283.p1.config;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.vmware.vim25.mo.ServiceInstance;

/**
 * Utilities.java
 *  Responsible for the configuration of the engine 
 *  Holds access to the properties and global objects such as the vmware service instance 
 *  
 * 
 * @author Sai Karra
 *
 */
public class Config {
	
	public enum ConfigIdents {
		VMWARE_HOST  ("vmware.host"),
		VMWARE_USER  ("vmware.user"),
		VMWARE_PASS  ("vmware.pass"),
		
		VM_HEARTBEAT("vm.heartbeat"),
		VM_SNAPSHOT_INTERVAL("vm.snapshot.interval"),
		VM_SNAPSHOT_MEMORY("vm.snapshot.memory"),
		VM_SNAPSHOT_QUIESCE("vm.snapshot.quiesce"),
		VHOST_REVIVE_ATTEMPTS("vhost.revive.attempts"),
		VHOST_REVIVE_TIMEOUT("vhost.revive.timeout"),
		VHOST_REVIVE_INTERVAL("vhost.revive.interval")
		;
		
		private String ident;
		private ConfigIdents(String ident) {
			this.ident = ident;
		}
		public String getIdentifier() {
			return ident;
		}
	}
	
	
	
	public static final String VMWARE_IDENTIFIER_VIRTUAL_MACHINE 		= "VirtualMachine";
	public static final String VMWARE_IDENTIFIER_VHOSTS					= "HostSystem";
	public static final String VMWARE_IDENTIFIER_VHOSTS_NAME			= "name";
	public static final String VMWARE_IDENTIFIER_DATACENTER		 		= "Datacenter";
	public static final String VMWARE_SNAPSHOT_NAME_SUFFIX				= "-snapshot";
	public static final String VMWARE_TASK_SUCCESS_STRING 				= "success";
	public static final String VMWARE_POWER_ON_ALARM_TRIG_SUFFIX		= "-OnAlarmTrig";
	
	public static final String 	REST_API_POST_HOST_URL	  	= "http://cmpe283.srkarra.com/stats/vhost";
	public static final String 	REST_API_POST_VM_URL		= "http://cmpe283.srkarra.com/stats/vm";
	public static final String 	REST_API_POST_VM_LOG		= "http://cmpe283.srkarra.com/stats/log";
	public static final int 	REST_API_RESPONSE_SUCCESS 	= 200;
	
	public static final int 	TYPE_HOST 			= 1;
	public static final int 	TYPE_VIRTUALMACHINE = 2;
	
	private static Config config;
	private Properties properties;
	private static List<ServiceInstance> serviceInstances;
	
	
	
	/****************************
	 *    Constructor/Access    *
	 ***************************/
	
	/**
	 * Configure config facade
	 * @param filename name of properties filename
	 */
	public static void createInstance(String filename) throws IOException {
		System.err.println("Creating Config Instance");
		Config.config = new Config(filename);
	}
	
	/**
	 * Return the Config instance
	 * @return instance of Config
	 */
	public static Config getInstance() {
		return config;
	}
	
	/**
	 * Configure the Config facade 
	 * @param filename name of the properties file
	 */
	private Config(String filename) throws IOException {
		properties = Utilities.readProperties(filename);
		serviceInstances = new ArrayList<>();
	}
	
	/**
	 * Destroy function that releases resources
	 */
	public void destroy() {
		System.err.println("Destorying Configuration and Service Instances");
		// Logout of vmware
		for (ServiceInstance serviceInstance : serviceInstances) {
			serviceInstance.getServerConnection().logout();
		}
	}
	
	
	/**
	 * Get property value
	 * @param configIdent property identifier
	 * @return value associated with the given key, returns null if it doesn't exist
	 */
	public static String getProperty(ConfigIdents configIdent) {
		return config.properties.getProperty(configIdent.getIdentifier());
	}
	
	
	
	/**
	 * Returns instance of the Service Instance  
	 */
	public static ServiceInstance getServiceInstance()  {
		try {
			ServiceInstance si = new ServiceInstance(new URL(getProperty(ConfigIdents.VMWARE_HOST)), getProperty(ConfigIdents.VMWARE_USER), getProperty(ConfigIdents.VMWARE_PASS), true);
			serviceInstances.add(si);
			return si;
		}
		catch (Exception e) {
			System.err.println("FAILED TO CREATE A SERVICE INSTANCE!");
			e.printStackTrace();
			return null; 
		}
	}
	
	
	

}
