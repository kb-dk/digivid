package dk.statsbiblioteket.deck;

import dk.statsbiblioteket.deck.config.LoadXMLConfig;
import java.net.URL;

/**
 * PACKAGE_NAME

 * @author std
 * @version 1.0
 * @since <pre>20-Nov-2006 ${Time}</pre>
 */
public interface Constants {

     public static final URL DEFAULT_CLIENTCONF =  Constants.class.getClassLoader().getResource("client.xml");



    public static final String UNIX_STREAMSERVER_EXECUTABLE=LoadXMLConfig.getProperty(DEFAULT_CLIENTCONF, "STREAMSERVER_UNIXEXEC");
    public static final String UNIX_RECORDER_EXECUTABLE=LoadXMLConfig.getProperty(DEFAULT_CLIENTCONF, "RECORDER_UNIXEXEC");

    public static final String HOOKS_BINDIR = LoadXMLConfig.getProperty(DEFAULT_CLIENTCONF, "HOOKS_BINDIR");
    public static final String RECORDER_BINDIR = LoadXMLConfig.getProperty(DEFAULT_CLIENTCONF, "RECORDER_BINDIR");
    public static final String STREAMER_STARTCOMMAND = LoadXMLConfig.getProperty(DEFAULT_CLIENTCONF, "STREAMSERVER_STARTCOMMAND");
    public static final String STREAMER_STOPCOMMAND = LoadXMLConfig.getProperty(DEFAULT_CLIENTCONF, "STREAMSERVER_STOPCOMMAND");
    public static final String STREAMER_BINDIR = LoadXMLConfig.getProperty(DEFAULT_CLIENTCONF, "STREAMSERVER_BINDIR");

    public static final Integer     DEFAULT_RECORDER_CARDNAME  =  LoadXMLConfig.getPropertyInt(DEFAULT_CLIENTCONF,"ENCODE_DEVICEID");
    public static final String      DEFAULT_CHANNELID          =  LoadXMLConfig.getProperty(DEFAULT_CLIENTCONF,"CHANNELID");

    public static final String      DEFAULT_FORMAT             =  LoadXMLConfig.getProperty(DEFAULT_CLIENTCONF,"FORMAT");
    public static final String      DEFAULT_EXTENSION          =  LoadXMLConfig.getProperty(DEFAULT_CLIENTCONF,"EXTENSION");
    // Definitions for both formats MPEG-1 and MPEG-2
    public static final Integer     DEFAULT_FRAME_WIDTH_MPEG1  =  LoadXMLConfig.getPropertyInt(DEFAULT_CLIENTCONF,"FRAME_WIDTH_MPEG1");
    public static final Integer     DEFAULT_FRAME_HEIGHT_MPEG1 =  LoadXMLConfig.getPropertyInt(DEFAULT_CLIENTCONF,"FRAME_HEIGHT_MPEG1");
    public static final Integer     DEFAULT_FRAME_WIDTH_MPEG2  =  LoadXMLConfig.getPropertyInt(DEFAULT_CLIENTCONF,"FRAME_WIDTH_MPEG2");
    public static final Integer     DEFAULT_FRAME_HEIGHT_MPEG2 =  LoadXMLConfig.getPropertyInt(DEFAULT_CLIENTCONF,"FRAME_HEIGHT_MPEG2");

    public static final Integer     DEFAULT_BITRATE_MPEG1      =  LoadXMLConfig.getPropertyInt(DEFAULT_CLIENTCONF,"BITRATE_MPEG1");
    public static final Integer     DEFAULT_MAXBITRATE_MPEG1   =  LoadXMLConfig.getPropertyInt(DEFAULT_CLIENTCONF,"MAXBITRATE_MPEG1");
    public static final Integer     DEFAULT_BITRATE_MPEG2      =  LoadXMLConfig.getPropertyInt(DEFAULT_CLIENTCONF,"BITRATE_MPEG2");
    public static final Integer     DEFAULT_MAXBITRATE_MPEG2   =  LoadXMLConfig.getPropertyInt(DEFAULT_CLIENTCONF,"MAXBITRATE_MPEG2");

    public static final Integer     DEFAULT_RECORDER_FILE_LIMIT_SIZE_MPEG1 = LoadXMLConfig.getPropertyInt(DEFAULT_CLIENTCONF,"RECORDER_FILE_LIMIT_SIZE_MPEG1");
    public static final Integer     DEFAULT_RECORDER_FILE_LIMIT_SIZE_MPEG2 = LoadXMLConfig.getPropertyInt(DEFAULT_CLIENTCONF,"RECORDER_FILE_LIMIT_SIZE_MPEG2");

    public static final String      DEFAULT_RECORDSDIR         =  LoadXMLConfig.getProperty(DEFAULT_CLIENTCONF,"RECORDSDIR");
    public static final String      DEFAULT_FILENAME           =  LoadXMLConfig.getProperty(DEFAULT_CLIENTCONF,"FILENAME");

    public static final Integer     DEFAULT_RECORDER_FILE_LIMIT_TIME = LoadXMLConfig.getPropertyInt(DEFAULT_CLIENTCONF,"RECORDER_FILE_LIMIT_TIME");

    public static final String      DEFAULT_DATE_REGEXPRESSION = LoadXMLConfig.getProperty(DEFAULT_CLIENTCONF,"DEFAULT_DATE_REGEXPRESSION");
    public static final String      DEFAULT_TIME_REGEXPRESSION = LoadXMLConfig.getProperty(DEFAULT_CLIENTCONF,"DEFAULT_TIME_REGEXPRESSION");

    public static final Integer     DEFAULT_STREAMSERVER_CARDNAME =  LoadXMLConfig.getPropertyInt(DEFAULT_CLIENTCONF,"STREAMSERVER_DEVICEID");
    public static final String      DEFAULT_STREAMSERVER_NAME  =  LoadXMLConfig.getProperty(DEFAULT_CLIENTCONF,"STREAMSERVER_NAME");
    public static final Integer     DEFAULT_STREAMSERVER_HTTP_PORT  =  LoadXMLConfig.getPropertyInt(DEFAULT_CLIENTCONF,"STREAMSERVER_HTTP_PORT");
    public static final String      DEFAULT_STREAMSERVER_MEDIA  =  LoadXMLConfig.getProperty(DEFAULT_CLIENTCONF,"STREAMSERVER_MEDIA");

    public static final Integer     DEFAULT_RMI_CLIENT_PORT  =  LoadXMLConfig.getPropertyInt(DEFAULT_CLIENTCONF,"RMI_PORT");

}
