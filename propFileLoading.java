/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package javaapplication12;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author Rishika
 */
public class propFileLoading {
    
    InputStream inputStream ;
    Properties prop = new Properties();
    
    /*Loading property file*/
    public propFileLoading(String Topology){                                    //gets property file name with topology name
      try{
        String propFileName = Topology;
        inputStream = this.getClass().getResourceAsStream(propFileName);        
            if (inputStream != null) 
		prop.load(inputStream);                                                   // loads the entire file data into inputstraem
            else
		throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");	
     }catch(Exception e)
           {
               System.out.println("peers not found" + e);
           }
    }

/*function to get neighbouring peers from property file*/
    public String getNeighbouringPeers(String peerName) throws IOException, RemoteException{
        String result = "";
        int peerID = getPortNumber(peerName);
        result = prop.getProperty(Integer.toString(peerID));	
        return result;
    }

  /*function to get port numbers of peers from property file*/  
    public int getPortNumber(String peerName) throws IOException, RemoteException{
        int result =   Integer.parseInt(prop.getProperty(peerName));
        return result;
    } 
     
}
