package com.edumap.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class College implements Serializable {
    private String fullname,historyId,collegeID,address,phone;
    private double longitude, latitude;
    private ArrayList<String> streams;

    public College() {
    }


    public College(String fullname, String historyId, String collegeID, String address, String phone, double longitude, double latitude, ArrayList<String> streams) {
        this.fullname = fullname;
        this.historyId = historyId;
        this.collegeID = collegeID;
        this.address = address;
        this.phone = phone;
        this.longitude = longitude;
        this.latitude = latitude;
        this.streams = streams;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public College(String fullname, String collegeID, String address, String phone, double longitude, double latitude, ArrayList<String> streams) {
        this.fullname = fullname;
        this.collegeID = collegeID;
        this.address = address;
        this.phone = phone;
        this.longitude = longitude;
        this.latitude = latitude;
        this.streams = streams;
    }

    public String getFullname() {
        return fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }


    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
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
