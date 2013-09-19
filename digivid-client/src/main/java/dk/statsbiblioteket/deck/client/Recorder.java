package dk.statsbiblioteket.deck.client;

import dk.statsbiblioteket.deck.Constants;
import dk.statsbiblioteket.deck.rmiInterface.compute.Task;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by IntelliJ IDEA.
 * User: bytestroop
 * Date: Nov 23, 2006
 * Time: 11:14:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class Recorder implements Task {

    /** constants used in pi computation */

    static Logger log = Logger.getLogger(Recorder.class.getName());
    private String recordType;
    private String userName;
    private String fileSerial;
    private String clientHostName;
    private String encoderName;
    private int cardName           =  Constants.DEFAULT_RECORDER_CARDNAME;
    private String channelID          =  Constants.DEFAULT_CHANNELID;
    private String captureFormat   =  Constants.DEFAULT_FORMAT;

    private int frameMPEG1Width         =  Constants.DEFAULT_FRAME_WIDTH_MPEG1;
    private int frameMPEG1Height        =  Constants.DEFAULT_FRAME_HEIGHT_MPEG1;
    private int frameMPEG2Width         =  Constants.DEFAULT_FRAME_WIDTH_MPEG2;
    private int frameMPEG2Height        =  Constants.DEFAULT_FRAME_HEIGHT_MPEG2;

    private int frameWidth;
    private int frameHeight;

    private int captureMPEG1Bitrate     =  Constants.DEFAULT_BITRATE_MPEG1;
    private int captureMPEG1MaxBitrate  =  Constants.DEFAULT_MAXBITRATE_MPEG1;
    private int captureMPEG2Bitrate     =  Constants.DEFAULT_BITRATE_MPEG2;
    private int captureMPEG2MaxBitrate  =  Constants.DEFAULT_MAXBITRATE_MPEG2;
    private int captureBitrate;
    private int captureMaxBitrate;

    private int captureMPEG1Size   =  Constants.DEFAULT_RECORDER_FILE_LIMIT_SIZE_MPEG1;
    private int captureMPEG2Size   =  Constants.DEFAULT_RECORDER_FILE_LIMIT_SIZE_MPEG2;
    private int captureSize;

    private String captureStorage  =  Constants.DEFAULT_RECORDSDIR;
    private String fileName        =  Constants.DEFAULT_FILENAME;
    private long captureLength     =  Constants.DEFAULT_RECORDER_FILE_LIMIT_TIME;
    private long origStartDate;

    /** command */
    private String command;    // the users control start | stop
    private String unixExecutable = Constants.UNIX_RECORDER_EXECUTABLE;

    private static boolean start= false;
    private static boolean stop = false;

    /**
     * Construct a task to operate the server to the specified
     * control command.
     */
     public Recorder(String command,
                        String recordType,
                        String userName,
                        String clientHostName,
                        String encoderName,
                        int cardName,
                        String channelID,
                        String captureFormat,
                        String fileName,
                        long captureLength,
                        int captureSize,
                        long origStartDate
    ) {

        this.command = command;
        this.recordType = recordType;
        this.userName = userName;
        this.clientHostName = clientHostName;
        this.encoderName = encoderName;
        this.cardName = cardName;
        if (channelID != null) this.channelID = channelID;

        if (captureFormat !=null) this.captureFormat = captureFormat;

        if (captureFormat.equalsIgnoreCase("1")) {
            this.frameWidth = frameMPEG1Width;
            this.frameHeight = frameMPEG1Height;
            this.captureBitrate = captureMPEG1Bitrate;
            this.captureMaxBitrate = captureMPEG1MaxBitrate;
            this.captureSize= captureMPEG1Size;
            this.origStartDate= origStartDate;
        } else if (captureFormat.equalsIgnoreCase("2")) {
            this.frameWidth = frameMPEG2Width;
            this.frameHeight = frameMPEG2Height;
            this.captureBitrate = captureMPEG2Bitrate;
            this.captureMaxBitrate = captureMPEG2MaxBitrate;
            this.captureSize= captureMPEG2Size;
            this.origStartDate= origStartDate;
        } 

        if (fileName != null)this.fileName = fileName;
        this.fileSerial = generateSerialNumber();
        if (captureLength != 0) this.captureLength= captureLength;

        log.debug("Command: " + command);
        log.debug("Type: " + recordType);
        log.debug("Person: " + userName);
        log.debug("From Hostname: " + clientHostName);
        log.debug("On Encoder: " + encoderName);
        log.debug("On Device: " + cardName);
        log.debug("On Channel: " + channelID);
        log.debug("Frame Width: " + frameWidth);
        log.debug("Frame Height: " + frameHeight);
        log.debug("Format: " + captureFormat);
        log.debug("Bitrate: " + captureBitrate);
        log.debug("MaxBitdrate: " + captureMaxBitrate);
        log.debug("Storage Location: " + captureStorage);
        log.debug("File name: " + fileName);
        log.debug("File serial: " + fileSerial);
        log.debug("Expected Length: " + captureLength);
        log.debug("Expected Size: " + captureSize);
        log.debug("Recording Orig: " + origStartDate);

    }
    public Object execute() {
        log.debug("trying to control the Recorder...");
        return executeCommand();
    }

    /**
     *
     *
     *   # $ENCODE_DEVICEID      eg. 0|1 or 2
     *   # $WIDTH                eg. 720
     *   # $HEIGHT               eg. 480
     *   # $FORMAT               eg. 1=MPEG-1, 2=MPEG-2
     *   # $BITRATE              eg. 6500000
     *   # $MAXBITRATE           eg. 8000000
     *   # $DIRECTORY            eg. ../records
     *   # $FILENAME             eg. video.mpg
     *   # $LENGTH               eg. 3595
     *
     * @return execCommand
     */
    //echo "capturectrl.sh -r <run start|stop> -t <capturetype eg. record> -u <user name> -c <client hostname> -d <device 0|1|2> -w <width> -h <height> -a <captureFormat> -

    public String unixCommandLine () {
        String execCommand;
        StringBuffer sb = new StringBuffer();
        if (unixExecutable!=null) sb.append(unixExecutable);
        if (command!=null) sb.append(" -r " + command);
        if (recordType!=null) sb.append(" -t " + recordType);
        if (userName!=null) sb.append(" -u " + userName);
        if (clientHostName!=null) sb.append(" -c " + clientHostName);
        sb.append(" -d " + cardName);
        if (channelID!=null)  sb.append(" -i " + channelID);
        if (frameWidth!=0)  sb.append(" -w " + frameWidth);
        if (frameHeight!=0)  sb.append(" -g " + frameHeight);
        if (captureFormat!=null)  sb.append(" -a " + captureFormat);
        if (captureBitrate!=0)  sb.append(" -b " + captureBitrate);
        if (captureMaxBitrate!=0)  sb.append(" -m " + captureMaxBitrate);
        if (captureStorage!=null)  sb.append(" -s " + captureStorage);
        if (fileName!=null)  sb.append(" -f " + fileName);
        if (fileSerial!=null)  sb.append(" -n " + fileSerial);
        if (captureLength!=0) sb.append(" -l " + captureLength);
        sb.append(" -k " + captureSize);
        if (origStartDate!=0) sb.append(" -o " + origStartDate);

        execCommand = sb.toString();

        System.out.println("Command: " + execCommand);
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

        String unix_command_line = unixCommandLine();
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(unix_command_line);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logOutput(p, log);

        /*if (unix_command_line != null) {
            try {
                Process p = Runtime.getRuntime().exec(unix_command_line);
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
        }*/
        return "Command: " + unix_command_line;
    }

    public static void logOutput(final Process p, final Logger log) {
        Runnable standard_output_thread = new Runnable() {
            public void run() {
                BufferedReader stdInput =
                        new BufferedReader(new InputStreamReader(p.getInputStream()));
                String s = null;
                try {
                    while (((s = stdInput.readLine()) != null)) {
                        log.debug(s);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } ;

         Runnable standard_error_thread = new Runnable() {
            public void run() {
                BufferedReader stdError =
                        new BufferedReader(new InputStreamReader(p.getErrorStream()));
                String s = null;
                try {
                    while (((s = stdError.readLine()) != null)) {
                        log.error(s);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } ;

        (new Thread(standard_output_thread)).start();
        (new Thread(standard_error_thread)).start();

    }


    private String generateSerialNumber() {
        fileSerial = DateConverter.printMillisecondsIntoSerialnumber(System.currentTimeMillis());
        return fileSerial;
    }

    @Override
    public String toString() {
        return "Recorder{" +
                "recordType='" + recordType + '\'' +
                ", userName='" + userName + '\'' +
                ", fileSerial='" + fileSerial + '\'' +
                ", clientHostName='" + clientHostName + '\'' +
                ", encoderName='" + encoderName + '\'' +
                ", cardName=" + cardName +
                ", channelID='" + channelID + '\'' +
                ", captureFormat='" + captureFormat + '\'' +
                ", frameMPEG1Width=" + frameMPEG1Width +
                ", frameMPEG1Height=" + frameMPEG1Height +
                ", frameMPEG2Width=" + frameMPEG2Width +
                ", frameMPEG2Height=" + frameMPEG2Height +
                ", frameWidth=" + frameWidth +
                ", frameHeight=" + frameHeight +
                ", captureMPEG1Bitrate=" + captureMPEG1Bitrate +
                ", captureMPEG1MaxBitrate=" + captureMPEG1MaxBitrate +
                ", captureMPEG2Bitrate=" + captureMPEG2Bitrate +
                ", captureMPEG2MaxBitrate=" + captureMPEG2MaxBitrate +
                ", captureBitrate=" + captureBitrate +
                ", captureMaxBitrate=" + captureMaxBitrate +
                ", captureMPEG1Size=" + captureMPEG1Size +
                ", captureMPEG2Size=" + captureMPEG2Size +
                ", captureSize=" + captureSize +
                ", captureStorage='" + captureStorage + '\'' +
                ", fileName='" + fileName + '\'' +
                ", captureLength=" + captureLength +
                ", origStartDate=" + origStartDate +
                ", command='" + command + '\'' +
                ", unixExecutable='" + unixExecutable + '\'' +
                '}';
    }
}
