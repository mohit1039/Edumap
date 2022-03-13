package com.edumap.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class College implements Serializable {
    private String fullname,eventId,historyId,academyId,collegeID;
    private float longitude, latitude;
    private ArrayList<String> streams;

    public College() {
    }

    public College(String fullname, String eventId, String historyId, String academyId, String collegeID, float longitude, float latitude, ArrayList<String> streams) {
        this.fullname = fullname;
        this.eventId = eventId;
        this.historyId = historyId;
        this.academyId = academyId;
        this.collegeID = collegeID;
        this.longitude = longitude;
        this.latitude = latitude;
        this.streams = streams;
    }

    public College(String fullname, String collegeID, float longitude, float latitude, ArrayList<String> streams) {
        this.fullname = fullname;
        this.collegeID = collegeID;
        this.longitude = longitude;
        this.latitude = latitude;
        this.streams = streams;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public String getAcademyId() {
        return academyId;
    }

    public void setAcademyId(String academyId) {
        this.academyId = academyId;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public ArrayList<String> getStreams() {
        return streams;
    }

    public void setStreams(ArrayList<String> streams) {
        this.streams = streams;
    }

    public String getCollegeID() {
        return collegeID;
    }

    public void setCollegeID(String collegeID) {
        this.collegeID = collegeID;
    }
}
