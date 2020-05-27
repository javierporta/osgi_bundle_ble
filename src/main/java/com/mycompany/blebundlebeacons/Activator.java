package com.mycompany.blebundlebeacons;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {

    private ServiceRegistration registration;

    public void start(BundleContext context) throws Exception {
        String userName = context.getProperty("user.name");
        System.out.println("Maven Hello Service: Started OSGi bundle");
        System.out.println("User Name: " + userName);
        
        cBluetoothService service = new cBluetoothService();
        registration=context.registerService(cBluetoothService.class.getName(),service,null);
        System.out.println("Hello Service is now registered...");
        
        
        //try calling a function of a register service
        service.SendEvent(context);
        

    }

    public void stop(BundleContext context) throws Exception {
        System.out.println("Maven Hello Service: Stopped OSGi bundle");
        
        //Unregister service
        registration.unregister();
        System.out.println("Hello Service is now unregistered...");
    }

}
