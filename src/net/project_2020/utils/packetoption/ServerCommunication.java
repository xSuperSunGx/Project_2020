package net.project_2020.utils.packetoption;

import java.io.Serializable;
import java.util.IllegalFormatException;

public class ServerCommunication implements Serializable {

    private String nickname;
    private String message;
    private Tag tag;
    private final String seperator = "%sep%";

    public  String getSeperator() {
        return seperator;
    }

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

    @Override
    public String toString() {
        return "[" + this.tag.name() + this.seperator + this.nickname + this.seperator + this.message + "]";
    }

    public static ServerCommunication getFromString(String text) throws IllegalFormatException {
        if (text.startsWith("[") || text.endsWith("]")) {
            ServerCommunication s = new ServerCommunication();
            String ne = text.substring(1, text.length()-1);
            String[] args = ne.split(s.getSeperator() + "");
            Tag tag = Tag.valueOf(args[0]);
            String nickname = args[1];
            String message = args[2];
            s.setTag(tag);
            s.setNickname(nickname);
            s.setMessage(message);
            return s;
        } else {
            throw new IllegalArgumentException("This text can not be cast to an ServerCommunication Object!");
        }
    }
}
