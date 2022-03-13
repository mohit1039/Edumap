package com.edumap.Model;

public class Event {
    private String eventID,name,description, collegeID;

    public Event() {
    }

    public Event(String eventID, String name, String description, String collegeID) {
        this.eventID = eventID;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCollegeID() {
        return collegeID;
    }

    public void setCollegeID(String collegeID) {
        this.collegeID = collegeID;
    }
}
