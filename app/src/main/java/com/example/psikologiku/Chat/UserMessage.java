package com.example.psikologiku.Chat;

public class UserMessage {

    private String curentUser;
    private String message;
    private String sended;
    private long createdAt;
    public UserMessage(){

    }
    public UserMessage(String curentUser,String message, String sender, long createdAt) {
        this.message = message;
        this.sended = sender;
        this.createdAt = createdAt;
        this.curentUser = curentUser;
    }

    public String getCurentUser() {
        return curentUser;
    }

    public void setCurentUser(String curentUser) {
        this.curentUser = curentUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSended() {
        return sended;
    }

    public void setSended(String sender) {
        this.sended = sender;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
