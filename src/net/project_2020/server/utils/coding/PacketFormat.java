package net.project_2020.server.utils.coding;

public class PacketFormat {

    private String string;

    public PacketFormat(String string) {
        this.string = string;
    }

    public String getNickname() {
        return this.string.split(CodingProperty.seperate)[1];
    }
    public String getMessage() {
        return this.string.split(CodingProperty.seperate)[0];
    }
}
