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

    static Logger log = Logger.getLogger(StreamServerCtrl.class.getName());

    //private static String prop;
    //private static int propInt;
    //private static final String configure = Constants.DEFAULT_CLIENTCONF_DIRECTORY + "/adminRMIClient.xml";

    final int encoderRMIport =  Constants.DEFAULT_RMI_CLIENT_PORT;
    private String encoderName;
    private String streamFromServerName;
    private String streamToClientName;
    private String command;
    private String streamType;
    private String streamName;
    private String fileName;
    private int cardName;
    private int streamServerPort;
    private String media;

    public StreamServerCtrl(String command,
                            String streamType,
                            String streamFromServerName,
                            String streamToClientName,
                            String encoderName,
                            int cardName,
                            String fileName,
                            int streamPort,
                            String streamName,
                            String media) {

        this.command = command;
        this.streamType = streamType;
        this.streamFromServerName = streamFromServerName;
        this.streamToClientName = streamToClientName;
        this.cardName = cardName;
        //if ((captureStorage != null) || (captureStorage !=""))
        //    this.captureStorage=captureStorage;
        //else

        this.fileName = fileName;
        this.streamName = streamName;
        this.encoderName = encoderName;
        this.streamServerPort= streamPort;
        this.media = media;

        log.debug("UDP_VideoStream_Dest_Name: " + streamToClientName);
        //System.out.println("RMI_port" + encoderRMIport);
        log.debug("RMI_Port: " + encoderRMIport);
        //System.out.println("RMI_Host" + encoderName);
        log.debug("RMI_Server: " + encoderName);
        //System.out.println("RMI_HOST: " + encoderName);
        log.debug("RMI_HOST: " + encoderName);
        log.debug("Records Dir: " + this.fileName);
        log.debug("View Media: " + this.media);
    }

    /** This is ignorant towards the available streamer application
     * Streamserver and encoder share the same host
     *
     * @throws RemoteException
     */
    public void remoteControl() throws RemoteException {

        System.out.println("Bind dk.statsbiblioteket.deck.client.StreamServerCtrl");
        if (System.getSecurityManager() == null) {
                System.setSecurityManager(new RMISecurityManager());
        }

        LocateRegistry.getRegistry (encoderRMIport);

        String name = "//" + encoderName + ":" +encoderRMIport+ "/Compute";
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
                            streamFromServerName,
                            streamToClientName,
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
}