package com.nomad.model;

public class EditProfileReqModel {

    private String filePath;
    private String firstName;
    private String lastName;
    private String suburbId;
    private String id;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSuburbId() {
        return suburbId;
    }

    public void setSuburbId(String suburbId) {
        this.suburbId = suburbId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
