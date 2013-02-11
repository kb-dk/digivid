package dk.statsbiblioteket.deck;

import dk.statsbiblioteket.deck.config.LoadXMLConfig;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * PACKAGE_NAME

 * @author std
 * @version 1.0
 * @since <pre>20-Nov-2006 ${Time}</pre>
 */
public interface Constants {
    //public static final String CONF_DIRECTORY_PARAM = "dk.statsbiblioteket.riff.utils.dk.statsbiblioteket.deck.config";
    //static final ResourceBundle build_properties = ResourceBundle.getBundle("build");
    //static final String application_name = build_properties.getString("application.name");
    //if you like to use your own properties put your home path here

    //TODO This is really horrible!
    //public static final String DEFAULT_CLIENTCONF_DIRECTORY = System.getProperty("user.home")+"/tomcat/webapps/"+application_name+"/WEB-INF";
    // CHANGE THESE TWO CONFIGURATION TO YOUR ENVIRONMENT,
    // NOTE: FURTHERMORE YOU HAVE TO CHANGE YOUR ANT PROPERTIE FILE TOO  IO TO DEPLOY YOUR CONFIGS
    //public static final String DEFAULT_CLIENTCONF = DEFAULT_CLIENTCONF_DIRECTORY + "/adminRMIClient_rigel_digibox01.xml";
    //public static final String DEFAULT_CLIENTCONF = DEFAULT_CLIENTCONF_DIRECTORY + "/client.xml";
     public static final URL DEFAULT_CLIENTCONF =  Constants.class.getClassLoader().getResource("client.xml");



    public static final String UNIX_STREAMSERVER_EXECUTABLE=LoadXMLConfig.getProperty(DEFAULT_CLIENTCONF, "STREAMSERVER_UNIXEXEC");
    public static final String UNIX_RECORDER_EXECUTABLE=LoadXMLConfig.getProperty(DEFAULT_CLIENTCONF, "RECORDER_UNIXEXEC");
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
    public static final String      DEFAULT_STREAMSERVER_IP  =  LoadXMLConfig.getProperty(DEFAULT_CLIENTCONF,"STREAMSERVER_IP");
    public static final Integer     DEFAULT_STREAMSERVER_HTTP_PORT  =  LoadXMLConfig.getPropertyInt(DEFAULT_CLIENTCONF,"STREAMSERVER_HTTP_PORT");
    public static final Integer     DEFAULT_STREAMSERVER_UDP_PORT  =  LoadXMLConfig.getPropertyInt(DEFAULT_CLIENTCONF,"STREAMSERVER_UDP_PORT");
    public static final Integer     DEFAULT_STREAMSERVER_RTP_PORT  =  LoadXMLConfig.getPropertyInt(DEFAULT_CLIENTCONF,"STREAMSERVER_RTP_PORT");
    public static final String      DEFAULT_STREAMSERVER_MEDIA  =  LoadXMLConfig.getProperty(DEFAULT_CLIENTCONF,"STREAMSERVER_MEDIA");
    //customize your stream player window input
    public static final Integer     DEFAULT_STREAMSERVER_FRAME_WIDTH  =  LoadXMLConfig.getPropertyInt(DEFAULT_CLIENTCONF,"STREAM_FRAME_WIDTH");
    public static final Integer     DEFAULT_STREAMSERVER_FRAME_HEIGHT  =  LoadXMLConfig.getPropertyInt(DEFAULT_CLIENTCONF,"STREAM_FRAME_HEIGHT");
    //Fix your Bandwith problems with a lower bitrate
    public static final Integer     DEFAULT_STREAMSERVER_BITRATE     =  LoadXMLConfig.getPropertyInt(DEFAULT_CLIENTCONF,"STREAM_BITRATE");
    public static final Integer     DEFAULT_STREAMSERVER_MAXBITRATE   =  LoadXMLConfig.getPropertyInt(DEFAULT_CLIENTCONF,"STREAM_MAXBITRATE");

    public static final Integer     DEFAULT_RMI_CLIENT_PORT  =  LoadXMLConfig.getPropertyInt(DEFAULT_CLIENTCONF,"RMI_PORT");
    public static final String      DEFAULT_RMI_CLIENT_NAME  =  LoadXMLConfig.getProperty(DEFAULT_CLIENTCONF,"RMI_CLIENT_NAME");
    public static final String      DEFAULT_RMI_CLIENT_IP  =  LoadXMLConfig.getProperty(DEFAULT_CLIENTCONF,"RMI_CLIENT_IP");
    public static final String      DEFAULT_RMI_CLIENT_CLASSES = LoadXMLConfig.getProperty(DEFAULT_CLIENTCONF,"RMI_CLIENT_CLASSES");
    public static final String      DEFAULT_RMI_CLASSSERVER_NAME = LoadXMLConfig.getProperty(DEFAULT_CLIENTCONF,"RMI_CLASSSERVER_NAME");
    public static final String      DEFAULT_RMI_CLASSSERVER_IP = LoadXMLConfig.getProperty(DEFAULT_CLIENTCONF,"RMI_CLASSSERVER_IP");


}
