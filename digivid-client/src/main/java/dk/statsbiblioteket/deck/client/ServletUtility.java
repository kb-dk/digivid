package dk.statsbiblioteket.deck.client;

/**
 * Created by IntelliJ IDEA.
 * User: bytestroop
 * Date: Nov 28, 2006
 * Time: 12:53:04 PM
 * To change this template use File | Settings | File Templates.
 */
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


/** Some simple time savers.
 *  Part of tutorial on servlets and JSP that appears at
 *  http://www.apl.jhu.edu/~hall/java/Servlet-Tutorial/
 *  1999 Marty Hall; may be freely used or adapted.
 */

public class ServletUtility {
  public static final String DOCTYPE =
    "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">";

 public static String headWithTitle(String title) {
    return(DOCTYPE + "\n" +
           "<HTML>\n" +
           "<HEAD><TITLE>" + title + "</TITLE></HEAD>\n");
  }

  public static String headWithTitle(String title, HttpServletRequest request) {

      System.out.println("Server ContextPath: " +request.getContextPath());
      System.out.println("Server PathInfo: " +request.getPathInfo());
      System.out.println("Server LocalAddress: " +request.getLocalAddr());
      System.out.println("Server ServletPath: " +request.getServletPath());
      System.out.println("Server ServletName: " +request.getServerName());

    return(DOCTYPE + "\n" +
           "<HTML>\n" +
           "<HEAD><TITLE>" + title + "</TITLE>\n" +
            "<META http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\" >\n"+
            "<META name=\"author\" content=\"std\">\n"+
            "<META name=\"copyright\" content=\"&copy; 2007 Statsbiblioteket\">\n"+
            "<META name=\"keywords\" content=\"TV, Radio, Library\">\n"+
            "<META name=\"date\" content=\"2007-01-12T08:49:37+00:00\">\n"+
            "<LINK rel=\"stylesheet\" type=\"text/css\" href=\""+request.getContextPath() +"/panels.css\">\n"+
            "<script type=\"text/javascript\" src=\""+request.getContextPath() +"/panels.js\" language=\"JavaScript\"></script>\n"+
            "<script type=\"text/javascript\" src=\""+request.getContextPath() +"/debugger.js\" language=\"JavaScript\"></script>\n"+
            "</HEAD>\n");
  }

  /** Read a parameter with the specified name, convert it to an int,
      and return it. Return the designated default value if the parameter
      doesn't exist or if it is an illegal integer format.
  */

  public static int getIntParameter(HttpServletRequest request,
                                    String paramName,
                                    int defaultValue) {
    String paramString = request.getParameter(paramName);
    int paramValue;
    try {
      paramValue = Integer.parseInt(paramString);
    } catch(NumberFormatException nfe) { // Handles null and bad format
      paramValue = defaultValue;
    }
    return(paramValue);
  }

  public static String getCookieValue(Cookie[] cookies,
                                      String cookieName,
                                      String defaultValue) {
    for(int i=0; i<cookies.length; i++) {
      Cookie cookie = cookies[i];
      if (cookieName.equals(cookie.getName()))
        return(cookie.getValue());
    }
    return(defaultValue);
  }

  // Approximate values are fine.

  public static final int SECONDS_PER_MONTH = 60*60*24*30;
  public static final int SECONDS_PER_YEAR = 60*60*24*365;
}

