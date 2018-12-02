/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 
/**
 *
 * @author Rishika 
 */
 
//package javaapplication12;
import java.io.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client extends TimerTask implements Runnable{

    /**
     * @param args the command line arguments
     */

     public void run() {
       /*making the client thread wait untill server thread creates registry */
       try{
            Thread.sleep(1000);
            }catch(InterruptedException e){
            }
       
       String FileToSearch;
       Registry registry = null;
       ServerInterface Si = null;
       String hostName = "localhost";
       String neibouringPeers = "";
       ArrayList<String> portNumber = new ArrayList<String>();
       ArrayList<String>found = new ArrayList<String>();
       Random rand = new Random();
       ArrayList<Integer>notFound = new ArrayList<Integer>();
       HashMap<String,String> cache= new HashMap<String,String>();
       HashMap<String,String> cache1= new HashMap<String,String>();
       
       int port=0;
       HashMap<Integer,ArrayList<String>> record= new HashMap<Integer,ArrayList<String>>();
       int messageID=0;
       int TTL;
       
       /* retreiving portnumber and topology */
	   String combinedValue =Thread.currentThread().getName(); //retrieving combined value of portname and topology name passed to client thread from MainThread class
	   String [] temp = combinedValue.spilt(",");            	//splitting values seperated by comma
	   String portNmaee = temp[0];                             	// Storing selected portname
	   String topology  = temp[1];								// Storing Selected topology
       propFileLoading pfl = new propFileLoading(topology);		// passing selected topology as argument into property file loading class
       
  
        String path = "";
        File filesArray [];
        HashMap dir = new HashMap();
        DirWatcher task = null ;
        String filter = "txt";
        String fileName;
        
     //file checker    
     
         try {
             registry = LocateRegistry.getRegistry(hostName,pfl.getPortNumber(portName)); 	//locating to the selected peers server
             Si  =(ServerInterface)registry.lookup("server");								//connecting to the same server
             path = Si.getDirectory(portName);												// getting the directory assigned to the selected peer from its server
             task = new DirWatcher(path, "txt"); 											// passing the directory path and its files before they are modified to Dirwatcher thread which constantly checks for modifications
             Timer time = new Timer();														// creating timer object
             time.schedule( task , new Date(), 1000 );                                       // scheduling dirwatcher thread to keep checking constantly for any modifications made
         } catch (IOException ex) {
             Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
         } catch (NotBoundException ex) {
             Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
         }
        
    while(true){
 
       /* assigning time-to-live depending on type of topology */
  				
        if (topology.equals("star.properties"))
           TTL = 10;									//assigning TTL value=10 for star topology
        else
           TTL = 3;										// assigning TTl value= 3 for mesh topology

        ArrayList<String> traversal = new ArrayList<String>();						//storing all the visisted neighbouring nodes in traversal arraylist
        String options = "Select an operation \n 1. search \n 2. exit"; 			//giving user options to search or exit
        int optionsselect;
        System.out.println(options);
        Scanner sc=new Scanner(System.in);
        optionsselect=sc.nextInt();
        /*pushprotocol - checking for modifications in master files*/
          if(task.getX()==2){														//checking condition if the file is modified or not
            String fn = task.getFile(); 											//getting the modified file
            System.out.println("Would u like to modify the contents in copied files \n 1.yes \n 2.no"); 
            Scanner s = new Scanner(System.in);            
            if(s.nextInt()== 1){
             try {
             cache1.putAll(Si.getCache());											//getting the list of cached files for the given master file
             Set KeySet = cache1.keySet();											//getting the filenames which are stored as keys in cache1(hashmap)
               if(KeySet.isEmpty())													//checking if the modified master file has been cached or not
                   System.out.println("File has not been cached into other directories yet");
               else if(KeySet.contains(fn))											// checking if the cached file belongs to masterfile
               {
                   System.out.println("destination peer "+cache1.get(fn));
                  if( Si.obtain(fn,cache1.get(fn),portName))						// if matched copy the contents of master file to cached file
                      System.out.println(fn+"contents in "+cache1.get(fn)+" has been reflected from"+portName);
                  else
                      System.out.println("havent changed");
               }
             }catch (IOException ex) {
             Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
         }
          }   
            else
                continue;															//continue otherwise
        }
        
        switch(optionsselect){
        case 1:        
        /*file Search*/
        int size=0;
        int search;
        int count=0;
        messageID = rand.nextInt(100) + 1;											// assigning randomn message id for each search
        System.out.println("enter the file to search");
        Scanner sc3= new Scanner(System.in);
        FileToSearch = sc3.nextLine();  
        traversal.clear();															// clearing the contents of traversal which contains visited peers portnames
        traversal.add(portName);													// adding the portname of client
        found.clear();																// clearing the contents of hit-nodes
        boolean timer = true;														
        while(timer && found.isEmpty()){											// looping untill time to live(TTL) value expires
        int i = 0;
        size = traversal.size();     												//storing the size of traversal
        outerloop:
            while(i<=size && found.isEmpty()){										//loop stops if the element is found and traversal length is finished
            try {
             /*getting NeighbouringPeers */
                neibouringPeers = pfl.getNeighbouringPeers(traversal.get(i));		// getting the neighbouring peers portnames from property file class
            } catch (IOException ex) {
             Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            /*Tokenizing neighbouring peers from the string obtained*/
            StringTokenizer st1 = new StringTokenizer(neibouringPeers,",");			//creating object for string tokenizer
            System.out.print("\n Neighbouring peers :");
                while(st1.hasMoreTokens()){
                         portNumber.add(st1.nextToken());							// tokenizing all the peernames seprated by comma
                            
                } 
                for(String present : portNumber)									// adding all the neighbouring ports into portNumber arraylist
                    System.out.print(" "+present);
                    
                  /*registering with neighbouring peers */
            int dummy=0;     
            for(String check : portNumber)											// traversing through each neighbouring peer
            {
               if(traversal.contains(check)){										//making sure already visited node is not visited again
                   System.out.println("node already visited");					
               }
               else{    
                try{
                    System.out.println("\n check in the port "+check);
                	registry = LocateRegistry.getRegistry(hostName,pfl.getPortNumber(check));        // locating neighbouring peer servers and connecting to them       
                    Si  =(ServerInterface)registry.lookup("server"); 
                    search = Si.query(messageID, TTL,FileToSearch, check);							// Intiating search in the neighbouring peer servers to check if the given file is present or not
                    TTL = TTL-1;																	// decrementing TTL value for each search
                   // System.out.println("TTL = "+ TTL);
                     if( search == 1){
                        traversal.add(check);														// adding the visited peer to traversal
                        System.out.println("File hit in "+ check);
                        found.add(check);															// adding the peer in which file is found
                     }
                     else if(search == 2){															
                         traversal.add(check);														// adding the visited peeer to traversal even if it is not found
                         System.out.println("File not found in "+ check);
                     }
                     else{
                         System.out.println("Timer expired!node unreachable");						
                         timer = false;																// making the timer variable false
                         break outerloop;															//breaking out of the outer loop												
                     }
                
                } catch (RemoteException | NotBoundException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }  catch (IOException ex) { 
                       Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                   } 
                }
            }
            portNumber.clear();																		// clearing the portnumber arraylist for the next search
            i++;
            }
           } 
           
                if(record.get(messageID)== null)
                    record.put(messageID,traversal);								//storing the messageID as key and the list of visited peers as values in a hashmap
                System.out.println("(messageID ,[list of visited peers])");
                for (int name: record.keySet()){
                    int key =name;
                    ArrayList<String> value = record.get(name);  
                    System.out.println("( " + key + "," + value + " )");  
                }
                /*listing the sent of peers in which the message is found*/
                 System.out.print("\n List of peers in which file is found  ( ");
                    for(String num : found){
                        System.out.print(num + " ");
                    }
                    System.out.println(") peers");

                /*connecting with the target peer*/  
                   System.out.println("Enter the peer you want to get the file from :");
                   Scanner sc2 = new Scanner(System.in);									// giving user input to select a destination peer from the list of peeers
                   String destPeer = sc2.nextLine();
                   if(found.contains(destPeer))
                   System.out.println("connect to "+destPeer+" server");
                   else
                   System.out.println("please enter a peername from the found peers");    
           try {               
               registry = LocateRegistry.getRegistry(hostName,pfl.getPortNumber(destPeer)); // connecting to the destination peers port
               Si  =(ServerInterface)registry.lookup("server");
               System.out.println("Waiting for download...");
              if( Si.obtain(FileToSearch, portName, destPeer))								// getting the file from destination port
              {
                  //fileTransfer.add(count,portName);
                  cache.put(FileToSearch, portName);										// storing the cached file name as key and its corresponding port name
                  Si.setCache(cache);
                 // task.setPortNumber(pfl.getPortNumber(portName));

                
                  System.out.println("File has been transferred successfully");
                  //count++;
                  
              }
              else
                  System.out.println("destination not found");
           }catch (RemoteException ex) {
               Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
           } catch (NotBoundException ) {
               Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
           } catch (IOException ex) {
               Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
           }
        break;
        case 2: 
        
        System.exit(0);
        break;
        }
        }
        
        }
    public static void main(String[] args) {
    Client tc=new Client();
    tc.run();																				//Running the client threads
   
    }
       