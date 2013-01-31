/**
 * Created by IntelliJ IDEA.
 * User: stephan
 * Date: Aug 12, 2006
 * Time: 1:46:43 PM
 * To change this template use File | Settings | File Templates.
 */
package dk.statsbiblioteket.deck.rmiInterface.compute;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Compute extends Remote {
    Object executeTask(Task t) throws RemoteException;
}
