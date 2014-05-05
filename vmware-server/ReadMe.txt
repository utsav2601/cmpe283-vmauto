README: CMPE 283 - VMware DRS/DPM and Charting by Team 04
==========================================================================================================================================

Project is hosted at the following URL: "cmpe283.srkarra.com" and will be available until June 1, 2014.


=====================================================================

Deployment Instructions:
----------------------------------------
To create the application from scratch you must complete the following:
1) Install MongoDB (or use MongoHQ), Maven, (Optionally, Spring.io's Tool Suite "STS")
2) Update the application.properties file in {projectLocation}/src/main/resources with the necessary properties.
4) Run the application with the following maven command "mvn clean install exec:java"
	  - If you wish not to configure maven, you may import this maven project into STS and the run this main function located in "edu.sjsu.cmpe283.VMWareWebServices.java"
	  - NOTE for linux users: This application runs on the port 80, you must run this application with privileged access (aka sudo).
	  - NOTE: change the port by passing in the following argument: "--server.port=8080" or change the port at the bottom of the pom.xml maven file.
5) Go to localhost (or the correct your url) in your favorite browser (IE is not supported at this time) and test available functions.


