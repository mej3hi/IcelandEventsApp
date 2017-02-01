package com.hbv2.icelandevents.Entities;

import java.util.Date;

/**
 * Created by Martin on 29.1.2017.
 */

public class Event {
    private Long id;
    private String username;
    private String name;
    private String location;
    private String date;
    private String time;
    private String imageurl;
    private String description;
    private String musicgenres;

    public String getMusicgenres() {
        return musicgenres;
    }

    public void setMusicgenres(String musicgenres) {
        this.musicgenres = musicgenres;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
