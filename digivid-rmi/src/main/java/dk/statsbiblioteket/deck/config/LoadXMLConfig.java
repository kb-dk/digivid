package dk.statsbiblioteket.deck.config;

/**
 * dk.statsbiblioteket.deck.config
 * ${Class_name}
 * <p/>
 * Created by IntelliJ IDEA.
 *
 * @author std
 * @version 1.0
 * @since <pre>20-Nov-2006 ${Time}</pre>
 */


import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

/**
 * Loads the configuration prameter for the RIFF framework from an XML file
 * <p/>
 * dk.statsbiblioteket.riff.utils.dk.statsbiblioteket.deck.config
 *
 * @author std
 * @version 1.0
 * @since <pre>14-Sep-2006 ${Time}</pre>
 */

public class LoadXMLConfig {

    private static final Logger log = Logger.getLogger(LoadXMLConfig.class.getName());
    //private static final String properties_file_name = Constants.DEFAULT_CONF_DIRECTORY + "server.xml";
    private static Properties _properties;

    public static String getProperty(String configure, String propertyName) {
        log.debug("Reading " + propertyName + " from " + configure);
        Properties prop = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(configure);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            prop.loadFromXML(fis);
        } catch (InvalidPropertiesFormatException fe) {
            fe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // if (Debug.DEBUG) prop.list(System.out);
        String value = prop.getProperty(propertyName);

        return value;
    }
    public static String getProperty(URL url, String propertyName) {
        log.debug("Reading " + propertyName + " from " + url);
        Properties prop = new Properties();
        try {
            prop.loadFromXML(url.openStream());
        } catch (InvalidPropertiesFormatException fe) {
            fe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // if (Debug.DEBUG) prop.list(System.out);
        String value = prop.getProperty(propertyName);

        return value;
    }

    public static Properties getProperties(String configure) {
        if (_properties == null) {
            _properties = new Properties();
            /*
            ClassLoader loader = Thread.currentThread().getContextClassLoader();

            if (loader.getResource(properties_file_name) == null) {
                log.error("ClassLoader could not locate " +
                        properties_file_name);
            }
            */
            try {
                //_properties.loadFromXML(loader.getResourceAsStream(properties_file_name));
                _properties.loadFromXML(new FileInputStream(configure));
            } catch (InvalidPropertiesFormatException e1) {
                log.error("Unable to parse the properties file.", e1);
            } catch (IOException e) {
                log.error("Unable to load properties file", e);
            }
        }
        return _properties;
    }

      public static Properties getProperties(URL url) {
        if (_properties == null) {
            _properties = new Properties();
            /*
            ClassLoader loader = Thread.currentThread().getContextClassLoader();

            if (loader.getResource(properties_file_name) == null) {
                log.error("ClassLoader could not locate " +
                        properties_file_name);
            }
            */
            try {
                //_properties.loadFromXML(loader.getResourceAsStream(properties_file_name));
                _properties.loadFromXML(url.openStream());
            } catch (InvalidPropertiesFormatException e1) {
                log.error("Unable to parse the properties file.", e1);
            } catch (IOException e) {
                log.error("Unable to load properties file", e);
            }
        }
        return _properties;
    }


    public static Integer getPropertyInt(String configure, String key) {
        try {
            return Integer.parseInt(getProperties(configure).getProperty(key));
        } catch (NumberFormatException ex) {
            log.error("Could not fetch the Integer-property " + key, ex);
            return null;
        }
    }

    public static Integer getPropertyInt(URL url, String key) {
        try {
            return Integer.parseInt(getProperties(url).getProperty(key));
        } catch (NumberFormatException ex) {
            log.error("Could not fetch the Integer-property " + key, ex);
            return null;
        }
    }
 
}