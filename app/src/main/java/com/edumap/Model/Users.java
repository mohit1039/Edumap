package com.edumap.Model;

public class Users {
    private String name,surname,userID,address,course,email;
    public Users() {
    }

    public Users(String name, String surname, String userID, String address, String course, String email) {
        this.name = name;
        this.surname = surname;
        this.userID = userID;
        this.address = address;
        this.course = course;
        this.email = email;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
