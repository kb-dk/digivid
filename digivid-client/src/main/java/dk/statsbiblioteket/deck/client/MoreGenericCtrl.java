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

import dk.statsbiblioteket.deck.rmiInterface.compute.Task;
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

public class MoreGenericCtrl {
    static Logger log = Logger.getLogger(GenericCtrl.class.getName());
    private String encoderIP;

    final int encoderRMIport = Constants.DEFAULT_RMI_CLIENT_PORT ;
    private boolean is_daemon = false;
    private Task task;

    public MoreGenericCtrl(String encoderIP, Task task, boolean is_daemon) {
        this.task = task;
        if (encoderIP == null) {
            throw new RuntimeException("Attempt to create GenericCtrl with null encoderIP");
        }
       this.encoderIP = encoderIP;

       this.is_daemon = is_daemon;
    }


    public MoreGenericCtrl(String encoderIP, Task task) {
       this(encoderIP, task, false);
    }

    public Object execute() throws RemoteException {
        log.debug("Executing: " + task.toString());
        System.out.println("Executing: " + task.toString());


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

        Object result;
        try {
            log.debug("Executing command");
            System.out.println("Executing command");
            result = comp.executeTask(task);
        } catch (RemoteException e1) {
            log.error(" Failure, Task not executed! ", e1);
            System.out.println("Task failed " + e1.toString());
            e1.printStackTrace();
            throw new RuntimeException("Failed to execute task '"+task.toString()+"'",e1);
        }

        System.out.println("Returning: "+result);
        return result;
    }

}
