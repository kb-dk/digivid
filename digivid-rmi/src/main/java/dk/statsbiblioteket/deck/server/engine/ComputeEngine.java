package dk.statsbiblioteket.deck.server.engine;

/**
 *
 */

import dk.statsbiblioteket.deck.ServerConstants;
import dk.statsbiblioteket.deck.rmiInterface.compute.Compute;
import dk.statsbiblioteket.deck.rmiInterface.compute.Task;


import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import org.apache.log4j.Logger;


import static java.rmi.registry.LocateRegistry.getRegistry;


public class ComputeEngine extends UnicastRemoteObject
        implements Compute {

    private static Logger log = Logger.getLogger(ComputeEngine.class.getName());
    private static Compute _engine;

    public ComputeEngine() throws RemoteException {
        super();
    }

    // here is where the action goes
    public Object executeTask(Task t) {
        return t.execute();
    }

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
        int port =  ServerConstants.DEFAULT_RMI_CLASSSERVER_PORT;
        System.out.println("Configuration port" + port);
        log.debug("Configuration Port: " + port);
        final String hostName = ServerConstants.DEFAULT_RMI_CLASSSERVER_NAME;
        System.out.println("Configuration Host" + hostName);
        log.debug("Configuration Host: " + hostName);
        //final String hostIP = ServerConstants.DEFAULT_RMI_CLASSSERVER_IP;
        final String hostIP;
        try {
            hostIP = InetAddress.getByName(hostName).getHostAddress();
        } catch (java.net.UnknownHostException e) {
            throw new RuntimeException("Could not find IP address for '"+hostName+"'");
        }
        System.out.println("Configuration IP" + hostIP);
        log.debug("Configuration IP: " + hostIP);




        String rmiUrl = "//" + hostIP +":" +port+ "/Compute";

        try {
            System.out.println("Find ComputeEngine");
            Registry registry = getRegistry(port);
            if (registry.REGISTRY_PORT == port){
                //String[] lst = registry.list();
                //for (int i = 0; i < lst.length; i++) {
                //    System.out.println("Registry listing " + lst[i]);
                //}
                _engine = new ComputeEngine();
                System.out.println("Bind ComputeEngine1");
                Naming.rebind(rmiUrl, _engine);
            }
            else {

              registry =
                java.rmi.registry.LocateRegistry.createRegistry(port);
                System.out.println("RMI registry ready.");
                _engine = new ComputeEngine();
                registry.rebind(rmiUrl,_engine);
                System.out.println("Bind ComputeEngine2");
                Naming.rebind(rmiUrl, _engine);
            }

        } catch (ConnectException failure) {
            try {

                ///////
                // Contains rmiUrl the port for rmiregistry???
                int portIndex = rmiUrl.indexOf(":", 6);
                if (portIndex > 0)
                    port = Integer.parseInt(rmiUrl.substring(portIndex + 1, rmiUrl.indexOf("/", portIndex)));
                System.out.println("Starting rmiregistry on port " + port);
                Runtime.getRuntime().exec("rmiregistry " + port); //FIXME
                int j = 1;
                while (j < 30) {  //(rb) FIXME: How long should we wait for
                    try {
                        Thread.sleep(500 * j); //(rb) FIXME: How long should we wait for
                        //_engine = new ComputeEngine();
                        Naming.rebind(rmiUrl, _engine);
                    } catch (ConnectException e) {
                        j = j + 2;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (RemoteException e) {
                System.err.println("ComputeEngine exception: " +
                        e.getMessage());
                e.printStackTrace();
                System.out.println("Rebinding " + rmiUrl + " failed, retrying ...");
                //   registry = LocateRegistry.createRegistry (port);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(6);
        }
        //}
        //try {
        //    System.out.println("Lookup verification: " + Naming.lookup ("rmi:" + host + ":" + port + "/" + "Compute"));
        //} catch (NotBoundException e) {
        //    e.printStackTrace();
        //} catch (MalformedURLException e) {
        //    e.printStackTrace();
        //}
        System.out.println("Registry started on port " + port + ".");
        System.out.println("ComputeEngine bound");
        System.out.println("Server ready.");
    }
}