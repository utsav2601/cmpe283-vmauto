package srkarra.cmpe283.p1.manager;

import java.util.Timer;
import java.util.TimerTask;

import srkarra.cmpe283.p1.config.Config;
import srkarra.cmpe283.p1.config.Utilities;
import srkarra.cmpe283.p1.config.Config.ConfigIdents;

import com.vmware.vim25.mo.ServiceInstance;

/**
 * Class: RefreshBackupCacheThread
 * Refreshes the backup cache for the virtual machine
 * NOTE: The backup cache is actually snapshots
 * 
 * @author Sai Karra
 */
public class RefreshBackupCacheThread extends TimerTask {
	
	private Timer timer;
	private Boolean enabled;
	private long sleepInterval; 
	private ServiceInstance serviceInstance;
	
	/**
	 * Instanciate the backup thread
	 */
	public RefreshBackupCacheThread() {
		this.enabled = true;
		this.serviceInstance = Config.getServiceInstance();
		this.sleepInterval = Long.parseLong(Config.getProperty(ConfigIdents.VM_SNAPSHOT_INTERVAL));
	}
	
	
	/**
	 * Run the backup cache code
	 */
	public void run() {
		if(enabled) {
			System.err.println("Creating a Snapshot on all Virtual Machines!");
			Utilities.createASnapShotOnAllVMs(serviceInstance);
			System.err.println("Snapshots for all Virtual Machines Created!");
		}
	}
	
	
	/**
	 * The timer that currently runs the current timer task thread
	 * @return the Timer
	 */
	public Timer getTimer() {
		return timer;
	}
	
	/**
	 * Set the timer that runs this timer task thread
	 * @param timer the time to set to
	 */
	public void setTimer(Timer timer) {
		this.timer = timer;
	}
	
	/**
	 * Enable the backup cache thread
	 */
	public void enable() {
		enabled = true;
	}
	
	/**
	 * Disable the thread from running
	 */
	public void disable() {
		enabled = false;
	}
	
	
	/**
	 * Sleep Interval at which this program currently sleeps at
	 * @return the sleep interval between refreshes
	 */
	public long getSleepInterval() {
		return sleepInterval;
	}

	/**
	 * Set the Sleep Interval
	 * @param sleepInterval set the sleep interval
	 */
	public void setSleepInterval(long sleepInterval) {
		this.sleepInterval = sleepInterval;
	}

	
}
