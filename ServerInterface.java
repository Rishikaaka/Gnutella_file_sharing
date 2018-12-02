/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package javaapplication12;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 *
 * @author Rishika
 */
public interface ServerInterface extends Remote{
    
   // String getPortNumber(String peerName) throws RemoteException;
   
    int query(int messageID, int TTL, String File, String port) throws RemoteException;				/*Searching operation for a given file*/
    boolean obtain(String filename,String sourcePeer, String destPeer) throws RemoteException;		/*retrieve operation for file from source to destination*/
    public  String getDirectory(String ID) throws RemoteException ;									/*operation for getting peers corresponding directory*/
    public void setCache(HashMap<String,String> cache) throws RemoteException;						/*setting peers cached files information*/
    public HashMap<String,String> getCache() throws RemoteException;								/*getting the cached files information*/
}
