package dk.statsbiblioteket.deck.client;

import dk.statsbiblioteket.deck.Constants;
import dk.statsbiblioteket.deck.rmiInterface.compute.Task;
import org.apache.log4j.Logger;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bytestroop
 * Date: Nov 23, 2006
 * Time: 11:14:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class UnixFileLister implements Task {

    /** constants used in pi computation */
    static Logger log = Logger.getLogger(ComputePi.class.getName());

    private static String prop;
    private static int propInt;
    //private static final String configure = Constants.DEFAULT_SERVERCONF_DIRECTORY + "/server.xml";
   // private static final String configure = Constants.DEFAULT_CLIENTCONF_DIRECTORY + "/client.xml";

    private String unixExecutable = "/bin/ls";
    private String captureStorage = Constants.DEFAULT_RECORDSDIR;
    private String extension;

    /**
     * Construct a task to operate the server to the specified
     * control command.
     */
     public UnixFileLister(String extension) {

        if (captureStorage != null) this.captureStorage=captureStorage;
        if (extension != null)this.extension = extension;

        log.debug("File directory: " + captureStorage);
        log.debug("File extension: " + extension);


    }
    public Object execute() {
        log.debug("trying to list records on encoder device...");
        return executeCommand();
    }

    public String unixCommandLine () {
        StringBuffer sb = new StringBuffer();
        if (unixExecutable!=null) sb.append(unixExecutable);
        if (captureStorage!=null) sb.append(" " + captureStorage);
        return sb.toString();
    }
    /**
     *
     * //@param ctlrCommand the unix command to execute
     * @return records
     */
    public List<ArrayList> executeCommand() {
        String s = null;
        String er = null;
        List records = new ArrayList();
        if (unixCommandLine() != null) {
            String execCommand = unixCommandLine();
            log.debug("Command: " + execCommand);
            try{
                Process p = Runtime.getRuntime().exec(execCommand);
                BufferedReader stdInput =
                        new BufferedReader(new InputStreamReader(p.getInputStream()));
                BufferedReader stdError =
                        new BufferedReader(new InputStreamReader(p.getErrorStream()));
                System.out.println("Here is the standard output of the command:\n");

                while ((s = stdInput.readLine()) != null) {
                     if(s.endsWith(extension.trim()))
                    System.out.println(s);
                    records.add(s);
                }
                while ((er = stdError.readLine()) != null) {
                    System.out.println(er);
                    log.error(er);
                }
            }
            catch(Exception e) {
                e.getStackTrace();
            }
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
        return "UnixFileLister{" +
                "unixExecutable='" + unixExecutable + '\'' +
                ", captureStorage='" + captureStorage + '\'' +
                ", extension='" + extension + '\'' +
                '}';
    }
}
