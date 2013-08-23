package dk.statsbiblioteket.deck.client.datastructures;

import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: abr
 * Date: 8/19/13
 * Time: 9:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class Comments {
    private String filename;
    private String comments;
    private Integer quality;
    private String encoderIP;
    private Long startDate;
    private Long endDate;
    private String channelLabel;
    private String channelID;
    private String captureFormat;
    private String username;


    public Comments() {
    }

    public static Comments fromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json,Comments.class);
    }

    public String toJSon(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getQuality() {
        return quality;
    }


    public void setQuality(Integer quality) {
        this.quality = quality;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public String getChannelLabel() {
        return channelLabel;
    }

    public void setChannelLabel(String channel) {
        this.channelLabel = channel;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getCaptureFormat() {
        return captureFormat;
    }

    public void setCaptureFormat(String captureFormat) {
        this.captureFormat = captureFormat;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEncoderIP() {
        return encoderIP;
    }

    public void setEncoderIP(String encoderIP) {
        this.encoderIP = encoderIP;
    }


    @Override
    public String toString() {
        return "Comments{" +
                "filename='" + filename + '\'' +
                ", comments='" + comments + '\'' +
                ", quality=" + quality +
                ", encoderIP='" + encoderIP + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", channelLabel='" + channelLabel + '\'' +
                ", channelID='" + channelID + '\'' +
                ", captureFormat='" + captureFormat + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    public String toParameterString(){
        return
        "--filename='" + filename + '\'' +
        " --comments='" + comments.replaceAll(Pattern.quote("'"), Matcher.quoteReplacement("'\\''")) + '\'' +
        " --quality=" + quality +
        " --encoderIP='" + encoderIP + '\'' +
        " --startDate=" + startDate +
        " --endDate=" + endDate +
        " --channelLabel='" + channelLabel + '\'' +
        " --channelID='" + channelID + '\'' +
        " --captureFormat='" + captureFormat + '\'' +
        " --username='" + username + '\'';
    }
}
