package dk.statsbiblioteket.deck.client;

import dk.statsbiblioteket.deck.rmiInterface.compute.Task;
import dk.statsbiblioteket.deck.Constants;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: bytestroop
 * Date: Nov 23, 2006
 * Time: 11:14:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class Streamer implements Task {

    /** constants used in pi computation */
    //private static boolean START = false;
    //private static boolean STOP = true;
    //private static boolean REPLAY = false;
    static Logger log = Logger.getLogger(ComputePi.class.getName());

    //private static String prop;
    //private static int propInt;
    //private static final String configure = Constants.DEFAULT_SERVERCONF_DIRECTORY + "/server.xml";
    //private static final String configure = Constants.DEFAULT_CLIENTCONF_DIRECTORY + "/adminRMIClient.xml";

    //private String unixExecutable  = getDefaultProperty("StreamServer.HOST.UNIXEXEC");// the unix shell or command executable
    //private String unixExecutable = "/home/bytestroop/Deck/vlc/bin/streamctrl.sh";
    private String unixExecutable = Constants.UNIX_STREAMSERVER_EXECUTABLE;
    private String streamToClientIP;  //getDefaultProperty("Client.HOST.IP");
    private int streamServerPort   =  Constants.DEFAULT_STREAMSERVER_HTTP_PORT;
    private String streamFromServerName = Constants.DEFAULT_STREAMSERVER_NAME;
    private String streamFromServerIP = Constants.DEFAULT_STREAMSERVER_IP;
    private String streamType;
    private String streamName;
    private String filePath;
    private String media           = Constants.DEFAULT_STREAMSERVER_MEDIA;

    private int cardName           =  Constants.DEFAULT_STREAMSERVER_CARDNAME;
    private String channelID          =  Constants.DEFAULT_CHANNELID;
    private int frameWidth         =  Constants.DEFAULT_STREAMSERVER_FRAME_WIDTH;
    private int frameHeight        =  Constants.DEFAULT_STREAMSERVER_FRAME_HEIGHT;
    private String captureFormat      =  Constants.DEFAULT_FORMAT;
    private int captureBitrate     =  Constants.DEFAULT_STREAMSERVER_BITRATE;
    private int captureMaxBitrate  =  Constants.DEFAULT_STREAMSERVER_MAXBITRATE;
    private String captureStorage     =  Constants.DEFAULT_RECORDSDIR;

    /** command */
    private String ctrlCommand; // the users control start | stop | replay



    /**
     * Construct a task to operate the server to the specified
     * control command.
     */
    public Streamer(String ctrlCommand,
                String streamType,
                String streamFromServerIP,
                String streamToClientIP,
                int cardName,
                String fileName,
                int streamServerPort,
                String streamName,
                String media) {

        this.ctrlCommand = ctrlCommand;
        this.streamType=streamType;
        this.streamFromServerIP = streamFromServerIP;
        this.streamToClientIP = streamToClientIP;
        this.cardName = cardName;
        if (fileName!=null) filePath =
                new StringBuilder().append(captureStorage).append("/").append(fileName).toString();
        this.streamServerPort = streamServerPort;
        this.streamName = streamName;
        this.media = media;


        log.debug("Config Unix Exec: " + unixExecutable);
        log.debug("Config Control Command: " + ctrlCommand);
        log.debug("Config streamType: " + streamType);
        log.debug("Config IP (stream server): " + streamFromServerIP);
        log.debug("Config IP (stream client): " + streamToClientIP);
        log.debug("Config Port (stream server/client): " + streamServerPort);
        log.debug("Config FilePath: " + filePath);
        log.debug("Config Stream Server Port: " + streamServerPort);
        log.debug("Config StreamName: " + streamName);
        log.debug("Config Media: " + media);
        unixCommandLine();
        //log.debug("Command is: " + unixCommandLine());
    }
    /*
    public Streamer(String ctlrCommand, String streamToClientIP, String cardName) {
        this.ctrlCommand = ctlrCommand;
        this.streamToClientIP = streamToClientIP;
        this.cardName = cardName;

        log.debug("Config Control Command: " + ctlrCommand);
        log.debug("Config Client Host: " + streamToClientIP);
        log.debug("Config Port (stream server): " + streamServerPort);
        log.debug("Config Host: " + streamFromServerName);
        log.debug("Config IP: " + streamFromServerIP);
        log.debug("Config Protocol: " + streamProtocol);
    }
    */
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
        if (streamFromServerIP!=null) sb.append(" -s " + streamFromServerIP);
        if (streamToClientIP!=null) sb.append(" -c " + streamToClientIP);
        sb.append(" -d " + cardName);
        if (filePath!=null)  sb.append(" -f " + filePath);
        if (streamServerPort!=0) sb.append(" -p " + streamServerPort);
        if (streamName!=null) sb.append(" -n " + streamName);
        if (media!=null) sb.append(" -m " + media);
        execCommand = sb.toString();

        //System.out.println("Command: " + execCommand);
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

        /*
        else if (ctlrCommand.equalsIgnoreCase("stop")) {
            try{
                Process p = Runtime.getRuntime().exec(execCommand());
                BufferedReader stdInput =
                        new BufferedReader(new InputStreamReader(p.getInputStream()));
                BufferedReader stdError =
                        new BufferedReader(new InputStreamReader(p.getErrorStream()));
                System.out.println("Here is the standard output of the command:\n");

                while ((s = stdInput.readLine()) != null) {
                    sb.append(s+"\n");
                    System.out.println(s);
                    }
                }
            catch(Exception e) {}
            status = "Stop the Server..." + sb.toString() ;
        }
        else if (ctlrCommand.equalsIgnoreCase("replay")) {

            try{
                Process p = Runtime.getRuntime().exec(execCommand());
                BufferedReader stdInput =
                        new BufferedReader(new InputStreamReader(p.getInputStream()));
                BufferedReader stdError =
                        new BufferedReader(new InputStreamReader(p.getErrorStream()));
                System.out.println("Here is the standard output of the command:\n");

                while ((s = stdInput.readLine()) != null) {
                    sb.append(s+"\n");
                    System.out.println(s);
                    }
                }
            catch(Exception e) {}
            status = "Replay captured..." + sb.toString() ;
        }
        else status = "Command not recognized";
        */
        //return status;
    }

}
