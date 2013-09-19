/*
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

import dk.statsbiblioteket.deck.Constants;
import dk.statsbiblioteket.deck.rmiInterface.compute.Compute;
import dk.statsbiblioteket.deck.rmiInterface.compute.Task;
import org.apache.log4j.Logger;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class GenericCtrl {
    static Logger log = Logger.getLogger(GenericCtrl.class.getName());
    private String encoderName;

    final int encoderRMIport = Constants.DEFAULT_RMI_CLIENT_PORT ;
    private Task task;

    public GenericCtrl(String encoderName, Task task) {
        this.task = task;
        if (encoderName == null) {
            throw new RuntimeException("Attempt to create MoreGenericCtrl with null encoderName");
        }
       this.encoderName = encoderName;
    }


    public Object execute() throws RemoteException {
        log.debug("Executing: " + task.toString());
        System.out.println("Executing: " + task.toString());


      if (System.getSecurityManager() == null) {
                System.setSecurityManager(new RMISecurityManager());
        }

        LocateRegistry.getRegistry (encoderRMIport);

        String name = "//" + encoderName + ":" +encoderRMIport+ "/Compute";
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
