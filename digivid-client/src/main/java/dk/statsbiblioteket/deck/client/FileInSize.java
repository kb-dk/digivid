package dk.statsbiblioteket.deck.client;

import dk.statsbiblioteket.deck.rmiInterface.compute.Task;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: bytestroop
 * Date: Nov 23, 2006
 * Time: 11:14:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class FileInSize implements Task {

    /** constants used in computation */
    static Logger log = Logger.getLogger(ComputePi.class.getName());

    private static String prop;
    private static int propInt;
    //private static final String configure = Constants.DEFAULT_SERVERCONF_DIRECTORY + "/server.xml";
   // private static final String configure = Constants.DEFAULT_CLIENTCONF_DIRECTORY + "/client.xml";

    //private String unixExecutable  = getDefaultProperty("RECORDER.HOST.UNIXEXEC"); // the unix shell or command executable

    private String captureStorage;
    private String fileName;
    private int limit;

    /**
     * Construct a task to operate the server to the specified
     * control command.
     */
     public FileInSize( String captureStorage,
                            String fileName,
                            int limit) {

        if (captureStorage != null) this.captureStorage=captureStorage;
        if (limit != 0)this.limit = limit;
        if (fileName != null) this.fileName = fileName;
        log.debug("File directory: " + captureStorage);
        log.debug("File Name: " + fileName);
        log.debug("File limit: " + limit);
    }

    public Object execute() {
        log.debug("trying to control the Recorder...");
        return inSize();
    }

    /**
     * Handled in the shell script this methode is not used yet but keept cz
     * at some point this is being done the java way
     * @return
     */
    public boolean inSize() {

        try
        {
            File f = new File(captureStorage);
            System.out.println("Directory: " + captureStorage);
            boolean flag =  f.isDirectory();
            if(flag)
            {
                File fs[] = f.listFiles();
                for(int i=0;i<fs.length;i++)
                {
                    if(!fs[i].isDirectory() && fs[i].getName().equalsIgnoreCase(fileName))
                    {
                        long size=fs[i].length()/1024;

                        if(size > limit) {
                            System.out.println("File: " + fileName);
                            System.out.println(" of " + size+"KB out of bounds of " + limit);
                            return false;
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return true;
    }

    /*
     private static String getDefaultProperty(String defaultPropertyName) {
           // System.out.println("Configuration" + configure);
           // log.debug("Configuration: " + configure);
           try {
               if (defaultPropertyName != null) {
                   prop = LoadXMLConfig.getProperty(configure, defaultPropertyName);
               }
           } catch (Exception e) {
               e.printStackTrace();
           }
           return prop;
       }

        private static int getDefaultPropertyInt(String defaultPropertyName) {
           System.out.println("Configuration: " + configure);
           log.debug("Configuration: " + configure);
           try {
               if (defaultPropertyName != null) {
                   propInt = LoadXMLConfig.getPropertyInt(configure, defaultPropertyName);
               }
           } catch (Exception e) {
               e.printStackTrace();
           }
           return propInt;
       }
    */
}
