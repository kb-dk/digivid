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

    private String comments;
    private Integer quality;


    public Comments(String comments, int quality) {
        this.comments = comments;
        this.quality = quality;
    }

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
}
