package net.project_2020.client.utils.coding;

import net.project_2020.server.utils.coding.CodingProperty;

public class PacketFormat {

    private String string;

    public PacketFormat(String string) {
        this.string = string;
    }

    public String getNickname() {
        return this.string.split(net.project_2020.server.utils.coding.CodingProperty.seperate)[1];
    }
    public String getMessage() {
        return this.string.split(CodingProperty.seperate)[0];
    }
}
