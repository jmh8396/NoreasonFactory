package com.example.gwpark.shit.Data;

/**
 * Created by gwpark on 16. 10. 6..
 */
public class Place {
    public String pid;
    public String name;
    public String location;
    public String tel;
    public String hasReview;
    public String hasPhoto;

    public Place(String pid, String name, String location, String tel, String hasReview, String hasPhoto) {
        this.pid = pid;
        this.name = name;
        this.location = location;
        this.tel = tel;
        this.hasReview = hasReview;
        this.hasPhoto = hasPhoto;
    }
}
