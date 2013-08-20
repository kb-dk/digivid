package dk.statsbiblioteket.deck.client.webinterface;

import dk.statsbiblioteket.deck.client.FileReader;
import dk.statsbiblioteket.deck.client.GenericCtrl;
import dk.statsbiblioteket.deck.client.datastructures.Comments;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.Map;
import java.util.Set;

import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.CAPTURE_FORMAT_PARAM;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.CARD_NAME_PARAM;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.CHANNEL_LABEL_PARAM;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.CONTROL_COMMAND_PARAM;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.ENCODER_NAME_PARAM;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.END_TIME_PARAM;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.FILE_LENGTH_PARAM;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.FILE_NAME_PARAM;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.INPUT_CHANNEL_ID_PARAM;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.RECORDING_QUALITY;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.RECORDING_TIME_PARAM;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.START_TIME_PARAM;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.STREAM_PORT_HTTP_PARAM;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.STREAM_PROTOCOL_PARAM;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.USER_NAME_PARAM;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.VHS_LABEL;
import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.getPresentationDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: abr
 * Date: 8/19/13
 * Time: 10:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class WebParams {


    private Integer card_name = -1;
    private String input_channel_id;
    private String stream_protocol;
    private Integer stream_port;
    private String encoder_IP;
    private String control_command;
    private String file_name;
    private String channel_label;
    private String capture_format;
    private Long recording_time;
    private String user_name;
    private Long start_time_ms;
    private Long stop_time_ms;
    private String vhs_label;
    private Integer quality = -1;
    private String encoder_name;


    private String parameterString="";



    public void unmarshallParams(Map<String, String[]> param_map) {
        Set<Map.Entry<String,String[]>> params = param_map.entrySet();
        parameterString = "";

        for (Map.Entry<String,String[]> entry : params) {
            String name = entry.getKey();
            String value = entry.getValue()[0];
            if (name.equals(CARD_NAME_PARAM)) {
                try {
                    card_name = Integer.parseInt(value);
                    addParam(CARD_NAME_PARAM,card_name);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("You must specify a recording source");
                }
            } else if (name.equals(INPUT_CHANNEL_ID_PARAM)) {
                input_channel_id = value;
                addParam(INPUT_CHANNEL_ID_PARAM,input_channel_id);
            } else if (name.equals(STREAM_PROTOCOL_PARAM)) {
                stream_protocol = value;
                addParam(STREAM_PROTOCOL_PARAM,stream_protocol);
            } else if (name.equals(STREAM_PORT_HTTP_PARAM)) {
                try {
                    stream_port = Integer.parseInt(value);
                    addParam(STREAM_PORT_HTTP_PARAM,stream_port);
                } catch (NumberFormatException e) {
                    throw new RuntimeException(e);
                }
            } else if (name.equals(ENCODER_NAME_PARAM)) {
                encoder_name = value;
                try {
                    encoder_IP = InetAddress.getByName(encoder_name).getHostAddress();
                    addParam(ENCODER_NAME_PARAM,encoder_IP);
                } catch (UnknownHostException e) {
                    throw new RuntimeException("Could not find host '" + encoder_name + "'");
                }
            } else if (name.equals(CONTROL_COMMAND_PARAM)) {
                control_command = value;
                addParam(CONTROL_COMMAND_PARAM,control_command);
            } else if (name.equals(FILE_NAME_PARAM)) {
                file_name = value;
                addParam(FILE_LENGTH_PARAM,file_name);
            } else if (name.equals(CHANNEL_LABEL_PARAM)) {
                channel_label = value;
                addParam(CHANNEL_LABEL_PARAM,channel_label);
            } else if (name.equals(START_TIME_PARAM)) {
                try {
                    start_time_ms = getPresentationDateFormat().parse(value).getTime();
                    addParam(START_TIME_PARAM,start_time_ms);
                } catch (ParseException e) {
                    throw new RuntimeException("You must specify a start time for the recording");
                }
            } else if (name.equals(END_TIME_PARAM)) {
                try {
                    stop_time_ms = getPresentationDateFormat().parse(value).getTime();
                    addParam(END_TIME_PARAM,stop_time_ms);
                } catch (ParseException e) {
                    throw new RuntimeException("You must specify a stop time for the recording");
                }
            } else if (name.equals(CAPTURE_FORMAT_PARAM)) {
                capture_format = value;
                addParam(CAPTURE_FORMAT_PARAM,capture_format);
            } else if (name.equals(VHS_LABEL)) {
                vhs_label = value;
            } else if (name.equals(RECORDING_QUALITY)) {
                try {
                    quality = Integer.parseInt(value);
                    addParam(RECORDING_QUALITY,quality);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("You must specify an integer value for the quality");
                }
            } else if (name.equals(RECORDING_TIME_PARAM)) {
                try {
                    recording_time = Long.parseLong(value);
                    addParam(RECORDING_TIME_PARAM,recording_time);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("You must enter a recording time as a whole number of minutes");
                }
            } else if (name.equals(USER_NAME_PARAM)) {
                user_name = value;
                addParam(USER_NAME_PARAM,user_name);
            }
        }
        Comments commentsStructure = getComments();


        if (vhs_label == null){
            if (commentsStructure != null && commentsStructure.getComments() != null){
                vhs_label =commentsStructure.getComments();
            } else {
                vhs_label = "";
            }
        }
        if (quality < 0){
            if (commentsStructure != null && commentsStructure.getQuality() != null) {
                quality = commentsStructure.getQuality();
            } else {
                quality = 5;
            }
        }
    }



    private void addParam(String name, Object value) {
        parameterString = parameterString + " '"+name+"="+value.toString()+"'";
    }


    private Comments getComments(){
        //Create comments file
        FileReader task = new FileReader(file_name);
        try {
            Object result = new GenericCtrl(encoder_IP, task).execute();
            if (result != null){
                return Comments.fromJson(result.toString());
            } else {
                return null;
            }
        } catch (RemoteException e1) {
            throw new RuntimeException(e1);
        }
    }


    public Integer getCard_name() {
        return card_name;
    }

    public String getInput_channel_id() {
        return input_channel_id;
    }

    public String getStream_protocol() {
        return stream_protocol;
    }

    public Integer getStream_port() {
        return stream_port;
    }

    public String getEncoder_IP() {
        return encoder_IP;
    }

    public String getControl_command() {
        return control_command;
    }

    public String getFile_name() {
        return file_name;
    }

    public String getChannel_label() {
        return channel_label;
    }

    public String getCapture_format() {
        return capture_format;
    }

    public Long getRecording_time() {
        return recording_time;
    }

    public String getUser_name() {
        return user_name;
    }

    public Long getStart_time_ms() {
        return start_time_ms;
    }

    public Long getStop_time_ms() {
        return stop_time_ms;
    }

    public String getVhs_label() {
        return vhs_label;
    }

    public Integer getQuality() {
        return quality;
    }

    public String getEncoder_name() {
        return encoder_name;
    }

    public String getParameterString() {
        return parameterString;
    }
}
