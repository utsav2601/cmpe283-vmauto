The main class is the "VMEngine.java" class located in the srkarra.cmpe283.p1 package


It takes the properties filename as the parameter. For example:
    java -cp ... srkarra.cmpe283.p1.VMEngine config.properties
    
    


Properties Explained:
    vm.heartbeat          - Seconds between a heart beat test, time in seconds
    vhost.revive.attempts - The number of times a Host System should attempted to revive 
    vhost.revive.interval - The time between each attempted revival
    vhost.revive.timeout  - The timeout to be specified to VMware to power on the connection 
    vm.snapshot.interval  - The seconds between when each snapshot should be taken
    vm.snapshot.memory    - Boolean, true to enable memory contents in the snapshot
    vm.snapshot.quiesce   - See vmware specification
    vmware.host           - vmWARE's host user
    vmware.user           - vmWARE username
    vmware.pass           - vmWARE password