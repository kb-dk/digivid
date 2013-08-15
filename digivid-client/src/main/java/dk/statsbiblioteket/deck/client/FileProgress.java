package dk.statsbiblioteket.deck.client;

import dk.statsbiblioteket.deck.rmiInterface.compute.Task;
import dk.statsbiblioteket.deck.Constants;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: bytestroop
 * Date: Nov 23, 2006
 * Time: 11:14:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class FileProgress implements Task {

    /** constants used in computation */
    static Logger log = Logger.getLogger(ComputePi.class.getName());

    private String fileName;
    private String captureStorage;
    private String recordSerial;
    private long limit;
    private String regExpession;
    /**
     * Construct a task to operate the server to the specified
     * control command.
     */
     public FileProgress(   String fileName,
                            String recordStartTime,
                            long limit) {
        
        if (recordStartTime != null)this.recordSerial = recordStartTime;
        if (limit != 0)this.limit = limit;
        if (fileName != null) this.fileName = fileName;
        captureStorage = Constants.DEFAULT_RECORDSDIR;
        log.debug("File directory: " + captureStorage);
        log.debug("File Name: " + fileName);
        log.debug("File limit: " + limit);
        regExpession = "([0-9]{4})_([0-9]{2})_([0-9]{2})_([0-9]{2})_([0-9]{2})_([0-9]{2})_([0-9]{3})";
    }

    public Object execute() {
        log.debug("trying to control the Recorder...");
        return getProgress();
    }

    public int getProgress() {
        int percentage=0;
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
                    if(!fs[i].isDirectory() && fs[i].getName().startsWith(recordSerial) && fs[i].getName().endsWith("mpeg"))
                    {
                        long lastmodified = fs[i].lastModified();
                        long creation = DateConverter.parseDateAsStringIntoMilliseconds(recordSerial, regExpession);

                        System.out.println("File Creation Time: " + creation);
                        System.out.println("File Modification Time: " + lastmodified);
                        log.debug("File Creation Time: " + creation);
                        log.debug("File Modification Time: " + lastmodified);

                        long duration = lastmodified - creation;
                        System.out.println("File Increase Time: " + duration);

                        if (duration <= limit)
                        {
                            System.out.println("File: " + fileName);
                            //System.out.println(" of " + duration +" out of bounds of " + limit);
                            percentage = (int) (duration * 100 / limit);
                            System.out.println("File: " + fileName +": " + percentage);
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return percentage;
    }

    @Override
    public String toString() {
        return "FileProgress{" +
                "fileName='" + fileName + '\'' +
                ", captureStorage='" + captureStorage + '\'' +
                ", recordSerial='" + recordSerial + '\'' +
                ", limit=" + limit +
                ", regExpession='" + regExpession + '\'' +
                '}';
    }
}
