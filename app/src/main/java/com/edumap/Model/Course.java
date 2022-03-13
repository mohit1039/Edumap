package com.edumap.Model;

import java.util.ArrayList;

public class Course {

    private String id, name;
    private ArrayList<String> streamID,collegeID;

    public Course() {
    }

    public Course(String id, String name, ArrayList<String> streamID, ArrayList<String> collegeID) {
        this.id = id;
        this.name = name;
        this.streamID = streamID;
        this.collegeID = collegeID;
    }

    public Course(String id, String name, ArrayList<String> streamID) {
        this.id = id;
        this.name = name;
        this.streamID = streamID;
    }

    public ArrayList<String> getStreamID() {
        return streamID;
    }

    public void setStreamID(ArrayList<String> streamID) {
        this.streamID = streamID;
    }

    public ArrayList<String> getCollegeID() {
        return collegeID;
    }

    public void setCollegeID(ArrayList<String> collegeID) {
        this.collegeID = collegeID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
