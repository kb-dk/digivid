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
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.CARDS_ATTR;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.FILE_LENGTH_ATTR;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.INDEX_JSP;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.PAGE_ATTR;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.PLAYBACK_JSP;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.PLAY_JSP;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.POSTPROCESS;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.POSTPROCESS_JSP;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.RECORDING_QUALITY;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.START_POSTPROCESS;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.START_PREVIEW;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.START_RECORDING;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.STATUS_JSP;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.STOP_PLAYBACK;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.STOP_PREVIEW;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.STOP_RECORDING;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.STREAM_URL_ATTR;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.VHS_LABEL;
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
            startPlayback(httpServletRequest, httpServletResponse,params);
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
        CommandLineCtrl ctrl = new CommandLineCtrl(params.getEncoder_IP(), unix_command, true);
        try {
            ctrl.execute();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute(STREAM_URL_ATTR, "http://" + params.getEncoder_IP() + ":" + params.getStream_port());
        request.setAttribute(CARDS_ATTR, new Integer(params.getCard_name()));
        request.setAttribute(PAGE_ATTR, PLAY_JSP);
    }

    private void stopPreview(HttpServletRequest request, HttpServletResponse response, WebParams params) {
        String unix_command = Constants.STREAMER_BINDIR + "/" + Constants.STREAMER_STOPCOMMAND +
                " -p " + params.getStream_port();
        CommandLineCtrl ctrl = new CommandLineCtrl(params.getEncoder_IP(), unix_command, false);
        try {
            ctrl.execute();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
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



        String unix_command = Constants.RECORDER_BINDIR + "/get_recording_name.sh " +
                " -d " + params.getCard_name() +
                " -i " + params.getChannel_label() +
                " -a " + params.getCapture_format() +
                " -f " + file_name +
                " -l " + params.getRecording_time() +
                " -o " + params.getStart_time_ms();
        CommandLineCtrl ctrl = new CommandLineCtrl(params.getEncoder_IP(), unix_command, false);
        List<String> output;
        try {
            output = ctrl.execute();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        String recordFile = findRecordFileFromOutput(output);


        unix_command = Constants.RECORDER_BINDIR + "/start_recording.sh " +
                " -d " + params.getCard_name() +
                " -i " + params.getChannel_label() +
                " -a " + params.getCapture_format() +
                " -f " + file_name +
                " -l " + params.getRecording_time() +
                " -o " + params.getStart_time_ms();
        ctrl = new CommandLineCtrl(params.getEncoder_IP(), unix_command, true);
        try {
            output = ctrl.execute();

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }


        //Create comments file
        Comments commentsStructure = new Comments();
        commentsStructure.setComments(params.getVhs_label());

        FileCreator task = new FileCreator(new File(recordFile), commentsStructure.toJSon());
        try {
            Object result = new GenericCtrl(params.getEncoder_IP(), task).execute();
        } catch (RemoteException e1) {
            throw new RuntimeException(e1);
        }

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
        CommandLineCtrl ctrl = new CommandLineCtrl(params.getEncoder_IP(), unix_command, false);
        try {
            ctrl.execute();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute(PAGE_ATTR, STATUS_JSP);
    }

    private void startPlayback(HttpServletRequest request, HttpServletResponse response, WebParams params) {
        //We allow only one playback at a time so always stop the current playback
        //here
        //stopPlayback(request, response);
        /*StreamServerCtrl ssctlr = new StreamServerCtrl("replay", "fileHTTP",
                params.getEncoder_IP(), null, params.getEncoder_IP(), 0, file_name,
                params.getStream_port(), null, null);
         try {
             ssctlr.remoteControl();
         } catch (RemoteException e) {
             throw new RuntimeException("Could not start replay of file", e);
         }*/

        // Get the file length
        File dir = new File(Constants.DEFAULT_RECORDSDIR);
        File file = new File(dir, params.getFile_name());
        String unix_command = Constants.STREAMER_BINDIR + "/" + Constants.STREAMER_STARTCOMMAND +
                " -f " + file.getAbsolutePath() + " -p " + params.getStream_port();
        CommandLineCtrl ctrl = new CommandLineCtrl(params.getEncoder_IP(), unix_command, true);
        try {
            ctrl.execute();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        ctrl = new CommandLineCtrl(params.getEncoder_IP(), Constants.RECORDER_BINDIR + "/get_mpeg_file_length " + file.getAbsolutePath());
        List<String> result = null;
        try {
            result = ctrl.execute();
        } catch (RemoteException e) {
            throw new RuntimeException("Could not find length of " + file.getAbsolutePath());
        }
        String file_length_S = result.get(0);
        long file_length = (long) Float.parseFloat(file_length_S);
        request.setAttribute(FILE_LENGTH_ATTR, new Long(file_length));
        request.setAttribute(RECORDING_QUALITY, params.getQuality());
        request.setAttribute(VHS_LABEL, params.getVhs_label());
        request.setAttribute(STREAM_URL_ATTR, "http://" + params.getEncoder_IP() + ":" + params.getStream_port());
        request.setAttribute(PAGE_ATTR, POSTPROCESS_JSP);
    }

    private void stopPlayback(HttpServletRequest request, HttpServletResponse response, WebParams params) {
        stopPreview(request, response, params);
    }

    private void postProcess(HttpServletRequest request, HttpServletResponse response, WebParams params) {
        File dir = new File(Constants.DEFAULT_RECORDSDIR);
        File old_file = new File(dir, params.getFile_name());
        String startTimeBart = getFilenameDateFormat().format((new Date(params.getStart_time_ms())));
        String endTimeBart = getFilenameDateFormat().format(new Date(params.getStop_time_ms()));
        String new_file_name = lognames.get(params.getChannel_label()) + "_digivid_" + params.getChannel_label() +
                "_" + "mpeg" + params.getCapture_format() + "_" + startTimeBart + "_" + endTimeBart +
                "_" + params.getEncoder_IP() + ".mpeg";
        File new_file = new File(dir, new_file_name);
        //rename the video file
        if (!old_file.equals(new_file)){
            CommandLineCtrl ctrl = new CommandLineCtrl(params.getEncoder_IP(), "mv " + old_file.getAbsolutePath() + " " + new_file.getAbsolutePath());
            System.out.println("Moving " + old_file.getAbsolutePath() + " to " + new_file.getAbsolutePath());
            try {
                ctrl.execute();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            //rename the log file
            String old_log_file = old_file.getAbsolutePath().replace(".mpeg", ".log");
            String new_log_file = new_file.getAbsolutePath().replace(".mpeg", ".log");
            System.out.println("Moving " + old_log_file + " to " + new_log_file);
            ctrl = new CommandLineCtrl(params.getEncoder_IP(), "mv " + old_log_file + " " + new_log_file);
            try {
                ctrl.execute();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        //Create comments file
        //Create comments file
        Comments commentsStructure = new Comments();
        commentsStructure.setComments(params.getVhs_label());
        commentsStructure.setQuality(params.getQuality());

        FileCreator task = new FileCreator(new_file_name, commentsStructure.toJSon());
        try {
            Object result = new GenericCtrl(params.getEncoder_IP(), task).execute();
        } catch (RemoteException e1) {
            throw new RuntimeException(e1);
        }


        String fileDirParam = " fileDir=\'" + Constants.DEFAULT_RECORDSDIR + "\' ";
        String filenameParam = " filename=\'" + new_file_name + "\' ";
        CommandLineCtrl ctrl = new CommandLineCtrl(params.getEncoder_IP(), Constants.HOOKS_BINDIR + "/post_postProcess.sh " + filenameParam +fileDirParam +params.getParameterString());
        List<String> result = null;
        try {
            result = ctrl.execute();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }


        request.setAttribute(PAGE_ATTR, PLAYBACK_JSP);



    }


}
