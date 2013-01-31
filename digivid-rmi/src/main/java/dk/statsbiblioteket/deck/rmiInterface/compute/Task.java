/**
 * Created by IntelliJ IDEA.
 * User: stephan
 * Date: Aug 12, 2006
 * Time: 1:47:50 PM
 * To change this template use File | Settings | File Templates.
 */
package dk.statsbiblioteket.deck.rmiInterface.compute;

import java.io.Serializable;

public interface Task extends Serializable {
    Object execute();
}
