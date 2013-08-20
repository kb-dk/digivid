package dk.statsbiblioteket.deck.client;

import dk.statsbiblioteket.deck.Constants;
import dk.statsbiblioteket.deck.rmiInterface.compute.Compute;
import org.apache.log4j.Logger;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Created by IntelliJ IDEA.
 * User: bytestroop
 * Date: Nov 23, 2006
 * Time: 11:12:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class StreamServerCtrl {

    //todo: make this an argument
    //private static String host = "//node02.portend.net";
    //private static String host = "encoder1.sb.statsbiblioteket.dk";
    //private static String hostIP = "172.18.249.253"; //encoder1.sb.statsbiblioteket.dk
    static Logger log = Logger.getLogger(StreamServerCtrl.class.getName());

    //private static String prop;
    //private static int propInt;
    //private static final String configure = Constants.DEFAULT_CLIENTCONF_DIRECTORY + "/adminRMIClient.xml";

    final int encoderRMIport =  Constants.DEFAULT_RMI_CLIENT_PORT;
    private String encoderName;
    private String encoderIP;
    private String streamFromServerName;
    private String streamFromServerIP;
    private String streamToClientIP;
    private String command;
    private String streamType;
    private String streamName;
    private String fileName;
    private int cardName;
    private int streamServerPort;
    private String media;

    public StreamServerCtrl(String command,
                            String streamType,
                            String streamFromServerIP,
                            String streamToClientIP,
                            String encoderIP,
                            int cardName,
                            String fileName,
                            int streamPort,
                            String streamName,
                            String media) {

        this.command = command;
        this.streamType = streamType;
        this.streamFromServerIP = streamFromServerIP;
        this.streamToClientIP = streamToClientIP;
        this.cardName = cardName;
        //if ((captureStorage != null) || (captureStorage !=""))
        //    this.captureStorage=captureStorage;
        //else

        this.fileName = fileName;
        this.streamName = streamName;
        this.encoderIP = encoderIP;
        this.streamServerPort= streamPort;
        this.media = media;

        //System.out.println("UDP_VideoStream_Dest_IP: " + streamToClientIP);
        log.debug("UDP_VideoStream_Dest_IP: " + streamToClientIP);
        //System.out.println("RMI_port" + encoderRMIport);
        log.debug("RMI_Port: " + encoderRMIport);
        //System.out.println("RMI_Host" + encoderName);
        log.debug("RMI_Server: " + encoderIP);
        //System.out.println("RMI_IP: " + encoderIP);
        log.debug("RMI_IP: " + encoderIP);
        log.debug("Records Dir: " + this.fileName);
        log.debug("View Media: " + this.media);
    }

    /** This is ignorant towards the available streamer application
     * Streamserver and encoder share the same IP address
     *
     * @throws RemoteException
     */
    public void remoteControl() throws RemoteException {

        System.out.println("Bind dk.statsbiblioteket.deck.client.StreamServerCtrl");
        if (System.getSecurityManager() == null) {
                System.setSecurityManager(new RMISecurityManager());
        }

        LocateRegistry.getRegistry (encoderRMIport);

        String name = "//" + encoderIP + ":" +encoderRMIport+ "/Compute";
        System.out.println("Stream Client looks up name address: " + name);

        Compute comp = null;
        try {
            comp = (Compute) Naming.lookup(name);
        } catch (NotBoundException nb) {
            System.out.println("Not Bound");
            nb.printStackTrace();
            throw new RuntimeException(nb);
        } catch (MalformedURLException murl) {
            System.out.println("Check URL");
            murl.printStackTrace();
            throw new RuntimeException(murl);
        } catch (RemoteException re) {
            re.printStackTrace();
            throw new RuntimeException(re);
        }

        System.out.println("Execute the Command...");


        Streamer task = new Streamer(command,
                            streamType,
                            streamFromServerIP,
                            streamToClientIP,
                            cardName,
                            fileName,
                            streamServerPort,
                            streamName,
                            media);
        String ctlr = null;
        try {
            ctlr = (String) (comp.executeTask(task));
        } catch (RemoteException e1) {
            log.error(" Failure, Task not executed! ", e1);
            e1.printStackTrace();
        }
        //System.out.println(ctlr);
        log.debug(ctlr);
    }

    /**   MAIN
     *
     * @param args
     * @throws RemoteException
     */
    /*
    public static void main(String args[]) throws RemoteException {

        System.out.println("Bind dk.statsbiblioteket.deck.client StreamServerCtrl");

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
        // args0 = class, args1=hostname, args2=port
        //int port = (args.length > 0) ? Integer.parseInt(args[2]) : 1099;

        // String portProperty = getDefaultProperty("Server.HOST.PORT");
        // Integer j = new Integer(portProperty);
        // final int port = j.intValue(); // unboxing
        final int port =  getDefaultPropertyInt("ClassServer.HOST.PORT");
        System.out.println("RMI_Port: " + port);
        log.debug("RMI_Port: " + port);
        final String encoderName = getDefaultProperty("ClassServer.HOST.NAME");
        System.out.println("RMI_Server: " + encoderName);
        log.debug("RMI_Server: " + encoderName);
        final String encoderIP = getDefaultProperty("ClassServer.HOST.IP");
        System.out.println("RMI_Server_IP" + encoderIP);
        log.debug("RMI_Server_IP: " + encoderIP);


        Registry registry = LocateRegistry.getRegistry (port);

        //boolean bound = false;
        //for (int i = 0; ! bound && i < 1; i++) {
            //if (args.length > 0) host = "//" + args[0];


           // try {
                //String name = "//" + args[0] + "/Compute";

        String name = "//" + encoderIP + ":" +port+ "/Compute";
        System.out.println("Client looks up name address: " + name);

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

        System.out.println("Execute the Command from main...");
        Streamer task = new Streamer(args[1],args[2],args[3]);
        String ctlrCommand = (String) (comp.executeTask(task));
        System.out.println(ctlrCommand);
    }
    */
}
