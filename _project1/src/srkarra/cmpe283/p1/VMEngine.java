package srkarra.cmpe283.p1;

import java.io.IOException;

import srkarra.cmpe283.p1.config.Config;
import srkarra.cmpe283.p1.manager.UserPrompt;

/**
 * VMEngine Class
 * 
 * The portal into the system
 * Prints the command line arguments if required
 * 
 * @author Sai Karra
 */
public class VMEngine {

	public static void main(String[] args) throws IOException {
		// Print usage information
		if(args.length <= 0) {
			System.out.println("Must provide the properties file.");
			System.out.println("java -cp ... srkarra.cmpe283.p1.VMEngine config.properties");
			System.exit(1);
		}

		// Configure Configuration
		String filename = args[0];
		Config.createInstance(filename);
		
		// Start the Prompt System
		UserPrompt userPrompt = new UserPrompt();
		userPrompt.enterUserPrompt();
	}
}
