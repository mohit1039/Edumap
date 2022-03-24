package com.edumap.Model;

public class History {

    private String historyID,year,about,collegeID;

    public History() {
    }

    public History(String historyID, String year, String about, String collegeID) {
        this.historyID = historyID;
        this.year = year;
        this.about = about;
        this.collegeID = collegeID;
    }

    public String getHistoryID() {
        return historyID;
    }

    public void setHistoryID(String historyID) {
        this.historyID = historyID;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getCollegeID() {
        return collegeID;
    }

    public void setCollegeID(String collegeID) {
        this.collegeID = collegeID;
    }
}
