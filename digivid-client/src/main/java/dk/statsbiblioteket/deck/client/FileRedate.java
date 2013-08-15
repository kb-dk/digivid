package dk.statsbiblioteket.deck.client;

import dk.statsbiblioteket.deck.rmiInterface.compute.Task;
import dk.statsbiblioteket.deck.Constants;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Date;
import java.text.DateFormat;

import sun.util.calendar.Gregorian;

/**
 * Created by IntelliJ IDEA.
 * User: bytestroop
 * Date: Nov 23, 2006
 * Time: 11:14:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class FileRedate implements Task {

    /** constants used in computation */
    static Logger log = Logger.getLogger(ComputePi.class.getName());


    private String fileName;
    private String captureStorage = Constants.DEFAULT_RECORDSDIR;;
    private long origStartTime;
    private long duration;

    /**
     * Construct a task to operate the server to the specified
     * control command.
     */
     public FileRedate(     String fileName,
                            long origStartTime
                            ) {
        if (fileName != null) this.fileName = fileName;
        log.debug("File directory: " + captureStorage);
        log.debug("File Name: " + fileName);

    }

    public Object execute() {
        log.debug("trying to control the Recorder...");
        return redate();
    }

     /**
     * Handled in the shell script this methode is not used yet but keept cz
     * at some point this is being done the java way
     * @return
     */
     
    public boolean redate() {
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
                        long stopDate = origStartTime + duration;
                        dateFormat(new Date(stopDate));

                        //if(size > limit) {
                        //    System.out.println("File: " + fileName);
                        //    System.out.println(" of " + size+"KB out of bounds of " + limit);
                        //    return false;
                        //}
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private void dateFormat(Date now) {
    // Make a new Date object. It will be initialized to the current time.
        //Date now = new Date();

        // See what toString() returns
        System.out.println(" 1. " + now.toString());

        // Next, try the default DateFormat
        System.out.println(" 2. " + DateFormat.getInstance().format(now));

        // And the default time and date-time DateFormats
        System.out.println(" 3. " + DateFormat.getTimeInstance().format(now));
        System.out.println(" 4. " +
            DateFormat.getDateTimeInstance().format(now));

        // Next, try the short, medium and long variants of the
        // default time format
        System.out.println(" 5. " +
            DateFormat.getTimeInstance(DateFormat.SHORT).format(now));
        System.out.println(" 6. " +
            DateFormat.getTimeInstance(DateFormat.MEDIUM).format(now));
        System.out.println(" 7. " +
            DateFormat.getTimeInstance(DateFormat.LONG).format(now));

        // For the default date-time format, the length of both the
        // date and time elements can be specified. Here are some examples:
        System.out.println(" 8. " + DateFormat.getDateTimeInstance(
            DateFormat.SHORT, DateFormat.SHORT).format(now));
        System.out.println(" 9. " + DateFormat.getDateTimeInstance(
            DateFormat.MEDIUM, DateFormat.SHORT).format(now));
        System.out.println("10. " + DateFormat.getDateTimeInstance(
            DateFormat.LONG, DateFormat.LONG).format(now));
    }

    @Override
    public String toString() {
        return "FileRedate{" +
                "fileName='" + fileName + '\'' +
                ", captureStorage='" + captureStorage + '\'' +
                ", origStartTime=" + origStartTime +
                ", duration=" + duration +
                '}';
    }
}
