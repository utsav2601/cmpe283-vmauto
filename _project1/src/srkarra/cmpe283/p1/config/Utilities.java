package srkarra.cmpe283.p1.config;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

import srkarra.cmpe283.p1.config.RestApi.HTTPPostResponse;
import com.vmware.vim25.HostCpuInfo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vmware.vim25.HostListSummary;
import com.vmware.vim25.HostListSummaryQuickStats;
import com.vmware.vim25.PerfEntityMetric;
import com.vmware.vim25.PerfEntityMetricBase;
import com.vmware.vim25.PerfMetricId;
import com.vmware.vim25.PerfMetricIntSeries;
import com.vmware.vim25.PerfMetricSeries;
import com.vmware.vim25.PerfProviderSummary;
import com.vmware.vim25.PerfQuerySpec;
import com.vmware.vim25.PerfSampleInfo;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.VirtualMachineCapability;
import com.vmware.vim25.VirtualMachineConfigInfo;
import com.vmware.vim25.VirtualMachineQuickStats;
import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.VirtualMachineSummary;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.PerformanceManager;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

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
	 * Retrieves some quick statistics of the virtual machine including but not limited to:
	 * 	Name, OS Type, Snapshot, Power State, Running Time, CPU/Memory/HardDisk Usage
	 * @param vm instance of the virtual machine, must not be null
	 * @return VMStatictics object containing some quick facts of the virtual machine 
	 * @throws RuntimeFault failed to retrieve vm's information
	 * @throws RemoteException failed to retrieve vm's information
	 */
	public static VmStatistics getVmStatistics(VirtualMachine vm, ServiceInstance si) throws RuntimeFault, RemoteException {
		if(vm == null) {
			return null;
		}
		// Variable Initialization
		final VirtualMachineConfigInfo vminfo 		= vm.getConfig();
		final VirtualMachineCapability vmc 			= vm.getCapability();
		final VirtualMachineRuntimeInfo vmruntime 	= vm.getRuntime();
		final VirtualMachineSummary vmsum 			= vm.getSummary();
		final VirtualMachineQuickStats vmqstats 	= vmsum.getQuickStats();

		vm.refreshStorageInfo();
		final Calendar 	bootTime 					= vm.getRuntime().getBootTime();
		final String 	poweredState 				= vmruntime.getPowerState().toString();
		final String 	poweredOnTime 				= bootTime == null ? "<data missing>" : String.format("%d mins", ((GregorianCalendar.getInstance().getTimeInMillis() - bootTime.getTimeInMillis())/1000/60));
		final String 	commitedStorage 			= String.format("%d MB", (vmsum.getStorage().getCommitted()/1024/1024));
		final Integer 	overallCpuUsage 			= vmqstats.getOverallCpuUsage();
		final String 	guestFullName 				= vminfo.getGuestFullName().trim();
		final String 	ipAddress 					= vmsum.getGuest().getIpAddress();
		final Integer 	guestMemoryUsage 			= vmqstats.getGuestMemoryUsage();
		final Integer 	privateMemory 				= vmqstats.getPrivateMemory();
		final boolean 	multipleSnapshotsSupported 	= vmc.isMultipleSnapshotsSupported();
		final String 	vmName 						= vm.getName().trim();
		final VmStatistics vmStats;
		
		// Create VM Statistics Object and assign local values
		vmStats = (VmStatistics) getMetrics(si, vm.getName(), vm, Config.TYPE_VIRTUALMACHINE); 
		
		//Get Thread and Process count
		try{
			String resultProcess = getStats("ps -e | wc -l");
			vmStats.setProcessCount(Integer.parseInt(resultProcess));
			 
			String resultThread = getStats("ps -eaFM | wc -l");
			vmStats.setThreadCount(Integer.parseInt(resultThread));
		}
		catch(Exception ee){/*do nothing*/};
		
		vmStats.setVmName			( vm.getName()				 );
		vmStats.setTimeStamp		( new Date()				 );
		vmStats.setCpuUsage			( overallCpuUsage 			 );
		vmStats.setGuestFullName	( guestFullName 			 );
		vmStats.setGuestIpAddress	( ipAddress 				 );
		vmStats.setGuestMemoryUsage	( guestMemoryUsage 			 );
		vmStats.setMaxHostMemory	( privateMemory 			 );
		vmStats.setPowerState		( poweredState 				 );
		vmStats.setStorageUsed		( commitedStorage 			 );
		vmStats.setSupportsSnapShot	( multipleSnapshotsSupported );
		vmStats.setSystemUpTime		( poweredOnTime 			 );
		vmStats.setVmName			( vmName 					 );
		vmStats.setTimeStamp		( new Date()				 );
		System.out.println(vmStats);
		
		ObjectMapper mapper = new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		try {
			String writeValue = mapper.writeValueAsString(vmStats);
			HTTPPostResponse response = doPost(Config.REST_API_POST_VM_URL, writeValue);
			System.out.println(response);
			if(response != null)
			{
				if(response.getResponseCode() == Config.REST_API_RESPONSE_SUCCESS)
					System.out.println("Data saved succesfully for " + vmStats.getVmName());
				else
					System.out.println("Unable to get response from webservice");
			}
		} catch (JsonProcessingException e) {
			System.out.println("Json Processing Exception: " + e.getMessage());
		}
		
		return  vmStats;
	}
	
	/**
	 * Retrieves some quick statistics of the vHost including but not limited to:
	 * 	Name, OS Type, Snapshot, Power State, Running Time, CPU/Memory/HardDisk Usage
	 * @param host instance of the virtual machine, must not be null
	 * @return VMStatictics object containing some quick facts of the virtual machine 
	 * @throws RuntimeFault failed to retrieve vm's information
	 * @throws RemoteException failed to retrieve vm's information
	 */
	public static HostStatistics getHostStatistics(HostSystem host, ServiceInstance si) throws RuntimeFault, RemoteException {
		if(host == null) {
			return null;
		}
		HostStatistics hostStats;
		HostCpuInfo cpuInfo = host.getHardware().getCpuInfo();
		final HostListSummary hostListSummary 		= host.getSummary();
		final HostListSummaryQuickStats hostqstats 	= hostListSummary.getQuickStats();
		
		final Integer 	overallCpuUsage 			= hostqstats.getOverallCpuUsage();
		final Integer 	overallMemUsage 			= hostqstats.getOverallMemoryUsage();
		final Integer 	distCpuFairness 			= hostqstats.getDistributedCpuFairness();
		final Integer 	distMemFairness 			= hostqstats.getDistributedMemoryFairness();
		final long   	cpuHz						= cpuInfo.getHz() * cpuInfo.getNumCpuCores();
		
		hostStats = (HostStatistics) getMetrics(si, host.getName(), host, Config.TYPE_HOST);		
		
		hostStats.setName(host.getName());
		hostStats.setTimeStamp(new Date());
		hostStats.setCpuHz(cpuHz);
		hostStats.setCpuUsage(overallCpuUsage);
		hostStats.setMemUsage(overallMemUsage);
		hostStats.setCpuFairness(distCpuFairness);
		hostStats.setMemFairness(distMemFairness);
		
		System.out.println(hostStats);
		
		ObjectMapper mapper = new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		try {
			String writeValue = mapper.writeValueAsString(hostStats);
			HTTPPostResponse response = doPost(Config.REST_API_POST_HOST_URL, writeValue);
			System.out.println(response);
			if(response != null)
			{
				if(response.getResponseCode() == Config.REST_API_RESPONSE_SUCCESS)
					System.out.println("Data saved succesfully for " + hostStats.getName());
				else
					System.out.println("Unable to get response from webservice");
			}
			
		} catch (JsonProcessingException e) {
			System.out.println("Json Processing Exception: " + e.getMessage());
		}
		
		return  hostStats;
	}
	
	public static LogStatistics getLogStatistics(String command, String vmName) throws RuntimeFault, RemoteException {
		LogStatistics logStats = new LogStatistics();
		try {
			logStats.setFileContent(getStats(command));
			logStats.setFileName(command);
			logStats.setVmName(vmName);
			logStats.setTimeStamp(new Date());
			System.out.println(logStats);
		
			ObjectMapper mapper = new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		
			String writeValue = mapper.writeValueAsString(logStats);
			HTTPPostResponse response = doPost(Config.REST_API_POST_VM_LOG, writeValue);
			System.out.println(response);
			if(response != null)
			{
			if(response.getResponseCode() == Config.REST_API_RESPONSE_SUCCESS)
			{
				System.out.println("Data saved succesfully for " + logStats.getVmName());
			}
			else
				System.out.println("Unable to get response from webservice");
			}
			
		} 
		catch (JsonProcessingException e) {
			System.out.println("Json Processing Exception: " + e.getMessage());
		}
		catch (FileNotFoundException e) {
			System.out.println("File Not Found Exception: " + e.getMessage());
        }
		catch (IOException e){
			System.out.println("IO Exception: " + e.getMessage());
		}

		return  logStats;
	}
	
	private static String getStats(String cmd) throws IOException
	{
		Runtime runTime = Runtime.getRuntime();
		Process process = runTime.exec(cmd);

		return getCommandResullt(process);
	}

	/**
	 * @param cmdResult
	 * @param process
	 * @return
	 * @throws IOException
	 */
	private static String getCommandResullt(Process process)
			throws IOException {
		String cmdResult = null;
		BufferedReader bufferedReader = new BufferedReader(new
		InputStreamReader(process.getInputStream()));
		String inputLine;
		while ((inputLine = bufferedReader.readLine()) != null) {
			cmdResult += inputLine;
		}		
		bufferedReader.close();
		return cmdResult;
	}
	
	public static Object getMetrics(ServiceInstance si, String entityName, ManagedEntity vEntity, int type)
	{
		try {
			int[] metricID = {125, 130, 131, 132, 143, 180, 181, 394, 395, 172};
			PerformanceManager perfManager = si.getPerformanceManager();
			
			PerfProviderSummary pps = perfManager.queryPerfProviderSummary(vEntity);
			int refreshRate = pps.getRefreshRate();
			
			ArrayList<PerfMetricId> wantedPerformanceMetrics = new ArrayList<PerfMetricId>();
			
			for (int i=0; i < metricID.length; i++)
			{
				PerfMetricId perfMetric = new PerfMetricId();
				perfMetric.setCounterId(metricID[i]);
				perfMetric.setInstance("");
				wantedPerformanceMetrics.add(perfMetric);
			}
			
			PerfMetricId[] pmis = wantedPerformanceMetrics.toArray(
					new PerfMetricId[(wantedPerformanceMetrics.size())]);
			
			// Set up query for metrics
			PerfQuerySpec qSpec = new PerfQuerySpec();
			qSpec.setEntity(vEntity.getMOR());
			qSpec.setMaxSample(3);
			qSpec.setMetricId(pmis);
			qSpec.setIntervalId(refreshRate);
			qSpec.setFormat("normal");
			PerfEntityMetricBase[] pembs = null;
			
			try { pembs = perfManager.queryPerf(new PerfQuerySpec[] {qSpec}); }
			catch(Exception ee){}
			Object statistics;
			if(type == Config.TYPE_HOST)
			{
				statistics = new HostStatistics();
			}
			else
			{
				statistics =  new VmStatistics();
			}
			
			for (int i=0; pembs != null && i<pembs.length; i++)
			{
				PerfEntityMetricBase val = pembs[i];
				PerfEntityMetric pem = (PerfEntityMetric) val;
				PerfMetricSeries[] vals = pem.getValue();
				PerfSampleInfo[] infos = pem.getSampleInfo();
				
				for (int j=0; vals != null && j<vals.length; ++j)
				{
					
					PerfMetricIntSeries val1 = (PerfMetricIntSeries) vals[j];
					System.out.println("Printing counter ID: " + val1.getId().getCounterId());
					long[] longs = val1.getValue();
					if(type == Config.TYPE_HOST)
					{
						if (val1.getId().getCounterId() == 125)
							((HostStatistics) statistics).setDiskUsageAverage((int) longs[longs.length-1]);
						else if (val1.getId().getCounterId() == 130)
							((HostStatistics) statistics).setDiskReadAverage((int) longs[longs.length-1]);
						else if (val1.getId().getCounterId() == 131)
							((HostStatistics) statistics).setDiskWriteAverage((int) longs[longs.length-1]);
						else if (val1.getId().getCounterId() == 132)
							((HostStatistics) statistics).setDiskTotalLantency((int) longs[longs.length-1]);
						else if (val1.getId().getCounterId() == 143)
							((HostStatistics) statistics).setNetUsageAverage((int) longs[longs.length-1]);
						else if (val1.getId().getCounterId() == 180)
							((HostStatistics) statistics).setDatastoreReadAverage((int) longs[longs.length-1]);
						else if (val1.getId().getCounterId() == 181)
							((HostStatistics) statistics).setDataStoreWriteAverage((int) longs[longs.length-1]);
						else if (val1.getId().getCounterId() == 394)
							((HostStatistics) statistics).setNetBytesRxAverage((int) longs[longs.length-1]);
						else if (val1.getId().getCounterId() == 395)
							((HostStatistics) statistics).setNetBytesTxAverage((int) longs[longs.length-1]);
					}
					else
					{
						if (val1.getId().getCounterId() == 125)
							((VmStatistics) statistics).setDiskUsageAverage((int) longs[longs.length-1]);
						else if (val1.getId().getCounterId() == 130)
							((VmStatistics) statistics).setDiskReadAverage((int) longs[longs.length-1]);
						else if (val1.getId().getCounterId() == 131)
							((VmStatistics) statistics).setDiskWriteAverage((int) longs[longs.length-1]);
						else if (val1.getId().getCounterId() == 132)
							((VmStatistics) statistics).setDiskTotalLantency((int) longs[longs.length-1]);
						else if (val1.getId().getCounterId() == 143)
							((VmStatistics) statistics).setNetUsageAverage((int) longs[longs.length-1]);
						else if (val1.getId().getCounterId() == 180)
							((VmStatistics) statistics).setDatastoreReadAverage((int) longs[longs.length-1]);
						else if (val1.getId().getCounterId() == 181)
							((VmStatistics) statistics).setDataStoreWriteAverage((int) longs[longs.length-1]);
						else if (val1.getId().getCounterId() == 394)
							((VmStatistics) statistics).setNetBytesRxAverage((int) longs[longs.length-1]);
						else if (val1.getId().getCounterId() == 395)
							((VmStatistics) statistics).setNetBytesTxAverage((int) longs[longs.length-1]);
					}
					
					System.out.println("CounterID: " + val1.getId().getCounterId()
							+ " Timestamp: " + infos[longs.length-1].getTimestamp().getTime()
							+ " Metric Value: " + longs[longs.length-1]); 
									
				}
			}
			return statistics;
		} catch (RuntimeFault e) {
			System.out.println("Runtime Fault: " + e.getMessage());
			e.printStackTrace();
		} catch (RemoteException e) {
			System.out.println("Remote Exception" + e.getMessage());
		}
		return null;
		
	}
	
	/**
     * HTTP Post
     */
    public static HTTPPostResponse doPost(String url, String payload) {
        try {
            // Create Connection
            HttpURLConnection post = (HttpURLConnection) new URL(url).openConnection();

            post.setRequestMethod("POST");
            post.setDoOutput(true);
            post.setRequestProperty("Content-Type", "application/json");

            // Write URL Params
            DataOutputStream outstream = new DataOutputStream(post.getOutputStream());
            outstream.writeBytes(payload);
            outstream.flush();
            outstream.close();
            
            // Get Response Code

            HTTPPostResponse response = new HTTPPostResponse(post.getResponseCode());

            // Get Response Msg if post returns a valid HTTP code

            if(response.getResponseCode() == 200) {

                BufferedReader in = new BufferedReader(new InputStreamReader(post.getInputStream()));

                String inputLine;
                String responseMsg = "";

                while ((inputLine = in.readLine()) != null) {
                    responseMsg += inputLine;
                }
                in.close();
               
                response.setResponseMsg(responseMsg);
            }
            return response;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}