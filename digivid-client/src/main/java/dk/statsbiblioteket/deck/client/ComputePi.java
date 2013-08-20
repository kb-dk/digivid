package dk.statsbiblioteket.deck.client;

/**
 * Created by IntelliJ IDEA.
 * User: stephan
 * Date: Aug 12, 2006
 * Time: 1:52:28 PM
 * To change this template use File | Settings | File Templates.
 */


import dk.statsbiblioteket.deck.Constants;
import dk.statsbiblioteket.deck.config.LoadXMLConfig;
import dk.statsbiblioteket.deck.rmiInterface.compute.Compute;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class  ComputePi {
    //todo: make this an argument
    //private static String host = "//node02.portend.net";
    //private static String host = "encoder1.sb.statsbiblioteket.dk";
    //private static String hostIP = "172.18.249.253"; //encoder1.sb.statsbiblioteket.dk
    static Logger log = Logger.getLogger(ComputePi.class.getName());

    private static String prop;
    private static int propInt;
    //private static final String configure = Constants.DEFAULT_CLIENTCONF_DIRECTORY + "/adminRMIClient.xml";
    private static final URL CONFIG = Constants.DEFAULT_CLIENTCONF;

    private static String getDefaultProperty(String defaultPropertyName) {
        log.debug("Loading configuration: " + CONFIG);
        try {
            if (defaultPropertyName != null) {
                prop = LoadXMLConfig.getProperty(CONFIG, defaultPropertyName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prop;
    }

     private static int getDefaultPropertyInt(String defaultPropertyName) {
        log.debug("Loading configuration: " + CONFIG);
        try {
            if (defaultPropertyName != null) {
                propInt = LoadXMLConfig.getPropertyInt(CONFIG, defaultPropertyName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return propInt;
    }

    public static void main(String args[]) throws RemoteException {

        System.out.println("Bind dk.statsbiblioteket.deck.client ComputerPi");

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
        // args0 = class, args1=hostname, args2=port
        //int port = (args.length > 0) ? Integer.parseInt(args[2]) : 1099;

        // String portProperty = getDefaultProperty("Server.HOST.PORT");
        // Integer j = new Integer(portProperty);
        // final int port = j.intValue(); /* unboxing */
        final int port =  getDefaultPropertyInt("ClassServer.HOST.PORT");
        System.out.println("Configuration port" + port);
        log.debug("Configuration Port: " + port);
        final String hostName = getDefaultProperty("ClassServer.HOST.NAME");
        System.out.println("Configuration Host" + hostName);
        log.debug("Configuration Host: " + hostName);
        final String hostIP = getDefaultProperty("ClassServer.HOST.IP");
        System.out.println("Configuration IP" + hostIP);
        log.debug("Configuration IP: " + hostIP);


        Registry registry = LocateRegistry.getRegistry (port);

        //boolean bound = false;
        //for (int i = 0; ! bound && i < 1; i++) {
            //if (args.length > 0) host = "//" + args[0];


           // try {
                //String name = "//" + args[0] + "/Compute";
                String name = "//" + hostIP + ":" +port+ "/Compute";
                System.out.println("Client looks up name address: " + name);

                //String[] lst = Naming.list("encoder1.sb.statsbiblioteket.dk") ;
                //for (int i=0; i<lst.length; i++) {
                //    System.out.println("registry listing "+ lst[i]);
                //}

                //Compute comp = (Compute) Naming.lookup(name);
        Compute comp = null;
        try {
            comp = (Compute) Naming.lookup(name);
        } catch (NotBoundException e) {
            System.out.println("Not Bound");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            System.out.println("Check URL");
            e.printStackTrace();
        }

        System.out.println("Make the Calculation...");
        Pi task = new Pi(Integer.parseInt(args[1]));
        BigDecimal pi = (BigDecimal) (comp.executeTask(task));
        System.out.println(pi);

            //} catch (Exception e) {
            //    System.err.println("ComputePi exception: " +
            //                       e.getMessage());
            //    e.printStackTrace();
            //}
        //}
    }
}
