package dk.statsbiblioteket.deck.client;

import dk.statsbiblioteket.deck.rmiInterface.compute.Task;
import dk.statsbiblioteket.deck.Constants;
import dk.statsbiblioteket.deck.config.LoadXMLConfig;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.URL;

import sun.util.calendar.Gregorian;

/**
 * Created by IntelliJ IDEA.
 * User: bytestroop
 * Date: Nov 23, 2006
 * Time: 11:14:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class FileInTime implements Task {

    /** constants used in computation */
    static Logger log = Logger.getLogger(ComputePi.class.getName());

    private static String prop;
    private static int propInt;
    //private static final String configure = Constants.DEFAULT_SERVERCONF_DIRECTORY + "/server.xml";
   //private static final String configure = Constants.DEFAULT_CLIENTCONF_DIRECTORY + "/adminRMIClient.xml";

    //private String unixExecutable  = getDefaultProperty("RECORDER.HOST.UNIXEXEC"); // the unix shell or command executable
    private static final URL CONFIG = Constants.DEFAULT_CLIENTCONF;


    private String fileName;
    private String captureStorage;
    private long limit = (long) LoadXMLConfig.getPropertyInt(CONFIG, "RECORDER.FILE.LIMIT.TIME");

    /**
     * Construct a task to operate the server to the specified
     * control command.
     */
     public FileInTime( String captureStorage,
                            String fileName,
                            long limit) {

        if (captureStorage != null) this.captureStorage=captureStorage;
        if (limit != 0)this.limit = limit;
        if (fileName != null) this.fileName = fileName;
        log.debug("File directory: " + captureStorage);
        log.debug("File Name: " + fileName);
        log.debug("File limit: " + limit);
    }

    public Object execute() {
        log.debug("trying to control the Recorder...");
        return inTime();
    }

     /**
     * Handled in the shell script this methode is not used yet but keept cz
     * at some point this is being done the java way
     * @return
     */

    public boolean inTime() {
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
                        long lastmodified = fs[i].lastModified();
                        long now = System.currentTimeMillis();
                        long duration = now - lastmodified;
                        if (duration > limit)
                        {
                            System.out.println("File: " + fileName);
                            System.out.println(" of " + duration +" out of bounds of " + limit);
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

    @Override
    public String toString() {
        return "FileInTime{" +
                "fileName='" + fileName + '\'' +
                ", captureStorage='" + captureStorage + '\'' +
                ", limit=" + limit +
                '}';
    }
}
