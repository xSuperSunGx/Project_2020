package net.project_2020.server.utils.mysql;

import net.project_2020.client.utils.coding.CodeHelper;
import net.project_2020.client.utils.coding.CodingProperty;

import java.io.Serializable;

public class MySQLData implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 7289379456776236574L;

    private String host;
    private String database;
    private int port;
    private String username;
    private String password;


    public MySQLData(String host, String database, int port, String username, String password) {
        this.host = host;
        this.database = database;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public void setAll(String host, String database, int port, String username, String password) {
        this.host = host;
        this.database = database;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public String getDatabase() {
        return database;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return CodingProperty.encode(CodeHelper.INFORMATION.getCode(), password);
    }
}