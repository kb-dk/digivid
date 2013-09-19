package dk.statsbiblioteket.deck.client;

import dk.statsbiblioteket.deck.Constants;
import dk.statsbiblioteket.deck.rmiInterface.compute.Task;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by IntelliJ IDEA.
 * User: bytestroop
 * Date: Nov 23, 2006
 * Time: 11:14:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class Streamer implements Task {


    static Logger log = Logger.getLogger(Streamer.class.getName());


    private String unixExecutable = Constants.UNIX_STREAMSERVER_EXECUTABLE;
    private String streamToClientName;
    private int streamServerPort   =  Constants.DEFAULT_STREAMSERVER_HTTP_PORT;
    private String streamFromServerName = Constants.DEFAULT_STREAMSERVER_NAME;
    private String streamType;
    private String streamName;
    private String filePath;
    private String media           = Constants.DEFAULT_STREAMSERVER_MEDIA;

    private int cardName           =  Constants.DEFAULT_STREAMSERVER_CARDNAME;
    private String captureStorage     =  Constants.DEFAULT_RECORDSDIR;

    /** command */
    private String ctrlCommand; // the users control start | stop | replay



    /**
     * Construct a task to operate the server to the specified
     * control command.
     */
    public Streamer(String ctrlCommand,
                String streamType,
                String streamFromServerName,
                String streamToClientName,
                int cardName,
                String fileName,
                int streamServerPort,
                String streamName,
                String media) {

        this.ctrlCommand = ctrlCommand;
        this.streamType=streamType;
        this.streamFromServerName = streamFromServerName;
        this.streamToClientName = streamToClientName;
        this.cardName = cardName;
        if (fileName!=null) filePath =
                new StringBuilder().append(captureStorage).append("/").append(fileName).toString();
        this.streamServerPort = streamServerPort;
        this.streamName = streamName;
        this.media = media;


        log.debug("Config Unix Exec: " + unixExecutable);
        log.debug("Config Control Command: " + ctrlCommand);
        log.debug("Config streamType: " + streamType);
        log.debug("Config hostname (stream server): " + streamFromServerName);
        log.debug("Config hostname (stream client): " + streamToClientName);
        log.debug("Config Port (stream server/client): " + streamServerPort);
        log.debug("Config FilePath: " + filePath);
        log.debug("Config Stream Server Port: " + streamServerPort);
        log.debug("Config StreamName: " + streamName);
        log.debug("Config Media: " + media);
        unixCommandLine();
    }

    public Object execute() {
        log.debug("trying to control the StreamServer...");
        return executeCommand();
    }

    public String unixCommandLine () {
        String execCommand;
        StringBuffer sb = new StringBuffer();
        if (unixExecutable!=null) sb.append(unixExecutable);
        if (ctrlCommand!=null) sb.append(" -r " + ctrlCommand);
        if (streamType!=null) sb.append(" -t " + streamType);
        if (streamFromServerName !=null) sb.append(" -s " + streamFromServerName);
        if (streamToClientName !=null) sb.append(" -c " + streamToClientName);
        sb.append(" -d " + cardName);
        if (filePath!=null)  sb.append(" -f " + filePath);
        if (streamServerPort!=0) sb.append(" -p " + streamServerPort);
        if (streamName!=null) sb.append(" -n " + streamName);
        if (media!=null) sb.append(" -m " + media);
        execCommand = sb.toString();

        log.debug("Command: " + execCommand);
        return execCommand;
    }
    /**
     *
     * //@param ctlrCommand the unix command to execute
     * @return status
     */
    public String executeCommand() {



        String status = "n/a";
        String s = null;
        String err = null;
        StringBuffer sbOut = new StringBuffer();
        StringBuffer sbError = new StringBuffer();

        String unix_command = unixCommandLine();
        if (unix_command != null) {
            try {
                Process p = Runtime.getRuntime().exec(unix_command);
                BufferedReader stdInput =
                        new BufferedReader(new InputStreamReader(p.getInputStream()));
                BufferedReader stdError =
                        new BufferedReader(new InputStreamReader(p.getErrorStream()));
                System.out.println("Here is the standard output of the command:\n");

                while (((s = stdInput.readLine()) != null) || ((err = stdError.readLine()) != null)) {

                    if (s != null)  {
                        sbOut.append(s +"\n");
                        System.out.println(s);
                        log.debug(s);
                    }
                    if (err != null) {
                        sbError.append(err +"\n");
                        System.out.println("Error: " + err);
                        log.error(err);
                    }
                }
            } catch(Exception e) {
                status = "Unix X-cute Error..." + sbError.toString() +" "+ e.getMessage() ;
            }

            status = sbOut.toString() ;
        }
        return status;


    }


    @Override
    public String toString() {
        return "Streamer{" +
                "unixExecutable='" + unixExecutable + '\'' +
                ", streamToClientName='" + streamToClientName + '\'' +
                ", streamServerPort=" + streamServerPort +
                ", streamFromServerName='" + streamFromServerName + '\'' +
                ", streamType='" + streamType + '\'' +
                ", streamName='" + streamName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", media='" + media + '\'' +
                ", cardName=" + cardName +
                ", captureStorage='" + captureStorage + '\'' +
                ", ctrlCommand='" + ctrlCommand + '\'' +
                '}';
    }
}
