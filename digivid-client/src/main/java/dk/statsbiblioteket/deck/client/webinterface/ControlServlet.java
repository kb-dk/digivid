/* File:        $RCSfile: ControlServlet.java,v $
 * Revision:    $Revision: 1.8 $
 * Author:      $Author: csr $
 * Date:        $Date: 2007/08/14 09:00:40 $
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

package dk.statsbiblioteket.deck.client.webinterface;

import dk.statsbiblioteket.deck.Constants;
import dk.statsbiblioteket.deck.client.CommandLineCtrl;
import dk.statsbiblioteket.deck.client.FileCreator;
import dk.statsbiblioteket.deck.client.GenericCtrl;
import dk.statsbiblioteket.deck.client.datastructures.Comments;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.CARDS_ATTR;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.FILE_LENGTH_ATTR;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.INDEX_JSP;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.PAGE_ATTR;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.PLAYBACK_JSP;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.PLAY_JSP;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.POSTPROCESS;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.POSTPROCESS_JSP;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.START_POSTPROCESS;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.START_PREVIEW;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.START_RECORDING;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.STATUS_JSP;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.STOP_PLAYBACK;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.STOP_PREVIEW;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.STOP_RECORDING;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.STREAM_URL_ATTR;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.getFilenameDateFormat;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.lognames;


/**
 * csr forgot to comment this!
 *
 * @author csr
 * @since Feb 26, 2007
 */

public class ControlServlet extends HttpServlet {


    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        doPost(httpServletRequest, httpServletResponse);
    }


    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        WebParams params = new WebParams();
        params.unmarshallParams(httpServletRequest.getParameterMap());
        if (START_PREVIEW.equals(params.getControl_command())) {
            startPreview(httpServletRequest, httpServletResponse,params);
        } else if (STOP_PREVIEW.equals(params.getControl_command())) {
            stopPreview(httpServletRequest, httpServletResponse,params);
        } else if (START_RECORDING.equals(params.getControl_command())) {
            startRecording(httpServletRequest, httpServletResponse,params);
        } else if (STOP_RECORDING.equals(params.getControl_command())) {
            stopRecording(httpServletRequest, httpServletResponse,params);
        } else if (START_POSTPROCESS.equals(params.getControl_command())) {
            startPostProcess(httpServletRequest, httpServletResponse, params);
        } else if (POSTPROCESS.equals(params.getControl_command())) {
            postProcess(httpServletRequest, httpServletResponse,params);
        } else if (STOP_PLAYBACK.equals(params.getControl_command())) {
            stopPlayback(httpServletRequest, httpServletResponse,params);
        } else if (params.getControl_command() == null || "".equals(params.getControl_command())) {
            httpServletRequest.setAttribute(PAGE_ATTR, STATUS_JSP);
        } else {
            throw new RuntimeException("Command '" + params.getControl_command() + "' not implemented");
        }
        //Give the command a chance to execute before requesting the new status
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        getServletContext().getRequestDispatcher(INDEX_JSP).forward(httpServletRequest, httpServletResponse);
    }

    private void startPreview(HttpServletRequest request, HttpServletResponse response, WebParams params) {
        stopPreview(request, response, params);
        String unix_command = Constants.STREAMER_BINDIR + "/" + Constants.STREAMER_STARTCOMMAND +
                " -d " + params.getCard_name() + " -p " + params.getStream_port();
        startUnixDaemon(params.getEncoder_IP(), unix_command);
        request.setAttribute(STREAM_URL_ATTR, getStreamUrl(params));
        request.setAttribute(CARDS_ATTR, params.getCard_name());
        request.setAttribute(PAGE_ATTR, PLAY_JSP);
    }



    private void stopPreview(HttpServletRequest request, HttpServletResponse response, WebParams params) {
        String unix_command = Constants.STREAMER_BINDIR + "/" + Constants.STREAMER_STOPCOMMAND +
                " -p " + params.getStream_port();
        runUnixCommand(params, unix_command);
        request.setAttribute(PAGE_ATTR, STATUS_JSP);
    }


    private void startRecording(HttpServletRequest request, HttpServletResponse response, WebParams params) {
        if (params.getCard_name() < 0) {
            throw new RuntimeException("You forgot to select a recording source");
        }
        String file_name = params.getFile_name().replaceAll(" ", "").replaceAll("_", "");
        if (params.getRecording_time() < 1) {
            throw new RuntimeException("You must set a recording time greater than 1 minute, not '" + params.getRecording_time() + "'");
        }


        String unixParams = " -d " + params.getCard_name() +
                " -i " + params.getChannel_label() +
                " -a " + params.getCapture_format().replaceAll("mpeg","") +
                " -f " + file_name +
                " -l " + params.getRecording_time() +
                " -o " + params.getStart_time_ms();

        String unix_command = Constants.RECORDER_BINDIR + "/get_recording_name.sh " + unixParams;
        List<String> output = runUnixCommand(params,unix_command);

        String recordFile = findRecordFileFromOutput(output);


        unix_command = Constants.RECORDER_BINDIR + "/start_recording.sh " + unixParams;
        startUnixDaemon(params.getEncoder_IP(), unix_command);

        //Create comments file
        Comments commentsStructure = WebParams.createCommentsFromParams(params,new File(recordFile));
        createCommentsFile(params,new File(recordFile),commentsStructure);

        request.setAttribute(PAGE_ATTR, STATUS_JSP);
    }


    private String findRecordFileFromOutput(List<String> output) {
        String result = "";
        for (String line : output) {
            if (line.startsWith("Record File:")){
                result = line.replace("Record File:","").trim();
            }
        }
        return result;
    }

    private void stopRecording(HttpServletRequest request, HttpServletResponse response, WebParams params) {
        String unix_command = Constants.RECORDER_BINDIR + "/stop_recording.sh -d " + params.getCard_name();
        runUnixCommand(params, unix_command);
        request.setAttribute(PAGE_ATTR, STATUS_JSP);
    }

    private void startPostProcess(HttpServletRequest request, HttpServletResponse response, WebParams params) {
        // Get the file length
        File dir = new File(Constants.DEFAULT_RECORDSDIR);
        File file = new File(dir, params.getFile_name());
        String unix_command = Constants.STREAMER_BINDIR + "/" + Constants.STREAMER_STARTCOMMAND +
                " -f " + file.getAbsolutePath() + " -p " + params.getStream_port();
        startUnixDaemon(params.getEncoder_IP(), unix_command);
        unix_command = Constants.RECORDER_BINDIR + "/get_mpeg_file_length " + file.getAbsolutePath();
        List<String> result = runUnixCommand(params,unix_command);

        String file_length_S = result.get(0);
        long file_length = (long) Float.parseFloat(file_length_S);
        request.setAttribute(FILE_LENGTH_ATTR, file_length);
        request.setAttribute(STREAM_URL_ATTR, getStreamUrl(params));
        request.setAttribute(PAGE_ATTR, POSTPROCESS_JSP);
    }


    private String getStreamUrl(WebParams params) {
        return "http://" + params.getEncoder_IP() + ":" + params.getStream_port();
    }

    private void stopPlayback(HttpServletRequest request, HttpServletResponse response, WebParams params) {
        stopPreview(request, response, params);
    }

    private void postProcess(HttpServletRequest request, HttpServletResponse response, WebParams params) {
        File dir = new File(Constants.DEFAULT_RECORDSDIR);
        File file = new File(dir, params.getFile_name());

        //Only rename on the initial post processing
        if (!params.getPostProcessed()){
            String new_file_name = filenameFromParams(params);
            File new_file = new File(dir, new_file_name);
            //rename the video file
            if (!file.equals(new_file)){
                renameFile(params,file.getAbsolutePath(),new_file.getAbsolutePath());

                String old_log_file = file.getAbsolutePath().replace(".mpeg", ".log");
                String new_log_file = new_file.getAbsolutePath().replace(".mpeg", ".log");
                renameFile(params, old_log_file, new_log_file);

                String old_comments_file = file.getAbsolutePath()+WebConstants.COMMENTS_SUFFIX;
                String new_comments_file = new_file.getAbsolutePath()+WebConstants.COMMENTS_SUFFIX;
                renameFile(params, old_comments_file, new_comments_file);
            }
            file = new_file;
        }

        //Create comments file
        Comments commentsStructure = WebParams.createCommentsFromParams(params, file);
        createCommentsFile(params, file, commentsStructure);


        String fileDirParam = "--fileDir=\'" + Constants.DEFAULT_RECORDSDIR + "\' ";

        String command = Constants.HOOKS_BINDIR + "/post_postProcess.sh " + fileDirParam +
                commentsStructure.toParameterString();
        command = command.replaceAll(Pattern.quote("\""), Matcher.quoteReplacement("\\\""));
        runUnixCommandWithArgs(params, "bash", new String[]{"-c", command}, 0);

        request.setAttribute(PAGE_ATTR, PLAYBACK_JSP);


    }

    public static List<String> runUnixCommand(String encoderIP, String unix_command, Integer... returnCodes) {
        CommandLineCtrl ctrl = new CommandLineCtrl(encoderIP, unix_command, false,returnCodes);
        try {
            return ctrl.execute();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static void startUnixDaemon(String encoderIP, String unix_command) {
        CommandLineCtrl ctrl = new CommandLineCtrl(encoderIP, unix_command, true);
        try {
            ctrl.execute();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> runUnixCommandWithArgs(WebParams params, String unix_command,
                                                      String[] arguments, Integer... returnCodes) {
        CommandLineCtrl ctrl = new CommandLineCtrl(params.getEncoder_IP(), unix_command, false,arguments, returnCodes);
        try {
            return ctrl.execute();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }


    public static List<String> runUnixCommand(WebParams params, String unix_command, Integer... returnCodes) {
        return runUnixCommand(params.getEncoder_IP(),unix_command,returnCodes);
    }

    private static void createCommentsFile(WebParams params, File file, Comments commentsStructure) {
        FileCreator task = new FileCreator(file.getName(), commentsStructure.toJSon());
        try {
            Object result = new GenericCtrl(params.getEncoder_IP(), task).execute();
        } catch (RemoteException e1) {
            throw new RuntimeException(e1);
        }
    }

    private static String filenameFromParams(WebParams params) {
        String startTimeBart = getFilenameDateFormat().format((new Date(params.getStart_time_ms())));
        String endTimeBart = getFilenameDateFormat().format(new Date(params.getStop_time_ms()));
        String host = getHostname(params.getEncoder_IP());
        return lognames.get(params.getChannel_label()) + "_digivid_" + params.getChannel_label() +
                "_" + params.getCapture_format() + "_" + startTimeBart + "_" + endTimeBart +
                "_" + host + ".mpeg";
    }

    private static String getHostname(String encoder_ip) {

        String host;
        try {
            InetAddress addr = InetAddress.getByName(encoder_ip);
            host = addr.getHostName();
        } catch (UnknownHostException e) {
            return encoder_ip;
        }
        if (host == null){
            return encoder_ip;
        }
        return host;
    }

    private static void renameFile(WebParams params, String file, String new_file) {
        CommandLineCtrl ctrl;//rename the log file
        ctrl = new CommandLineCtrl(params.getEncoder_IP(), "mv " + file + " " + new_file,0,1);
        try {
            ctrl.execute();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


}
