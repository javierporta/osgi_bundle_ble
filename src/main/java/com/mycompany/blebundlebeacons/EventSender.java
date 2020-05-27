/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.blebundlebeacons;

import java.math.BigInteger;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;


/**
 *
 * @author java
 */
public class EventSender {
    
    public void sendBlePacketReceived(String data, String beaconMAC, BundleContext context)
    {
        ServiceReference ref = context.getServiceReference(EventAdmin.class.getName());
        if (ref != null)
        {
            EventAdmin eventAdmin = (EventAdmin) context.getService(ref);

            if(eventAdmin == null)
            {
                System.out.println("Event admin null");
                return;
            }
            
            Map<String,String> params = new HashMap();
            params.put("message", data);

            String replacedMAC = beaconMAC.replace(":","-");
            Event blePacketReceivedEvent = new Event("msg/beacon/eddystoneURL/"+replacedMAC, params);

            eventAdmin.sendEvent(blePacketReceivedEvent);
            System.out.println("Event sent");
            System.out.println("beaconMAC ~ " + beaconMAC);
            System.out.println("message ~ " + data);
            }
            else
            {
                System.out.println("reference is null");
            }
    }
}
