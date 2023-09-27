package com.abc.jdbc.dto;

public class MembersDTO {
    private String id;
    private String password;
    private String name;
    private String inputId;

    public MembersDTO() {

    }

    public MembersDTO(String inputId, String password, String name) {
        this.inputId = inputId;
        this.password = password;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInputId() {
        return inputId;
    }

    public void setInputId(String inputId) {
        this.inputId = inputId;
    }
}