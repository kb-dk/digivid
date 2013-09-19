package dk.statsbiblioteket.deck.client.webinterface;

import com.google.code.regexp.Matcher;
import dk.statsbiblioteket.deck.client.FileReader;
import dk.statsbiblioteket.deck.client.GenericCtrl;
import dk.statsbiblioteket.deck.client.datastructures.Comments;

import java.io.File;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.Map;
import java.util.Set;

import static dk.statsbiblioteket.deck.client.webinterface.WebConstants.lognames;

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
    private String control_command;
    private String file_name;
    private String channel_label;
    private String capture_format;
    private Long recording_time;
    private String user_name;
    private Long start_time_ms;
    private Long stop_time_ms;
    private String vhs_label;
    private Integer quality;
    private String encoderName;
    private Boolean isPostProcessed;


    private String parameterString = "";



    public void unmarshallParams(Map<String, String[]> param_map) {
        Set<Map.Entry<String, String[]>> params = param_map.entrySet();
        parameterString = "";

        for (Map.Entry<String, String[]> entry : params) {
            String name = entry.getKey();
            String value = entry.getValue()[0];

            if (name.equals(WebConstants.CARD_NAME_PARAM)) {
                try {
                    card_name = Integer.parseInt(value);
                    addParam(WebConstants.CARD_NAME_PARAM, card_name);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("You must specify a recording source");
                }
            } else if (name.equals(WebConstants.IS_PROCESSED_PARAM)) {
                isPostProcessed = Boolean.parseBoolean(value);
                addParam(WebConstants.IS_PROCESSED_PARAM, isPostProcessed);
            } else if (name.equals(WebConstants.INPUT_CHANNEL_ID_PARAM)) {
                input_channel_id = value;
                addParam(WebConstants.INPUT_CHANNEL_ID_PARAM, input_channel_id);
            } else if (name.equals(WebConstants.STREAM_PROTOCOL_PARAM)) {
                stream_protocol = value;
                addParam(WebConstants.STREAM_PROTOCOL_PARAM, stream_protocol);
            } else if (name.equals(WebConstants.STREAM_PORT_HTTP_PARAM)) {
                try {
                    stream_port = Integer.parseInt(value);
                    addParam(WebConstants.STREAM_PORT_HTTP_PARAM, stream_port);
                } catch (NumberFormatException e) {
                    throw new RuntimeException(e);
                }
            } else if (name.equals(WebConstants.ENCODER_NAME_PARAM)) {
                encoderName = value;
            } else if (name.equals(WebConstants.CONTROL_COMMAND_PARAM)) {
                control_command = value;
                addParam(WebConstants.CONTROL_COMMAND_PARAM, control_command);
            } else if (name.equals(WebConstants.FILE_NAME_PARAM)) {
                file_name = value;
                if (file_name == null || file_name.isEmpty()){
                    file_name = "default";
                }
                addParam(WebConstants.FILE_LENGTH_PARAM, file_name);
            } else if (name.equals(WebConstants.CHANNEL_LABEL_PARAM)) {
                channel_label = value;
                addParam(WebConstants.CHANNEL_LABEL_PARAM, channel_label);
            } else if (name.equals(WebConstants.START_TIME_PARAM)) {
                try {
                    start_time_ms = WebConstants.getPresentationDateFormat().parse(value).getTime();
                    addParam(WebConstants.START_TIME_PARAM, start_time_ms);
                } catch (ParseException e) {
                    throw new RuntimeException("You must specify a start time for the recording");
                }
            } else if (name.equals(WebConstants.END_TIME_PARAM)) {
                try {
                    stop_time_ms = WebConstants.getPresentationDateFormat().parse(value).getTime();
                    addParam(WebConstants.END_TIME_PARAM, stop_time_ms);
                } catch (ParseException e) {
                    throw new RuntimeException("You must specify a stop time for the recording");
                }
            } else if (name.equals(WebConstants.CAPTURE_FORMAT_PARAM)) {
                capture_format = value;
                addParam(WebConstants.CAPTURE_FORMAT_PARAM, capture_format);
            } else if (name.equals(WebConstants.VHS_LABEL_PARAM)) {
                vhs_label = value;
            } else if (name.equals(WebConstants.RECORDING_QUALITY)) {
                try {
                    quality = Integer.parseInt(value);
                    addParam(WebConstants.RECORDING_QUALITY, quality);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("You must specify an integer value for the quality");
                }
            } else if (name.equals(WebConstants.RECORDING_TIME_PARAM)) {
                try {
                    recording_time = Long.parseLong(value);
                    addParam(WebConstants.RECORDING_TIME_PARAM, recording_time);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("You must enter a recording time as a whole number of minutes");
                }
            } else if (name.equals(WebConstants.USER_NAME_PARAM)) {
                user_name = value;
                addParam(WebConstants.USER_NAME_PARAM, user_name);
            }
        }
    }


    private void addParam(String name, Object value) {
        parameterString = parameterString + " --" + name + "='" + value.toString() + "'";
    }

    public static Comments createCommentsFromParams(WebParams params, File file) {
        Comments commentsStructure = new Comments();
        commentsStructure.setFilename(file.getName());
        commentsStructure.setChannelID(lognames.get(params.getChannel_label()));
        commentsStructure.setChannelLabel(params.getChannel_label());
        commentsStructure.setCaptureFormat(params.getCapture_format());
        commentsStructure.setStartDate(params.getStart_time_ms());
        commentsStructure.setEndDate(params.getStop_time_ms());
        commentsStructure.setEncoderName(params.getEncoderName());
        commentsStructure.setComments(params.getVhs_label());
        commentsStructure.setQuality(params.getQuality());
        commentsStructure.setUsername(params.getUser_name());
        return commentsStructure;
    }


    public static Comments getComments(String file_name, String encoderName) {
        //Create comments file
        if (file_name == null){
            return null;
        }
        FileReader task = new FileReader(file_name);

        try {
            Object result = new GenericCtrl(encoderName, task).execute();
            if (result != null && ! result.toString().isEmpty()) {
                return Comments.fromJson(result.toString());
            } else {
                Matcher matcher = WebConstants.UNPROCESSED_FILE_PATTERN.matcher(file_name);
                Comments comments = new Comments();
                comments.setFilename(file_name);
                comments.setComments("");
                comments.setQuality(5);
                comments.setEncoderName(encoderName);

                if (matcher.matches()){
                    comments.setStartDate(Long.parseLong(matcher.group("startTimestamp")));
                    //Additional file name component?
                } else {
                    matcher = WebConstants.BART_FILE_PATTERN.matcher(file_name);
                    if (matcher.matches()){
                        comments.setChannelID(matcher.group("channelID"));
                        comments.setStartDate(WebConstants.getFilenameDateFormat().parse(matcher.group("startTime"))
                                .getTime());
                        comments.setEndDate(WebConstants.getFilenameDateFormat().parse(matcher.group("endTime"))
                                .getTime());
                    } else{
                        return comments;
                    }
                }
                //shared groups
                comments.setUsername(matcher.group("user"));
                comments.setChannelLabel(matcher.group("channelLabel"));
                comments.setCaptureFormat(matcher.group("capturingFormat"));
                return comments;
            }
        } catch (RemoteException e1) {
            throw new RuntimeException(e1);
        } catch (ParseException e) {
            throw new RuntimeException(e);
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

    public String getEncoderName() {
        return encoderName;
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

    public String getParameterString() {
        return parameterString;
    }

    public Boolean getPostProcessed() {
        return isPostProcessed;
    }
}
