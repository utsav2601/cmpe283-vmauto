package edu.sjsu.cmpe283;

import java.io.FileNotFoundException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class VMWareWebServices {
    public static void main( String[] args ) throws Exception {
        // Check app props
        if(VMWareWebServices.class.getResource("/application.properties") == null) {
            throw new FileNotFoundException("Application properties is missing!");
        }
        
        SpringApplication app = new SpringApplication(VMWareWebServices.class);
        app.setShowBanner(false);
        app.run(args);
    }
}
