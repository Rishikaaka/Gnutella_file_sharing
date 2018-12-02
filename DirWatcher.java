//package javaapplication12;
import java.util.*;
import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DirWatcher extends TimerTask {                       //extending timertask to make the thread execute repeatedly in the given intervals

  /*Declaring all the variables*/
  private String path;
  private File filesArray [];
  private HashMap dir = new HashMap();
  private DirFilterWatcher dfw;
  private int X;
  private String fileName;
  private int portNumber; 
  HashMap<String,String> h1 = new HashMap<String,String>();
 
  
 /*getting directory path and type of files from client class*/ 
  public DirWatcher(String path, String filter) {
    this.path = path;                                               //storing directory path
    dfw = new DirFilterWatcher(filter);
    filesArray = new File(path).listFiles(dfw);                     //getting all the files in directory

    // transfer to the hashmap be used a reference and keep the
    // lastModfied value
    for(int i = 0; i < filesArray.length; i++) {
       dir.put(filesArray[i], new Long(filesArray[i].lastModified())); //storing the filename as key and their last modified value as value in hash map
    }
  }

  public final void run() {
    HashSet checkedFiles = new HashSet();
    filesArray = new File(path).listFiles(dfw);                     //getting the list of files in given directory

    // scan the files and check for modification/addition
int x = 0;
    for(int i = 0; i < filesArray.length; i++) {
      Long current = (Long)dir.get(filesArray[i]);
      checkedFiles.add(filesArray[i]);
      /*Adding new files to check*/
      if (current == null) {
        // new file
        dir.put(filesArray[i], new Long(filesArray[i].lastModified()));
        onChange(filesArray[i], "add", x);
      }
      /*checking for modifications in file*/
      else if (current.longValue() != filesArray[i].lastModified()){
        // modified file
        x = 2;                                                          //setting the x value to 2 inorder to notify client about modifications in file
        setX(x);
        dir.put(filesArray[i], new Long(filesArray[i].lastModified()));
        setFile(filesArray[i].getName());                               //setting the last modified file name
        onChange(filesArray[i], "modify", x);
      }
    }
   
    // now check for deleted files
    Set ref = ((HashMap)dir.clone()).keySet();
    ref.removeAll((Set)checkedFiles);
    Iterator it = ref.iterator();
    while (it.hasNext()) {
      File deletedFile = (File)it.next();
      dir.remove(deletedFile);
      onChange(deletedFile, "delete", x);
    }
    
  }

/*prints the modified filename*/
  protected void onChange( File file, String action, int type){
    System.out.println
                   ( "File: "+ file.getName() +" action: " + action );
                   
  }

/*sets x value*/

  public void setX(int x){     
      X= x;     
  }

/*defines getter method for x value*/
  public int getX(){     
      return X;
  }

/*sets modified filename*/
  public void setFile(String Filename){
      fileName=Filename;
  }

/*defines getter method for file*/
  public String getFile(){
  return fileName;
  }
  
}
