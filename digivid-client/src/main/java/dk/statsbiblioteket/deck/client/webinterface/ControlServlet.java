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

import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.*;
import dk.statsbiblioteket.deck.client.StreamServerCtrl;
import dk.statsbiblioteket.deck.client.RecorderCtrl;
import dk.statsbiblioteket.deck.client.GenericCtrl;
import dk.statsbiblioteket.deck.Constants;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.Date;
import java.util.List;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * csr forgot to comment this!
 *
 * @author csr
 * @since Feb 26, 2007
 */

public class ControlServlet extends HttpServlet {

    private int card_name = -1;
    private String input_channel_id;
    private String stream_protocol;
    private int stream_port;
    private String encoder_IP;
    private String control_command;
    private String file_name;
    private String channel_label;
    private String capture_format;
    private long recording_time;
    private String user_name;
    private long start_time_ms;
    private String encoder_name;

    private static final String  start_time_format_string="S";
    private SimpleDateFormat start_time_format;
    {
        start_time_format = new SimpleDateFormat(start_time_format_string);
    }

    private void unmarshallParams(Map param_map) {
        Set<Map.Entry> params = param_map.entrySet();
        for (Map.Entry entry: params) {
            String name = (String) entry.getKey();
            String value = ((String[]) entry.getValue())[0];
            if (name.equals(CARD_NAME_PARAM)) {
                try {
                    card_name = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("You must specify a recording source");
                }
            } else
            if (name.equals(INPUT_CHANNEL_ID_PARAM)) {
                input_channel_id = value;
            } else
            if (name.equals(STREAM_PROTOCOL_PARAM)) {
                stream_protocol = value;
            } else
            if (name.equals(STREAM_PORT_HTTP_PARAM)) {
                try {
                    stream_port = Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    throw new RuntimeException(e);
                }
            } else
            if (name.equals(ENCODER_NAME_PARAM)) {
                encoder_name = value;
                try {
                    encoder_IP = InetAddress.getByName(encoder_name).getHostAddress();
                } catch (UnknownHostException e) {
                    throw new RuntimeException("Could not find host '"+encoder_name+"'");
                }
            } else
            if (name.equals(CONTROL_COMMAND_PARAM)) {
                control_command = value;
            } else
            if (name.equals(FILE_NAME_PARAM)) {
                file_name = value;
            } else
            if (name.equals(CHANNEL_LABEL_PARAM)) {
                channel_label = value;
            } else
            if (name.equals(START_TIME_PARAM)) {
                try {
                    start_time_ms = Long.parseLong(value);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("You must specify a start time for the recording");
                }
            } else
            if (name.equals(CAPTURE_FORMAT_PARAM)) {
                capture_format = value;
            } else
            if (name.equals(RECORDING_TIME_PARAM)) {
                try {
                    recording_time = Long.parseLong(value);
                } catch (NumberFormatException e) {
                   throw new RuntimeException("You must enter a recording time as a whole number of minutes");
                }
            } else
            if (name.equals(USER_NAME_PARAM)) {
                user_name = value;
            }
        }
    }

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        doPost(httpServletRequest, httpServletResponse);
    }


    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        unmarshallParams(httpServletRequest.getParameterMap());
        if (START_PREVIEW.equals(control_command)) {
             startPreview(httpServletRequest, httpServletResponse);
        } else
        if (STOP_PREVIEW.equals(control_command)) {
            stopPreview(httpServletRequest, httpServletResponse);
        } else
        if (START_RECORDING.equals(control_command)) {
            startRecording(httpServletRequest, httpServletResponse);
        } else
        if (STOP_RECORDING.equals(control_command)) {
            stopRecording(httpServletRequest, httpServletResponse);           
        } else
        if (START_POSTPROCESS.equals(control_command)) {
            startPlayback(httpServletRequest, httpServletResponse);
        } else
        if (POSTPROCESS.equals(control_command)) {
            postProcess(httpServletRequest, httpServletResponse);
        } else
        if (STOP_PLAYBACK.equals(control_command)) {
            stopPlayback(httpServletRequest, httpServletResponse);
        } else
        if (control_command == null || "".equals(control_command)) {
             httpServletRequest.setAttribute(PAGE_ATTR, STATUS_JSP);
        }
        else
        {
            throw new RuntimeException("Comand '"+control_command+"' not implemented");
        }
        //Give the command a chance to execute before requesting the new status
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        getServletContext().getRequestDispatcher(INDEX_JSP).forward(httpServletRequest, httpServletResponse);
    }

    private void startPreview(HttpServletRequest request, HttpServletResponse response) {
        stopPreview(request, response);
        String unix_command = Constants.STREAMER_BINDIR+"/"+Constants.STREAMER_STARTCOMMAND +
                " -d " + card_name +" -p " + stream_port;
        GenericCtrl ctrl = new GenericCtrl(encoder_IP, unix_command, true);
        try {
            ctrl.execute();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute(STREAM_URL_ATTR, "http://"+encoder_IP+":"+stream_port);
        request.setAttribute(CARDS_ATTR, new Integer(card_name));
        request.setAttribute(PAGE_ATTR, PLAY_JSP);
    }

    private void stopPreview(HttpServletRequest request, HttpServletResponse response) {
         String unix_command = Constants.STREAMER_BINDIR+"/"+Constants.STREAMER_STOPCOMMAND +
                 " -p " + stream_port;
        GenericCtrl ctrl = new GenericCtrl(encoder_IP, unix_command, false);
        try {
            ctrl.execute();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute(PAGE_ATTR, STATUS_JSP);
    }

    private void startRecording(HttpServletRequest request, HttpServletResponse response) {
        if (card_name < 0) {
            throw new RuntimeException("You forgot to select a recording source");
        }
        file_name = file_name.replaceAll(" ", "").replaceAll("_","");
        if (recording_time < 1) {
            throw new RuntimeException("You must set a recording time greater than 1 minute, not '"+recording_time+"'");
        }

        String unix_command = Constants.RECORDER_BINDIR+"/start_recording.sh " +
                " -d " + card_name +
                " -i " + channel_label +
                " -a " + capture_format +
                " -f " + file_name +
                " -l " + recording_time +
                " -o " + start_time_ms;
        GenericCtrl ctrl = new GenericCtrl(encoder_IP, unix_command, true);
        try {
            ctrl.execute();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute(PAGE_ATTR, STATUS_JSP);
    }

    private void stopRecording(HttpServletRequest request, HttpServletResponse response) {
        String unix_command = Constants.RECORDER_BINDIR+"/stop_recording.sh -d " + card_name;
        GenericCtrl ctrl = new GenericCtrl(encoder_IP, unix_command, false);
        try {
            ctrl.execute();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute(PAGE_ATTR, STATUS_JSP);
    }

     private void startPlayback(HttpServletRequest request, HttpServletResponse response) {
        //We allow only one playback at a time so always stop the current playback
        //here
       //stopPlayback(request, response);
        /*StreamServerCtrl ssctlr = new StreamServerCtrl("replay", "fileHTTP",
                encoder_IP, null, encoder_IP, 0, file_name,
                stream_port, null, null);
         try {
             ssctlr.remoteControl();
         } catch (RemoteException e) {
             throw new RuntimeException("Could not start replay of file", e);
         }*/

         // Get the file length
         File dir = new File(Constants.DEFAULT_RECORDSDIR);
         File file = new File(dir, file_name);
         String unix_command = Constants.STREAMER_BINDIR+"/"+Constants.STREAMER_STARTCOMMAND +
                 " -f " + file.getAbsolutePath() +" -p " + stream_port;
         GenericCtrl ctrl = new GenericCtrl(encoder_IP, unix_command, true);
         try {
             ctrl.execute();
         } catch (RemoteException e) {
             throw new RuntimeException(e);
         }
         ctrl = new GenericCtrl(encoder_IP, Constants.RECORDER_BINDIR+"/get_mpeg_file_length "+file.getAbsolutePath());
         List<String> result = null;
         try {
             result = ctrl.execute();
         } catch (RemoteException e) {
             throw new RuntimeException("Could not find length of "+file.getAbsolutePath());
         }
         String file_length_S = result.get(0);
         long file_length = (long) Float.parseFloat(file_length_S);
         request.setAttribute(FILE_LENGTH_ATTR, new Long(file_length));
         request.setAttribute(STREAM_URL_ATTR, "http://"+encoder_IP+":"+stream_port);
         request.setAttribute(PAGE_ATTR, POSTPROCESS_JSP);
     }

    private void stopPlayback(HttpServletRequest request, HttpServletResponse response) {
        stopPreview(request, response);
    }

    private void postProcess(HttpServletRequest request, HttpServletResponse response) {
        File dir = new File(Constants.DEFAULT_RECORDSDIR);
        File old_file = new File(dir, file_name);
        String startTimeBart = BART_DATE_FORMAT.format((new Date(start_time_ms)));
        long end_time = start_time_ms + recording_time*1000;
        String endTimeBart = BART_DATE_FORMAT.format(new Date(end_time));
        String new_file_name = lognames.get(channel_label)+"_digivid_"+channel_label+
             "_"+"mpeg"+capture_format+"_"+startTimeBart+"_"+endTimeBart+
                "_"+encoder_IP+".mpeg";
        File new_file = new File(dir, new_file_name);
        //rename the video file
        GenericCtrl ctrl = new GenericCtrl(encoder_IP, "mv "+old_file.getAbsolutePath()+" "+new_file.getAbsolutePath());
        System.out.println("Moving "+old_file.getAbsolutePath()+ " to "+new_file.getAbsolutePath());
        try {
            ctrl.execute();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        //rename the log file
        String old_log_file = old_file.getAbsolutePath().replace(".mpeg", ".log");
        String new_log_file = new_file.getAbsolutePath().replace(".mpeg", ".log");
        System.out.println("Moving "+old_log_file+ " to "+new_log_file);       
        ctrl = new GenericCtrl(encoder_IP, "mv "+old_log_file+" "+new_log_file);
        try {
            ctrl.execute();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute(PAGE_ATTR, PLAYBACK_JSP);
    }
}
