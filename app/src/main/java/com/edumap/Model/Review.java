package com.edumap.Model;

public class Review {

    private String fullname,userID,review,date,collegeID,reviewID;
    private float rating;

    public Review() {
    }

    public String getCollegeID() {
        return collegeID;
    }

    public void setCollegeID(String collegeID) {
        this.collegeID = collegeID;
    }

    public String getReviewID() {
        return reviewID;
    }

    public void setReviewID(String reviewID) {
        this.reviewID = reviewID;
    }

    public Review(String fullname, String userID, String review, String date, String collegeID, String reviewID, float rating) {
        this.fullname = fullname;
        this.userID = userID;
        this.review = review;
        this.date = date;
        this.collegeID = collegeID;
        this.reviewID = reviewID;
        this.rating = rating;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
