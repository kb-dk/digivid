package dk.statsbiblioteket.deck;

import dk.statsbiblioteket.deck.config.LoadXMLConfig;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: stephan
 * Date: Jan 18, 2007
 * Time: 1:48:51 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ServerConstants {

    public static final URL DEFAULT_CONF = ServerConstants.class.getClassLoader().getResource("server.xml");

    public static final Integer DEFAULT_RMI_CLASSSERVER_PORT = LoadXMLConfig.getPropertyInt(DEFAULT_CONF, "RMI_PORT");
    public static final String  DEFAULT_RMI_CLASSSERVER_NAME = LoadXMLConfig.getProperty(DEFAULT_CONF, "RMI_CLASSSERVER_NAME");
}
