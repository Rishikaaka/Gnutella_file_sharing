/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package javaapplication12;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author Rishika
 */
public class Server implements Runnable {
    
    public void run() {
    try{  
        
       int port = 0; 
       String combinedValue =Thread.currentThread().getName();            //Getting the combined server thread name which contains peers portname and topology
       ArrayList<String> temp = new ArrayList<String>();
       StringTokenizer st = new StringTokenizer(combinedValue, ",");
       while(st.hasMoreTokens()){                                         // using string tokenizer to split the thread
           temp.add(st.nextToken());
       }
       String portName = temp.get(0);                                     // storing portnames
       String topology = temp.get(1);                                     // storing topology name
       
       propFileLoading pfl = new propFileLoading(topology);               // loading property file with given topology
         try {
           port =  pfl.getPortNumber(portName);                           // getting portnumbers from propety file for the corresponding portname
         } catch (IOException ex) {
             Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
         }
        //int port = pfl.getPortNumber(portName);
        Registry registry = LocateRegistry.createRegistry(port);            //Creating RMI registry for the given port number
        ServerInterface stub =(ServerInterface) new ServerImplementation();
        registry.bind("server", stub);
        System.out.println("successfully created "+ portName + " server");
         }catch(Exception e)
         {
             System.err.println("server exception:  "+e.toString());
             e.printStackTrace();
         }
        
    }   
    public static void main(String args[])
    {
        Server ts = new Server();
        ts.run();                                                           // Strating server thread
        
    }
     
}
