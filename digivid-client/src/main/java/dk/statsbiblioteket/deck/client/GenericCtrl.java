/* File:        $RCSfile: GenericCtrl.java,v $
 * Revision:    $Revision: 1.2 $
 * Author:      $Author: csr $
 * Date:        $Date: 2007/03/22 13:50:42 $
 *
 * Copyright Det Kongelige Bibliotek og Statsbiblioteket, Danmark
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package dk.statsbiblioteket.deck.client;

import org.apache.log4j.Logger;
import dk.statsbiblioteket.deck.Constants;
import dk.statsbiblioteket.deck.rmiInterface.compute.Compute;
import java.util.List;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.net.MalformedURLException;

/**
 * csr forgot to comment this!
 *
 * @author csr
 * @since Feb 22, 2007
 */

public class GenericCtrl {
    static Logger log = Logger.getLogger(GenericCtrl.class.getName());
    private String encoderIP;
    private String unix_command;
    final int encoderRMIport = Constants.DEFAULT_RMI_CLIENT_PORT ;
    private boolean is_daemon = false;
    private Integer[] returncodes;

    public GenericCtrl(String encoderIP, String unix_command, boolean is_daemon, Integer... returncodes) {
        this.returncodes = returncodes;
        if (encoderIP == null) {
            throw new RuntimeException("Attempt to create GenericCtrl with null encoderIP");
        }
       this.encoderIP = encoderIP;
       this.unix_command = unix_command;
       this.is_daemon = is_daemon;
        if (returncodes == null || returncodes.length == 0){
            returncodes = new Integer[]{0};
        }
        this.returncodes = returncodes;
    }


    public GenericCtrl(String encoderIP, String unix_command,Integer... returncodes) {
       this(encoderIP, unix_command, false, returncodes);
    }

    public List<String> execute() throws RemoteException {
        log.debug("Executing: " + unix_command);
        System.out.println("Executing: " + unix_command);
        List<String> result = null;

      if (System.getSecurityManager() == null) {
                System.setSecurityManager(new RMISecurityManager());
        }

        LocateRegistry.getRegistry (encoderRMIport);

        String name = "//" + encoderIP + ":" +encoderRMIport+ "/Compute";
        System.out.println("Client looks up name address: " + name);
        log.debug("Client looks up name address: " + name);
        
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

        GenericTask task = new GenericTask(unix_command, is_daemon, returncodes);
        try {
            log.debug("Executing command");
            System.out.println("Executing command");
            result = (List<String>) (comp.executeTask(task));
        } catch (RemoteException e1) {
            log.error(" Failure, Task not executed! ", e1);
            System.out.println("Task failed " + e1.toString());
            e1.printStackTrace();
            throw new RuntimeException("Failed to execute task '"+unix_command+"'",e1);
        }

        System.out.println("Returning: "+result);
        return result;
    }


    @Override
    public String toString() {
        return "GenericCtrl{" +
                "encoderIP='" + encoderIP + '\'' +
                ", unix_command='" + unix_command + '\'' +
                ", encoderRMIport=" + encoderRMIport +
                ", is_daemon=" + is_daemon +
                '}';
    }
}
