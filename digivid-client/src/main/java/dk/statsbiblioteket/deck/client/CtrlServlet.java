package dk.statsbiblioteket.deck.client;

import dk.statsbiblioteket.deck.Constants;
import org.apache.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * Created by IntelliJ IDEA.
 * User: bytestroop
 * Date: Nov 23, 2006
 * Time: 1:47:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class CtrlServlet extends HttpServlet {


    /**
     * Shows all the parameters sent to the servlet via either
     * GET or POST. Specially marks parameters that have no values or
     * multiple values.
     */
    final private String dateRegex = Constants.DEFAULT_DATE_REGEXPRESSION;
    final private String timeRegex = Constants.DEFAULT_TIME_REGEXPRESSION;
    private static Logger log;
    private String pageTitle = "summary parameter";
    private String ctrlCommand;
    private String clientHostName;
    private String encoderName;
    private int cardName=0;
    private int frameWidth=0;
    private int frameHeight=0;
    private int bitrate=0;
    private int maxBitrate=0;
    private String fileName;
    private String streamName;
    private String streamProtocol;
    private int streamPortUDP=0;
    private int streamPortHTTP=0;
    private int streamPortRTP=0;
    private String userName;
    private String password;
    private String captureFormat;
    private String captureTime;
    private long _captureTime;
    private int captureSize=0;
    private String media = "tape";
    private String inputChannelID;
    private String labelChannelID;
    private String captureStorage;
    private String recordSerial; //capture
    //private long _recordSerial;
    private String origStartDate;
    private long _origStartDate;
    private String origStartTime;
    private long _origStartTime;


    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // InitParameter fetch
        //String log4j = config.getInitParameter("log4j" );
        //System.out.println(">>>>>>>" + log4j);
        // Log4J initialize
        //PropertyConfigurator.configure( CtrlServlet.class.getResource(log4j) );
        log = Logger.getLogger(StreamServerCtrl.class.getName());
        log.debug("servlet is initialized");
    }


    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {

        doPost(request, response);
    }
    private void allFormParameter(PrintWriter out) {
        out.println("<INPUT TYPE=\"HIDDEN\" NAME=\"pageTitle\" VALUE=\""+pageTitle+"\">\n");
        out.println("<INPUT TYPE=\"HIDDEN\" NAME=\"media\" VALUE=\""+media+"\">\n");
        out.println("<INPUT TYPE=\"HIDDEN\" NAME=\"userName\" VALUE=\""+userName+"\">\n");
        out.println("<INPUT TYPE=\"HIDDEN\" NAME=\"clientHostName\" VALUE=\""+ clientHostName +"\">\n");
        out.println("<INPUT TYPE=\"HIDDEN\" NAME=\"encoderName\" VALUE=\""+ encoderName +"\">\n");
        out.println("<INPUT TYPE=\"HIDDEN\" NAME=\"cardName\" VALUE=\""+cardName+"\">\n");
        out.println("<INPUT TYPE=\"HIDDEN\" NAME=\"inputChannelID\" VALUE=\""+inputChannelID+"\">\n");
        out.println("<INPUT TYPE=\"HIDDEN\" NAME=\"captureFormat\" VALUE=\""+captureFormat+"\">\n");
        out.println("<INPUT TYPE=\"HIDDEN\" NAME=\"fileName\" VALUE=\""+fileName+"\">\n");
        out.println("<INPUT TYPE=\"HIDDEN\" NAME=\"recordSerial\" VALUE=\""+recordSerial+"\">\n");
        out.println("<INPUT TYPE=\"HIDDEN\" NAME=\"origStartDate\" VALUE=\""+origStartDate+"\">\n");
        out.println("<INPUT TYPE=\"HIDDEN\" NAME=\"origStartTime\" VALUE=\""+origStartTime+"\">\n");
        out.println("<INPUT TYPE=\"HIDDEN\" NAME=\"captureTime\" VALUE=\""+captureTime+"\">\n");
        out.println("<INPUT TYPE=\"HIDDEN\" NAME=\"captureSize\" VALUE=\""+captureSize+"\">\n");
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, IOException {

        //doGet(request, response);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        System.out.println("Remote Addr " + request.getRemoteAddr());
        System.out.println("Remote Host " + request.getRemoteHost());
        System.out.println("Remote Port " + request.getRemotePort());
        System.out.println("Remote User " + request.getRemoteUser());
        System.out.println("Remote Url " + request.getRequestURL());
      
        System.out.println("Remote Authtype" + request.getAuthType());

        String title = pageTitle;
        out.println(ServletUtility.headWithTitle(title,request) +
                "<BODY BGCOLOR=\"#FDF5E6\" id=\"theBody\" onload=\"parent.showPage('contentLayer')\">\n" +
                "<H1 ALIGN=CENTER>" + title + "</H1>\n" +
                "<FORM NAME=\"ctrl\" ACTION=\"/bartdeck/CtrlServlet\" METHOD=\"GET\">\n" +
                "<TABLE BORDER=1 ALIGN=CENTER>\n" +
                "<TR BGCOLOR=\"#FFAD00\">\n" +
                "<TD>Parameter Name</TD><TD>Parameter Value(s)</TD>\n"+
                "</TR>\n"
        );

        Enumeration paramNames = request.getParameterNames();

        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            out.println("<TR><TD>" + paramName + "</TD><TD>");
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() == 0)
                    out.print("<I>No Value</I>");
                else
                    out.print(paramValue);

                if (paramName.equalsIgnoreCase("pageTitle")) {
                    pageTitle = paramValue;
                }

                if (paramName.equalsIgnoreCase("ctrlCommand")) {
                    ctrlCommand = paramValue;
                }
                if (request.getRemoteHost()!= null) {
                    clientHostName = request.getRemoteHost();
                }
                if (paramName.equalsIgnoreCase("encoderName")) {
                    encoderName = paramValue;
                }
                if (paramName.equalsIgnoreCase("userName")) {
                    userName = paramValue;
                }
                if (paramName.equalsIgnoreCase("password")) {
                    password = paramValue;
                }
                if (paramName.equalsIgnoreCase("captureFormat")) {
                    captureFormat = paramValue;
                }
                if (paramName.equalsIgnoreCase("frameWidth")) {
                    frameWidth = Integer.parseInt(paramValue);
                }
                if (paramName.equalsIgnoreCase("frameHeight")) {
                    frameHeight = Integer.parseInt(paramValue);
                }
                if (paramName.equalsIgnoreCase("bitrate")) {
                    bitrate = Integer.parseInt(paramValue);
                }
                if (paramName.equalsIgnoreCase("maxBitrate")) {
                    maxBitrate = Integer.parseInt(paramValue);
                }
                if (paramName.equalsIgnoreCase("captureTime")) {
                    captureTime = paramValue;
                }
                if (paramName.equalsIgnoreCase("captureSize")) {
                    captureSize = Integer.parseInt(paramValue);
                }
                if (paramName.equalsIgnoreCase("captureStorage")) {
                    captureStorage = paramValue;
                }
                if (paramName.equalsIgnoreCase("cardName")) {
                    cardName = Integer.parseInt(paramValue);
                }
                if (paramName.equalsIgnoreCase("fileName")) {
                    fileName = paramValue;
                }
                if (paramName.equalsIgnoreCase("recordSerial")) {
                    recordSerial = paramValue;
                } 
                if (paramName.equalsIgnoreCase("origStartDate")) {
                    origStartDate = paramValue;
                }
                if (paramName.equalsIgnoreCase("origStartTime")) {
                    origStartTime = paramValue;
                }
                if (paramName.equalsIgnoreCase("inputChannelID")) {
                    inputChannelID = paramValue;
                    if (inputChannelID.equalsIgnoreCase("SB-tape1") || inputChannelID.equalsIgnoreCase("Camera1")) {
                        media="tape";
                    } else media="tv";
                }
                //if (paramName.equalsIgnoreCase("media")) {
                //    media = paramValue;
                //}
                //if (paramName.equalsIgnoreCase("labelChannelID")) {
                //    labelChannelID = paramValue;
                //}
                if (paramName.equalsIgnoreCase("streamName")) {
                    streamName = paramValue;
                }
                if (paramName.equalsIgnoreCase("streamProtocol")) {
                    streamProtocol = paramValue;
                }
                if (paramName.equalsIgnoreCase("streamPortUDP")) {
                    if (paramValue != null) streamPortUDP = Integer.parseInt(paramValue);
                }
                if (paramName.equalsIgnoreCase("streamPortHTTP")) {
                    if (paramValue != null) streamPortHTTP = Integer.parseInt(paramValue);
                }
                if (paramName.equalsIgnoreCase("streamPortRTP")) {
                    if (paramValue != null) streamPortRTP = Integer.parseInt(paramValue);
                }
                out.println("</TD>\n");
                out.println("</TR>\n");
            } else {
                out.println("<UL?");
                for (int i = 0; i < paramValues.length; i++) {
                    out.println("<LI>" + paramValues[i]);
                }
                out.println("</UL>");
            }
        }

        out.println("</TD>\n");
        out.println("</TR>\n");

        /*
        out.println("<TR><TD><embed type=\"application/x-vlc-plugin\"");
        out.println("name=\"video1\"");
        out.println("autoplay=\"no\" loop=\"no\" width=\"720\" height=\"576\"");
        //the streamserver address and the file to stream
        if (streamProtocol.equalsIgnoreCase("UDP"))out.println("target=\"udp://@:"+streamPortUDP+ "\" />");
        log.debug("stream target=\"udp://@:"+streamPortUDP+ "\" />");
        if (streamProtocol.equalsIgnoreCase("HTTP"))out.println("target=\"http://" + encoderName + ":"+streamPortHTTP+"/"+streamName+"\" />");
        log.debug("stream target=\"http://" + encoderName + ":" +streamPortHTTP+ "/" +streamName + "\"");
        if (streamProtocol.equalsIgnoreCase("RTP"))out.println("target=\"rtsp://" + encoderName + ":"+streamPortRTP+"/"+streamName+"\" />");
        log.debug("target=\"rtsp://" + encoderName + ":"+streamPortRTP+"/"+streamName+"\"");
        out.println("<br/>");
        out.println("<a href=\"javascript:;\" onclick='document.video1.play()'>Play</a>");
        out.println("<a href=\"javascript:;\" onclick='document.video1.pause()'>Pause</a>");
        out.println("<a href=\"javascript:;\" onclick='document.video1.stop()'>Stop</a>");
        out.println("<a href=\"javascript:;\" onclick='document.video1.fullscreen()'>Fullscreen</a>");
        out.println("</TD></TR>");
        */

        if (origStartDate!=null && origStartTime != null) {
            String dateTime = origStartDate + "_" + origStartTime;

            if (origStartDate!=null) { _origStartDate = DateConverter.parseDateAsStringIntoMilliseconds(dateTime,dateRegex);}
            log.debug("Set Original Start date: " + _origStartDate); 
        }

        if (captureTime!=null)   { _captureTime =   DateConverter.parseTimeAsStringIntoMilliseconds(captureTime,timeRegex);}
        log.debug("Set Capturetime: " + _captureTime);


        //some logic
        // recording
        if (ctrlCommand.equalsIgnoreCase("start_record")) {

            if ((media != null )|| (encoderName != null) || (clientHostName != null) || (userName != null) || (password != null) || (origStartDate !=null) || (origStartTime !=null)) {
                //todo check username/password

                RecorderCtrl rctlr = new RecorderCtrl("start",media,userName, clientHostName, encoderName,cardName,inputChannelID,captureFormat,fileName,_origStartDate,_captureTime,captureSize);
                recordSerial = rctlr.remoteControl();

                //System.out.println("StartTime: " + startTime);
                allFormParameter(out);

                out.println("<TR><TD COLSPAN=\"2\" ALIGN=\"RIGHT\" BGCOLOR=\"RED\">\n");
                out.println("<INPUT TYPE=\"SUBMIT\" NAME=\"ctrlCommand\" VALUE=\"info_progress\">\n");
                out.println("<INPUT TYPE=\"SUBMIT\" NAME=\"ctrlCommand\" VALUE=\"stop_record\">\n");
                out.println("</TD></TR>\n");
                out.println("</TABLE>\n");
                out.println("</FORM>\n");

            } else {
                System.out.println("Missing parameter - start_record failed, Exiting");
                log.error("Missing parameter - start_record failed, Exiting");
            }
        } else if (ctrlCommand.equalsIgnoreCase("info_progress")) {
                FSCtrl fsctlr = new FSCtrl(media,userName, clientHostName, encoderName,cardName,inputChannelID,fileName,recordSerial,_origStartDate,_captureTime,captureSize);
                Integer percentage = fsctlr.getProgress();
                System.out.println("Percentage: " + percentage);

                allFormParameter(out);

                out.println("<TR><TD>\n");
                out.println("<H2>" +percentage+"</H2>\n");
                out.println("<TR><TD COLSPAN=\"2\" ALIGN=\"RIGHT\" BGCOLOR=\"RED\">\n");
                out.println("<INPUT TYPE=\"SUBMIT\" NAME=\"ctrlCommand\" VALUE=\"info_progress\">\n");
                out.println("<INPUT TYPE=\"SUBMIT\" NAME=\"ctrlCommand\" VALUE=\"stop_record\">\n");
                out.println("</TD></TR>\n");
                out.println("</TABLE>\n");
                out.println("</FORM>\n");

        } else if (ctrlCommand.equalsIgnoreCase("stop_record")) {

            //todo check username/password
            if ((media != null) || (encoderName != null) || (clientHostName != null) || (userName != null) || (password != null)) {
                RecorderCtrl rctlr = new RecorderCtrl("stop",media,userName, clientHostName, encoderName,cardName,inputChannelID,captureFormat,fileName,_origStartDate,_captureTime,captureSize);
                rctlr.remoteControl();
                out.println("</TD></TR>\n");
                out.println("</TABLE>\n");
                out.println("</FORM>\n");

            } else {
                System.out.println("Missing parameter - start_record failed, Exiting");
                log.error("Missing parameter - start_record failed, Exiting");
            }
        }

        // preview and replay
        if (ctrlCommand.equalsIgnoreCase("play")) {
            if (streamProtocol.equalsIgnoreCase("UDP")) {

                if ((streamPortUDP != 0) || (clientHostName != null) || (media != null)) {
                    StreamServerCtrl ssctlr = new StreamServerCtrl("play","devUDP",null, clientHostName, encoderName,cardName,null,streamPortUDP,null,media);
                    ssctlr.remoteControl();

                    out.println("<TR><TD COLSPAN=\"2\" ALIGN=\"RIGHT\" BGCOLOR=\"RED\">\n");
                    //out.println("<object width=\"352\" height=\"288\">\n");
                    //out.println("<object >\n");
                    out.println("<embed" +
                            " width=\"720\" height=\"576\"" +
                            " loop=\"false\"" +
                            " playeveryframe=\"true\"" +
                            " cache=\"true\""+
                            " controller=\"true\""+
                            " autoplay=\"true\"" +
                            " src=\"udp://@:" + streamPortUDP + "\"" +
                            " type=\"application/x-google-vlc-plugin\"/>\n");
                    //out.println("</object>");
                    out.println("<TR><TD COLSPAN=\"2\" ALIGN=\"RIGHT\" BGCOLOR=\"RED\">\n");
                    out.println("<INPUT TYPE=\"SUBMIT\" NAME=\"ctrlCommand\" VALUE=\"stop_preview\"></TD>");
                    out.println("</TD></TR>\n");
                    out.println("</TABLE>\n");

                    out.println("</FORM>\n");
                    
                } else {
                    System.out.println("Missing parameter - play failed, Exiting");
                    log.error("Missing parameter - play failed, Exiting");
                }

            } else if (streamProtocol.equalsIgnoreCase("HTTP")) {

                if ((encoderName != null) || (media != null) || (streamPortHTTP != 0)) {
                    StreamServerCtrl ssctlr = new StreamServerCtrl("play","devHTTP", encoderName,null, encoderName,cardName,null,streamPortHTTP,null,media);
                    ssctlr.remoteControl();
                    request.setAttribute("stream_url", "http://"+ encoderName +":"+streamPortHTTP);
                    getServletContext().getRequestDispatcher("/play.jsp").forward(request, response);
                    if (1==1) return;
                    //todo: make mplayer and vlc-player compatible
                    out.println("<TR><TD COLSPAN=\"2\" ALIGN=\"RIGHT\" BGCOLOR=\"RED\">\n");
                    //out.println("<object>\n");
                    out.println("<embed" +
                            " width=\"720\" height=\"576\""+
                            " loop=\"false\"" +
                            " playeveryframe=\"true\"" +
                            " cache=\"true\""+
                            " controller=\"true\""+
                            " autoplay=\"true\"" +
                            " src=\"http://" + encoderName + ":" + streamPortHTTP + "\"" +
                            " type=\"application/x-google-vlc-plugin\"/>\n");
                    //out.println("</object>");
                    out.println("<TR><TD COLSPAN=\"2\" ALIGN=\"RIGHT\" BGCOLOR=\"RED\">\n");
                    out.println("<INPUT TYPE=\"SUBMIT\" NAME=\"ctrlCommand\" VALUE=\"stop_preview\"></TD>");
                    out.println("</TD></TR>\n");
                    out.println("</TABLE>\n");

                    out.println("</FORM>\n");

                } else {
                    System.out.println("Missing parameter - play via http failed, Exiting");
                    log.error("Missing parameter - play via Http failed, Exiting");
                }

            } else if (streamProtocol.equalsIgnoreCase("RTP")) {

                if ((encoderName != null) || (clientHostName != null) || (media != null) || (streamPortRTP!=0) || (streamName!=null)) {
                    StreamServerCtrl ssctlr = new StreamServerCtrl("play","devRTP", encoderName, clientHostName,
                                                                   encoderName,cardName,null,streamPortRTP,streamName,media);
                    ssctlr.remoteControl();

                    out.println("<TR><TD COLSPAN=\"2\" ALIGN=\"RIGHT\" BGCOLOR=\"RED\">\n");
                    out.println("<object>\n");
                    out.println("<embed" +
                            " width=\"720\" height=\"576\""+
                            " loop=\"false\"" +
                            " playeveryframe=\"true\"" +
                            " cache=\"true\""+
                            " controller=\"true\""+
                            " autoplay=\"true\"" +
                            " src=\"rtsp://" + encoderName + ":" + streamPortRTP + "/"+ streamName+"\"" +
                            " type=\"application/x-mplayer2\"/>\n");
                    out.println("</object>");
                    out.println("</TD></TR>\n");
                    out.println("</TABLE>\n");
                    out.println("<INPUT TYPE=\"SUBMIT\" NAME=\"ctrlCommand\" VALUE=\"stop_preview\"></TD>");
                    out.println("</FORM>\n");

                } else {
                    System.out.println("Missing parameter - play via rtp failed, Exiting");
                    log.error("Missing parameter - play via rtp failed, Exiting");
                }
            }
        } else if (ctrlCommand.equalsIgnoreCase("stop_preview")) {
            if (streamProtocol.equalsIgnoreCase("UDP")) {
                if ((streamPortUDP != 0) || (clientHostName != null) || (streamPortUDP!=0)) {
                    StreamServerCtrl ssctlr = new StreamServerCtrl("stop", "devUDP", null, clientHostName, encoderName, cardName,null,streamPortUDP,null,null);
                    ssctlr.remoteControl();

                    out.println("</TD></TR>\n");
                    out.println("</TABLE>\n");
                    out.println("</FORM>\n");

                } else {
                    System.out.println("Encoder, card device number or client's hostname not specified");
                    log.error("Encoder, card device number or client's hostname not specified");
                }
            } else if (streamProtocol.equalsIgnoreCase("HTTP")) {
                if ((cardName != 0) || (encoderName != null)) {
                    StreamServerCtrl ssctlr = new StreamServerCtrl("stop", "devHTTP", encoderName, null, encoderName, cardName,null,streamPortHTTP,null,null);
                    ssctlr.remoteControl();
                    out.println("</TD></TR>\n");
                    out.println("</TABLE>\n");
                    out.println("</FORM>\n");
                    //response.sendRedirect("./index.html?contentLayer=./preview.jsp");


                } else {
                    System.out.println("Missing parameter - stop_preview of http stream failed, Exiting");
                    log.error("Missing parameter - stop_preview of Http stream failed, Exiting");
                }

            } else if (streamProtocol.equalsIgnoreCase("RTP")) {
                if ((encoderName != null) || (clientHostName != null) || (streamPortRTP!=0) || (streamName!=null)) {
                    StreamServerCtrl ssctlr = new StreamServerCtrl("stop", "devRTP", encoderName, clientHostName,
                                                                   encoderName,cardName,null,streamPortRTP,streamName,null);
                    ssctlr.remoteControl();

                    out.println("</TD></TR>\n");
                    out.println("</TABLE>\n");
                    out.println("</FORM>\n");

                } else {
                    System.out.println("Missing parameter - stop_preview of rtp stream failed, Exiting");
                    log.error("Missing parameter - stop_preview of rtp stream failed, Exiting");
                }
            }

        } else if (ctrlCommand.equalsIgnoreCase("replay")) {
            if (streamProtocol.equalsIgnoreCase("UDP")) {
                if (((new File(fileName)).exists()) || (encoderName != null) || (clientHostName != null) || (streamPortUDP!=0)) {
                    StreamServerCtrl ssctlr = new StreamServerCtrl("replay", "fileUDP", null, clientHostName,
                                                                   encoderName,0,fileName,streamPortUDP, null,null);
                    ssctlr.remoteControl();

                    out.println("<TR><TD COLSPAN=\"2\" ALIGN=\"RIGHT\" BGCOLOR=\"RED\">\n");
                    out.println("<object width=\"720\" height=\"576\">\n");
                    out.println("<embed" +
                            " loop=\"false\"" +
                            " playeveryframe=\"true\"" +
                            " cache=\"true\""+
                            " controller=\"true\""+
                            " autoplay=\"true\"" +
                            " src=\"@:" + streamPortUDP + "\"" +
                            " type=\"application/x-mplayer2\"/>\n");
                    out.println("</object>");
                    out.println("</TD></TR>\n");
                    out.println("<TR><TD COLSPAN=\"2\" ALIGN=\"RIGHT\" BGCOLOR=\"RED\">\n");
                    out.println("<INPUT TYPE=\"SUBMIT\" NAME=\"ctrlCommand\" VALUE=\"stop_replay\"></TD>");
                    out.println("</TD></TR>\n");
                    out.println("</TABLE>\n");
                    out.println("</FORM>\n");


                } else {
                    System.out.println("Missing parameter - replay via udp stream failed, Exiting");
                    log.error("Missing parameter - replay via udp stream failed, Exiting");
                }
            } else if (streamProtocol.equalsIgnoreCase("HTTP")) {
                if ((new File(fileName)).exists() || (encoderName != null) || (streamPortHTTP!=0) || (fileName!=null)) {
                    StreamServerCtrl ssctlr = new StreamServerCtrl("replay", "fileHTTP", encoderName, null, encoderName,0,fileName,streamPortHTTP,null,null);
                    ssctlr.remoteControl();

                    out.println("<TR><TD COLSPAN=\"2\" ALIGN=\"RIGHT\" BGCOLOR=\"RED\">\n");
                    //out.println("<object>\n");
                    out.println("<embed" +
                            " width=\"720\" height=\"576\"" +
                            " loop=\"false\"" +
                            " playeveryframe=\"true\"" +
                            " cache=\"true\""+
                            " controller=\"true\""+
                            " autoplay=\"true\"" +
                            " src=\"http://" + encoderName + ":" + streamPortHTTP + "\"" +
                           // " type=\"application/x-mplayer2\"/>\n");
                           " type=\"application/x-google-vlc-plugin\"/>\n"); 
                    //out.println("</object>");
                    out.println("</TD></TR>\n");
                    out.println("<TR><TD COLSPAN=\"2\" ALIGN=\"RIGHT\" BGCOLOR=\"RED\">\n");
                    out.println("<INPUT TYPE=\"SUBMIT\" NAME=\"ctrlCommand\" VALUE=\"stop_replay\"></TD>");
                    out.println("</TD></TR>\n");
                    out.println("</TABLE>\n");

                    out.println("</FORM>\n");

                } else {
                    System.out.println("Missing parameter - replay via http stream failed, Exiting");
                    log.error("Missing parameter - replay via http stream failed, Exiting");
                }

            } else if (streamProtocol.equalsIgnoreCase("RTP")) {

                if (((new File(fileName)).exists()) || (encoderName !=null) || (clientHostName !=null) || (streamPortRTP!=0) || (streamName!=null)) {
                    StreamServerCtrl ssctlr = new StreamServerCtrl("replay", "fileRTP", encoderName, clientHostName,
                                                                   encoderName,0,fileName,streamPortRTP,streamName,null);
                    ssctlr.remoteControl();

                    out.println("<TR><TD COLSPAN=\"2\" ALIGN=\"RIGHT\" BGCOLOR=\"RED\">\n");
                    out.println("<object width=\"720\" height=\"576\">\n");
                    out.println("<embed" +
                            " loop=\"false\"" +
                            " playeveryframe=\"true\"" +
                            " cache=\"true\""+
                            " controller=\"true\""+
                            " autoplay=\"true\"" +
                            " src=\"rtsp://" + encoderName + ":" + streamPortRTP + "/"+ streamName+"\"" +
                            " type=\"application/x-mplayer2\"/>\n");
                    out.println("</object>");
                    out.println("</TD></TR>\n");
                    out.println("<TR><TD COLSPAN=\"2\" ALIGN=\"RIGHT\" BGCOLOR=\"RED\">\n");
                    out.println("<INPUT TYPE=\"SUBMIT\" NAME=\"ctrlCommand\" VALUE=\"stop_replay\"></TD>");
                    out.println("</TD></TR>\n");
                    out.println("</TABLE>\n");
                    out.println("</FORM>\n");

                } else {
                    System.out.println("Missing parameter - replay via rtp stream failed, Exiting");
                    log.error("Missing parameter - replay via rtp stream failed, Exiting");
                }
            }
        } else if (ctrlCommand.equalsIgnoreCase("stop_replay")) {
            if (streamProtocol.equalsIgnoreCase("UDP")) {
                if (((new File(fileName)).exists()) || (encoderName !=null) || (clientHostName !=null) || (streamPortUDP!=0)) {
                    StreamServerCtrl ssctlr = new StreamServerCtrl("stop", "fileUDP", null, clientHostName, encoderName,0,fileName,streamPortUDP,null,null);
                    ssctlr.remoteControl();

                    out.println("</TD></TR>\n");
                    out.println("</TABLE>\n");
                    out.println("</FORM>\n");

                } else {
                    System.out.println("Missing parameter - stop_replay of udp stream failed, Exiting");
                    log.error("Missing parameter - stop_replay of udp stream failed, Exiting");
                }
            } else if (streamProtocol.equalsIgnoreCase("HTTP")) {
                if ((new File(fileName)).exists() || (encoderName !=null) || (streamPortHTTP!=0)) {
                    StreamServerCtrl ssctlr = new StreamServerCtrl("stop", "fileHTTP", encoderName, null, encoderName,0,fileName,streamPortHTTP,null,null);
                    ssctlr.remoteControl();

                    out.println("</TD></TR>\n");
                    out.println("</TABLE>\n");
                    out.println("</FORM>\n");

                } else {
                    System.out.println("Missing parameter - stop_replay of http stream failed, Exiting");
                    log.error("Missing parameter - stop_replay of http stream failed, Exiting");
                }
            } else if (streamProtocol.equalsIgnoreCase("RTP")) {
                if (((new File(fileName)).exists()) || (encoderName != null) || (clientHostName != null) || (streamPortRTP != 0) || (streamName != null)) {
                    StreamServerCtrl ssctlr = new StreamServerCtrl("stop", "fileRTP", encoderName, clientHostName,
                                                                   encoderName,0,fileName, streamPortRTP,streamName,null);
                    ssctlr.remoteControl();

                    out.println("</TD></TR>\n");
                    out.println("</TABLE>\n");
                    out.println("</FORM>\n");

                } else {
                    System.out.println("Missing parameter - stop_replay of rtp stream failed, Exiting");
                    log.error("Missing parameter - stop_replay of rtp stream failed, Exiting");
                }
                /*
                } else {
                System.out.println("cmd: " + ctrlCommand +
                                   " serverName: " + encoderName +
                                   " clientName: " + clientHostName +
                                   " encoderName: " + encoderName +
                                   " cardName: " +  cardName +
                                   " fileName: " + fileName +
                                   " streamPort: " + streamPortRTP +
                                   " streamName: " + streamName);
                System.out.println("Not supported by protocol command");
                */
            }
        }
        out.println("</BODY>\n");
        out.println("</HTML>\n");
    }
}


