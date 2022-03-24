package com.edumap.Model;

public class Event {
    private String eventID,name, collegeID;

    public Event() {
    }

    public Event(String eventID, String name, String collegeID) {
        this.eventID = eventID;
        this.name = name;
        this.collegeID = collegeID;
    }


    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCollegeID() {
        return collegeID;
    }

    public void setCollegeID(String collegeID) {
        this.collegeID = collegeID;
    }
}
