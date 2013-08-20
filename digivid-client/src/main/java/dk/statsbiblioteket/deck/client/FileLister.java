package dk.statsbiblioteket.deck.client;

import dk.statsbiblioteket.deck.Constants;
import dk.statsbiblioteket.deck.rmiInterface.compute.Task;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bytestroop
 * Date: Nov 23, 2006
 * Time: 11:14:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class FileLister implements Task {

    /** constants used in computation */
    static Logger log = Logger.getLogger(ComputePi.class.getName());

    private static String prop;
    private static int propInt;
    //private static final String configure = Constants.DEFAULT_SERVERCONF_DIRECTORY + "/server.xml";
   // private static final String configure = Constants.DEFAULT_CLIENTCONF_DIRECTORY + "/client.xml";

    //private String unixExecutable  = getDefaultProperty("RECORDER.HOST.UNIXEXEC"); // the unix shell or command executable

    private String captureStorage = Constants.DEFAULT_RECORDSDIR;

    private String extension;



    /**
     * Construct a task to operate the server to the specified
     * control command.
     */
     public FileLister(String extension) {

        if (captureStorage != null) this.captureStorage=captureStorage;
        if (extension != null)this.extension = extension;
        log.debug("File directory: " + captureStorage);
        log.debug("File extension: " + extension);


    }
    public Object execute() {
        log.debug("trying to control the Recorder...");
        return getFileInfo();
    }

    public List<String[]> getFileInfo() {
        List<String[]> records = new ArrayList<String[]>();
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
                    if(!fs[i].isDirectory())
                    {
                        String filename = fs[i].getName();
                        long size=fs[i].length()/1024L;

                        if(filename.endsWith(extension.trim())) {
                            System.out.println("File: " + filename);
                            System.out.println(" of " + size+"KB");
                            String[] fileinfo = new String[] {filename, String.valueOf(size)};
                            records.add(fileinfo);
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return records;
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

    @Override
    public String toString() {
        return "FileLister{" +
                "captureStorage='" + captureStorage + '\'' +
                ", extension='" + extension + '\'' +
                '}';
    }
}
