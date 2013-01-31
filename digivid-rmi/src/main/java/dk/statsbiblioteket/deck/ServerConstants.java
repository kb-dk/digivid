package dk.statsbiblioteket.deck;

import dk.statsbiblioteket.deck.config.LoadXMLConfig;

/**
 * Created by IntelliJ IDEA.
 * User: stephan
 * Date: Jan 18, 2007
 * Time: 1:48:51 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ServerConstants {
    public static final String CONF_DIRECTORY_PARAM = "dk.statsbiblioteket.riff.utils.dk.statsbiblioteket.deck.config";

    //if you like to use your own properties put your home path here
    public static final String DEFAULT_SERVERCONF_DIRECTORY = "/home/bart/Deck/rmi";

    // CHANGE THESE TWO CONFIGURATION TO YOUR ENVIRONMENT,
    // NOTE: FURTHERMORE YOU HAVE TO CHANGE YOUR ANT PROPERTIE FILE TOO  IO TO DEPLOY YOUR CONFIGS

    public static final String DEFAULT_SERVERCONF = DEFAULT_SERVERCONF_DIRECTORY +"/server.xml";

    public static final Integer DEFAULT_RMI_CLASSSERVER_PORT = LoadXMLConfig.getPropertyInt(DEFAULT_SERVERCONF, "RMI_PORT");
    public static final String  DEFAULT_RMI_CLASSSERVER_NAME = LoadXMLConfig.getProperty(DEFAULT_SERVERCONF, "RMI_CLASSSERVER_NAME");
    public static final String  DEFAULT_RMI_CLASSSERVER_IP = LoadXMLConfig.getProperty(DEFAULT_SERVERCONF, "RMI_CLASSSERVER_IP");
    public static final String  DEFAULT_RMI_CLASSSERVER_CLASSES = LoadXMLConfig.getProperty(DEFAULT_SERVERCONF, "RMI_CLASSSERVER_CLASSES");
}
