/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.blebundlebeacons;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.osgi.framework.BundleContext;
import tinyb.BluetoothDevice;
import tinyb.BluetoothException;
import tinyb.BluetoothManager;

/**
 *
 * @author java
 */
public class cBluetoothService implements iBluetoothService {
    //ibeacon
    //private String BEACON_MAC_ADDRESS = "4C:A3:6C:AB:57:68";
    //eddystone
    private String BEACON_MAC_ADDRESS = "57:29:D2:AD:DB:8C";
    
    public void SendEvent(BundleContext context)
    {
       BluetoothDevice sensor = null;
        
       BluetoothManager manager = BluetoothManager.getBluetoothManager();
       manager.startDiscovery();
        System.out.println("started discovering");
        try {
            BluetoothDevice device = getDevice(manager);
            manager.stopDiscovery();
            
            System.out.println("Using device with MAC" + device.getAddress());
            printDevice(device);
            String urlBroadcasted = getBeaconUrlBroadcastedHexadecimal(device);
            System.out.println("URL broadcasted > " + urlBroadcasted);
            
            //Now publish url in EventAdmin
            EventSender eSender = new EventSender();
            eSender.sendBlePacketReceived(urlBroadcasted, device.getAddress(), context);
        } catch (InterruptedException ex) {
            Logger.getLogger(cBluetoothService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     BluetoothDevice getDevice(BluetoothManager manager) throws InterruptedException {
         //Try searching for the device 10 times, splitted by 1 second
        for (int i = 0; i < 10; i++) {
            System.out.print("..");
            System.out.println();
            List<BluetoothDevice> list = manager.getDevices();
            if (list == null)
                return null;
            for (BluetoothDevice device : list) {
                System.out.println(device.getAddress());
                if (BEACON_MAC_ADDRESS.equals(device.getAddress())) {
                    return device;
                }
            }
            Thread.sleep(1000);
        }
        //Did not found the beacon
        System.out.println("Beacon not found. Make sure the MAC is corrrect");
        return null;
    }
     
     void printDevice(BluetoothDevice device) {
        System.out.print(" Address = " + device.getAddress());
        System.out.print(" Name = " + device.getName());
        System.out.print(" Connected = " + device.getConnected());
        
        System.out.print(" UUID length= " + device.getUUIDs().length);
        System.out.print(" UUID = " + device.getUUIDs());
        System.out.print(" RSSI = " + device.getRSSI());
        
        Map<Short, byte[]> serviceManufacturer = null;
        
        serviceManufacturer = device.getManufacturerData();
        
   
            if(serviceManufacturer == null){
                System.out.println("Service manufacturer is null...");
                
                //System.exit(0);
            }
            if(serviceManufacturer.isEmpty()){
                System.out.println("Service manufacturer is empty...");
                System.out.println(serviceManufacturer);
                
            } 
    }
     
     String getBeaconUrlBroadcastedHexadecimal(BluetoothDevice device){
         Map<String, byte[]> servicedata = null;
              servicedata = device.getServiceData();
            if(servicedata == null){
                System.out.println("Service data is null...");
                
                //System.exit(0);
            }
            if(servicedata.isEmpty()){
                System.out.println("Service data is empty...");
                System.out.println(servicedata);
                
            }
            
            
         System.out.println(servicedata.size());
            System.out.println(servicedata.values());
            
            
            for(String key : servicedata.keySet()){
		System.out.println("Key=" + key + " Value=" + Arrays.toString(servicedata.get(key)));
                byte[] IDRaw = null;
                try{
                    IDRaw = servicedata.get(key);
                }catch(BluetoothException e){
                    System.out.println("BluetoothException while reading Gatt characteristic");
                }
                if(IDRaw == null){
                    System.out.println("Error while reading Gatt characteristic => null");
                }
                String IDString = "";
                for (byte b : IDRaw) {
                    IDString = IDString + String.format("%02x", b );
                }
                //Here is the url for eddystone
                System.out.println("ID raw data = " + IDString);
                return IDString;
            }
        System.out.println();
        return "";
     }
}