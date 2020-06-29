package net.project_2020.utils.packetoption;

import java.io.Serializable;

public class ServerCommunication implements Serializable {

    private String nickname;
    private String message;
    private Tag tag;

    public void loginRequest(String username, String password) {
        this.tag = Tag.LOGIN;
        this.nickname = username;
        this.message = password;
    }

    public void registerRequest(String username, String password) {
        this.tag = Tag.REGISTER;
        this.nickname = username;
        this.message = password;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public boolean isFromServer() {
        return this.getNickname().equalsIgnoreCase("Server");
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
