package com.edumap.Model;

import java.util.ArrayList;

public class Academic {

    private String ranker,academicID, collegeID,streamID;

    public Academic(String ranker, String academicID, String collegeID, String streamID) {
        this.ranker = ranker;
        this.academicID = academicID;
        this.collegeID = collegeID;
        this.streamID = streamID;
    }

    public Academic() {
    }

    public String getRanker() {
        return ranker;
    }

    public void setRanker(String ranker) {
        this.ranker = ranker;
    }

    public String getAcademicID() {
        return academicID;
    }

    public void setAcademicID(String academicID) {
        this.academicID = academicID;
    }

    public String getCollegeID() {
        return collegeID;
    }

    public void setCollegeID(String collegeID) {
        this.collegeID = collegeID;
    }

    public String getStreamID() {
        return streamID;
    }

    public void setStreamID(String streamID) {
        this.streamID = streamID;
    }
}
