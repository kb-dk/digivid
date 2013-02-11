package dk.statsbiblioteket.deck.exception;

import javax.servlet.ServletException;

/**
 * User: std
 * Date: 11-05-2005
 * Time: 14:28:40
 */
public class BasicException extends ServletException {

    public BasicException(String msg, Throwable cause){
        super(msg, cause);
    }

    public BasicException(String msg){
        super(msg);
    }
}
