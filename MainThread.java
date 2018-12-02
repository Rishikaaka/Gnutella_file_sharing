/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package javaapplication12;

import java.rmi.RemoteException;
import java.util.Scanner;
/**
 *
 * @author Rishika
 */
public class MainThread {
      public static void main(String[] args) throws RemoteException{
       
        /* host number to make client acting as a server */
      
        String option = "Enter the peerID \n 1.peerone \n 2.peertwo \n 3.peerthree \n 4.peerfour\n 5.peerfive \n 6.peersix \n 7.peerseven \n 8.peereight \n 9.peernine \n 10.peerten\n";
        System.out.println(option);
        Scanner sc = new Scanner(System.in);
        String hostNumber = selectPeer(sc.nextInt());
        System.out.println("HostNumber :"+hostNumber);				//selecting peername
      
       
        String opt = "Select the topology \n 1.star \n 2.mesh";
        /* Selecting Topology*/
        System.out.println(opt);
        Scanner sc1 = new Scanner(System.in);
        String topology = selectTopology(sc1.nextInt());			//selecting topology name
         System.out.println("Topology :"+topology);
        
        
        String combinedValue = hostNumber+","+topology;				//combining peername and topology name with comma
        
         /* Creating two instance variable to create threads */
        Server ts=new Server();										//instantiating client class
        Client tc=new Client();										//instantiating server class
        
        
      
        
        /* Creating a Client-Server architecture using Threads */
        Thread t2=new Thread(ts,combinedValue);						//creating client thread and passing combined value as thread name
        Thread t1=new Thread(tc,combinedValue);						//creating server thread and passing combined value as thread name
       
        
        
        t2.start();  /* starting server thread */
        t1.start(); /* starting client thread */
         
        
        
        
    }

    /*method to select type of topology */
      private static String selectTopology(int number){
      String topology;
        switch (number) {
            case 1:
                topology = "star.properties";
                break;
            case 2:
                topology = "mesh.properties";
                break;
            default:
                 topology = "wrong input";
                 break;
        }
     return topology;
     }
     
     /*selecting the peername*/ 
      private static String selectPeer(int number){
      String peer;
        switch (number) {
            case 1:
                peer = "peerone";
                break;
            case 2:
                peer = "peertwo";
                break;
            case 3:
                peer = "peerthree";
                 break;
            case 4:
                peer = "peerfour";
                break;
            case 5:
                peer = "peerfive";
                break;
            case 6:
                peer = "peersix";
                 break;
            case 7:
                peer = "peerseven";
                break;
            case 8:
                peer = "peereight";
                break;
            case 9:
                peer = "peernine";
                 break;
            case 10:
                peer = "peerten";
                 break;
            default:
                peer = "Invalid input";
                 break;
        }
     return peer;
     }
}
