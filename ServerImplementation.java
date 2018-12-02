/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package javaapplication12;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.apache.commons.io.FileUtils;
/**
 *
 * @author Rishika
 */

public class ServerImplementation extends UnicastRemoteObject implements ServerInterface{
    InputStream inputStream;
    String result = "" ;
    static int message ;
    static int timeToLive;
    HashMap<String,String> h = new  HashMap<String,String>();
    public ServerImplementation() throws RemoteException{
       super();
    }
    
    /*checking if a file is present in the given peer or not*/

    public  int  query( int mesageID, int TTL, String fil , String port) throws RemoteException{
      // System.out.println("enterd");
      int temp ;
      if(checkTTL(TTL) == false)					/*Cheks if time is expired or not*/
          temp = 0;									/*Returns 0 if time to live value is expired*/
      else{
      String directory = getDirectory(port);		/*Retrieves peers corresponding diectory*/
        if( isFound(fil,new File(directory)) )		// paases the directory as arument to serach file method
          temp = 1;									// sets temp value to 1 if file is found and returns temp
        else
          temp = 2;									// Otherwise it sets the value to 2 and returns temp
      }
     return temp;  
    }
    
    /*checks if a file is present in the selected directory*/

    public  boolean isFound(String name,File file)
    {
        boolean temp = false;
        //System.out.println(name+""+file);
        File[] list = file.listFiles();								// lists all the files in given directory
        if(list!=null)
        for (File fil : list)
        {
            if (fil.isDirectory())									// if there are any inner directories it further searches for files
            {
                isFound(name,fil);
            }
            else if (name.equalsIgnoreCase(fil.getName()))			// checks if the given files is present in list of files
            {
                System.out.println(fil.getParentFile());
                temp= true;											// if it is equal returns file name
            }
        }
        return temp;
    }
    
    
    /*Assigning directory to each peer*/

    public  String getDirectory(String ID) throws RemoteException {
        String sourceDirectory;
       // String absoluteFilepath="C:\\Users\\Rishika\\Documents\\NetBeansProjects\\JavaApplication12\\src\\javaapplication12";
        switch (ID) {
            case "peerone":
                sourceDirectory = "Peer_1/";
                break;
            case "peertwo":
                sourceDirectory = "Peer_2/";
                break;
            case "peerthree":
                sourceDirectory = "Peer_3/";
                break;
            case "peerfour":
                sourceDirectory = "Peer_4/";
                break;
            case "peerfive":
                sourceDirectory = "Peer_5/";
                break;
            case "peersix":
                sourceDirectory = "Peer_6/";
                break;
            case "peerseven":
                sourceDirectory = "Peer_7/";
                break;
            case "peereight":
                sourceDirectory = "Peer_8/";
                break;
            case "peernine":
                sourceDirectory = "Peer_9/";
                break;
            case "peerten":
                sourceDirectory = "Peer_10/";
                break;
            default:
                sourceDirectory="";
        }
        return sourceDirectory;
    }
    
/*Checking the TTL value is zero or not*/

    public boolean checkTTL(int TTL){       
        if(TTL <= 0)									// checks if TTL is zero .returns false if that is the case
            return false;
        else 
            return true;
    }
   
    
    
 /*Getting file from sourcepeer to destination peer*/

    public boolean obtain(String fileName, String sourcePeer, String destPeer) throws RemoteException{
        String sourceDirectory = getDirectory(destPeer);										//getting source peers directory
        String destDirectory= getDirectory(sourcePeer);											//getting dest peers directory
       File source = new File(sourceDirectory+fileName);										//getting source file
       File dest = new File(destDirectory+fileName);											//creating destination with same filename
 
        InputStream input = null;
	OutputStream output = null;
	try {
		input = new FileInputStream(source);
		output = new FileOutputStream(dest);
		byte[] buf = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buf)) > 0) {											// writing the contents from  source file to destination file
			output.write(buf, 0, bytesRead);
		}
	} catch (FileNotFoundException ex) {
            Logger.getLogger(ServerImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                input.close();
                output.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }
	}
 return true;   
}
    
 /*Maintaining set of cached files*/

    public void setCache(HashMap<String,String> cache) throws RemoteException{			//storing the cached files information
    h.putAll(cache);
    }
 
 /*Retrieving cached files*/

    public HashMap<String,String> getCache() throws RemoteException{					//getting the cached files information
    return h;
    }
}