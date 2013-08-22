package dk.statsbiblioteket.deck.client.datastructures;

import com.google.gson.Gson;

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
    private long startDate;
    private long endDate;
    private long duration;
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

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
